package com.tutorialsninja.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.tutorialsninja.qa.utils.ConfigReader;

public class SearchPage extends BasePage {

    @FindBy(linkText = "HP LP3065")
    private WebElement validHPProduct;

    @FindBy(xpath = "//div[@id='content']/p[contains(text(),'There is no product that matches')]")
    private WebElement noProductMessage;

    private static final By PRODUCT_LINK_LOCATOR = By.xpath("//div[contains(@class,'product-layout')]//h4/a | //h4/a");
    private static final By PRODUCT_DETAIL_ADD_TO_CART_BTN = By.id("button-cart");
    private static final By SUCCESS_ALERT_BANNER = By.xpath("//div[contains(@class,'alert-success')]");

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    public boolean displayStatusOfHPValidProduct() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(validHPProduct)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String retrieveNoProductMessageText() {
        return getText(noProductMessage);
    }

    /**
     * Navigates directly into the product detail page and adds the item to the cart,
     * fulfilling any default option requirements (like Delivery Date for HP LP3065).
     */
    public void addFirstDisplayedProductToCart() {
        // 1. Open the product details page
        WebElement productLink = wait.until(ExpectedConditions.elementToBeClickable(PRODUCT_LINK_LOCATOR));
        click(productLink);

        // 2. Click the 'Add to Cart' button on the product details page
        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(PRODUCT_DETAIL_ADD_TO_CART_BTN));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", addToCartBtn);
        click(addToCartBtn);

        // 3. Wait until the success confirmation banner is displayed
        wait.until(ExpectedConditions.visibilityOfElementLocated(SUCCESS_ALERT_BANNER));
    }

    public CartPage navigateToShoppingCartPage() {
        String currentUrl = driver.getCurrentUrl();
        String baseUrl = (currentUrl != null && currentUrl.contains("index.php"))
                ? currentUrl.split("index\\.php")[0]
                : ConfigReader.getUrl();
        driver.get(baseUrl + "index.php?route=checkout/cart");
        return new CartPage(driver);
    }
}