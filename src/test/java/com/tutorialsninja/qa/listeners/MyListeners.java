package com.tutorialsninja.qa.listeners;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.tutorialsninja.qa.driver.DriverManager;
import com.tutorialsninja.qa.utils.ExtentManager;
import com.tutorialsninja.qa.utils.ExtentReporterNg;

public class MyListeners implements ITestListener {

    private static ExtentReports extentReports;

    @Override
    public void onStart(ITestContext context) {
        extentReports = ExtentReporterNg.generateExtentReport();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extentReports.createTest(
                result.getMethod().getMethodName(),
                result.getMethod().getDescription());
        test.assignCategory(result.getTestClass().getRealClass().getSimpleName());
        ExtentManager.setTest(test);
        ExtentManager.getTest().log(Status.INFO, "Execution started for: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentManager.getTest().log(Status.PASS, result.getName() + " passed successfully.");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentManager.getTest().log(Status.FAIL, result.getThrowable());

        if (DriverManager.getDriver() != null) {
            try {
                String base64 = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
                ExtentManager.getTest().addScreenCaptureFromBase64String("data:image/png;base64," + base64, result.getName());
            } catch (Exception e) {
                ExtentManager.getTest().log(Status.WARNING, "Failed to capture screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.SKIP, result.getName() + " was skipped.");
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extentReports != null) {
            extentReports.flush();
        }
        ExtentManager.unload();
    }
}