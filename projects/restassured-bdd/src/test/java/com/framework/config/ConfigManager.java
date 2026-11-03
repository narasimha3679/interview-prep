package com.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads test configuration once from classpath: config/config.properties.
 * System properties override file values (useful for CI / local overrides).
 */
public final class ConfigManager {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream("config/config.properties")) {
            if (input == null) {
                throw new IllegalStateException("config/config.properties not found on classpath");
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private ConfigManager() {
    }

    public static String get(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue.trim();
        }
        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing config key: " + key);
        }
        return value.trim();
    }

    public static String getOptional(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null) {
            return systemValue.trim();
        }
        String fromApiKeyAlias = null;
        if ("apiKey".equals(key)) {
            fromApiKeyAlias = System.getProperty("api.key");
            if (fromApiKeyAlias != null) {
                return fromApiKeyAlias.trim();
            }
        }
        String value = PROPERTIES.getProperty(key);
        return value == null ? "" : value.trim();
    }

    public static String getBaseUrl() {
        return get("baseUrl");
    }

    public static String getApiKey() {
        return getOptional("apiKey");
    }

    public static int getConnectionTimeoutMs() {
        return Integer.parseInt(get("connectionTimeoutMs"));
    }

    public static int getSocketTimeoutMs() {
        return Integer.parseInt(get("socketTimeoutMs"));
    }
}
