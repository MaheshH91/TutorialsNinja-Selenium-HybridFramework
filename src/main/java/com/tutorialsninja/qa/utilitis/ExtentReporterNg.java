package com.tutorialsninja.qa.utilitis;

import java.io.File;
import java.io.FileInputStream;
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
        sparkReporter.config().setReportName("TutorialsNinja Automation Execution Report");
        sparkReporter.config().setDocumentTitle("QA Execution Report");
        sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

        extentReport.attachReporter(sparkReporter);

        Properties configProp = new Properties();
        File configFile = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "config.properties").toFile();

        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                configProp.load(fis);
                extentReport.setSystemInfo("Application URL", configProp.getProperty("url", "N/A"));
                extentReport.setSystemInfo("Browser", System.getProperty("browser", configProp.getProperty("browserName", "chrome")));
            } catch (Exception e) {
                // Fallback for CI environments
            }
        }

        extentReport.setSystemInfo("OS", System.getProperty("os.name"));
        extentReport.setSystemInfo("Java Version", System.getProperty("java.version"));
        extentReport.setSystemInfo("Runner", System.getenv("CI") != null ? "CI Environment" : System.getProperty("user.name"));

        return extentReport;
    }
}