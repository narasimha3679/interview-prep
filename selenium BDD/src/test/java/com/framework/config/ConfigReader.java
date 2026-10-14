package com.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads framework settings from classpath config/config.properties.
 * System properties override file values (useful for CI: -Dbrowser=firefox).
 */
public final class ConfigReader {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigReader.class);
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream stream = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config/config.properties")) {
            if (stream == null) {
                throw new IllegalStateException("Missing config/config.properties on classpath");
            }
            PROPERTIES.load(stream);
            LOG.info("Loaded framework configuration");
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }
        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing config key: " + key);
        }
        return value.trim();
    }

    public static String getBrowser() {
        return get("browser");
    }

    public static String getBaseUrl() {
        return get("baseUrl");
    }

    public static int getExplicitWait() {
        return Integer.parseInt(get("explicitWait"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(get("pageLoadTimeout"));
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(get("headless"));
    }
}
