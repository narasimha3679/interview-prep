package com.framework.pages;

import com.framework.driver.DriverFactory;
import com.framework.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Shared page actions and wait helpers for all page objects.
 */
public abstract class BasePage {

    protected WebDriver driver() {
        return DriverFactory.getDriver();
    }

    protected WebElement findVisible(By locator) {
        return WaitUtils.visible(locator);
    }

    protected void click(By locator) {
        WaitUtils.clickable(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = findVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String textOf(By locator) {
        return findVisible(locator).getText();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return findVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
