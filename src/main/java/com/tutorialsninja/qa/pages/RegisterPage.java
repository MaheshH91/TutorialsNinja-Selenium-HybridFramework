package com.tutorialsninja.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RegisterPage extends BasePage {

    @FindBy(id = "input-firstname")
    private WebElement firstNameField;

    @FindBy(id = "input-lastname")
    private WebElement lastNameField;

    @FindBy(id = "input-email")
    private WebElement emailAddressField;

    @FindBy(id = "input-telephone")
    private WebElement telephoneField;

    @FindBy(id = "input-password")
    private WebElement passwordField;

    @FindBy(id = "input-confirm")
    private WebElement passwordConfirmField;

    @FindBy(name = "agree")
    private WebElement privacyPolicyCheckbox;

    @FindBy(xpath = "//input[@name='newsletter'][@value='1']")
    private WebElement yesNewsletterOption;

    @FindBy(xpath = "//input[@value='Continue']")
    private WebElement continueButton;

    @FindBy(xpath = "//div[contains(@class,'alert-danger')]")
    private WebElement duplicateEmailAddressWarning;

    @FindBy(xpath = "//div[contains(@class,'alert-danger')]")
    private WebElement privacyPolicyWarning;

    @FindBy(xpath = "//input[@id='input-firstname']/following-sibling::div")
    private WebElement firstNameWarning;

    @FindBy(xpath = "//input[@id='input-lastname']/following-sibling::div")
    private WebElement lastNameWarning;

    @FindBy(xpath = "//input[@id='input-email']/following-sibling::div")
    private WebElement emailWarning;

    @FindBy(xpath = "//input[@id='input-telephone']/following-sibling::div")
    private WebElement telephoneWarning;

    @FindBy(xpath = "//input[@id='input-password']/following-sibling::div")
    private WebElement passwordWarning;

    public RegisterPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.or(
            ExpectedConditions.urlContains("route=account/register"),
            ExpectedConditions.visibilityOf(firstNameField)
        ));
    }

    public void enterFirstName(String firstNameText) {
        sendKeys(firstNameField, firstNameText);
    }

    public void enterLastName(String lastNameText) {
        sendKeys(lastNameField, lastNameText);
    }

    public void enterEmailAddress(String emailText) {
        sendKeys(emailAddressField, emailText);
    }

    public void enterTelephoneNumber(String telephoneText) {
        sendKeys(telephoneField, telephoneText);
    }

    public void enterPassword(String passwordText) {
        sendKeys(passwordField, passwordText);
    }

    public void enterConfirmPassword(String passwordText) {
        sendKeys(passwordConfirmField, passwordText);
    }

    public void selectPrivacyPolicy() {
        click(privacyPolicyCheckbox);
    }

    public void selectYesNewsletterOption() {
        click(yesNewsletterOption);
    }

    public AccountSuccessPage clickOnContinueButton() {
        click(continueButton);
        return new AccountSuccessPage(driver);
    }

    public AccountSuccessPage registerWithMandatoryFields(String firstName, String lastName, String email, String telephone, String password) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmailAddress(email);
        enterTelephoneNumber(telephone);
        enterPassword(password);
        enterConfirmPassword(password);
        selectPrivacyPolicy();
        return clickOnContinueButton();
    }

    public AccountSuccessPage registerWithAllFields(String firstName, String lastName, String email, String telephone, String password) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmailAddress(email);
        enterTelephoneNumber(telephone);
        enterPassword(password);
        enterConfirmPassword(password);
        selectYesNewsletterOption();
        selectPrivacyPolicy();
        return clickOnContinueButton();
    }

    public String retrieveDuplicateEmailAddressWarning() {
        return getText(duplicateEmailAddressWarning);
    }

    public String retrievePrivacyPolicyWarning() {
        return getText(privacyPolicyWarning);
    }

    public String retrieveFirstNameWarning() {
        return getText(firstNameWarning);
    }

    public String retrieveLastNameWarning() {
        return getText(lastNameWarning);
    }

    public String retrieveEmailWarning() {
        return getText(emailWarning);
    }

    public String retrieveTelephoneWarning() {
        return getText(telephoneWarning);
    }

    public String retrievePasswordWarning() {
        return getText(passwordWarning);
    }
}