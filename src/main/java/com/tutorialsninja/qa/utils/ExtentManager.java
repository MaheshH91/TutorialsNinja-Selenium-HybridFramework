package com.tutorialsninja.qa.utils;

import com.aventstack.extentreports.ExtentTest;

public final class ExtentManager {

    private ExtentManager() {}

    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    public static ExtentTest getTest() {
        return extentTest.get();
    }

    public static void setTest(ExtentTest test) {
        extentTest.set(test);
    }

    public static void unload() {
        extentTest.remove();
    }
}