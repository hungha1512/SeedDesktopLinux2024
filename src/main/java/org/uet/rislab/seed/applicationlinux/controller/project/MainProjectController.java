package org.uet.rislab.seed.applicationlinux.controller.project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.uet.rislab.seed.applicationlinux.controller.DashboardController;
import org.uet.rislab.seed.applicationlinux.global.AppProperties;
import org.uet.rislab.seed.applicationlinux.model.enums.EAwns;
import org.uet.rislab.seed.applicationlinux.model.enums.EColor;
import org.uet.rislab.seed.applicationlinux.model.enums.ESeedType;
import org.uet.rislab.seed.applicationlinux.service.AlertService;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Properties;
import java.util.ResourceBundle;

public class MainProjectController implements Initializable {
    @FXML
    public Button btn_create;
    @FXML
    public Button btn_open;
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
    public Button btn_edit_project;
    @FXML
    public Button btn_cancel;
    @FXML
    public Label lbl_parent_path;
    @FXML
    public Button btn_edit_save;

    private DashboardController dashboardController;

    private final AlertService alertService = new AlertService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AppProperties.setPropertiesFilePath("src/main/resources/application.properties");
        // Input data to project name text field
        try {
            txt_project_name.setText(new String(AppProperties.getProperty("projectName").getBytes(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        txt_project_name.setEditable(false);

        // Input data to project description text field
        try {
            txt_project_description.setText(new String(AppProperties.getProperty("description").getBytes(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        txt_project_description.setEditable(false);

        try {
            txt_weight.setText(new String(AppProperties.getProperty("weight").getBytes(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        txt_weight.setEditable(false);

        // Input data to seed type combo box
        try {
            cb_seed_type.setValue(new String(AppProperties.getProperty("eSeedType").getBytes(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        cb_seed_type.setDisable(true);

        // Input data to awns combo box
        cb_awns.setValue(AppProperties.getProperty("eAwns"));
        cb_awns.setDisable(true);

        // Input data to color combo box
        cb_color.setValue(AppProperties.getProperty("eColor"));
        cb_color.setDisable(true);

        // Input data to parent path label
        lbl_parent_path.setText(AppProperties.getProperty("parentPath"));
        btn_parent_path.setDisable(true);

        btn_edit_project.setOnAction(event -> handleEditProject());

        btn_create.setOnAction(event -> handleCreateProject());
        btn_open.setOnAction(event -> handleOpenProject());

    }

    public void handleCreateProject() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/uet/rislab/seed/applicationlinux/view/project/create-project.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Tạo Dự Án Mới");
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            CreateProjectController controller = loader.getController();
            controller.setDashboardController(dashboardController);

            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handleOpenProject() {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Chọn Tệp application.properties");
        fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Properties Files", "*.properties"));
        java.io.File selectedFile = fileChooser.showOpenDialog(btn_open.getScene().getWindow());

        if (selectedFile != null) {
            try {
                AppProperties.setPropertiesFilePath("src/main/resources/application.properties");

                Properties properties = new Properties();
                try (InputStreamReader reader = new InputStreamReader(new FileInputStream(selectedFile), StandardCharsets.UTF_8)) {
                    properties.load(reader);
                }

                // Update UI fields with loaded properties
                txt_project_name.setText(properties.getProperty("projectName", ""));
                txt_project_description.setText(properties.getProperty("description", ""));
                txt_weight.setText(properties.getProperty("weight", ""));
                cb_seed_type.setValue(properties.getProperty("eSeedType", ""));
                cb_awns.setValue(properties.getProperty("eAwns", ""));
                cb_color.setValue(properties.getProperty("eColor", ""));
                lbl_parent_path.setText(properties.getProperty("parentPath", "Chưa chọn thư mục nào"));

                // Update global properties
                AppProperties.setProperty("projectName", properties.getProperty("projectName", ""));
                AppProperties.setProperty("description", properties.getProperty("description", ""));
                AppProperties.setProperty("weight", properties.getProperty("weight", ""));
                AppProperties.setProperty("eSeedType", properties.getProperty("eSeedType", ""));
                AppProperties.setProperty("eAwns", properties.getProperty("eAwns", ""));
                AppProperties.setProperty("eColor", properties.getProperty("eColor", ""));
                AppProperties.setProperty("parentPath", properties.getProperty("parentPath", ""));
                AppProperties.setProperty("isProjectOpened", "true");

                System.out.println("Dự án đã được mở thành công từ: " + selectedFile.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Lỗi khi mở file properties.");
            }
        } else {
            System.out.println("Chua chon file");
        }
    }

    public void handleEditProject() {
        txt_project_name.setEditable(true);
        txt_weight.setEditable(true);
        txt_project_description.setEditable(true);
        cb_seed_type.setDisable(false);
        cb_awns.setDisable(false);
        cb_color.setDisable(false);
        lbl_parent_path.setDisable(false);
        btn_parent_path.setDisable(false);

        btn_edit_project.setVisible(false);
        btn_edit_save.setVisible(true);
        btn_cancel.setVisible(true);

        btn_parent_path.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Chọn Thư Mục Dự Án");
            File selectedDirectory = directoryChooser.showDialog(btn_parent_path.getScene().getWindow());

            if (selectedDirectory != null) {
                lbl_parent_path.setText(selectedDirectory.getAbsolutePath());
            }
        });

        cb_seed_type.getItems().clear();
        cb_seed_type.getItems().addAll(Arrays.stream(ESeedType.values()).map(ESeedType::getType).toArray(String[]::new));
        cb_seed_type.setValue(AppProperties.getProperty("eSeedType"));

        cb_awns.getItems().clear();
        cb_awns.getItems().addAll(Arrays.stream(EAwns.values()).map(EAwns::getAwns).toArray(String[]::new));
        cb_awns.setValue(AppProperties.getProperty("eAwns"));

        cb_color.getItems().clear();
        cb_color.getItems().addAll(Arrays.stream(EColor.values()).map(EColor::getColor).toArray(String[]::new));
        cb_color.setValue(AppProperties.getProperty("eColor"));

        btn_edit_save.setOnAction(event -> {
            File propertiesFile;
            Properties properties = new Properties();
            try {
                String newProjectName = txt_project_name.getText().trim();
                String projectDescription = txt_project_description.getText().trim();
                String weightInput = txt_weight.getText().trim();
                String seedType = cb_seed_type.getValue().toString();
                String awns = cb_awns.getValue().toString();
                String color = cb_color.getValue().toString();
                String oldParentPath = AppProperties.getProperty("parentPath");
                String newParentPath = lbl_parent_path.getText();

                weightInput = weightInput.replace(",", ".");
                double weight;

                try {
                    weight = Double.parseDouble(weightInput);
                } catch (NumberFormatException e) {
                    alertService.showAlert(Alert.AlertType.ERROR, "Lỗi", "Trọng lượng không hợp lệ! Vui lòng nhập số hợp lệ.");
                    return;
                }

                if (newProjectName.isEmpty() || projectDescription.isEmpty() || newParentPath.isEmpty()) {
                    alertService.showAlert(Alert.AlertType.ERROR, "Lỗi", "Các trường không được để trống!");
                    return;
                }

                File oldProjectFolder = new File(oldParentPath);

                // Case 1: Change project name only
                if (!AppProperties.getProperty("projectName").equals(newProjectName) && oldParentPath.equals(newParentPath)) {
                    File parentDirectory = oldProjectFolder.getParentFile();
                    File newProjectFolder = new File(parentDirectory, newProjectName);

                    if (newProjectFolder.exists()) {
                        alertService.showAlert(Alert.AlertType.ERROR, "Lỗi", "Thư mục mới đã tồn tại. Vui lòng chọn tên khác.");
                        return;
                    }

                    updateProjectFolder(oldProjectFolder, newProjectFolder);
                }
                // Case 2: Change parent path only
                else if (!oldParentPath.equals(newParentPath)) {
                    File newProjectFolder = new File(newParentPath, newProjectName);

                    if (newProjectFolder.exists()) {
                        alertService.showAlert(Alert.AlertType.ERROR, "Lỗi", "Thư mục đích đã tồn tại. Vui lòng chọn tên khác.");
                        return;
                    }

                    updateProjectFolder(oldProjectFolder, newProjectFolder);
                }
                // Case 3: Nothing changes
                else {
                    AppProperties.setPropertiesFilePath(newParentPath + "/application.properties");
                }

                // Update properties
                AppProperties.setProperty("projectName", newProjectName);
                AppProperties.setProperty("description", projectDescription);
                AppProperties.setProperty("weight", String.valueOf(weight));
                AppProperties.setProperty("eSeedType", seedType);
                AppProperties.setProperty("eAwns", awns);
                AppProperties.setProperty("eColor", color);
                AppProperties.setProperty("parentPath", newParentPath);
                AppProperties.setProperty("isProjectOpened", "true");

                resetEditMode();
                alertService.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Dự án đã được cập nhật thành công!");

                // Reload project data
                reloadProjectData();
            } catch (Exception e) {
                e.printStackTrace();
                alertService.showAlert(Alert.AlertType.ERROR, "Lỗi", "Có lỗi xảy ra khi lưu dự án: " + e.getMessage());
            }
        });

        btn_cancel.setOnAction(event -> {
            reloadProjectData();
            resetEditMode();
        });
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    private void reloadProjectData() {
        txt_project_name.setText(AppProperties.getProperty("projectName"));
        txt_project_description.setText(AppProperties.getProperty("description"));
        txt_weight.setText(AppProperties.getProperty("weight"));
        cb_seed_type.setValue(AppProperties.getProperty("eSeedType"));
        cb_awns.setValue(AppProperties.getProperty("eAwns"));
        cb_color.setValue(AppProperties.getProperty("eColor"));
        lbl_parent_path.setText(AppProperties.getProperty("parentPath"));
    }


    private void resetEditMode() {
        txt_project_name.setEditable(false);
        txt_project_description.setEditable(false);
        txt_weight.setEditable(false);
        cb_seed_type.setDisable(true);
        cb_awns.setDisable(true);
        cb_color.setDisable(true);
        lbl_parent_path.setDisable(true);
        btn_parent_path.setDisable(true);

        btn_edit_project.setVisible(true);
        btn_edit_save.setVisible(false);
        btn_cancel.setVisible(false);
    }

    private void copyFolder(File source, File target) throws IOException {
        if (source.isDirectory()) {
            if (!target.exists()) {
                target.mkdirs();
            }
            String[] children = source.list();
            for (String file : children) {
                copyFolder(new File(source, file), new File(target, file));
            }
        } else {
            Files.copy(source.toPath(), target.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    private void updateProjectFolder(File oldProjectFolder, File newProjectFolder) throws IOException {
        // Sao chép và xóa thư mục cũ
        copyFolder(oldProjectFolder, newProjectFolder);
        deleteFolder(oldProjectFolder);

        // Tạo file application.properties trong thư mục mới
        File propertiesFile = new File(newProjectFolder, "application.properties");
        if (!propertiesFile.exists()) {
            propertiesFile.createNewFile();
        }

        // Cập nhật file properties
        AppProperties.setPropertiesFilePath(propertiesFile.getAbsolutePath());
        AppProperties.setProperty("parentPath", newProjectFolder.getAbsolutePath());
    }

}
