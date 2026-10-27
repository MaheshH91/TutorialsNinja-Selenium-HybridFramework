package com.tutorialsninja.qa.utilitis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;

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

public class UtilitiesCLS {

	public static final int IMPLICIT_WAIT_TIME = 10;
	public static final int PAGE_LOAD_TIME = 5;
	public static final int EXPLICIT_WAIT_TIME = 10;

	public static String generateEmailWithTimeStamp() {
		Date date = new Date();
		String timestamp = date.toString().replace(" ", "_").replace(":", "_");
		return "amotoori" + timestamp + "@gmail.com";
	}

	public static Object[][] getTestDataFromExcel(String sheetName) {
		File excelFile = new File(
				"src/main/java/com/tutorialsninja/qa/com.tutorialsninja.qa.testdata/TutorialsNinjaTestData.xlsx");
		try (FileInputStream fisExcel = new FileInputStream(excelFile);
				XSSFWorkbook workbook = new XSSFWorkbook(fisExcel)) {

			XSSFSheet sheet = workbook.getSheet(sheetName);
			int rowCount = sheet.getLastRowNum() + 1; // Add 1 to include header row
			int colCount = sheet.getRow(0).getLastCellNum();

			Object[][] data = new Object[rowCount][colCount];

			for (int i = 0; i < rowCount; i++) {
				XSSFRow row = sheet.getRow(i);
				if (row == null) {
					continue; // Skip empty rows
				}

				for (int j = 0; j < colCount; j++) {
					XSSFCell cell = row.getCell(j);
					if (cell == null) {
						continue; // Skip empty cells
					}

					data[i][j] = getCellValue(cell);
				}
			}

			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Object getCellValue(XSSFCell cell) {
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			return cell.getNumericCellValue();
		case BOOLEAN:
			return cell.getBooleanCellValue();
		default:
			// Handle other cell types or return null
			return null;
		}
	}

	public static String captureScreenshot(WebDriver driver, String testName) {
		File srcScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String destinationScreenshotPath = System.getProperty("user.dir") + "\\Screenshots\\" + testName + ".png";

		try {
			FileHandler.copy(srcScreenshot, new File(destinationScreenshotPath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return destinationScreenshotPath;
	}

	public static WebElement waitForElementToBeVisible(WebDriver driver, WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT_TIME));
		return wait.until(ExpectedConditions.visibilityOf(element));
	}
}
