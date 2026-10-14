package com.framework.hooks;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.framework.driver.DriverFactory;
import com.framework.reporting.ExtentReportManager;
import com.framework.utils.ScreenshotUtils;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cucumber lifecycle hooks: driver, Extent test, failure screenshots.
 */
public class Hooks {

    private static final Logger LOG = LoggerFactory.getLogger(Hooks.class);

    @BeforeAll
    public static void beforeAll() {
        ExtentReportManager.getInstance();
        LOG.info("Suite starting — Extent report initialized");
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        LOG.info("Starting scenario: {}", scenario.getName());
        DriverFactory.initDriver();
        ExtentReportManager.createTest(scenario.getName());
        ExtentReportManager.getTest().log(Status.INFO, "Scenario started: " + scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            LOG.error("Scenario failed: {}", scenario.getName());
            String path = ScreenshotUtils.capture(scenario.getName());
            byte[] bytes = ScreenshotUtils.captureBytes();
            scenario.attach(bytes, "image/png", "Failure screenshot");
            if (path != null && ExtentReportManager.getTest() != null) {
                ExtentReportManager.getTest().fail(
                        "Scenario failed",
                        MediaEntityBuilder.createScreenCaptureFromPath(path).build()
                );
            } else if (ExtentReportManager.getTest() != null) {
                ExtentReportManager.getTest().fail("Scenario failed");
            }
        } else if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().pass("Scenario passed");
        }
        ExtentReportManager.removeTest();
        DriverFactory.quitDriver();
        LOG.info("Finished scenario: {} -> {}", scenario.getName(), scenario.getStatus());
    }

    @AfterAll
    public static void afterAll() {
        ExtentReportManager.flush();
        LOG.info("Suite finished — Extent report flushed");
    }
}
