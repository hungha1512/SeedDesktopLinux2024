package org.uet.rislab.seed.applicationlinux.controller.project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.uet.rislab.seed.applicationlinux.global.AppProperties;
import org.uet.rislab.seed.applicationlinux.model.enums.EAwns;
import org.uet.rislab.seed.applicationlinux.model.enums.EColor;
import org.uet.rislab.seed.applicationlinux.model.enums.ESeedType;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class CreateProjectController implements Initializable {

    @FXML
    public TextField txt_project_name;
    @FXML
    public TextField txt_project_description;
    @FXML
    public ComboBox cb_seed_type;
    @FXML
    public ComboBox cb_awns;
    @FXML
    public ComboBox cb_color;
    @FXML
    public Button btn_parent_path;
    @FXML
    public Button btn_create;
    @FXML
    public Button btn_cancel;
    @FXML
    public Label lbl_parent_path;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set the enter key for project name text field
        txt_project_name.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                try {
                    handleCreate();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Set the enter key for project description text field
        txt_project_description.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                try {
                    handleCreate();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Initialize the seed type combobox
        cb_seed_type.getItems().clear();
        cb_seed_type.getItems().addAll(Arrays.stream(ESeedType.values()).map(ESeedType::getType).toArray(String[]::new));
        cb_seed_type.getSelectionModel().select(0);

        // Initialize the awns combobox
        cb_awns.getItems().clear();
        cb_awns.getItems().addAll(Arrays.stream(EAwns.values()).map(EAwns::getAwns).toArray(String[]::new));
        cb_awns.getSelectionModel().select(0);

        // Initialize the color combobox
        cb_color.getItems().clear();
        cb_color.getItems().addAll(Arrays.stream(EColor.values()).map(EColor::getColor).toArray(String[]::new));
        cb_color.getSelectionModel().select(0);

        // Set the action for the parent path button
        btn_parent_path.setOnAction(event -> handleParentPath());

        // Set the action for the create button
        btn_create.setOnAction(event -> {
            try {
                handleCreate();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Set the action for the cancel button
        btn_cancel.setOnAction(event -> handleCancel());
    }

    public void handleParentPath() {
        javafx.stage.DirectoryChooser directoryChooser = new javafx.stage.DirectoryChooser();
        directoryChooser.setTitle("Chọn Thư Mục Dự Án");
        java.io.File selectedDirectory = directoryChooser.showDialog(btn_parent_path.getScene().getWindow());

        if (selectedDirectory != null) {
            lbl_parent_path.setText(selectedDirectory.getAbsolutePath());
        } else {
            lbl_parent_path.setText("Chưa chọn thư mục nào");
        }
    }

    public void handleCreate() throws IOException {
        String eSeedType = getComboBoxValue(cb_seed_type);
        String eAwns = getComboBoxValue(cb_awns);
        String eColor = getComboBoxValue(cb_color);

        String projectName = txt_project_name.getText();
        String projectDescription = txt_project_description.getText();
        if (!isInputValid(projectName, projectDescription)) {
            showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Tên dự án và mô tả không được để trống");
            return;
        }

        File projectFolder = createProjectFolders(lbl_parent_path.getText(), projectName);

        saveProjectProperties(projectFolder, projectName, projectDescription, eSeedType, eAwns, eColor);

        showAlert(Alert.AlertType.INFORMATION, "Tạo Dự Án Thành Công", "Dự án đã được tạo thành công");
//        navigateToProjectPage(lbl_parent_path.getText());
    }

    public void handleCancel() {
        Stage stage = (Stage) btn_cancel.getScene().getWindow();
        stage.close();
    }

    private String getComboBoxValue(ComboBox comboBox) {
        return comboBox.getSelectionModel().getSelectedItem() != null
                ? comboBox.getSelectionModel().getSelectedItem().toString()
                : "";
    }

    private boolean isInputValid(String projectName, String projectDescription) {
        return !(projectName == null || projectName.trim().isEmpty() ||
                projectDescription == null || projectDescription.trim().isEmpty());
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private File createProjectFolders(String parentPath, String projectName) throws IOException {
        File projectFolder = new File(parentPath, projectName);
        if (!projectFolder.exists()) {
            projectFolder.mkdirs();
        }

        createFolderIfNotExists(new File(projectFolder, "Ảnh chụp"));
        createFolderIfNotExists(new File(projectFolder, "Kết quả"));
        createFolderIfNotExists(new File(projectFolder, "Phân tích"));
        return projectFolder;
    }

    private void createFolderIfNotExists(File folder) {
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private void saveProjectProperties(File projectFolder, String projectName, String description,
                                       String eSeedType, String eAwns, String eColor) throws IOException {
        File propertiesFile = new File(projectFolder, "application.properties");
        if (!propertiesFile.exists()) {
            propertiesFile.createNewFile();
        }

        AppProperties.setPropertiesFilePath(propertiesFile.getAbsolutePath());

        AppProperties.setProperty("projectName", projectName);
        AppProperties.setProperty("description", description);
        AppProperties.setProperty("eSeedType", eSeedType);
        AppProperties.setProperty("eAwns", eAwns);
        AppProperties.setProperty("eColor", eColor);
        AppProperties.setProperty("creationDate", new java.util.Date().toString());
        AppProperties.setProperty("updateDate", new java.util.Date().toString());
    }

//    private void navigateToProjectPage(String parentPath) {
//        System.out.println("Navigating to project page for project in folder: " + parentPath);
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/uet/rislab/seed/applicationlinux/view/project/projectPage.fxml"));
//        Parent root;
//        try {
//            root = loader.load();
//            ProjectPageController controller = loader.getController();
//            controller.setParentPath(parentPath);
//            Stage stage = (Stage) btn_create.getScene().getWindow();
//            stage.setScene(new Scene(root));
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
