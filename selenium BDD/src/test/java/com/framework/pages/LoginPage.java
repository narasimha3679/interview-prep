package com.framework.pages;

import com.framework.config.ConfigReader;
import org.openqa.selenium.By;

/**
 * Sauce Demo login page: https://www.saucedemo.com/
 */
public class LoginPage extends BasePage {

    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage open() {
        driver().get(ConfigReader.getBaseUrl());
        findVisible(usernameInput);
        return this;
    }

    public LoginPage enterUsername(String username) {
        type(usernameInput, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(passwordInput, password);
        return this;
    }

    public ProductsPage submitLogin() {
        click(loginButton);
        return new ProductsPage();
    }

    public ProductsPage loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return submitLogin();
    }

    public String getErrorMessage() {
        return textOf(errorMessage);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }
}
