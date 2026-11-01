package com.tutorialsninja.qa.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.tutorialsninja.qa.driver.DriverManager;
import com.tutorialsninja.qa.utils.ExtentManager;
import com.tutorialsninja.qa.utils.ExtentReporterNg;
import com.tutorialsninja.qa.utils.ScreenshotUtils;

public class MyListeners implements ITestListener {

    private static final Logger logger = LogManager.getLogger(MyListeners.class);
    private static ExtentReports extentReports;

    @Override
    public void onStart(ITestContext context) {
        logger.info("Initializing Test Suite: {}", context.getName());
        extentReports = ExtentReporterNg.generateExtentReport();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.info("Starting Test Method: {}", testName);

        ExtentTest test = extentReports.createTest(
                testName,
                result.getMethod().getDescription());
        test.assignCategory(result.getTestClass().getRealClass().getSimpleName());
        ExtentManager.setTest(test);
        ExtentManager.getTest().log(Status.INFO, "Execution started for: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test PASSED: {}", result.getName());
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.PASS, result.getName() + " passed successfully.");
            ExtentManager.unload();
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test FAILED: {} | Cause: {}", result.getName(), result.getThrowable());
        
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.FAIL, result.getThrowable());

            if (DriverManager.getDriver() != null) {
                try {
                    String base64Screenshot = ScreenshotUtils.getBase64Screenshot(DriverManager.getDriver());
                    ExtentManager.getTest().addScreenCaptureFromBase64String("data:image/png;base64," + base64Screenshot, result.getName());
                    ScreenshotUtils.captureScreenshotFile(DriverManager.getDriver(), result.getName());
                } catch (Exception e) {
                    logger.error("Failed to capture screenshot for test {}: {}", result.getName(), e.getMessage());
                    ExtentManager.getTest().log(Status.WARNING, "Failed to capture screenshot: " + e.getMessage());
                }
            }
            ExtentManager.unload();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test SKIPPED: {}", result.getName());
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.SKIP, result.getName() + " was skipped.");
            ExtentManager.unload();
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Finishing Test Suite: {}", context.getName());
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}