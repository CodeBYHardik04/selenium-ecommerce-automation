package dev.hardik.qa.tests;

import dev.hardik.qa.base.BaseTest;
import dev.hardik.qa.pages.ProductPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class InventoryTest extends BaseTest {
    @Test(groups = "smoke")
    public void catalogListsProducts() {
        var names = loggedIn().productNames();
        Assert.assertEquals(names.size(), 6, "Demo catalog should contain six products");
        Assert.assertTrue(names.contains("Sauce Labs Backpack"));
    }
    @Test(groups = "regression")
    public void productDetailsMatchSelectedItem() {
        ProductPage details = loggedIn().openProduct("Sauce Labs Backpack");
        Assert.assertEquals(details.name(), "Sauce Labs Backpack");
        Assert.assertFalse(details.description().isBlank());
        Assert.assertEquals(details.price(), "$29.99");
        Assert.assertEquals(details.backToProducts().heading(), "Products");
    }
}
