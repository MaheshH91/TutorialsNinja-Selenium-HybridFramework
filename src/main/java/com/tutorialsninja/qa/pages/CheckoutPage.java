package com.tutorialsninja.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class CheckoutPage extends BasePage {

    // Step 2: Billing Details Form
    @FindBy(id = "input-payment-firstname")
    private WebElement firstNameField;

    @FindBy(id = "input-payment-lastname")
    private WebElement lastNameField;

    @FindBy(id = "input-payment-address-1")
    private WebElement address1Field;

    @FindBy(id = "input-payment-city")
    private WebElement cityField;

    @FindBy(id = "input-payment-postcode")
    private WebElement postCodeField;

    @FindBy(id = "input-payment-country")
    private WebElement countryDropdown;

    @FindBy(id = "input-payment-zone")
    private WebElement zoneDropdown;

    @FindBy(xpath = "//input[@id='button-payment-address']")
    private WebElement continueBillingAddressButton;

    // Step 3: Delivery Details
    @FindBy(id = "button-shipping-address")
    private WebElement continueDeliveryAddressButton;

    // Step 4: Delivery Method
    @FindBy(id = "button-shipping-method")
    private WebElement continueDeliveryMethodButton;

    // Step 5: Payment Method & Agreement
    @FindBy(name = "agree")
    private WebElement paymentTermsCheckbox;

    @FindBy(id = "button-payment-method")
    private WebElement continuePaymentMethodButton;

    // Step 6: Confirm Order
    @FindBy(id = "button-confirm")
    private WebElement confirmOrderButton;

    private static final By ORDER_SUCCESS_H1 = By.xpath("//div[@id='content']/h1");

    public CheckoutPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.urlContains("route=checkout/checkout"));
    }

    /**
     * Completes all checkout accordion steps:
     * 1. Fill address & select country/zone dropdowns ->
     * 2. Continue Delivery Address -> 3. Continue Delivery Method ->
     * 4. Agree to terms & Continue Payment -> 5. Confirm Order
     */
    public void completeCheckoutSteps(String firstName, String lastName, String address, String city, String postCode, String country, String state) {
        // Step 2: Billing Details
        fillBillingDetailsIfRequired(firstName, lastName, address, city, postCode, country, state);

        // Step 3: Delivery Details (Existing Address)
        wait.until(ExpectedConditions.elementToBeClickable(continueDeliveryAddressButton)).click();

        // Step 4: Delivery Method
        wait.until(ExpectedConditions.elementToBeClickable(continueDeliveryMethodButton)).click();

        // Step 5: Payment Method & Terms
        WebElement terms = wait.until(ExpectedConditions.elementToBeClickable(paymentTermsCheckbox));
        if (!terms.isSelected()) {
            terms.click();
        }
        wait.until(ExpectedConditions.elementToBeClickable(continuePaymentMethodButton)).click();

        // Step 6: Confirm Order (AJAX submission)
        wait.until(ExpectedConditions.elementToBeClickable(confirmOrderButton)).click();
    }

    /**
     * Helper to populate billing details and wait for dynamic zone AJAX resolution
     */
    public void fillBillingDetailsIfRequired(String firstName, String lastName, String address, String city, String postCode, String country, String state) {
        try {
            // Check if address form fields are visible
            WebElement fName = wait.until(ExpectedConditions.visibilityOf(firstNameField));
            fName.clear();
            fName.sendKeys(firstName);

            lastNameField.clear();
            lastNameField.sendKeys(lastName);

            address1Field.clear();
            address1Field.sendKeys(address);

            cityField.clear();
            cityField.sendKeys(city);

            postCodeField.clear();
            postCodeField.sendKeys(postCode);

            // Select Country
            Select countrySelect = new Select(countryDropdown);
            countrySelect.selectByVisibleText(country);

            // Wait until AJAX populates the zones for the selected country (removes '--- Please Select ---' placeholder)
            wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(
                zoneDropdown, 
                By.xpath(".//option[normalize-space(text())='" + state + "']")
            ));

            // Select Region / State
            Select zoneSelect = new Select(zoneDropdown);
            zoneSelect.selectByVisibleText(state);

            // Click Continue Billing Address
            click(continueBillingAddressButton);

        } catch (Exception e) {
            // If already filled or user selected existing address radio, click continue directly
            try {
                wait.until(ExpectedConditions.elementToBeClickable(continueBillingAddressButton)).click();
            } catch (Exception ex) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", continueBillingAddressButton);
            }
        }
    }

    public String retrieveOrderSuccessHeading() {
        // Synchronize on the success route transition to avoid stale element references
        wait.until(ExpectedConditions.urlContains("route=checkout/success"));
        WebElement successH1 = wait.until(ExpectedConditions.visibilityOfElementLocated(ORDER_SUCCESS_H1));
        return successH1.getText().trim();
    }
}