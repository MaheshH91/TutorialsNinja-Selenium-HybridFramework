package com.tutorialsninja.qa.testcases;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tutorialsninja.qa.base.Base;
import com.tutorialsninja.qa.pages.HomePage;
import com.tutorialsninja.qa.pages.SearchPage;

public class SearchTest extends Base {

    private HomePage homePage;
    private SearchPage searchPage;

    public SearchTest() {
        super();
    }

    @BeforeMethod
    public void setup() {
        initializeBrowserAndOpenApplication(prop.getProperty("browserName"));
        homePage = new HomePage(getDriver());
    }

    @AfterMethod
    public void tearDownTest() {
        tearDown();
    }

    @Test(priority = 1)
    public void verifySearchWithValidProduct() {
        searchPage = homePage.searchForAProduct(dataProp.getProperty("validProduct"));
        Assert.assertTrue(searchPage.displayStatusOfHPValidProduct(),
                "Valid product (HP) was not displayed in the search results.");
    }

    @Test(priority = 2)
    public void verifySearchWithInvalidProduct() {
        searchPage = homePage.searchForAProduct(dataProp.getProperty("invalidProduct"));
        Assert.assertEquals(searchPage.retrieveNoProductMessageText(),
                dataProp.getProperty("NoProductTextInSearchResults"),
                "Expected 'No product matches' message was not displayed.");
    }

    @Test(priority = 3, dependsOnMethods = {"verifySearchWithValidProduct", "verifySearchWithInvalidProduct"})
    public void verifySearchWithoutAnyProduct() {
        searchPage = homePage.clickOnSearchButton();
        Assert.assertEquals(searchPage.retrieveNoProductMessageText(),
                dataProp.getProperty("NoProductTextInSearchResults"),
                "Expected 'No product matches' message was not displayed.");
    }
}