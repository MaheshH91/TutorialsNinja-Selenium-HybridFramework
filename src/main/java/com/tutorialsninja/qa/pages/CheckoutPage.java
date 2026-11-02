package com.tutorialsninja.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutPage extends BasePage {

    @FindBy(id = "button-payment-address")
    private WebElement continueBillingAddressButton;

    @FindBy(id = "button-shipping-address")
    private WebElement continueDeliveryAddressButton;

    @FindBy(id = "button-shipping-method")
    private WebElement continueDeliveryMethodButton;

    @FindBy(name = "agree")
    private WebElement paymentTermsCheckbox;

    @FindBy(id = "button-payment-method")
    private WebElement continuePaymentMethodButton;

    @FindBy(id = "button-confirm")
    private WebElement confirmOrderButton;

    private static final By ORDER_SUCCESS_H1 = By.xpath("//div[@id='content']/h1");

    public CheckoutPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.urlContains("route=checkout/checkout"));
    }

    /**
     * Completes standard logged-in checkout accordion steps:
     * 1. Billing Details -> 2. Delivery Details -> 3. Delivery Method ->
     * 4. Payment Method & Agree Terms -> 5. Confirm Order (AJAX submission)
     */
    public void completeCheckoutSteps() {
        // Step 2: Billing Details (uses default existing address for registered users)
        wait.until(ExpectedConditions.elementToBeClickable(continueBillingAddressButton)).click();

        // Step 3: Delivery Details
        wait.until(ExpectedConditions.elementToBeClickable(continueDeliveryAddressButton)).click();

        // Step 4: Delivery Method
        wait.until(ExpectedConditions.elementToBeClickable(continueDeliveryMethodButton)).click();

        // Step 5: Payment Method & Terms
        WebElement terms = wait.until(ExpectedConditions.elementToBeClickable(paymentTermsCheckbox));
        if (!terms.isSelected()) {
            terms.click();
        }
        wait.until(ExpectedConditions.elementToBeClickable(continuePaymentMethodButton)).click();

        // Step 6: Confirm Order
        wait.until(ExpectedConditions.elementToBeClickable(confirmOrderButton)).click();
    }

    public String retrieveOrderSuccessHeading() {
        // Synchronize on the target route transition before polling the heading element
        wait.until(ExpectedConditions.urlContains("route=checkout/success"));
        WebElement successH1 = wait.until(ExpectedConditions.visibilityOfElementLocated(ORDER_SUCCESS_H1));
        return successH1.getText().trim();
    }
}