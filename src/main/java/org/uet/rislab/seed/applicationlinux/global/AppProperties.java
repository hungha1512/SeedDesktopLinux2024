package org.uet.rislab.seed.applicationlinux.global;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class AppProperties {
    private static Properties PROPERTIES = new Properties();
    private static String propertiesFilePath;

    public static void setPropertiesFilePath(String path) {
        propertiesFilePath = path;
        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream(propertiesFilePath), StandardCharsets.UTF_8)) {
            PROPERTIES.clear(); // Clear any existing properties
            PROPERTIES.load(reader);
        } catch (IOException e) {
            System.err.println("Error loading properties file: " + propertiesFilePath);
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
