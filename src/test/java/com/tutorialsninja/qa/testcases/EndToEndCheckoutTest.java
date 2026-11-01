package com.tutorialsninja.qa.testcases;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tutorialsninja.qa.base.BaseTest;
import com.tutorialsninja.qa.driver.DriverManager;
import com.tutorialsninja.qa.pages.AccountSuccessPage;
import com.tutorialsninja.qa.pages.CartPage;
import com.tutorialsninja.qa.pages.CheckoutPage;
import com.tutorialsninja.qa.pages.HomePage;
import com.tutorialsninja.qa.pages.RegisterPage;
import com.tutorialsninja.qa.pages.SearchPage;
import com.tutorialsninja.qa.utils.ConfigReader;
import com.tutorialsninja.qa.utils.UtilitiesCLS;

public class EndToEndCheckoutTest extends BaseTest {

    private HomePage homePage;
    private RegisterPage registerPage;
    private AccountSuccessPage accountSuccessPage;
    private SearchPage searchPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    @BeforeMethod
    public void setupPage() {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) {
            throw new IllegalStateException("WebDriver initialization failed in BaseTest.setUp(). Skipping page setup.");
        }
        homePage = new HomePage(driver);
    }

    @Test(priority = 1, description = "E2E: Register user -> Search product -> Add to cart -> Checkout -> Confirm order")
    public void verifyEndToEndProductCheckoutWorkflow() {
        // 1. User Registration
        registerPage = homePage.navigateToRegisterPage();
        accountSuccessPage = registerPage.registerWithMandatoryFields(
                ConfigReader.getProperty("firstName"),
                ConfigReader.getProperty("lastName"),
                UtilitiesCLS.generateEmailWithTimeStamp(),
                ConfigReader.getProperty("telephoneNumber"),
                ConfigReader.getProperty("validPassword")
        );

        Assert.assertEquals(
                accountSuccessPage.retrieveAccountSuccessPageHeading(),
                ConfigReader.getProperty("accountSuccessfullyCreatedHeading"),
                "User registration failed in E2E workflow."
        );

        // 2. Search for valid product
        searchPage = homePage.searchForAProduct(ConfigReader.getProperty("validProduct"));
        Assert.assertTrue(
                searchPage.displayStatusOfHPValidProduct(),
                "Target product not found during E2E flow."
        );

        // 3. Add to Cart & Navigate to Cart Page
        searchPage.addFirstDisplayedProductToCart();
        cartPage = searchPage.navigateToShoppingCartPage();

        // 4. Checkout
        checkoutPage = cartPage.clickOnCheckoutButton();
        checkoutPage.enterBillingDetails("Mahesh", "Holkar", "Main Street 10", "Pune", "411001");
        checkoutPage.proceedThroughShippingAndPayment();

        // 5. Assert Order Confirmation
        Assert.assertEquals(
                checkoutPage.retrieveOrderSuccessHeading(),
                "Your order has been placed!",
                "Order confirmation heading mismatch."
        );
    }
}