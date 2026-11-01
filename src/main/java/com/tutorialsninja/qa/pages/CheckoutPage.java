package com.tutorialsninja.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutPage extends BasePage {

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

    @FindBy(xpath = "//div[@id='content']/h1")
    private WebElement orderSuccessHeading;

    public CheckoutPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.urlContains("route=checkout/checkout"));
    }

    public void enterBillingDetails(String firstName, String lastName, String address, String city, String postCode) {
        if (isElementDisplayed(firstNameField)) {
            sendKeys(firstNameField, firstName);
            sendKeys(lastNameField, lastName);
            sendKeys(address1Field, address);
            sendKeys(cityField, city);
            sendKeys(postCodeField, postCode);
        }
        click(continueBillingAddressButton);
    }

    public void proceedThroughShippingAndPayment() {
        click(continueDeliveryAddressButton);
        click(continueDeliveryMethodButton);
        click(paymentTermsCheckbox);
        click(continuePaymentMethodButton);
        click(confirmOrderButton);
    }

    public String retrieveOrderSuccessHeading() {
        return getText(orderSuccessHeading);
    }

    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}