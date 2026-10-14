package com.framework.stepdefinitions;

import com.framework.api.UserApi;
import com.framework.context.TestContext;
import com.framework.models.UserRequest;
import com.framework.models.UserResponse;
import com.framework.utils.JsonUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;

/**
 * User-domain step definitions: orchestrate API calls and domain assertions.
 */
public class UserSteps {

    private final UserApi userApi = new UserApi();

    @Given("the API is available")
    public void theApiIsAvailable() {
        // Lightweight readiness check — list page 1 should respond successfully.
        Response response = userApi.listUsers(1);
        Assert.assertTrue(response.getStatusCode() < 500,
                "API appears unavailable. Status=" + response.getStatusCode());
    }

    @When("I request user with id {int}")
    public void iRequestUserWithId(int id) {
        Response response = userApi.getUser(id);
        TestContext.get().setResponse(response);
        TestContext.get().put("userId", id);
    }

    @When("I create a user from payload {string}")
    public void iCreateAUserFromPayload(String payloadFile) {
        String body = JsonUtils.loadPayload("payloads/" + payloadFile);
        Response response = userApi.createUser(body);
        TestContext.get().setResponse(response);
        TestContext.get().put("payloadFile", payloadFile);
    }

    @When("I create a user named {string} with job {string}")
    public void iCreateAUserNamedWithJob(String name, String job) {
        UserRequest request = new UserRequest(name, job);
        Response response = userApi.createUser(request);
        TestContext.get().setResponse(response);
        TestContext.get().put("userRequest", request);
    }

    @When("I update user {int} with name {string} and job {string}")
    public void iUpdateUserWithNameAndJob(int id, String name, String job) {
        UserRequest request = new UserRequest(name, job);
        Response response = userApi.updateUser(id, request);
        TestContext.get().setResponse(response);
        TestContext.get().put("userRequest", request);
    }

    @When("I delete user with id {int}")
    public void iDeleteUserWithId(int id) {
        Response response = userApi.deleteUser(id);
        TestContext.get().setResponse(response);
    }

    @Then("the user email should not be empty")
    public void theUserEmailShouldNotBeEmpty() {
        Response response = TestContext.get().getResponse();
        String email = response.jsonPath().getString("email");
        Assert.assertNotNull(email, "email was null");
        Assert.assertFalse(email.isBlank(), "email was blank");
    }

    @Then("the created user should echo name {string} and job {string}")
    public void theCreatedUserShouldEchoNameAndJob(String name, String job) {
        Response response = TestContext.get().getResponse();
        UserResponse user = response.as(UserResponse.class);
        Assert.assertEquals(user.getName(), name);
        Assert.assertEquals(user.getJob(), job);
        Assert.assertNotNull(user.getId(), "created user id should not be null");
    }

    @Then("the updated user should echo name {string} and job {string}")
    public void theUpdatedUserShouldEchoNameAndJob(String name, String job) {
        Response response = TestContext.get().getResponse();
        UserResponse user = response.as(UserResponse.class);
        Assert.assertEquals(user.getName(), name);
        Assert.assertEquals(user.getJob(), job);
    }
}
