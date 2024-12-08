package org.uet.rislab.seed.applicationlinux.global;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class AppProperties {
    private static final Properties PROPERTIES = new Properties();
    private static String propertiesFilePath;

    public static void setPropertiesFilePath(String path) {
        propertiesFilePath = path;
        try {
            InputStream inputStream = AppProperties.class.getClassLoader().getResourceAsStream(propertiesFilePath);
            if (inputStream != null) {
                PROPERTIES.load(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static void setProperty(String key, String value) {
        PROPERTIES.setProperty(key, value);
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(propertiesFilePath), StandardCharsets.UTF_8)) {
            PROPERTIES.store(writer, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
