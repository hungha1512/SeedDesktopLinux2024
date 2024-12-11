package org.uet.rislab.seed.applicationlinux.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import org.uet.rislab.seed.applicationlinux.controller.camera.MainCameraController;
import org.uet.rislab.seed.applicationlinux.controller.project.MainProjectController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    public Button btn_home_page;
    @FXML
    public Button btn_project;
    @FXML
    public Button btn_camera;
    @FXML
    public Button btn_analyze;
    @FXML
    public Button btn_result;
    @FXML
    public StackPane main_screen;

    private Object currentPageController;

    public void setContent(Parent newContentPane) {
        main_screen.getChildren().clear();
        main_screen.getChildren().add(newContentPane);
    }

    public void getContentPane(String fileFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fileFXML));
            Parent newContentPane = loader.load();

            if (fileFXML.contains("main-project.fxml")) {
                MainProjectController mainProjectController = loader.getController();
                mainProjectController.setDashboardController(this);
            }

            // Clean up resources for the current controller before switching
            if (currentPageController instanceof MainCameraController) {
                ((MainCameraController) currentPageController).onPageExit();
            }

            // Update current controller
            currentPageController = loader.getController();

            setContent(newContentPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        getContentPane("/org/uet/rislab/seed/applicationlinux/view/homepage/home-page.fxml");

        btn_project.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getContentPane("/org/uet/rislab/seed/applicationlinux/view/project/main-project.fxml");

            }
        });

        btn_home_page.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getContentPane("/org/uet/rislab/seed/applicationlinux/view/homepage/home-page.fxml");
            }
        });

        btn_camera.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getContentPane("/org/uet/rislab/seed/applicationlinux/view/camera/main-camera.fxml");
            }
        });

        btn_analyze.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getContentPane("/org/uet/rislab/seed/applicationlinux/view/analyze/main-analyze.fxml");
            }
        });

        btn_result.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getContentPane("/org/uet/rislab/seed/applicationlinux/view/result/main-result.fxml");
            }
        });
    }
}
