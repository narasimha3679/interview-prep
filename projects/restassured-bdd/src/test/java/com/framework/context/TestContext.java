package com.framework.context;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Scenario-scoped shared state.
 * Cucumber step methods are separate instances of different classes,
 * so we keep response/data here for the current scenario thread.
 */
public class TestContext {

    private static final ThreadLocal<TestContext> HOLDER = ThreadLocal.withInitial(TestContext::new);

    private Response response;
    private final Map<String, Object> data = new HashMap<>();

    public static TestContext get() {
        return HOLDER.get();
    }

    public static void reset() {
        HOLDER.remove();
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) data.get(key);
    }
}
