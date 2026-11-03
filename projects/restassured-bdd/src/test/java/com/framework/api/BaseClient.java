package com.framework.api;

import com.framework.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Shared RestAssured setup: base URI, headers, timeouts, and logging.
 * API client classes build requests from {@link #spec()}.
 */
public final class BaseClient {

    private static final RequestSpecification REQUEST_SPEC;

    static {
        RestAssuredConfig config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", ConfigManager.getConnectionTimeoutMs())
                        .setParam("http.socket.timeout", ConfigManager.getSocketTimeoutMs()));

        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(ConfigManager.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setConfig(config)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL));

        String apiKey = ConfigManager.getApiKey();
        if (apiKey != null && !apiKey.isBlank()) {
            builder.addHeader("x-api-key", apiKey);
        }

        REQUEST_SPEC = builder.build();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    private BaseClient() {
    }

    public static RequestSpecification spec() {
        return RestAssured.given().spec(REQUEST_SPEC);
    }
}
