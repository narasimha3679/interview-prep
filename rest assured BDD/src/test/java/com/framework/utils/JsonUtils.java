package com.framework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Loads JSON payload files from classpath (e.g. payloads/create_user.json).
 */
public final class JsonUtils {

    private JsonUtils() {
    }

    public static String loadPayload(String classpathRelativePath) {
        String path = classpathRelativePath.startsWith("/")
                ? classpathRelativePath.substring(1)
                : classpathRelativePath;
        try (InputStream input = JsonUtils.class.getClassLoader().getResourceAsStream(path)) {
            if (input == null) {
                throw new IllegalArgumentException("Payload not found on classpath: " + path);
            }
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read payload: " + path, e);
        }
    }
}
