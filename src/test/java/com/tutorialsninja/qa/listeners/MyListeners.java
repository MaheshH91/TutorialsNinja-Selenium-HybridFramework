package com.tutorialsninja.qa.listeners;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.tutorialsninja.qa.base.Base;
import com.tutorialsninja.qa.utilitis.ExtentReporterNg;

public class MyListeners implements ITestListener {

    private static ExtentReports extentReport;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        extentReport = ExtentReporterNg.generateExtentReport();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extentReport.createTest(result.getTestClass().getName() + " -> " + result.getMethod().getMethodName());
        extentTest.set(test);
        extentTest.get().log(Status.INFO, result.getName() + " execution started");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, result.getName() + " executed successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = null;
        Object testInstance = result.getInstance();

        if (testInstance instanceof Base) {
            driver = ((Base) testInstance).getDriver();
        }

        if (driver != null) {
            try {
                String base64Image = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                extentTest.get().addScreenCaptureFromBase64String("data:image/png;base64," + base64Image, result.getName());
            } catch (Exception e) {
                extentTest.get().log(Status.WARNING, "Screenshot could not be captured: " + e.getMessage());
            }
        }

        extentTest.get().log(Status.FAIL, result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (result.getThrowable() != null) {
            extentTest.get().log(Status.SKIP, result.getThrowable());
        } else {
            extentTest.get().log(Status.SKIP, result.getName() + " was skipped");
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extentReport != null) {
            extentReport.flush();
        }
    }
}