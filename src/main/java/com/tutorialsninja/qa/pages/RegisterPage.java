package com.tutorialsninja.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

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

    @FindBy(xpath = "//input[@value='Continue']")
    private WebElement continueButton;

    @FindBy(xpath = "//input[@name='newsletter'][@value='1']")
    private WebElement subscribeNewsletterOption;

    @FindBy(xpath = "//div[contains(@class,'alert-dismissible')]")
    private WebElement warningAlert;

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
    }

    public void enterFirstName(String firstNameText) {
        sendKeys(firstNameField, firstNameText);
    }

    public void enterLastName(String lastNameText) {
        sendKeys(lastNameField, lastNameText);
    }

    public void enterEmail(String emailText) {
        sendKeys(emailAddressField, emailText);
    }

    public void enterTelephone(String telephoneText) {
        sendKeys(telephoneField, telephoneText);
    }

    public void enterPassword(String passwordText) {
        sendKeys(passwordField, passwordText);
    }

    public void enterConfirmPassword(String passwordText) {
        sendKeys(passwordConfirmField, passwordText);
    }

    public void checkPrivacyPolicy() {
        click(privacyPolicyCheckbox);
    }

    public void selectSubscribeNewsletter() {
        click(subscribeNewsletterOption);
    }

    public AccountSuccessPage clickContinue() {
        click(continueButton);
        return new AccountSuccessPage(driver);
    }

    public AccountSuccessPage registerWithMandatoryFields(
            String firstName, 
            String lastName, 
            String email, 
            String telephone, 
            String password) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterTelephone(telephone);
        enterPassword(password);
        enterConfirmPassword(password);
        checkPrivacyPolicy();
        return clickContinue();
    }

    public AccountSuccessPage registerWithAllFields(
            String firstName, 
            String lastName, 
            String email, 
            String telephone, 
            String password) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterTelephone(telephone);
        enterPassword(password);
        enterConfirmPassword(password);
        selectSubscribeNewsletter();
        checkPrivacyPolicy();
        return clickContinue();
    }

    public String retrieveDuplicateEmailAddressWarning() {
        return getText(warningAlert);
    }

    public String retrievePrivacyPolicyWarning() {
        return getText(warningAlert);
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

    public boolean displayStatusOfWarningMessages(
            String expectedPrivacyPolicyWarning,
            String expectedFirstNameWarning,
            String expectedLastNameWarning,
            String expectedEmailWarning,
            String expectedTelephoneWarning,
            String expectedPasswordWarning) {

        return getText(warningAlert).contains(expectedPrivacyPolicyWarning)
                && getText(firstNameWarning).equals(expectedFirstNameWarning)
                && getText(lastNameWarning).equals(expectedLastNameWarning)
                && getText(emailWarning).equals(expectedEmailWarning)
                && getText(telephoneWarning).equals(expectedTelephoneWarning)
                && getText(passwordWarning).equals(expectedPasswordWarning);
    }
}