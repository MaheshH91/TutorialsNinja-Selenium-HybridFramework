package com.tutorialsninja.qa.listeners;

import java.awt.GraphicsEnvironment;
import java.io.File;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
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
        ExtentTest test = extentReport.createTest(result.getTestClass().getName() + " :: " + result.getMethod().getMethodName());
        extentTest.set(test);
        extentTest.get().log(Status.INFO, result.getName() + " started executing");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, result.getName() + " passed successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = null;
        try {
            // Retrieve driver safely from test instance
            Object currentClass = result.getInstance();
            Class<?> clazz = result.getTestClass().getRealClass();
            
            while (clazz != null && driver == null) {
                try {
                    java.lang.reflect.Field field = clazz.getDeclaredField("driver");
                    field.setAccessible(true);
                    driver = (WebDriver) field.get(currentClass);
                } catch (NoSuchFieldException e) {
                    clazz = clazz.getSuperclass();
                }
            }
        } catch (Exception e) {
            extentTest.get().log(Status.WARNING, "Unable to extract driver: " + e.getMessage());
        }

        if (driver != null) {
            try {
                // Base64 screenshot preserves image directly in HTML report for CI/Email delivery
                String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                extentTest.get().addScreenCaptureFromBase64String("data:image/png;base64," + base64Screenshot, result.getName());
            } catch (Exception e) {
                extentTest.get().log(Status.WARNING, "Screenshot capture failed: " + e.getMessage());
            }
        }

        extentTest.get().log(Status.FAIL, result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (result.getThrowable() != null) {
            extentTest.get().log(Status.SKIP, result.getThrowable());
        } else {
            extentTest.get().log(Status.SKIP, result.getName() + " got skipped");
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extentReport != null) {
            extentReport.flush();
        }
    }
}