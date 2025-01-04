package org.uet.rislab.seed.applicationlinux.controller.analyze;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import org.uet.rislab.seed.applicationlinux.global.AppProperties;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainAnalyzeController implements Initializable {
    @FXML
    public Button btn_analyze;
    @FXML
    public ProgressBar pb_analyze;

    private static String IMAGE_FOLDER = AppProperties.getProperty("parentPath") + "/Image"; // Update this to your Image folder path
    private static final String PYTHON_SCRIPT = "src/main/java/org/uet/rislab/seed/applicationlinux/pythoncore/inference_image_plus_plus.py"; // Update this to your Python script path
    private static final String PROJECT_DIR = AppProperties.getProperty("parentPath"); // Update this to your project directory
    private static final int THREAD_COUNT = 2; // Number of files to process simultaneously

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_analyze.setOnAction(event -> handleAnalyze());
    }

    private void handleAnalyze() {
        File imageFolder = new File(IMAGE_FOLDER);
        if (!imageFolder.exists() || !imageFolder.isDirectory()) {
            showAlert("Error", "Image folder does not exist or is not a directory.");
            return;
        }

        File[] imageFiles = imageFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png"));
        if (imageFiles == null || imageFiles.length == 0) {
            showAlert("Info", "No image files found in the Image folder.");
            return;
        }

        int totalFiles = imageFiles.length;
        pb_analyze.setProgress(0);

        // Create a Task to process the images
        Task<Void> analyzeTask = new Task<>() {
            private volatile int completedCount = 0; // Counter for completed tasks

            @Override
            protected Void call() throws Exception {
                ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
                long startTime = System.currentTimeMillis();

                for (File imageFile : imageFiles) {
                    executorService.submit(() -> {
                        try {
                            runPythonScript(imageFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            synchronized (this) {
                                completedCount++;
                                updateProgress(completedCount, totalFiles); // Update progress after task completion
                            }
                        }
                    });
                }

                executorService.shutdown();
                executorService.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.MILLISECONDS);

                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;

                System.out.println("Total duration for all tasks: " + duration + "ms");
                Platform.runLater(() -> showAlert("Info", "Analysis completed. Total duration: " + duration / 1000 + " seconds."));
                return null;
            }
        };

        pb_analyze.progressProperty().bind(analyzeTask.progressProperty());

        // Run the task in a background thread
        Thread analyzeThread = new Thread(analyzeTask);
        analyzeThread.setDaemon(true); // Ensure the thread does not block JVM shutdown
        analyzeThread.start();
    }

    private void runPythonScript(File imageFile) throws IOException, InterruptedException {
        String condaActivatePath = "/home/tower/anaconda3/bin/activate"; // Path to conda activate
        String pythonExecutable = "python"; // Use "python" after activating environment

        // Construct the command to activate the environment and run the script
        String command = String.format(
                "bash -c 'source %s ApplicationLinux && %s %s --image-path \"%s\" --project-dir \"%s\"'",
                condaActivatePath, pythonExecutable, PYTHON_SCRIPT, imageFile.getAbsolutePath(), PROJECT_DIR
        );

        System.out.println("Executing command: " + command);

        // Record start time
        long startTime = System.currentTimeMillis();

        // Execute the command using ProcessBuilder
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
        processBuilder.redirectErrorStream(true); // Redirect error stream for easier debugging

        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        // Record end time
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        if (exitCode == 0) {
            System.out.println("Processed: " + imageFile.getName());
            System.out.println("Duration: " + duration + "ms");
        } else {
            System.err.println("Failed to process: " + imageFile.getName());
            System.err.println("Duration: " + duration + "ms");
            try (var errorStream = process.getInputStream()) {
                System.err.println(new String(errorStream.readAllBytes())); // Print error output
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
