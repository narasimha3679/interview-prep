package com.framework.stepdefinitions;

import com.framework.context.TestContext;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.testng.Assert;

/**
 * Shared Then steps used across features (status codes, common field checks).
 */
public class CommonSteps {

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int expectedStatus) {
        Response response = TestContext.get().getResponse();
        Assert.assertNotNull(response, "No API response stored in TestContext");
        Assert.assertEquals(response.getStatusCode(), expectedStatus,
                "Unexpected HTTP status. Body: " + response.asString());
    }

    @Then("the response should contain field {string}")
    public void theResponseShouldContainField(String jsonPath) {
        Response response = TestContext.get().getResponse();
        Assert.assertNotNull(response, "No API response stored in TestContext");
        Object value = response.jsonPath().get(jsonPath);
        Assert.assertNotNull(value, "Expected field '" + jsonPath + "' to be present");
    }
}
