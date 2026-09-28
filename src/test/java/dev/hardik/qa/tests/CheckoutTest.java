package dev.hardik.qa.tests;

import dev.hardik.qa.base.BaseTest;
import dev.hardik.qa.pages.CheckoutPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CheckoutTest extends BaseTest {
    private CheckoutPage beginCheckout() {
        return loggedIn().addProduct("Sauce Labs Backpack").openCart().checkout();
    }
    @Test(groups = "smoke")
    public void completeCheckoutShowsOrderConfirmation() {
        var overview = beginCheckout().enterInformation("Alex", "Tester", "10001").continueToOverview();
        Assert.assertEquals(overview.productNames(), java.util.List.of("Sauce Labs Backpack"));
        Assert.assertTrue(overview.total().startsWith("Total: $"));
        var confirmation = overview.finish();
        Assert.assertEquals(confirmation.heading(), "Thank you for your order!");
        Assert.assertFalse(confirmation.message().isBlank());
    }
    @DataProvider(name = "missingCheckoutFields")
    public Object[][] missingCheckoutFields() {
        return new Object[][] {
                {"", "Tester", "10001", "First Name is required"},
                {"Alex", "", "10001", "Last Name is required"},
                {"Alex", "Tester", "", "Postal Code is required"}
        };
    }
    @Test(dataProvider = "missingCheckoutFields", groups = "negative")
    public void missingCheckoutInformationShowsValidation(String first, String last, String postal, String expected) {
        var page = beginCheckout().enterInformation(first, last, postal).continueExpectingError();
        Assert.assertTrue(page.errorMessage().contains(expected));
    }
}
