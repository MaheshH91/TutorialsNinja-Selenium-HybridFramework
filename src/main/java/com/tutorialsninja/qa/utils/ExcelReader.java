package com.tutorialsninja.qa.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ExcelReader {

    private static final Logger logger = LogManager.getLogger(ExcelReader.class);

    private ExcelReader() {}

    public static Object[][] getSheetData(String fileName, String sheetName) {
        InputStream fis = null;

        try {
            // Check classpath in subfolder testdata/ first, then classpath root
            fis = ExcelReader.class.getClassLoader().getResourceAsStream("testdata/" + fileName);
            if (fis == null) {
                fis = ExcelReader.class.getClassLoader().getResourceAsStream(fileName);
            }

            // Fallback to direct filesystem checks
            if (fis == null) {
                File directFile = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "testdata", fileName).toFile();
                if (!directFile.exists()) {
                    directFile = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", fileName).toFile();
                }
                if (directFile.exists()) {
                    fis = new FileInputStream(directFile);
                }
            }

            if (fis == null) {
                logger.error("Excel sheet '{}' could not be found on classpath or filesystem.", fileName);
                return new Object[0][0];
            }

            try (XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
                XSSFSheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    logger.warn("Target sheet '{}' was not found in workbook '{}'", sheetName, fileName);
                    return new Object[0][0];
                }

                int rows = sheet.getLastRowNum();
                if (rows == 0) {
                    return new Object[0][0];
                }

                XSSFRow headerRow = sheet.getRow(0);
                int cols = (headerRow != null) ? headerRow.getLastCellNum() : 0;

                Object[][] data = new Object[rows][cols];
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
                return data;
            }
        } catch (Exception e) {
            logger.error("Error reading Excel data from {}: {}", fileName, e.getMessage(), e);
            return new Object[0][0];
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ignored) {}
            }
        }
    }
}