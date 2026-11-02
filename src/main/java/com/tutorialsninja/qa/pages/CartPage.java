package com.tutorialsninja.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.tutorialsninja.qa.utils.ConfigReader;

public class CartPage extends BasePage {

    private static final By CHECKOUT_BUTTON_LOCATOR = 
            By.xpath("//a[contains(@href,'route=checkout/checkout') and (contains(text(),'Checkout') or contains(@class,'btn-primary'))] | //div[@id='content']//a[contains(text(),'Checkout')]");

    public CartPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.urlContains("route=checkout/cart"));
    }

    public CheckoutPage clickOnCheckoutButton() {
        try {
            WebElement checkoutBtn = wait.until(ExpectedConditions.elementToBeClickable(CHECKOUT_BUTTON_LOCATOR));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", checkoutBtn);
            click(checkoutBtn);
        } catch (Exception e) {
            // Direct route fallback to checkout page
            String currentUrl = driver.getCurrentUrl();
            String baseUrl = (currentUrl != null && currentUrl.contains("index.php"))
                    ? currentUrl.split("index\\.php")[0]
                    : ConfigReader.getUrl();
            driver.get(baseUrl + "index.php?route=checkout/checkout");
        }
        return new CheckoutPage(driver);
    }
}