from PIL import Image
import argparse

def center_crop_to_square(image_path, output_path):
    img = Image.open(image_path)
    width, height = img.size
    if width == height:
        img.save(output_path)
        return
    if width > height:
        left = (width - height) // 2
        right = left + height
        top = 0
        bottom = height
    else:
        top = (height - width) // 2
        bottom = top + width
        left = 0
        right = width
    img = img.crop((left, top, right, bottom))
    img.save(output_path)

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Center crop an image to a square.')
    parser.add_argument('--input', type=str, required=True, help='Path to the input image.')
    parser.add_argument('--output', type=str, required=True, help='Path to the output image.')
    args = parser.parse_args()
    center_crop_to_square(args.input, args.output)