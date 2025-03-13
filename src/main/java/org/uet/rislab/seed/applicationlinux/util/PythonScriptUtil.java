package org.uet.rislab.seed.applicationlinux.util;

import java.io.*;

public class PythonScriptUtil {
    public static String extractPythonScript() throws IOException {
        // Adjust the resource path to match its location in the jar.
        // Since you moved it to src/main/resources/pythoncore, the resource path becomes "/pythoncore/infer_opt.py"
        InputStream scriptStream = PythonScriptUtil.class.getResourceAsStream("/pythoncore/infer_opt.py");
        if (scriptStream == null) {
            throw new FileNotFoundException("Python script resource not found in jar at /pythoncore/infer_opt.py");
        }

        // Create a temporary file; it will be deleted on exit.
        File tempScript = File.createTempFile("infer_opt", ".py");
        tempScript.deleteOnExit();

        try (FileOutputStream out = new FileOutputStream(tempScript)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = scriptStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        return tempScript.getAbsolutePath();
    }
}
