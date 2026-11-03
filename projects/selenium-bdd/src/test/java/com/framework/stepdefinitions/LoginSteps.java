package com.framework.stepdefinitions;

import com.framework.pages.LoginPage;
import com.framework.pages.ProductsPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginSteps {

    private LoginPage loginPage;
    private ProductsPage productsPage;

    @Given("I am on the Sauce Demo login page")
    public void iAmOnTheSauceDemoLoginPage() {
        loginPage = new LoginPage().open();
    }

    @When("I login with username {string} and password {string}")
    public void iLoginWithUsernameAndPassword(String username, String password) {
        productsPage = loginPage.loginAs(username, password);
    }

    @Then("I should be on the products page")
    public void iShouldBeOnTheProductsPage() {
        if (productsPage == null) {
            productsPage = new ProductsPage();
        }
        assertThat(productsPage.isLoaded())
                .as("Products page should be loaded")
                .isTrue();
    }

    @Then("the products page title should be {string}")
    public void theProductsPageTitleShouldBe(String expectedTitle) {
        assertThat(productsPage.getTitleText()).isEqualTo(expectedTitle);
    }

    @Then("I should see a login error containing {string}")
    public void iShouldSeeALoginErrorContaining(String expectedMessage) {
        assertThat(loginPage.isErrorDisplayed()).isTrue();
        assertThat(loginPage.getErrorMessage()).contains(expectedMessage);
    }
}
