package dev.hardik.qa.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {
    private final By title = By.cssSelector("[data-test='title']");
    private final By names = By.cssSelector("[data-test='inventory-item-name']");
    private final By checkout = By.id("checkout");
    public CartPage(WebDriver driver) { super(driver); }
    public String heading() { return text(title); }
    public List<String> productNames() { visible(title); return driver.findElements(names).stream().map(e -> e.getText()).toList(); }
    public CartPage remove(String name) { click(By.id("remove-" + InventoryPage.slug(name))); return this; }
    public CheckoutPage checkout() { click(checkout); return new CheckoutPage(driver); }
}
