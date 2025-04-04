package org.uet.rislab.seed.applicationlinux.service;

import com.sun.jna.Pointer;
import org.uet.rislab.seed.applicationlinux.generic.IGPhoto2;
import org.uet.rislab.seed.applicationlinux.global.AppProperties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public class CaptureCameraService {
    private Pointer camera;
    private Pointer context;
    private ConnectCameraService connectCameraService;

    private File lastCapturedImage;

    private static final String PYTHON_SCRIPT = "src/main/java/org/uet/rislab/seed/applicationlinux/pythoncore/preprocess_capture_image.py";
    Properties properties = new Properties();
    InputStream inputEnv = getClass().getClassLoader().getResourceAsStream("setting.properties");

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

            File destinationDir = new File(outputDir);
            if (!destinationDir.exists()) {
                destinationDir.mkdirs();
            }

            int nextId = getNextImageId(destinationDir);
            String formattedId = String.format("%06d", nextId); // Zero-padded ID
            String projectName = AppProperties.getProperty("projectName");
            lastCapturedImage = new File(destinationDir, projectName + "_" + formattedId + ".jpg");

            // Command to capture preview
            String[] command = {
                    "bash", "-c",
                    "gphoto2 --capture-preview --stdout > \"" + lastCapturedImage.getAbsolutePath() + "\""
            };

            System.out.println("Executing capture command...");
            ProcessBuilder pb = new ProcessBuilder(command);
            Map<String, String> env = pb.environment();
            env.put("PATH", System.getenv("PATH"));
            pb.redirectErrorStream(true);

            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode == 0 && lastCapturedImage.exists()) {
                preprocessCapturedImage(lastCapturedImage.getAbsolutePath());
                System.out.println("Image captured successfully at: " + lastCapturedImage.getAbsolutePath());
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

    private int getNextImageId(File directory) {
        int maxId = 0;
        File[] files = directory.listFiles((dir, name) -> name.matches(".*_\\d{6}\\.jpg"));
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                int startIndex = name.lastIndexOf('_') + 1;
                int endIndex = name.lastIndexOf('.');
                try {
                    int id = Integer.parseInt(name.substring(startIndex, endIndex));
                    maxId = Math.max(maxId, id);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    System.out.println("Invalid image file name: " + name + ". Skipping...");
                }
            }
        }

        return maxId + 1;
    }

    public boolean deleteLatestCapture() {
        if (lastCapturedImage != null && lastCapturedImage.exists()) {
            boolean deleted = lastCapturedImage.delete();
            if (deleted) {
                System.out.println("Deleted latest captured image: " + lastCapturedImage.getName());
                lastCapturedImage = null; // Reset as the image has been deleted
                return true;
            } else {
                System.err.println("Failed to delete the latest captured image.");
            }
        } else {
            System.err.println("No image to delete.");
        }
        return false;
    }

    private void preprocessCapturedImage(String inputImagePath) throws IOException, InterruptedException {
        properties.load(inputEnv);
        String condaActivatePath = properties.getProperty("condaActivatePath");
        String pythonExecutable = properties.getProperty("pythonExecutableName");
        String environmentName = properties.getProperty("condaEnvName");

        // Construct the command to activate the environment and run the script
        String command = String.format(
                "bash -c 'source %s %s && %s %s --input \"%s\" --output \"%s\"'",
                condaActivatePath, environmentName, pythonExecutable, PYTHON_SCRIPT, inputImagePath, inputImagePath
        );

        System.out.println("Executing command: " + command);

        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
        processBuilder.redirectErrorStream(true); // Redirect error stream for easier debugging

        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            System.out.println("Preprocessed: " + inputImagePath);
        } else {
            System.err.println("Failed to preprocess: " + inputImagePath);
            try (var errorStream = process.getInputStream()) {
                System.err.println(new String(errorStream.readAllBytes())); // Print error output
            }
        }
    }
}
