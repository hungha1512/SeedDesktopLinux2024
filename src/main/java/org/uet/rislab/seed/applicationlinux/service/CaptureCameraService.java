package org.uet.rislab.seed.applicationlinux.service;

import com.sun.jna.Pointer;
import org.uet.rislab.seed.applicationlinux.generic.IGPhoto2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class CaptureCameraService {
    private Pointer camera;
    private Pointer context;

    private ConnectCameraService connectCameraService;

    public void setConnectCameraService(ConnectCameraService connectCameraService) {
        this.connectCameraService = connectCameraService;
    }

    public CaptureCameraService(Pointer camera, Pointer context) {
        this.camera = camera;
        this.context = context;
    }

    public void captureImage(String outputDir) {
        try {
            System.out.println("Stopping live view...");
            connectCameraService.stopLiveView(); // Stop live view task

            System.out.println("Releasing camera resources...");

            // Generate timestamped filename
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File destinationDir = new File(outputDir);
            if (!destinationDir.exists()) {
                destinationDir.mkdirs();
            }
            File finalFile = new File(destinationDir, "captured_image_" + timestamp + ".jpg");

            // Command to capture preview and move the file
            String[] command = {
                    "bash", "-c",
                    "gphoto2 --capture-preview --stdout > \"" + finalFile.getAbsolutePath() + "\""
            };

            System.out.println("Executing capture command...");
            ProcessBuilder pb = new ProcessBuilder(command);
            Map<String, String> env = pb.environment();
            env.put("PATH", System.getenv("PATH"));
            pb.redirectErrorStream(true);

            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode == 0 && finalFile.exists()) {
                System.out.println("Image captured successfully at: " + finalFile.getAbsolutePath());
            } else {
                System.err.println("Failed to capture image. Exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.err.println("Error while capturing image: " + e.getMessage());
        } finally {
            try {
                System.out.println("Re-initializing camera for live view...");
                IGPhoto2.INSTANCE.gp_camera_init(camera, context);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.err.println("Error while reinitializing camera: " + e.getMessage());
            }
        }
    }
}
