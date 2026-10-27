package com.tutorialsninja.qa.utilitis;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReporterNg {

    public static ExtentReports generateExtentReport() {
        ExtentReports extentReport = new ExtentReports();

        String reportPath = Paths.get(System.getProperty("user.dir"), "test-output", "ExtentReports", "extentReport.html").toString();
        File reportFile = new File(reportPath);
        reportFile.getParentFile().mkdirs();

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFile);
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setReportName("TutorialsNinja Automation Test Results");
        sparkReporter.config().setDocumentTitle("TN Automation Report");
        sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

        extentReport.attachReporter(sparkReporter);

        Properties configProp = new Properties();
        try (InputStream fis = ExtentReporterNg.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (fis != null) {
                configProp.load(fis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        extentReport.setSystemInfo("Application URL", configProp.getProperty("url", "https://tutorialsninja.com/demo/"));
        extentReport.setSystemInfo("Browser Name", System.getProperty("browser", configProp.getProperty("browserName", "chrome")));
        extentReport.setSystemInfo("Operating System", System.getProperty("os.name"));
        extentReport.setSystemInfo("Java Runtime Version", System.getProperty("java.version"));
        extentReport.setSystemInfo("Executed By", System.getenv("CI") != null ? "CI Agent" : System.getProperty("user.name"));

        return extentReport;
    }
}