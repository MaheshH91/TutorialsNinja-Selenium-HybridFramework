package com.tutorialsninja.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SearchPage extends BasePage {

    @FindBy(linkText = "HP LP3065")
    private WebElement validHPProduct;

    @FindBy(xpath = "//div[@id='content']/h2/following-sibling::p")
    private WebElement noProductMessage;
    
    @FindBy(xpath = "//div[@class='product-layout']//button[contains(@onclick,'cart.add')]")
    private WebElement addToCartButton;

    @FindBy(xpath = "//a[@title='Shopping Cart']")
    private WebElement shoppingCartNavOption;
    public SearchPage(WebDriver driver) {
        super(driver);
    }

    public String retrieveNoProductMessageText() {
        return getText(noProductMessage);
    }

    public boolean displayStatusOfHPValidProduct() {
        return isElementDisplayed(validHPProduct);
    }
    
    public void addFirstDisplayedProductToCart() {
        click(addToCartButton);
    }

    public CartPage navigateToShoppingCartPage() {
        click(shoppingCartNavOption);
        return new CartPage(driver);
    }
}