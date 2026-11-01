package com.tutorialsninja.qa.testcases;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.tutorialsninja.qa.base.BaseTest;
import com.tutorialsninja.qa.constants.FrameworkConstants;
import com.tutorialsninja.qa.driver.DriverManager;
import com.tutorialsninja.qa.pages.AccountPage;
import com.tutorialsninja.qa.pages.HomePage;
import com.tutorialsninja.qa.pages.LoginPage;
import com.tutorialsninja.qa.utils.ConfigReader;
import com.tutorialsninja.qa.utils.ExcelReader;
import com.tutorialsninja.qa.utils.UtilitiesCLS;

public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void setupPage() {
        HomePage homePage = new HomePage(DriverManager.getDriver());
        loginPage = homePage.navigateToLoginPage();
    }

    @Test(priority = 1, dataProvider = "validCredentialsSupplier")
    public void verifyLoginWithValidCredentials(String email, String password) {
        AccountPage accountPage = loginPage.login(email, password);
        Assert.assertTrue(
                accountPage.getDisplayStatusOfEditYourAccountInformationOption(),
                "The option 'Edit your account information' was not displayed."
        );
    }

    @DataProvider(name = "validCredentialsSupplier", parallel = false)
    public Object[][] supplyTestData() {
        return ExcelReader.getSheetData(FrameworkConstants.EXCEL_TESTDATA_NAME, "Login");
    }

    @Test(priority = 2)
    public void verifyLoginWithInvalidCredentials() {
        loginPage.login(UtilitiesCLS.generateEmailWithTimeStamp(), ConfigReader.getProperty("invalidPassword"));
        String actualWarning = loginPage.retrieveEmailPasswordNotMatchingWarningMessageText();
        Assert.assertTrue(
                actualWarning.contains(ConfigReader.getProperty("emailPasswordNoMatchWarning")),
                "The expected warning message was not displayed."
        );
    }

    @Test(priority = 3)
    public void verifyLoginWithInvalidEmailAndValidPassword() {
        loginPage.login(UtilitiesCLS.generateEmailWithTimeStamp(), ConfigReader.getProperty("validPassword"));
        String actualWarning = loginPage.retrieveEmailPasswordNotMatchingWarningMessageText();
        Assert.assertTrue(
                actualWarning.contains(ConfigReader.getProperty("emailPasswordNoMatchWarning")),
                "The expected warning message was not displayed."
        );
    }

    @Test(priority = 4)
    public void verifyLoginWithValidEmailAndInvalidPassword() {
        loginPage.login(ConfigReader.getProperty("validEmail"), ConfigReader.getProperty("invalidPassword"));
        String actualWarning = loginPage.retrieveEmailPasswordNotMatchingWarningMessageText();
        Assert.assertTrue(
                actualWarning.contains(ConfigReader.getProperty("emailPasswordNoMatchWarning")),
                "The expected warning message was not displayed."
        );
    }

    @Test(priority = 5)
    public void verifyLoginWithoutProvidingCredentials() {
        loginPage.login("", "");
        String actualWarning = loginPage.retrieveEmailPasswordNotMatchingWarningMessageText();
        Assert.assertTrue(
                actualWarning.contains(ConfigReader.getProperty("emailPasswordNoMatchWarning")),
                "The expected warning message was not displayed."
        );
    }
}