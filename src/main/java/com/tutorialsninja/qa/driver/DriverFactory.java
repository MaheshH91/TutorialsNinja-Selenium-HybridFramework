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

		// Must NOT reside in C:\ProgramData or C:\Windows under Windows SYSTEM accounts
		String baseProfileDir = "C:" + File.separator + "Temp" + File.separator + "browser-profiles";
		File dir = new File(baseProfileDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
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
				// Use legacy headless flag for Windows Service (Session 0) compatibility
				edgeOptions.addArguments("--headless");
				edgeOptions.addArguments("--disable-gpu");
				edgeOptions.addArguments("--no-sandbox");
				edgeOptions.addArguments("--disable-dev-shm-usage");
				edgeOptions.addArguments("--window-size=1920,1080");
				edgeOptions.addArguments("--remote-allow-origins=*");
				edgeOptions.addArguments("--disable-extensions");
				edgeOptions.addArguments("--disable-software-rasterizer");

				// Bypass Windows Session 0 AppContainer & Renderer crashes under SYSTEM account
				edgeOptions.addArguments("--no-zygote");
				edgeOptions.addArguments("--disable-features=RendererAppContainer");
				edgeOptions.addArguments("--enable-automation");
				edgeOptions.addArguments("--remote-debugging-port=0");

				// Isolate browser profile into a dedicated, clean folder
				String edgeProfile = "C:\\Temp\\browser-profiles\\edge-" + System.currentTimeMillis() + "-"
						+ Thread.currentThread().threadId();
				edgeOptions.addArguments("--user-data-dir=" + edgeProfile);
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
				chromeOptions.addArguments("--start-maximized");
				chromeOptions.addArguments("--ignore-certificate-errors");
				chromeOptions.addArguments("--remote-allow-origins=*");
				chromeOptions.addArguments("--disable-extensions");
				chromeOptions.addArguments("--disable-software-rasterizer");
				chromeOptions.addArguments("--user-data-dir=" + safeUserDataDir);
			}
			driver = new ChromeDriver(chromeOptions);
			break;
		}

		driver.manage().window().setSize(new Dimension(1920, 1080));
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(FrameworkConstants.PAGE_LOAD_TIMEOUT));

		return driver;
	}
}