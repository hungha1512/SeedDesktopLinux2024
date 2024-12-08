package org.uet.rislab.seed.applicationlinux.controller.python;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

public class PythonTestController implements Initializable {
    @FXML
    private Button btn_run_python;
    @FXML
    private TextArea txt_output;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_run_python.setOnAction(event -> runPythonScriptWithParams("5", "10")); // Example params
    }

    private void runPythonScriptWithParams(String... params) {
        try {
            // Path to the Python file
            String filePath = System.getProperty("user.dir") + "/src/main/java/org/uet/rislab/seed/applicationlinux/pythoncore/test_file.py";

            // Create the command to execute the Python file with parameters
            ProcessBuilder processBuilder = new ProcessBuilder(buildCommand(filePath, params));

            // Start the process
            Process process = processBuilder.start();

            // Read the script output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Read any errors
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder errors = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errors.append(line).append("\n");
            }

            // Display output or errors
            if (errors.length() > 0) {
                txt_output.setText("Error:\n" + errors.toString());
            } else {
                txt_output.setText("Output:\n" + output.toString());
            }

        } catch (IOException e) {
            txt_output.setText("Exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to build the command with parameters
    private String[] buildCommand(String filePath, String... params) {
        String[] command = new String[2 + params.length];
        command[0] = "python3"; // Python interpreter
        command[1] = filePath;  // Python file path

        // Add parameters to the command
        System.arraycopy(params, 0, command, 2, params.length);
        return command;
    }
}
