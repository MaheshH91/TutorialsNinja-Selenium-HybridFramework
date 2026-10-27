package com.tutorialsninja.qa.utils;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.tutorialsninja.qa.constants.FrameworkConstants;

public final class ExtentReporterNg {

    private ExtentReporterNg() {}

    public static ExtentReports generateExtentReport() {
        ExtentReports extentReport = new ExtentReports();
        File reportFile = new File(FrameworkConstants.EXTENT_REPORT_PATH);
        reportFile.getParentFile().mkdirs();

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFile);
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setReportName("TutorialsNinja Regression Test Automation");
        sparkReporter.config().setDocumentTitle("QA Execution Report");
        sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

        extentReport.attachReporter(sparkReporter);

        Properties config = new Properties();
        try (InputStream is = ExtentReporterNg.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) config.load(is);
        } catch (Exception ignored) {}

        extentReport.setSystemInfo("Application URL", config.getProperty("url", "https://tutorialsninja.com/demo/"));
        extentReport.setSystemInfo("Target Browser", System.getProperty("browser", config.getProperty("browserName", "chrome")));
        extentReport.setSystemInfo("Java Version", System.getProperty("java.version"));
        extentReport.setSystemInfo("Operating System", System.getProperty("os.name"));
        extentReport.setSystemInfo("Execution Context", System.getenv("CI") != null ? "CI Runner" : "Local Environment");

        return extentReport;
    }
}