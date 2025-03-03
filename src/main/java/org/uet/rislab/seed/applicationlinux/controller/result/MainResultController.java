package org.uet.rislab.seed.applicationlinux.controller.result;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.uet.rislab.seed.applicationlinux.global.AppProperties;
import org.uet.rislab.seed.applicationlinux.model.entity.Result;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainResultController implements Initializable {

    @FXML
    private ScrollPane sp_image;

    @FXML
    private ImageView iv_result;

    @FXML
    private TableView<Result> tv_result;

    @FXML
    private TableColumn<Result, Integer> tc_id;

    @FXML
    private TableColumn<Result, Double> tc_height;

    @FXML
    private TableColumn<Result, Double> tc_width;

    @FXML
    public Button btn_next;

    @FXML
    public Button btn_back;

    @FXML
    private Slider slider_zoom;

    @FXML
    public Label lbl_image;

    @FXML
    public Label lbl_project_name;

    @FXML
    public Label lbl_color;

    @FXML
    public Label lbl_type;

    @FXML
    public Label lbl_weight;

    @FXML
    public Label lbl_image_result;

    private static final String IMAGE_ANALYSIS_PATH = AppProperties.getProperty("parentPath") + "/Image_analysis";
    private static final String CSV_RESULT_PATH = AppProperties.getProperty("parentPath") + "/Result";

    private File[] imageFiles;
    private File[] csvFiles;
    private int currentIndex = 0;

    private double zoomFactor = 0.1;
    private double zoomStep = 0.1;
    private final double minZoom = 0.2;
    private final double maxZoom = 1;
    private double dragStartX, dragStartY;
    private double originalWidth, originalHeight;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        setupImageInteraction();
        loadFiles();
        if (imageFiles.length > 0 && csvFiles.length > 0) {
            displayResult(currentIndex);
        }
        setupNavigationButtons();

        lbl_project_name.setText(AppProperties.getProperty("projectName"));
        lbl_color.setText(AppProperties.getProperty("eColor"));
        lbl_type.setText(AppProperties.getProperty("eAwns") + " râu");
        lbl_weight.setText(AppProperties.getProperty("weight") + " gram");
    }

    private void setupTableColumns() {
        tc_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tc_height.setCellValueFactory(new PropertyValueFactory<>("height"));
        tc_width.setCellValueFactory(new PropertyValueFactory<>("width"));
    }

    private void loadFiles() {
        File imageDir = new File(IMAGE_ANALYSIS_PATH);
        File csvDir = new File(CSV_RESULT_PATH);

        imageFiles = imageDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));
        csvFiles = csvDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

        if (imageFiles == null || imageFiles.length == 0) {
            System.err.println("No image files found in " + IMAGE_ANALYSIS_PATH);
        }
        if (csvFiles == null || csvFiles.length == 0) {
            System.err.println("No CSV files found in " + CSV_RESULT_PATH);
        }
    }

    private void displayResult(int index) {
        if (index < 0 || index >= imageFiles.length || index >= csvFiles.length) {
            System.err.println("Invalid index: " + index);
            return;
        }

        loadImage(imageFiles[index]);
        loadCSV(csvFiles[index]);
        lbl_image_result.setText(imageFiles[index].getName());
        updateImageLabel();
    }

    private void loadImage(File imageFile) {
        if (imageFile.exists()) {
            Image image = new Image(imageFile.toURI().toString());
            iv_result.setImage(image);

            originalWidth = image.getWidth();
            originalHeight = image.getHeight();

            iv_result.setFitWidth(originalWidth);
            iv_result.setFitHeight(originalHeight);

            slider_zoom.setValue(zoomFactor);
            updateImageScale();
        } else {
            System.err.println("Image not found: " + imageFile.getAbsolutePath());
            lbl_image.setText("Image not found.");
        }
    }

    private void loadCSV(File csvFile) {
        if (csvFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                List<Result> results = new ArrayList<>();
                String line;

                // Skip header
                reader.readLine();

                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 5) {
                        int id = Integer.parseInt(data[0].trim());
                        double height = Double.parseDouble(data[1].trim());
                        double width = Double.parseDouble(data[2].trim());

                        height = Math.round(height * 100.0) / 100.0;
                        width = Math.round(width * 100.0) / 100.0;

                        results.add(new Result(id, height, width));
                    }
                }
                tv_result.getItems().setAll(results);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("CSV file not found: " + csvFile.getAbsolutePath());
        }
    }

    private void setupNavigationButtons() {
        btn_next.setOnAction(event -> {
            if (currentIndex < Math.min(imageFiles.length, csvFiles.length) - 1) {
                currentIndex++;
                displayResult(currentIndex);
            }
        });

        btn_back.setOnAction(event -> {
            if (currentIndex > 0) {
                currentIndex--;
                displayResult(currentIndex);
            }
        });
    }

    private void updateImageLabel() {
        lbl_image.setText((currentIndex + 1) + "/" + imageFiles.length);
    }

    private void setupImageInteraction() {
        // Zoom bằng slider
        slider_zoom.valueProperty().addListener((obs, oldVal, newVal) -> {
            zoomFactor = newVal.doubleValue();
            iv_result.setScaleX(zoomFactor);
            iv_result.setScaleY(zoomFactor);
        });

        // Zoom bằng cuộn chuột
        iv_result.setOnScroll(event -> {
            if (event.getDeltaY() > 0) {
                zoomFactor += zoomStep;  // Phóng to
            } else {
                zoomFactor -= zoomStep;  // Thu nhỏ
            }

            // Giới hạn zoom
            zoomFactor = Math.max(minZoom, Math.min(zoomFactor, maxZoom));
            slider_zoom.setValue(zoomFactor);
            updateImageScale();
        });

        // Kéo ảnh bằng chuột
        iv_result.setOnMousePressed(event -> {
            dragStartX = event.getSceneX();
            dragStartY = event.getSceneY();
        });

        iv_result.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - dragStartX;
            double deltaY = event.getSceneY() - dragStartY;

            sp_image.setHvalue(sp_image.getHvalue() - deltaX / sp_image.getWidth());
            sp_image.setVvalue(sp_image.getVvalue() - deltaY / sp_image.getHeight());

            dragStartX = event.getSceneX();
            dragStartY = event.getSceneY();
        });
    }

    private void updateImageScale() {
        iv_result.setFitWidth(originalWidth * zoomFactor);
        iv_result.setFitHeight(originalHeight * zoomFactor);
        sp_image.setHvalue(0.5);
        sp_image.setVvalue(0.5);
    }
}
