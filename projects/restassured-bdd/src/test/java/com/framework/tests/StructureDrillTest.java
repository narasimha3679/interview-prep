package com.framework.tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class SyntaxDemoTest {

    private RequestSpecification spec;

    @BeforeClass
    public void buildSpec() {
        spec = new RequestSpecBuilder()
                .setBaseUri("https://jsonplaceholder.typicode.com")
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void getOnePost() {
        given()
                .spec(spec)
        .when()
                .get("/posts/1")
        .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .header("Content-Type", containsString("json"));
    }

    @Test
    public void createPost() {
        Map<String, Object> body = Map.of("title", "nara", "body", "drill", "userId", 1);

        given()
                .spec(spec)
                .body(body)
        .when()
                .post("/posts")
        .then()
                .statusCode(201)
                .body("title", equalTo("nara"));
    }
}