package com.tutorialsninja.qa.base;

import java.io.InputStream;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import com.tutorialsninja.qa.driver.DriverFactory;
import com.tutorialsninja.qa.driver.DriverManager;

public class BaseTest {

    protected Properties prop;
    protected Properties dataProp;

    public BaseTest() {
        prop = new Properties();
        dataProp = new Properties();

        try (InputStream fisProp = getClass().getClassLoader().getResourceAsStream("config.properties");
             InputStream fisDataProp = getClass().getClassLoader().getResourceAsStream("testdata.properties")) {
            if (fisProp != null) prop.load(fisProp);
            if (fisDataProp != null) dataProp.load(fisDataProp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeMethod
    public void setUp() {
        String browser = System.getProperty("browser", prop.getProperty("browserName", "chrome"));
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        WebDriver driver = DriverFactory.createDriver(browser, headless);
        DriverManager.setDriver(driver);
        DriverManager.getDriver().get(prop.getProperty("url"));
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }
}