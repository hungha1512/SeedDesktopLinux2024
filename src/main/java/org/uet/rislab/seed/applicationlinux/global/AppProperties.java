package org.uet.rislab.seed.applicationlinux.global;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {
    private static final Properties PROPERTIES = new Properties();
    private static final String PROPERTIES_PATH_FILE = System.getProperty("user.dir") + "/src/main/resources/application.properties";

    static {
        try {
            InputStream inputStream = AppProperties.class.getClassLoader().getResourceAsStream(PROPERTIES_PATH_FILE);
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
        try {
            FileOutputStream fos = new FileOutputStream(PROPERTIES_PATH_FILE);
            PROPERTIES.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
