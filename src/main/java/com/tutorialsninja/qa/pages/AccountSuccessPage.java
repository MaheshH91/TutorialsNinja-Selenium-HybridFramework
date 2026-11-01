package com.tutorialsninja.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AccountSuccessPage extends BasePage {

    @FindBy(xpath = "//div[@id='content']/h1")
    private WebElement accountSuccessPageHeading;

    public AccountSuccessPage(WebDriver driver) {
        super(driver);
    }

    public String retrieveAccountSuccessPageHeading() {
        return getText(accountSuccessPageHeading);
    }
}