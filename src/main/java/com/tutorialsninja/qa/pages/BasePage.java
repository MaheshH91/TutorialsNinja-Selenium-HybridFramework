package com.tutorialsninja.qa.pages;

import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.tutorialsninja.qa.constants.FrameworkConstants;

public abstract class BasePage {

    protected final Logger logger = LogManager.getLogger(this.getClass());
    protected WebDriver driver;
    protected WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.EXPLICIT_WAIT));
        PageFactory.initElements(driver, this);
    }

    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        logger.debug("Clicked element: {}", element);
    }

    protected void sendKeys(WebElement element, String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOf(element));
        el.clear();
        el.sendKeys(text != null ? text : "");
        logger.debug("Typed '{}' into element: {}", text, element);
    }

    protected String getText(WebElement element) {
        String text = wait.until(ExpectedConditions.visibilityOf(element)).getText().trim();
        logger.debug("Retrieved text '{}' from element: {}", text, element);
        return text;
    }

    protected boolean isElementDisplayed(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (Exception e) {
            logger.debug("Element not displayed: {}", element);
            return false;
        }
    }

    protected void jsClick(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        logger.debug("JS clicked element: {}", element);
    }
}