package com.tutorialsninja.qa.testcases;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tutorialsninja.qa.base.BaseTest;
import com.tutorialsninja.qa.driver.DriverManager;
import com.tutorialsninja.qa.pages.AccountSuccessPage;
import com.tutorialsninja.qa.pages.HomePage;
import com.tutorialsninja.qa.pages.RegisterPage;
import com.tutorialsninja.qa.utils.ConfigReader;
import com.tutorialsninja.qa.utils.UtilitiesCLS;

public class RegisterTest extends BaseTest {

    private HomePage homePage;
    private RegisterPage registerPage;
    private AccountSuccessPage accountSuccessPage;

    @BeforeMethod
    public void setupPage() {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) {
            throw new IllegalStateException("WebDriver initialization failed in BaseTest.setUp(). Skipping page setup.");
        }
        homePage = new HomePage(driver);
        registerPage = homePage.navigateToRegisterPage();
    }

    @Test(priority = 1)
    public void verifyRegisteringAnAccountWithMandatoryFields() {
        accountSuccessPage = registerPage.registerWithMandatoryFields(
                ConfigReader.getProperty("firstName"),
                ConfigReader.getProperty("lastName"),
                UtilitiesCLS.generateEmailWithTimeStamp(),
                ConfigReader.getProperty("telephoneNumber"),
                ConfigReader.getProperty("validPassword")
        );

        Assert.assertEquals(
                accountSuccessPage.retrieveAccountSuccessPageHeading(),
                ConfigReader.getProperty("accountSuccessfullyCreatedHeading"),
                "Account Success page heading is not displayed"
        );
    }

    @Test(priority = 2)
    public void verifyRegisteringAccountWithAllFields() {
        accountSuccessPage = registerPage.registerWithAllFields(
                ConfigReader.getProperty("firstName"),
                ConfigReader.getProperty("lastName"),
                UtilitiesCLS.generateEmailWithTimeStamp(),
                ConfigReader.getProperty("telephoneNumber"),
                ConfigReader.getProperty("validPassword")
        );

        Assert.assertEquals(
                accountSuccessPage.retrieveAccountSuccessPageHeading(),
                ConfigReader.getProperty("accountSuccessfullyCreatedHeading"),
                "Account Success page heading is not displayed"
        );
    }

    @Test(priority = 3)
    public void verifyRegisteringAccountWithExistingEmailAddress() {
        registerPage.registerWithAllFields(
                ConfigReader.getProperty("firstName"),
                ConfigReader.getProperty("lastName"),
                ConfigReader.getProperty("validEmail"),
                ConfigReader.getProperty("telephoneNumber"),
                ConfigReader.getProperty("validPassword")
        );

        Assert.assertTrue(
                registerPage.retrieveDuplicateEmailAddressWarning().contains(ConfigReader.getProperty("duplicateEmailWarning")),
                "Warning message regarding duplicate email address is not displayed"
        );
    }

    @Test(priority = 4)
    public void verifyRegisteringAccountWithoutFillingAnyInformation() {
        registerPage.clickContinue();

        Assert.assertTrue(
                registerPage.displayStatusOfWarningMessages(
                        ConfigReader.getProperty("privacyPolicyWarning"),
                        ConfigReader.getProperty("firstNameWarning"),
                        ConfigReader.getProperty("lastNameWarning"),
                        ConfigReader.getProperty("emailWarning"),
                        ConfigReader.getProperty("telephoneWarning"),
                        ConfigReader.getProperty("passwordWarning")
                ),
                "One or more warning messages are not displayed as expected"
        );
    }
}