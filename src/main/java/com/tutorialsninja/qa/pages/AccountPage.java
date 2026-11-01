package com.tutorialsninja.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AccountPage extends BasePage {

    @FindBy(xpath = "//a[.='Edit your account information']")
    private WebElement editYourAccountInformationOption;

    public AccountPage(WebDriver driver) {
        super(driver);
    }

    public boolean getDisplayStatusOfEditYourAccountInformationOption() {
        return isElementDisplayed(editYourAccountInformationOption);
    }
}