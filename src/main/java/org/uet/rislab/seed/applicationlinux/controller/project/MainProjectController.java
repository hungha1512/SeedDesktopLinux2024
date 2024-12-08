package org.uet.rislab.seed.applicationlinux.controller.project;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainProjectController implements Initializable {
    public Button btn_create;
    public Button btn_open;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_create.setOnAction(event -> handleCreateProject());
        btn_open.setOnAction(event -> handleOpenProject());
    }

    public void handleCreateProject() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/uet/rislab/seed/applicationlinux/view/project/create-project.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Create Project");
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            CreateProjectController controller = loader.getController();
            controller.initialize(null, null);

            stage.showAndWait();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void handleOpenProject() {
        System.out.println("Open project clicked");
    }
}
