package dev.hardik.qa.tests;

import dev.hardik.qa.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {
    @Test(groups = "smoke")
    public void addSingleProductFromDetailPage() {
        var cart = loggedIn().openProduct("Sauce Labs Backpack").addToCart().openCart();
        Assert.assertEquals(cart.productNames(), java.util.List.of("Sauce Labs Backpack"));
    }
    @Test(groups = "regression")
    public void multipleProductsAppearAndOneCanBeRemoved() {
        var inventory = loggedIn().addProduct("Sauce Labs Backpack").addProduct("Sauce Labs Bike Light");
        Assert.assertEquals(inventory.cartCount(), 2);
        var cart = inventory.openCart();
        Assert.assertTrue(cart.productNames().containsAll(java.util.List.of("Sauce Labs Backpack", "Sauce Labs Bike Light")));
        cart.remove("Sauce Labs Backpack");
        Assert.assertEquals(cart.productNames(), java.util.List.of("Sauce Labs Bike Light"));
    }
}
