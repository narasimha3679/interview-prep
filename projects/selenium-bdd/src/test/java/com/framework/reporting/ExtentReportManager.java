package com.framework.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.nio.file.Path;

/**
 * Singleton ExtentReports manager — one HTML report for the suite run.
 */
public final class ExtentReportManager {

    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();

    private ExtentReportManager() {
    }

    public static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            Path reportPath = Path.of("target", "extent-reports", "SparkReport.html");
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath.toString());
            spark.config().setDocumentTitle("Selenium BDD Framework Report");
            spark.config().setReportName("Sauce Demo BDD Suite");
            spark.config().setTheme(Theme.STANDARD);

            extentReports = new ExtentReports();
            extentReports.attachReporter(spark);
            extentReports.setSystemInfo("Framework", "Selenium 4 + Cucumber + TestNG");
            extentReports.setSystemInfo("Application", "Sauce Demo");
        }
        return extentReports;
    }

    public static void createTest(String name) {
        TEST.set(getInstance().createTest(name));
    }

    public static ExtentTest getTest() {
        return TEST.get();
    }

    public static void removeTest() {
        TEST.remove();
    }

    public static void flush() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}
