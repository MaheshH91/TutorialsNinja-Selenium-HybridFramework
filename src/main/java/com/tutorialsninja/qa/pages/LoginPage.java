package com.tutorialsninja.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    @FindBy(id = "input-email")
    private WebElement emailAddressField;

    @FindBy(id = "input-password")
    private WebElement passwordField;

    @FindBy(xpath = "//input[@value='Login']")
    private WebElement loginButton;

    @FindBy(xpath = "//div[contains(@class,'alert-dismissible')]")
    private WebElement emailPasswordNotMatchingWarning;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public AccountPage login(String emailText, String passwordText) {
        sendKeys(emailAddressField, emailText);
        sendKeys(passwordField, passwordText);
        click(loginButton);
        return new AccountPage(driver);
    }

    public String retrieveEmailPasswordNotMatchingWarningMessageText() {
        return getText(emailPasswordNotMatchingWarning);
    }
}