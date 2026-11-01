package com.tutorialsninja.qa.testcases;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tutorialsninja.qa.base.BaseTest;
import com.tutorialsninja.qa.driver.DriverManager;
import com.tutorialsninja.qa.pages.HomePage;
import com.tutorialsninja.qa.pages.SearchPage;
import com.tutorialsninja.qa.utils.ConfigReader;

public class SearchTest extends BaseTest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void setupPage() {
        homePage = new HomePage(DriverManager.getDriver());
    }

    @Test(priority = 1)
    public void verifySearchWithValidProduct() {
        SearchPage searchPage = homePage.searchForAProduct(ConfigReader.getProperty("validProduct"));
        Assert.assertTrue(
                searchPage.displayStatusOfHPValidProduct(),
                "Valid product (HP) was not displayed in the search results."
        );
    }

    @Test(priority = 2)
    public void verifySearchWithInvalidProduct() {
        SearchPage searchPage = homePage.searchForAProduct(ConfigReader.getProperty("invalidProduct"));
        Assert.assertEquals(
                searchPage.retrieveNoProductMessageText(),
                ConfigReader.getProperty("NoProductTextInSearchResults"),
                "Expected 'No product matches' message was not displayed."
        );
    }

    @Test(priority = 3, dependsOnMethods = {"verifySearchWithValidProduct", "verifySearchWithInvalidProduct"})
    public void verifySearchWithoutAnyProduct() {
        SearchPage searchPage = homePage.clickOnSearchButton();
        Assert.assertEquals(
                searchPage.retrieveNoProductMessageText(),
                ConfigReader.getProperty("NoProductTextInSearchResults"),
                "Expected 'No product matches' message was not displayed."
        );
    }
}