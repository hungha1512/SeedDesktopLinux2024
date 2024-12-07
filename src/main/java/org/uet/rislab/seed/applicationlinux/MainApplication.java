package org.uet.rislab.seed.applicationlinux;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/org/uet/rislab/seed/applicationlinux/view/dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("UET Application");
        stage.getIcons().add(new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("/org/uet/rislab/seed/applicationlinux/media/logo4.png"))));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}