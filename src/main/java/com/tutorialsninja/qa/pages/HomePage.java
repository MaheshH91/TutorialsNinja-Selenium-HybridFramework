package com.tutorialsninja.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

    @FindBy(xpath = "//span[text()='My Account']")
    private WebElement myAccountDropMenu;

    @FindBy(linkText = "Login")
    private WebElement loginOption;

    @FindBy(linkText = "Register")
    private WebElement registerOption;

    @FindBy(name = "search")
    private WebElement searchBoxField;

    @FindBy(xpath = "//div[@id='search']/descendant::button")
    private WebElement searchButton;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public SearchPage clickOnSearchButton() {
        click(searchButton);
        return new SearchPage(driver);
    }

    public SearchPage searchForAProduct(String productText) {
        sendKeys(searchBoxField, productText);
        click(searchButton);
        return new SearchPage(driver);
    }

    public void enterProductIntoSearchBoxField(String productText) {
        sendKeys(searchBoxField, productText);
    }

    public void clickOnMyAccount() {
        click(myAccountDropMenu);
    }

    public LoginPage selectLoginOption() {
        click(loginOption);
        return new LoginPage(driver);
    }

    public LoginPage navigateToLoginPage() {
        click(myAccountDropMenu);
        click(loginOption);
        return new LoginPage(driver);
    }

    public RegisterPage selectRegisterOption() {
        click(registerOption);
        return new RegisterPage(driver);
    }

    public RegisterPage navigateToRegisterPage() {
        click(myAccountDropMenu);
        click(registerOption);
        return new RegisterPage(driver);
    }
}