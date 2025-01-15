package org.uet.rislab.seed.applicationlinux.controller.project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.uet.rislab.seed.applicationlinux.controller.DashboardController;
import org.uet.rislab.seed.applicationlinux.global.AppProperties;
import org.uet.rislab.seed.applicationlinux.model.enums.EAwns;
import org.uet.rislab.seed.applicationlinux.model.enums.EColor;
import org.uet.rislab.seed.applicationlinux.model.enums.ESeedType;
import org.uet.rislab.seed.applicationlinux.service.AlertService;

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
    public TextField txt_weight;
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
    @FXML
    public TextField txt_ground_truth;

    private final AlertService alertService = new AlertService();

    private DashboardController dashboardController;

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

        txt_weight.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
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
        DirectoryChooser directoryChooser = new javafx.stage.DirectoryChooser();
        directoryChooser.setTitle("Chọn Thư Mục Dự Án");
        File selectedDirectory = directoryChooser.showDialog(btn_parent_path.getScene().getWindow());

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

        String projectName = txt_project_name.getText().trim();
        String projectDescription = txt_project_description.getText().trim();
        String weightInput = txt_weight.getText().trim();
        String groundTruth = txt_ground_truth.getText().trim();

        if (!isInputValid(projectName, projectDescription)) {
            alertService.showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Tên dự án và mô tả không được để trống");
            return;
        }

        weightInput = weightInput.replace(",", ".");
        double weight;

        groundTruth = groundTruth.replace(",", ".");
        double groundTruthValue;

        try {
            weight = Double.parseDouble(weightInput);
        } catch (NumberFormatException e) {
            alertService.showAlert(Alert.AlertType.ERROR, "Lỗi", "Trọng lượng không hợp lệ! Vui lòng nhập số hợp lệ.");
            return;
        }

        try {
            groundTruthValue = Double.parseDouble(groundTruth);
        } catch (NumberFormatException e) {
            alertService.showAlert(Alert.AlertType.ERROR, "Lỗi", "Giá trị vật đố chứng không hợp lệ! Vui lòng nhập số hợp lệ.");
            return;
        }

        File projectFolder = createProjectFolders(lbl_parent_path.getText(), projectName);

        saveProjectProperties(projectFolder, projectName, projectDescription, eSeedType, eAwns, eColor, weight, groundTruthValue);

        alertService.showAlert(Alert.AlertType.INFORMATION, "Tạo Dự Án Thành Công", "Dự án đã được tạo thành công");
        navigateToProjectPage();
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

    private File createProjectFolders(String parentPath, String projectName) throws IOException {
        File projectFolder = new File(parentPath, projectName);
        if (!projectFolder.exists()) {
            projectFolder.mkdirs();
        }

        createFolderIfNotExists(new File(projectFolder, "Image"));
        createFolderIfNotExists(new File(projectFolder, "Result"));
        createFolderIfNotExists(new File(projectFolder, "Image_analysis"));
        return projectFolder;
    }

    private void createFolderIfNotExists(File folder) {
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private void saveProjectProperties(File projectFolder, String projectName, String description,
                                       String eSeedType, String eAwns, String eColor, double weight, double groundTruth) throws IOException {
        File propertiesFile = new File(projectFolder, "application.properties");
        if (!propertiesFile.exists()) {
            propertiesFile.createNewFile();
        }

        AppProperties.setPropertiesFilePath(propertiesFile.getAbsolutePath());

        AppProperties.setProperty("projectName", projectName);
        AppProperties.setProperty("description", description);
        AppProperties.setProperty("weight", String.valueOf(weight));
        AppProperties.setProperty("groundTruth", String.valueOf(groundTruth));
        AppProperties.setProperty("eSeedType", eSeedType);
        AppProperties.setProperty("eAwns", eAwns);
        AppProperties.setProperty("eColor", eColor);
        AppProperties.setProperty("creationDate", new java.util.Date().toString());
        AppProperties.setProperty("updateDate", new java.util.Date().toString());
        AppProperties.setProperty("parentPath", projectFolder.getAbsolutePath());
    }

    private void navigateToProjectPage() {
        try {
            String globalPropertiesPath = "src/main/resources/application.properties";
            AppProperties.setPropertiesFilePath(globalPropertiesPath);
            AppProperties.setProperty("isProjectOpened", "true");
            AppProperties.setProperty("parentPath", lbl_parent_path.getText() + "/" + txt_project_name.getText());
            AppProperties.setProperty("projectName", txt_project_name.getText());
            AppProperties.setProperty("description", txt_project_description.getText());
            AppProperties.setProperty("weight", txt_weight.getText());
            AppProperties.setProperty("groundTruth", txt_ground_truth.getText());
            AppProperties.setProperty("eSeedType", getComboBoxValue(cb_seed_type));
            AppProperties.setProperty("eAwns", getComboBoxValue(cb_awns));
            AppProperties.setProperty("eColor", getComboBoxValue(cb_color));
            AppProperties.setProperty("updateDate", new java.util.Date().toString());

            System.out.println("Global application.properties updated successfully.");

            if (dashboardController != null) {
                dashboardController.getContentPane("/org/uet/rislab/seed/applicationlinux/view/project/main-project.fxml");
            }

            Stage currentStage = (Stage) btn_create.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            alertService.showAlert(Alert.AlertType.ERROR, "Lỗi Điều Hướng", "Không thể tải lại trang chính của dự án.");
        }
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }
}
