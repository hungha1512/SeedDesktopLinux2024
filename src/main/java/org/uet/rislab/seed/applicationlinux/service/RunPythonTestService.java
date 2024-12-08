package org.uet.rislab.seed.applicationlinux.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RunPythonTestService {
    /**
     * Runs a Python script with optional parameters.
     *
     * @param filePath The absolute path to the Python script.
     * @param params   Command-line parameters to pass to the script.
     * @return The output or errors from the script execution.
     */
    public String runPythonScript(String filePath, String... params) {
        StringBuilder output = new StringBuilder();
        StringBuilder errors = new StringBuilder();

        try {
            // Build the command: python3 <filePath> <param1> <param2> ...
            List<String> command = new ArrayList<>();
            command.add("python3"); // Python executable
            command.add(filePath);  // Python file path
            for (String param : params) {
                command.add(param);
            }

            // Initialize ProcessBuilder
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(false); // Separate stdout and stderr

            // Start the process
            Process process = processBuilder.start();

            // Read output from the process
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            // Read errors from the process
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errors.append(line).append("\n");
                }
            }

            // Wait for the process to finish
            int exitCode = process.waitFor();
            output.append("\nExit Code: ").append(exitCode);

        } catch (IOException | InterruptedException e) {
            errors.append("Exception occurred: ").append(e.getMessage());
            e.printStackTrace();
        }

        // Return output or errors
        if (errors.length() > 0) {
            return "Error:\n" + errors.toString();
        } else {
            return "Output:\n" + output.toString();
        }
    }
}