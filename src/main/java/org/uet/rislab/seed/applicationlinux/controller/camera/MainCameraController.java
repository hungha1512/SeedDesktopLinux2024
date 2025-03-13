package org.uet.rislab.seed.applicationlinux.controller.camera;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.uet.rislab.seed.applicationlinux.global.AppProperties;
import org.uet.rislab.seed.applicationlinux.service.CaptureCameraService;
import org.uet.rislab.seed.applicationlinux.service.ConnectCameraService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ResourceBundle;

public class MainCameraController implements Initializable {
    @FXML
    public ImageView iv_camera;
    @FXML
    public Button btn_capture;
    @FXML
    public Button btn_next_capture;
    @FXML
    public Button btn_recapture;
    @FXML
    public GridPane gp_image_view;

    private ConnectCameraService connectCameraService = new ConnectCameraService();
    private CaptureCameraService captureCameraService;

    private String imageFolderPath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageFolderPath = AppProperties.getProperty("parentPath") + "/Image";

        connectCameraService.cleanupCamera();
        connectCameraService.initCamera();
        connectCameraService.startLiveView(iv_camera);

        captureCameraService = new CaptureCameraService(
                connectCameraService.getCamera(),
                connectCameraService.getContext());

        captureCameraService.setConnectCameraService(connectCameraService);

        btn_capture.setOnAction(event -> {
            try {
                String outputPath = AppProperties.getProperty("parentPath") + "/Image";
                captureCameraService.captureImage(outputPath);
                connectCameraService.startLiveView(iv_camera);
                loadImageGrid();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error capturing image.");
            }
            btn_capture.setVisible(false);
            btn_next_capture.setVisible(true);
            btn_recapture.setVisible(true);
        });

        btn_next_capture.setOnAction(event -> {
            connectCameraService.startLiveView(iv_camera);
            btn_capture.setVisible(true);
            btn_next_capture.setVisible(false);
            btn_recapture.setVisible(false);
        });

        btn_recapture.setOnAction(event -> {
            boolean deleted = captureCameraService.deleteLatestCapture();
            if (deleted) {
                connectCameraService.startLiveView(iv_camera);
                btn_capture.setVisible(true);
                btn_next_capture.setVisible(false);
                btn_recapture.setVisible(false);
                loadImageGrid();
            } else {
                System.err.println("Failed to delete the latest image or no image to delete.");
            }
        });

        loadImageGrid();
    }

    private void loadImageGrid() {
        gp_image_view.getChildren().clear();
        gp_image_view.getColumnConstraints().clear();
        gp_image_view.getRowConstraints().clear();
        gp_image_view.setHgap(10);
        gp_image_view.setVgap(10);

        File imageFolder = new File(imageFolderPath);
        File[] images = imageFolder.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png"));

        if (images == null || images.length == 0) {
            return;
        }
        Arrays.sort(images, Comparator.comparingLong(File::lastModified).reversed());

        // Create a background Task
        Task<Void> loadImagesTask = new Task<>() {
            @Override
            protected Void call() {
                for (int i = 0; i < images.length; i++) {

                    if (isCancelled()) {
                        break;
                    }
                    final File imageFile = images[i];

                    Image thumbnail = new Image(imageFile.toURI().toString(), 95, 95, true, true);

                    final int index = i;
                    Platform.runLater(() -> {
                        ImageView imageView = new ImageView(thumbnail);
                        imageView.setFitWidth(95);
                        imageView.setFitHeight(95);
                        imageView.setPreserveRatio(true);

                        Label imageNameLabel = new Label(imageFile.getName());
                        imageNameLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #333333;");
                        imageNameLabel.setWrapText(true);
                        imageNameLabel.setPrefWidth(95);

                        VBox imageBox = new VBox(5, imageView, imageNameLabel);
                        imageBox.setAlignment(Pos.CENTER);

                        gp_image_view.add(imageBox, index, 0);
                    });

                }
                return null;
            }
        };

        Thread loadThread = new Thread(loadImagesTask);
        loadThread.setDaemon(true);
        loadThread.start();
    }

    @FXML
    public void onPageExit() {
        connectCameraService.cleanupCamera();
    }
}
