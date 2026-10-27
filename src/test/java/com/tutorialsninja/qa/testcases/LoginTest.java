package com.tutorialsninja.qa.testcases;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.tutorialsninja.qa.base.Base;
import com.tutorialsninja.qa.pages.AccountPage;
import com.tutorialsninja.qa.pages.HomePage;
import com.tutorialsninja.qa.pages.LoginPage;
import com.tutorialsninja.qa.utilitis.UtilitiesCLS;

public class LoginTest extends Base {

    private LoginPage loginPage;
    private AccountPage accountPage;

    public LoginTest() {
        super();
    }

    @BeforeMethod
    public void setup() {
        initializeBrowserAndOpenApplication(prop.getProperty("browserName"));
        HomePage homePage = new HomePage(getDriver());
        loginPage = homePage.naviageToLoginPage();
    }

    @AfterMethod
    public void tearDownTest() {
        tearDown();
    }

    @Test(priority = 1, dataProvider = "validCredentialsSupplier")
    public void verifyLoginWithValidCredentials(String email, String password) {
        accountPage = loginPage.login(email, password);
        Assert.assertTrue(accountPage.getDisplayStatusOfEditYourAccountInformationOption(),
                "The option 'Edit your account information' was not displayed.");
    }

    @DataProvider(name = "validCredentialsSupplier")
    public Object[][] supplyTestData() {
        return UtilitiesCLS.getTestDataFromExcel("Login");
    }

    @Test(priority = 2)
    public void verifyLoginWithInvalidCredentials() {
        loginPage.login(UtilitiesCLS.generateEmailWithTimeStamp(), dataProp.getProperty("invalidPassword"));
        String actualWarningMessage = loginPage.retrieveEmailPasswordNotMatchingWarningMessageText();
        String expectedWarningMessage = dataProp.getProperty("emailPasswordNoMatchWarning");
        Assert.assertTrue(actualWarningMessage.contains(expectedWarningMessage),
                "The expected warning message was not displayed.");
    }

    @Test(priority = 3)
    public void verifyLoginWithInvalidEmailAndValidPassword() {
        loginPage.login(UtilitiesCLS.generateEmailWithTimeStamp(), prop.getProperty("validPassword"));
        String actualWarningMessage = loginPage.retrieveEmailPasswordNotMatchingWarningMessageText();
        String expectedWarningMessage = dataProp.getProperty("emailPasswordNoMatchWarning");
        Assert.assertTrue(actualWarningMessage.contains(expectedWarningMessage),
                "The expected warning message was not displayed.");
    }

    @Test(priority = 4)
    public void verifyLoginWithValidEmailAndInvalidPassword() {
        loginPage.login(prop.getProperty("validEmail"), dataProp.getProperty("invalidPassword"));
        String actualWarningMessage = loginPage.retrieveEmailPasswordNotMatchingWarningMessageText();
        String expectedWarningMessage = dataProp.getProperty("emailPasswordNoMatchWarning");
        Assert.assertTrue(actualWarningMessage.contains(expectedWarningMessage),
                "The expected warning message was not displayed.");
    }

    @Test(priority = 5)
    public void verifyLoginWithoutProvidingCredentials() {
        loginPage.login("", "");
        String actualWarningMessage = loginPage.retrieveEmailPasswordNotMatchingWarningMessageText();
        String expectedWarningMessage = dataProp.getProperty("emailPasswordNoMatchWarning");
        Assert.assertTrue(actualWarningMessage.contains(expectedWarningMessage),
                "The expected warning message was not displayed.");
    }
}