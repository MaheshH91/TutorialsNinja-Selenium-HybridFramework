package com.tutorialsninja.qa.testcases;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tutorialsninja.qa.base.Base;
import com.tutorialsninja.qa.pages.AccountSuccessPage;
import com.tutorialsninja.qa.pages.HomePage;
import com.tutorialsninja.qa.pages.RegisterPage;
import com.tutorialsninja.qa.utilitis.UtilitiesCLS;

public class RegisterTest extends Base {

    private RegisterPage registerPage;
    private AccountSuccessPage accountSuccessPage;

    public RegisterTest() {
        super();
    }

    @BeforeMethod
    public void setup() {
        initializeBrowserAndOpenApplication(prop.getProperty("browserName"));
        HomePage homePage = new HomePage(getDriver());
        registerPage = homePage.navigateToRegisterPage();
    }

    @AfterMethod
    public void tearDownTest() {
        tearDown();
    }

    @Test(priority = 1)
    public void verifyRegisteringAnAccountWithMandatoryFields() {
        accountSuccessPage = registerPage.registerWithMandatoryFields(
                dataProp.getProperty("firstName"),
                dataProp.getProperty("lastName"),
                UtilitiesCLS.generateEmailWithTimeStamp(),
                dataProp.getProperty("telephoneNumber"),
                prop.getProperty("validPassword")
        );

        Assert.assertEquals(accountSuccessPage.retrieveAccountSuccessPageHeading(),
                dataProp.getProperty("accountSuccessfullyCreatedHeading"),
                "Account Success page heading is not displayed correctly.");
    }

    @Test(priority = 2)
    public void verifyRegisteringAccountWithAllFields() {
        accountSuccessPage = registerPage.registerWithAllFields(
                dataProp.getProperty("firstName"),
                dataProp.getProperty("lastName"),
                UtilitiesCLS.generateEmailWithTimeStamp(),
                dataProp.getProperty("telephoneNumber"),
                prop.getProperty("validPassword")
        );

        Assert.assertEquals(accountSuccessPage.retrieveAccountSuccessPageHeading(),
                dataProp.getProperty("accountSuccessfullyCreatedHeading"),
                "Account Success page heading is not displayed correctly.");
    }

    @Test(priority = 3)
    public void verifyRegisteringAccountWithExistingEmailAddress() {
        registerPage.registerWithMandatoryFields(
                dataProp.getProperty("firstName"),
                dataProp.getProperty("lastName"),
                prop.getProperty("validEmail"),
                dataProp.getProperty("telephoneNumber"),
                prop.getProperty("validPassword")
        );

        Assert.assertEquals(registerPage.retrieveDuplicateEmailAddressWarning(),
                dataProp.getProperty("duplicateEmailWarning"),
                "Warning message regarding duplicate email was not displayed.");
    }

    @Test(priority = 4)
    public void verifyRegisteringAccountWithoutFillingAnyInformation() {
        registerPage.clickOnContinueButton();

        Assert.assertTrue(registerPage.displayStatusOfWarningMessages(
                dataProp.getProperty("privacyPolicyWarning"),
                dataProp.getProperty("firstNameWarning"),
                dataProp.getProperty("lastNameWarning"),
                dataProp.getProperty("emailWarning"),
                dataProp.getProperty("telephoneWarning"),
                dataProp.getProperty("passwordWarning")),
                "Expected field warning messages were not displayed.");
    }
}