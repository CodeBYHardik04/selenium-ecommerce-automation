package dev.hardik.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductPage extends BasePage {
    private final By name = By.cssSelector("[data-test='inventory-item-name']");
    private final By description = By.cssSelector("[data-test='inventory-item-desc']");
    private final By price = By.cssSelector("[data-test='inventory-item-price']");
    private final By back = By.id("back-to-products");
    private final By cart = By.cssSelector("[data-test='shopping-cart-link']");
    public ProductPage(WebDriver driver) { super(driver); }
    public String name() { return text(name); }
    public String description() { return text(description); }
    public String price() { return text(price); }
    public ProductPage addToCart() { click(By.id("add-to-cart")); return this; }
    public CartPage openCart() { click(cart); return new CartPage(driver); }
    public InventoryPage backToProducts() { click(back); return new InventoryPage(driver); }
}
