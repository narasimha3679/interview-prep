package com.framework.utils;

import com.framework.config.ConfigReader;
import com.framework.driver.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Centralized explicit waits — prefer these over Thread.sleep.
 */
public final class WaitUtils {

    private WaitUtils() {
    }

    public static WebDriverWait waitFor() {
        return new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    public static WebElement visible(By locator) {
        return waitFor().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement clickable(By locator) {
        return waitFor().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static boolean invisible(By locator) {
        return waitFor().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static List<WebElement> allVisible(By locator) {
        return waitFor().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static boolean urlContains(String fragment) {
        return waitFor().until(ExpectedConditions.urlContains(fragment));
    }
}
