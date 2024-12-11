package org.uet.rislab.seed.applicationlinux.controller.camera;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import org.uet.rislab.seed.applicationlinux.service.ConnectCameraService;

import java.net.URL;
import java.util.ResourceBundle;

public class MainCameraController implements Initializable {
    @FXML
    public ImageView iv_camera;
    public Button btn_capture;
    public Button btn_next_capture;
    public Button btn_recapture;

    private ConnectCameraService connectCameraService = new ConnectCameraService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectCameraService.cleanupCamera();
        connectCameraService.initCamera();
        connectCameraService.startLiveView(iv_camera);


    }

    @FXML
    public void onPageExit() {
        connectCameraService.cleanupCamera();
    }
}
