package com.tutorialsninja.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    @FindBy(id = "input-email")
    private WebElement emailAddressField;

    @FindBy(id = "input-password")
    private WebElement passwordField;

    @FindBy(xpath = "//input[@value='Login']")
    private WebElement loginButton;

    @FindBy(xpath = "//div[contains(@class,'alert-danger')]")
    private WebElement emailPasswordNoMatchWarning;

    public LoginPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.or(
            ExpectedConditions.urlContains("route=account/login"),
            ExpectedConditions.visibilityOf(emailAddressField)
        ));
    }

    public void enterEmailAddress(String emailText) {
        sendKeys(emailAddressField, emailText);
    }

    public void enterPassword(String passwordText) {
        sendKeys(passwordField, passwordText);
    }

    public AccountPage clickOnLoginButton() {
        click(loginButton);
        return new AccountPage(driver);
    }

    public AccountPage login(String emailText, String passwordText) {
        enterEmailAddress(emailText);
        enterPassword(passwordText);
        return clickOnLoginButton();
    }

    public String retrieveEmailPasswordNotMatchingWarningMessageText() {
        return getText(emailPasswordNoMatchWarning);
    }
}