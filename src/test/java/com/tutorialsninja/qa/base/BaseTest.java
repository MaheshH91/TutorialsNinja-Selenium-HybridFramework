package com.tutorialsninja.qa.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import com.tutorialsninja.qa.driver.DriverFactory;
import com.tutorialsninja.qa.driver.DriverManager;
import com.tutorialsninja.qa.utils.ConfigReader;

public class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    @BeforeMethod
    public void setUp() {
        String browserName = ConfigReader.getBrowser();
        boolean isHeadless = ConfigReader.isHeadless();

        WebDriver driver = DriverFactory.createDriver(browserName, isHeadless);
        DriverManager.setDriver(driver);

        String url = ConfigReader.getUrl();
        loadUrlWithRetry(driver, url, 3);
    }

    private void loadUrlWithRetry(WebDriver driver, String url, int maxAttempts) {
        int attempt = 0;
        while (attempt < maxAttempts) {
            try {
                attempt++;
                driver.get(url);
                return;
            } catch (Exception e) {
                logger.warn("Failed to load URL on attempt {} of {}: {}", attempt, maxAttempts, e.getMessage());
                if (attempt == maxAttempts) {
                    throw e;
                }
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }
}