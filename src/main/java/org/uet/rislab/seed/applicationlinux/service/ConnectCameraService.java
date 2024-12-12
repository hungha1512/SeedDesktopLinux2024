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

    public Pointer getCamera() {
        return camera;
    }

    public Pointer getContext() {
        return context;
    }

    public void initCamera() {
        context = IGPhoto2.INSTANCE.gp_context_new();

        PointerByReference cameraRef = new PointerByReference();
        int ret = IGPhoto2.INSTANCE.gp_camera_new(cameraRef);
        if (ret != 0) {
            System.err.println("Error creating camera: " + ret);
            return;
        }
        camera = cameraRef.getValue();

        ret = IGPhoto2.INSTANCE.gp_camera_init(camera, context);
        if (ret != 0) {
            System.err.println("Cannot connect to camera: " + ret);
            cleanupCamera();
        } else {
            System.out.println("Camera connected successfully!");
        }
    }

    public void startLiveView(ImageView imageView) {
        if (liveViewRunning) {
            System.out.println("Live view already running.");
            return;
        }

        liveViewRunning = true;
        liveViewTask = new Task<>() {
            @Override
            protected Void call() {
                while (liveViewRunning && !isCancelled()) {
                    try {
                        PointerByReference fileRef = new PointerByReference();
                        int ret = IGPhoto2.INSTANCE.gp_file_new(fileRef);
                        if (ret != 0) break;

                        Pointer file = fileRef.getValue();
                        ret = IGPhoto2.INSTANCE.gp_camera_capture_preview(camera, file, context);
                        if (ret != 0) {
                            IGPhoto2.INSTANCE.gp_file_free(file);
                            break;
                        }

                        PointerByReference dataRef = new PointerByReference();
                        NativeLongByReference sizeRef = new NativeLongByReference();
                        ret = IGPhoto2.INSTANCE.gp_file_get_data_and_size(file, dataRef, sizeRef);
                        if (ret != 0) {
                            IGPhoto2.INSTANCE.gp_file_free(file);
                            break;
                        }

                        byte[] imageData = dataRef.getValue().getByteArray(0, (int) sizeRef.getValue().longValue());
                        ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
                        Platform.runLater(() -> imageView.setImage(new Image(bis)));

                        IGPhoto2.INSTANCE.gp_file_free(file);
//                        Thread.sleep(100); // 100ms delay
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
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
        if (liveViewTask != null) {
            liveViewTask.cancel();
            liveViewTask = null;
        }

        if (camera != null && context != null) {
            IGPhoto2.INSTANCE.gp_camera_exit(camera, context);
            System.out.println("Live view stopped and camera resources released.");
            try {
                Thread.sleep(500); // Ensure camera cleanup delay
            } catch (InterruptedException e) {
                System.err.println("Error during live view stop delay: " + e.getMessage());
            }
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
