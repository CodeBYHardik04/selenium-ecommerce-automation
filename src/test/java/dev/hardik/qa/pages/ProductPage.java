package dev.hardik.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductPage extends BasePage {
    private final By name = By.cssSelector("[data-test='inventory-item-name']");
    private final By description = By.cssSelector("[data-test='inventory-item-desc']");
    private final By price = By.cssSelector("[data-test='inventory-item-price']");
    private final By back = By.id("back-to-products");
    private final By cart = By.cssSelector("[data-test='shopping-cart-link']");
    private final By details = By.cssSelector("[data-test='inventory-container'] [data-test='inventory-item']");
    private final By add = By.cssSelector("[data-test='add-to-cart']");
    private final By remove = By.cssSelector("[data-test='remove']");
    private final By badge = By.cssSelector("[data-test='shopping-cart-badge']");
    public ProductPage(WebDriver driver) { super(driver); }
    public ProductPage waitUntilLoaded() { onPath("/inventory-item.html"); visible(details); return this; }
    public String name() { return text(name); }
    public String description() { return text(description); }
    public String price() { return text(price); }
    public ProductPage addToCart() {
        clickAndAwait(add, d -> !d.findElements(remove).isEmpty()
                && !d.findElements(badge).isEmpty() && "1".equals(d.findElement(badge).getText()));
        return this;
    }
    public CartPage openCart() { navigate(cart, "/cart.html"); return new CartPage(driver).waitUntilLoaded(); }
    public InventoryPage backToProducts() { navigate(back, "/inventory.html"); return new InventoryPage(driver).waitUntilLoaded(); }
}
