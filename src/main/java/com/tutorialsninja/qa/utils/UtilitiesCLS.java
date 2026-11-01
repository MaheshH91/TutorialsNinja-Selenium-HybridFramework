package com.tutorialsninja.qa.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.tutorialsninja.qa.constants.FrameworkConstants;

public final class UtilitiesCLS {

    private UtilitiesCLS() {}

    public static String generateEmailWithTimeStamp() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
        return "maheshholkar_" + timestamp + "@gmail.com";
    }

    public static WebElement waitForElementToBeVisible(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.EXPLICIT_WAIT));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForElementToBeClickable(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.EXPLICIT_WAIT));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }
}