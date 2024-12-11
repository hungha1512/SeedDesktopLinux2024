package org.uet.rislab.seed.applicationlinux.service;

import javafx.scene.control.Alert;

public class AlertService {
    public void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
