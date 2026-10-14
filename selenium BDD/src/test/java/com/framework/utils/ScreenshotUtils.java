package com.framework.utils;

import com.framework.driver.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Captures PNG screenshots under target/screenshots for failure evidence.
 */
public final class ScreenshotUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final Path SCREENSHOT_DIR = Path.of("target", "screenshots");
    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtils() {
    }

    public static String capture(String scenarioName) {
        try {
            Files.createDirectories(SCREENSHOT_DIR);
            String safeName = scenarioName.replaceAll("[^a-zA-Z0-9-_]", "_");
            Path file = SCREENSHOT_DIR.resolve(safeName + "_" + LocalDateTime.now().format(TS) + ".png");
            byte[] bytes = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
            Files.write(file, bytes);
            LOG.info("Screenshot saved: {}", file.toAbsolutePath());
            return file.toAbsolutePath().toString();
        } catch (IOException e) {
            LOG.error("Failed to capture screenshot", e);
            return null;
        }
    }

    public static byte[] captureBytes() {
        return ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
    }
}
