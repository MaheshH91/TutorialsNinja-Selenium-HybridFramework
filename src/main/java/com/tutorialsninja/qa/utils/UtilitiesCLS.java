package com.tutorialsninja.qa.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Date;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class UtilitiesCLS {

    // Private constructor to prevent instantiation of utility class
    private UtilitiesCLS() {
    }

    public static final int IMPLICIT_WAIT_TIME = 10;
    public static final int PAGE_LOAD_TIME = 40;
    public static final int EXPLICIT_WAIT_TIME = 30;

    /**
     * Generates a dynamic email address using the current timestamp to avoid duplicate user registration conflicts.
     */
    public static String generateEmailWithTimeStamp() {
        Date date = new Date();
        String timestamp = date.toString().replace(" ", "_").replace(":", "_");
        return "maheshholkar" + timestamp + "@gmail.com";
    }

    /**
     * Reads test data from TutorialsNinjaTestData.xlsx located in src/test/resources.
     * Uses DataFormatter to handle numbers, strings, and blank cells consistently.
     */
    public static Object[][] getTestDataFromExcel(String sheetName) {
        Object[][] data = null;
        InputStream fis = null;

        try {
            // 1. Primary ClassLoader check for src/test/resources/TutorialsNinjaTestData.xlsx
            fis = UtilitiesCLS.class.getClassLoader().getResourceAsStream("TutorialsNinjaTestData.xlsx");

            // 2. Direct filesystem fallback for IDE execution environments
            if (fis == null) {
                File directFile = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "TutorialsNinjaTestData.xlsx").toFile();
                if (directFile.exists()) {
                    fis = new FileInputStream(directFile);
                }
            }

            if (fis == null) {
                throw new IOException("Unable to locate 'TutorialsNinjaTestData.xlsx' on classpath or in src/test/resources.");
            }

            try (XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
                XSSFSheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    System.err.println("Target sheet '" + sheetName + "' was not found in TutorialsNinjaTestData.xlsx");
                    return new Object[0][0];
                }

                int rows = sheet.getLastRowNum();
                if (rows == 0) {
                    return new Object[0][0];
                }

                XSSFRow headerRow = sheet.getRow(0);
                int cols = headerRow != null ? headerRow.getLastCellNum() : 0;

                data = new Object[rows][cols];
                DataFormatter formatter = new DataFormatter();

                for (int i = 0; i < rows; i++) {
                    XSSFRow row = sheet.getRow(i + 1);
                    for (int j = 0; j < cols; j++) {
                        if (row == null) {
                            data[i][j] = "";
                            continue;
                        }
                        XSSFCell cell = row.getCell(j);
                        data[i][j] = (cell != null) ? formatter.formatCellValue(cell).trim() : "";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return data != null ? data : new Object[0][0];
    }

    /**
     * Captures a screenshot to the Screenshots/ directory and returns the absolute path.
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        if (driver == null) {
            return "";
        }
        File srcScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Path screenshotsDir = Paths.get(System.getProperty("user.dir"), "Screenshots");
        Path destinationPath = screenshotsDir.resolve(testName + "_" + System.currentTimeMillis() + ".png");

        try {
            Files.createDirectories(screenshotsDir);
            FileHandler.copy(srcScreenshot, destinationPath.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return destinationPath.toString();
    }

    /**
     * Explicit wait helper for visibility of element.
     */
    public static WebElement waitForElementToBeVisible(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT_TIME));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Explicit wait helper for element to be clickable.
     */
    public static WebElement waitForElementToBeClickable(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT_TIME));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }
}