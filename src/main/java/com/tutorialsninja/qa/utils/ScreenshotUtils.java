package com.tutorialsninja.qa.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

public final class ScreenshotUtils {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);

    private ScreenshotUtils() {}

    public static String getBase64Screenshot(WebDriver driver) {
        if (driver == null) {
            return "";
        }
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    public static String captureScreenshotFile(WebDriver driver, String testName) {
        if (driver == null) {
            return "";
        }
        File srcScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Path screenshotsDir = Paths.get(System.getProperty("user.dir"), "Screenshots");
        Path destinationPath = screenshotsDir.resolve(testName + "_" + System.currentTimeMillis() + ".png");

        try {
            Files.createDirectories(screenshotsDir);
            FileHandler.copy(srcScreenshot, destinationPath.toFile());
            return destinationPath.toAbsolutePath().toString();
        } catch (IOException e) {
            logger.error("Failed to save screenshot file for test '{}': {}", testName, e.getMessage());
            return "";
        }
    }
}