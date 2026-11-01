package com.tutorialsninja.qa.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import com.tutorialsninja.qa.driver.DriverFactory;
import com.tutorialsninja.qa.driver.DriverManager;
import com.tutorialsninja.qa.utils.ConfigReader;

public abstract class BaseTest {

    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional String browserXml) {
        String targetBrowser = (browserXml != null && !browserXml.isBlank()) 
                ? browserXml 
                : ConfigReader.getBrowser();

        WebDriver driver = DriverFactory.createDriver(targetBrowser, ConfigReader.isHeadless());
        DriverManager.setDriver(driver);
        DriverManager.getDriver().get(ConfigReader.getUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }
}