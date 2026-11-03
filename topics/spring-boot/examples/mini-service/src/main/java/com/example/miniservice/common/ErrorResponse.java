package com.example.miniservice.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorResponse(
        Instant timestamp,
        int status,
        String code,
        String message,
        String path,
        Map<String, String> fieldErrors,
        String traceId
) {
    public static ErrorResponse of(int status, String code, String message, String path) {
        return new ErrorResponse(
                Instant.now(), status, code, message, path, Map.of(), UUID.randomUUID().toString());
    }

    public static ErrorResponse validation(String path, Map<String, String> fieldErrors) {
        return new ErrorResponse(
                Instant.now(),
                400,
                "VALIDATION_ERROR",
                "Request validation failed",
                path,
                fieldErrors,
                UUID.randomUUID().toString()
        );
    }
}
