package com.framework.api;

import io.restassured.response.Response;

/**
 * User endpoint client — HTTP only, no assertions.
 * Step definitions call these methods and assert on the returned Response.
 */
public class UserApi {

    public Response getUser(int id) {
        return BaseClient.spec()
                .when()
                .get("/users/{id}", id);
    }

    public Response listUsers(int page) {
        return BaseClient.spec()
                .queryParam("_page", page)
                .when()
                .get("/users");
    }

    public Response createUser(Object body) {
        return BaseClient.spec()
                .body(body)
                .when()
                .post("/users");
    }

    public Response updateUser(int id, Object body) {
        return BaseClient.spec()
                .body(body)
                .when()
                .put("/users/{id}", id);
    }

    public Response deleteUser(int id) {
        return BaseClient.spec()
                .when()
                .delete("/users/{id}", id);
    }
}
