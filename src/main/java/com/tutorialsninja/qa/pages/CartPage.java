package com.tutorialsninja.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CartPage extends BasePage {

    @FindBy(xpath = "//div[@id='content']//a[contains(text(),'Checkout')]")
    private WebElement checkoutButton;

    public CartPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.urlContains("route=checkout/cart"));
    }

    public CheckoutPage clickOnCheckoutButton() {
        click(checkoutButton);
        return new CheckoutPage(driver);
    }
}