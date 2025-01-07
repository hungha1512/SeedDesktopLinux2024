import argparse
import cv2
import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
import os

import tensorflow as tf
from tensorflow.keras.preprocessing.image import img_to_array, load_img
from tensorflow.keras.layers import Input, Conv2D, MaxPooling2D, Conv2DTranspose, concatenate
from tensorflow.keras.models import Model

from sklearn.model_selection import train_test_split
import glob

gpus = tf.config.experimental.list_physical_devices('GPU')
if gpus:
    try:
        for gpu in gpus:
            tf.config.experimental.set_memory_growth(gpu, True)  # Enable memory growth
            tf.config.experimental.set_virtual_device_configuration(
                gpu,
                [tf.config.experimental.VirtualDeviceConfiguration(memory_limit=4096)]  # Set limit in MB
            )
    except RuntimeError as e:
        print(e)

IMAGE_PATH = '/home/tower/Documents/App Counting/Test Prj/Test 17/Image/Anh1.jpg'
PRETRAINED_WEIGHT = 'src/main/java/org/uet/rislab/seed/applicationlinux/pythoncore/unet_plus_plus_model.h5'
image_size = (256, 256)  # Resize ảnh về kích thước cố định


def load_image_infer(img_path):
    img = cv2.imread(img_path)
    img = cv2.resize(img, image_size)
    img = np.array(img, dtype=np.float32) / 255
    return img[None, ...]  # expand dims


def calculate_dimensions(mask, mask_threshold=0.5):
    dimensions = []
    contours, _ = cv2.findContours((mask >= mask_threshold).astype(np.uint8),
                                   cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    for contour in contours:
        rect = cv2.minAreaRect(contour)
        width, height = rect[1]
        dimensions.append((max(width, height), min(width, height)))
    return dimensions


def visualize_results_optimized(image, mask, dimensions, save_image_path, result_csv_path, min_size=5, max_size=30):
    """
    Trực quan hóa các hạt lúa với độ rõ ràng hơn:
    - Lọc bỏ các đối tượng quá lớn hoặc quá nhỏ.
    - Hiển thị chữ không bị chồng lấn.
    """
    output_image = image.copy()
    contours, _ = cv2.findContours((mask > 0.5).astype(np.uint8),
                                   cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    count = -1
    widths = []
    lengths = []
    for i, contour in enumerate(contours):
        length, width = dimensions[i]
        if length < min_size or width < min_size or length > max_size or width > max_size:
            continue

        count += 1
        widths.append(width)
        lengths.append(length)

        # Vẽ contour
        rect = cv2.minAreaRect(contour)
        box = cv2.boxPoints(rect)
        box = np.int0(box)
        cv2.drawContours(output_image, [box], 0, (0, 255, 0), 1)

        # Chú thích
        x, y = int(rect[0][0]), int(rect[0][1]) - 10
        cv2.putText(output_image, f"{count}",
                    (x, y), cv2.FONT_HERSHEY_SIMPLEX, 0.2, (255, 0, 0), 1, cv2.LINE_AA)

    plt.figure(figsize=(10, 10))
    plt.imshow(cv2.cvtColor(output_image, cv2.COLOR_BGR2RGB))
    plt.title("Optimized Visualization")
    plt.axis("off")

    plt.savefig(save_image_path)

    # Lưu kết quả vào CSV
    df_result = pd.DataFrame({'width': widths, 'height': lengths})
    df_result.to_csv(result_csv_path, index=True)


def conv_block(x, filters, kernel_size=3, activation='relu', padding='same'):
    x = Conv2D(filters, kernel_size, activation=activation, padding=padding)(x)
    x = Conv2D(filters, kernel_size, activation=activation, padding=padding)(x)
    return x


# Khối upsample & concat (giống U-Net)
def up_concat_block(x, skip, filters):
    x = Conv2DTranspose(filters, (2, 2), strides=(2, 2), padding='same')(x)
    x = concatenate([x, skip])
    x = conv_block(x, filters)
    return x


def unet_plus_plus_model(input_size=(256, 256, 3)):
    inputs = Input(input_size)

    # --------------------- ENCODER ---------------------
    # block 1
    c1_0 = conv_block(inputs, 64)  # X_1,0
    p1_0 = MaxPooling2D((2, 2))(c1_0)

    # block 2
    c2_0 = conv_block(p1_0, 128)  # X_2,0
    p2_0 = MaxPooling2D((2, 2))(c2_0)

    # block 3
    c3_0 = conv_block(p2_0, 256)  # X_3,0
    p3_0 = MaxPooling2D((2, 2))(c3_0)

    # block 4
    c4_0 = conv_block(p3_0, 512)  # X_4,0
    p4_0 = MaxPooling2D((2, 2))(c4_0)

    # bottleneck
    c5_0 = conv_block(p4_0, 1024)  # X_5,0 (bottleneck)

    # --------------------- DECODER: STAGE 1 ---------------------
    # Mỗi tầng up từ bottleneck/đầu ra trước và concat với encoder
    c4_1 = up_concat_block(c5_0, c4_0, 512)  # X_4,1
    c3_1 = up_concat_block(c4_0, c3_0, 256)  # X_3,1
    c2_1 = up_concat_block(c3_0, c2_0, 128)  # X_2,1
    c1_1 = up_concat_block(c2_0, c1_0, 64)  # X_1,1

    # --------------------- DECODER: STAGE 2 (++ connections) ---------------------
    # Kết hợp thêm skip của stage 1 + skip gốc encoder
    x4_2_input = concatenate([c4_1, c4_0])
    c4_2 = up_concat_block(c5_0, x4_2_input, 512)

    x3_2_input = concatenate([c3_1, c3_0])
    c3_2 = up_concat_block(c4_1, x3_2_input, 256)

    x2_2_input = concatenate([c2_1, c2_0])
    c2_2 = up_concat_block(c3_1, x2_2_input, 128)

    x1_2_input = concatenate([c1_1, c1_0])
    c1_2 = up_concat_block(c2_1, x1_2_input, 64)

    # Output cuối (chọn c1_2)
    outputs = Conv2D(1, (1, 1), activation='sigmoid')(c1_2)

    model = Model(inputs=[inputs], outputs=[outputs])

    return model


if __name__ == '__main__':
    # -- 1. Parse command-line arguments --
    parser = argparse.ArgumentParser(description="Inference script for rice seed segmentation.")
    parser.add_argument("--image-path", type=str, required=True, help="Path to the input image.")
    parser.add_argument("--project-dir", type=str, required=True, help="Path to the project directory.")
    args = parser.parse_args()

    # -- 2. Define folder paths --
    image_analysis_dir = os.path.join(args.project_dir, "Image_analysis")
    result_dir = os.path.join(args.project_dir, "Result")

    # Create directories if they don't exist
    os.makedirs(image_analysis_dir, exist_ok=True)
    os.makedirs(result_dir, exist_ok=True)

    # -- 3. Derive output filenames from the input filename --
    input_filename = os.path.basename(args.image_path)  # Extract filename (e.g., "image.jpg")
    base_name, ext = os.path.splitext(input_filename)  # Split into base name and extension
    save_image_path = os.path.join(image_analysis_dir, f"{base_name}_result{ext}")  # e.g., "image_result.jpg"
    result_csv_path = os.path.join(result_dir, f"{base_name}_result.csv")  # e.g., "image_dimensions.csv"

    # -- 4. Load model and weights --
    model = unet_plus_plus_model()
    model.load_weights(PRETRAINED_WEIGHT)

    # -- 5. Preprocess and predict --
    image = load_image_infer(args.image_path)
    mask = model.predict(image)

    # -- 6. Postprocess and visualize --
    # Convert back to uint8 for visualization
    original_image = (image[0] * 255).astype(np.uint8)
    mask = mask[0, :, :, 0]
    dimensions = calculate_dimensions(mask)

    visualize_results_optimized(original_image, mask, dimensions, save_image_path, result_csv_path)
