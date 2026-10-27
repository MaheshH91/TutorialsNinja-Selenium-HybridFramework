package com.tutorialsninja.qa.constants;

import java.nio.file.Paths;

public final class FrameworkConstants {

    private FrameworkConstants() {}

    public static final int IMPLICIT_WAIT = 10;
    public static final int EXPLICIT_WAIT = 25;
    public static final int PAGE_LOAD_TIMEOUT = 30;

    public static final String EXTENT_REPORT_PATH = Paths.get(
            System.getProperty("user.dir"), "test-output", "ExtentReports", "extentReport.html").toString();

    public static final String EXCEL_TESTDATA_NAME = "TutorialsNinjaTestData.xlsx";
}