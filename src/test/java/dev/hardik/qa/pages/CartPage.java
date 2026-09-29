package dev.hardik.qa.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {
    private final By title = By.cssSelector("[data-test='title']");
    private final By names = By.cssSelector("[data-test='inventory-item-name']");
    private final By contents = By.cssSelector("[data-test='cart-contents-container']");
    private final By checkout = By.id("checkout");
    public CartPage(WebDriver driver) { super(driver); }
    public CartPage waitUntilLoaded() { onPath("/cart.html"); visible(contents); return this; }
    public String heading() { return text(title); }
    public List<String> productNames() { visible(contents); return driver.findElements(names).stream().map(e -> e.getText()).toList(); }
    public CartPage remove(String name) {
        clickAndAwait(By.id("remove-" + InventoryPage.slug(name)), d -> !productNames().contains(name));
        return this;
    }
    public CheckoutPage checkout() { navigate(checkout, "/checkout-step-one.html"); return new CheckoutPage(driver).waitUntilLoaded(); }
}
