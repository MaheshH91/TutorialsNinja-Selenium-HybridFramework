package com.tutorialsninja.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SearchPage extends BasePage {

    @FindBy(linkText = "HP LP3065")
    private WebElement validHPProduct;

    @FindBy(xpath = "//div[@id='content']/h2/following-sibling::p")
    private WebElement noProductMessage;

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    public String retrieveNoProductMessageText() {
        return getText(noProductMessage);
    }

    public boolean displayStatusOfHPValidProduct() {
        return isElementDisplayed(validHPProduct);
    }
}