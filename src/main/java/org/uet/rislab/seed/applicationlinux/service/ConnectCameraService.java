    package org.uet.rislab.seed.applicationlinux.service;

    import com.sun.jna.Pointer;
    import com.sun.jna.ptr.NativeLongByReference;
    import com.sun.jna.ptr.PointerByReference;
    import javafx.application.Platform;
    import javafx.concurrent.Task;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import org.uet.rislab.seed.applicationlinux.generic.IGPhoto2;

    import java.io.ByteArrayInputStream;

    public class ConnectCameraService {
        private Pointer camera;
        private Pointer context;

        private Task<Void> liveViewTask;

        private volatile boolean liveViewRunning;

        public void initCamera() {
            // Create context
            context = IGPhoto2.INSTANCE.gp_context_new();

            // Create reference to camera
            PointerByReference cameraRef = new PointerByReference();

            // Create camera object
            int ret = IGPhoto2.INSTANCE.gp_camera_new(cameraRef);
            if (ret != 0) {
                System.err.println("Lỗi khi tạo đối tượng camera: " + ret);
                return;
            }

            camera = cameraRef.getValue();

            // Khởi tạo kết nối với camera
            if (camera == null || context == null) {
                System.err.println("Camera hoặc context chưa được khởi tạo.");
                return;
            }

            ret = IGPhoto2.INSTANCE.gp_camera_init(camera, context);
            if (ret != 0) {
                System.err.println("Không thể kết nối với camera: " + ret);
                cleanupCamera();
                return;
            }
            System.out.println("Kết nối thành công với máy ảnh!");
        }

        public void startLiveView(ImageView imageView) {
            if (liveViewRunning) {
                System.out.println("Live view is already running.");
                return; // Avoid starting another live view thread
            }

            stopLiveView();
            liveViewRunning = true;

            liveViewTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    while (liveViewRunning && !isCancelled()) {
                        // Tạo đối tượng tệp để lưu dữ liệu ảnh
                        PointerByReference fileRef = new PointerByReference();
                        int ret = IGPhoto2.INSTANCE.gp_file_new(fileRef);
                        if (ret != 0) {
                            System.err.println("Lỗi khi tạo tệp: " + ret);
                            break;
                        }
                        Pointer file = fileRef.getValue();

                        // Lấy dữ liệu Live View
                        ret = IGPhoto2.INSTANCE.gp_camera_capture_preview(camera, file, context);
                        if (ret != 0) {
                            System.err.println("Lỗi khi lấy dữ liệu Live View: " + ret);
                            IGPhoto2.INSTANCE.gp_file_free(file);
                            break;
                        }

                        // Lấy dữ liệu và kích thước từ tệp
                        PointerByReference dataRef = new PointerByReference();
                        NativeLongByReference sizeRef = new NativeLongByReference();
                        ret = IGPhoto2.INSTANCE.gp_file_get_data_and_size(file, dataRef, sizeRef);
                        if (ret != 0) {
                            System.err.println("Lỗi khi lấy dữ liệu từ tệp: " + ret);
                            IGPhoto2.INSTANCE.gp_file_free(file);
                            break;
                        }

                        // Chuyển đổi dữ liệu thành mảng byte
                        Pointer dataPointer = dataRef.getValue();
                        long size = sizeRef.getValue().longValue();
                        byte[] imageData = dataPointer.getByteArray(0, (int) size);

                        // Chuyển đổi mảng byte thành Image của JavaFX
                        ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
                        Image image = new Image(bis);

                        // Hiển thị hình ảnh trên ImageView
                        Platform.runLater(() -> {
                            imageView.setImage(image);
                        });

                        // Giải phóng bộ nhớ tệp
                        IGPhoto2.INSTANCE.gp_file_free(file);

                        // Ngừng một chút trước khi lấy khung hình tiếp theo
                        Thread.sleep(50);
                    }
                    return null;
                }
            };

            Thread thread = new Thread(liveViewTask);
            thread.setDaemon(true);
            thread.start();
        }

        public void stopLiveView() {
            liveViewRunning = false;
            if (liveViewTask != null && !liveViewTask.isCancelled()) {
                liveViewTask.cancel();
                liveViewTask = null;
            }
        }

        public void cleanupCamera() {
            stopLiveView();
            if (camera != null) {
                IGPhoto2.INSTANCE.gp_camera_exit(camera, context);
                IGPhoto2.INSTANCE.gp_camera_free(camera);
                camera = null;
            }
            if (context != null) {
                IGPhoto2.INSTANCE.gp_context_unref(context);
                context = null;
            }
        }
    }
