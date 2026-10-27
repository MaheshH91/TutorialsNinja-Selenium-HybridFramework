package com.tutorialsninja.qa.base;

import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import com.tutorialsninja.qa.utilitis.UtilitiesCLS;

public class Base {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    public Properties prop;
    public Properties dataProp;

    public Base() {
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

    public WebDriver getDriver() {
        return driver.get();
    }

    public WebDriver initializeBrowserAndOpenApplication(String browserName) {
        String cliBrowser = System.getProperty("browser");
        String finalBrowser = (cliBrowser != null && !cliBrowser.isEmpty()) ? cliBrowser : browserName;
        boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        WebDriver localDriver;

        switch (finalBrowser.toLowerCase()) {
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (isHeadless) firefoxOptions.addArguments("-headless");
                localDriver = new FirefoxDriver(firefoxOptions);
                break;
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                if (isHeadless) edgeOptions.addArguments("--headless=new");
                localDriver = new EdgeDriver(edgeOptions);
                break;
            case "chrome":
            default:
                ChromeOptions chromeOptions = new ChromeOptions();
                if (isHeadless) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--window-size=1920,1080");
                }
                localDriver = new ChromeDriver(chromeOptions);
                break;
        }

        driver.set(localDriver);
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(UtilitiesCLS.IMPLICIT_WAIT_TIME));
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(UtilitiesCLS.PAGE_LOAD_TIME));
        getDriver().get(prop.getProperty("url"));

        return getDriver();
    }

    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            driver.remove();
        }
    }
}