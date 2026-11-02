package com.tutorialsninja.qa.driver;

import java.io.File;
import java.time.Duration;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.tutorialsninja.qa.constants.FrameworkConstants;

public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver(String browserName, boolean isHeadless) {
        WebDriver driver;
        String browser = (browserName != null) ? browserName.trim().toLowerCase() : "chrome";

        // Directory outside of C:\ProgramData and C:\Windows for Session 0 Jenkins write access
        String baseProfileDir = "C:" + File.separator + "Temp" + File.separator + "browser-profiles";
        File dir = new File(baseProfileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Unique directory per thread to eliminate parallel test profile lock collisions
        String safeUserDataDir = baseProfileDir + File.separator + "profile-" + System.currentTimeMillis() + "-"
                + Thread.currentThread().threadId();

        switch (browser) {
        case "firefox":
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
            if (isHeadless) {
                firefoxOptions.addArguments("-headless");
                firefoxOptions.addArguments("--headless");
                firefoxOptions.addArguments("--width=1920");
                firefoxOptions.addArguments("--height=1080");
            }
            driver = new FirefoxDriver(firefoxOptions);
            break;

        case "edge":
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);

            if (isHeadless) {
                edgeOptions.addArguments("--headless=new");
                edgeOptions.addArguments("--window-size=1920,1080");
            }

            driver = new EdgeDriver(edgeOptions);
            break;
        case "chrome":
        default:
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);

            if (isHeadless) {
                chromeOptions.addArguments("--headless=new");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--window-size=1920,1080");
                chromeOptions.addArguments("--ignore-certificate-errors");
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("--disable-software-rasterizer");
                chromeOptions.addArguments("--disable-features=RendererAppContainer");
                chromeOptions.addArguments("--user-data-dir=" + safeUserDataDir);
            }

            driver = new ChromeDriver(chromeOptions);
            break;
        }

        // Only invoke OS window management when running in headed/interactive mode
        if (!isHeadless) {
            driver.manage().window().setSize(new Dimension(1920, 1080));
            driver.manage().window().maximize();
        }

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(FrameworkConstants.PAGE_LOAD_TIMEOUT));

        return driver;
    }
}