package com.tutorialsninja.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

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

    private static final By MY_ACCOUNT_FALLBACK_LOCATOR = 
            By.xpath("//a[@title='My Account' or contains(., 'My Account')]");

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
        try {
            wait.until(ExpectedConditions.elementToBeClickable(myAccountDropMenu)).click();
        } catch (Exception e) {
            try {
                WebElement fallbackElement = wait.until(
                    ExpectedConditions.presenceOfElementLocated(MY_ACCOUNT_FALLBACK_LOCATOR)
                );
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", fallbackElement);
            } catch (Exception ex) {
                logger.warn("My Account dropdown could not be clicked: {}", ex.getMessage());
            }
        }
    }

    public LoginPage selectLoginOption() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(loginOption)).click();
        } catch (Exception e) {
            String currentUrl = driver.getCurrentUrl();
            String baseUrl = currentUrl.split("index\\.php")[0];
            driver.get(baseUrl + "index.php?route=account/login");
        }
        return new LoginPage(driver);
    }

    public LoginPage navigateToLoginPage() {
        clickOnMyAccount();
        return selectLoginOption();
    }

    public RegisterPage selectRegisterOption() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(registerOption)).click();
        } catch (Exception e) {
            String currentUrl = driver.getCurrentUrl();
            String baseUrl = currentUrl.split("index\\.php")[0];
            driver.get(baseUrl + "index.php?route=account/register");
        }
        return new RegisterPage(driver);
    }

    public RegisterPage navigateToRegisterPage() {
        clickOnMyAccount();
        return selectRegisterOption();
    }
}