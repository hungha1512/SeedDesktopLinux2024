package org.uet.rislab.seed.applicationlinux.controller.camera;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import org.uet.rislab.seed.applicationlinux.global.AppProperties;
import org.uet.rislab.seed.applicationlinux.service.CaptureCameraService;
import org.uet.rislab.seed.applicationlinux.service.ConnectCameraService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainCameraController implements Initializable {
    @FXML
    public ImageView iv_camera;
    public Button btn_capture;
    public Button btn_next_capture;
    public Button btn_recapture;

    private ConnectCameraService connectCameraService = new ConnectCameraService();
    private CaptureCameraService captureCameraService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

                // Use the already initialized captureCameraService
                captureCameraService.captureImage(outputPath);
                connectCameraService.startLiveView(iv_camera);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error capturing image.");
            }
            btn_capture.setVisible(false);
            btn_next_capture.setVisible(true);
            btn_recapture.setVisible(true);
        });
    }

    @FXML
    public void onPageExit() {
        connectCameraService.cleanupCamera();
    }
}
