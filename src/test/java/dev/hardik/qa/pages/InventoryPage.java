package dev.hardik.qa.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage extends BasePage {
    private final By title = By.cssSelector("[data-test='title']");
    private final By names = By.cssSelector("[data-test='inventory-item-name']");
    private final By cart = By.cssSelector("[data-test='shopping-cart-link']");
    private final By badge = By.cssSelector("[data-test='shopping-cart-badge']");
    private final By menu = By.id("react-burger-menu-btn");
    private final By logout = By.id("logout_sidebar_link");
    public InventoryPage(WebDriver driver) { super(driver); }
    public InventoryPage waitUntilLoaded() { onPath("/inventory.html"); visible(names); return this; }
    public String heading() { return text(title); }
    public List<String> productNames() { visible(names); return driver.findElements(names).stream().map(e -> e.getText()).toList(); }
    public ProductPage openProduct(String name) {
        By productLink = By.xpath(cardXPath(name) + "//a[contains(@data-test,'-title-link')]");
        click(productLink);
        return new ProductPage(driver).waitUntilLoaded();
    }
    public InventoryPage addProduct(String name) {
        int previousCount = cartCount();
        String slug = slug(name);
        click(By.xpath(cardXPath(name) + "//button[@data-test='add-to-cart-" + slug + "']"));
        By removeButton = By.xpath(cardXPath(name) + "//button[@data-test='remove-" + slug + "']");
        wait.until(d -> !d.findElements(removeButton).isEmpty() && cartCount() == previousCount + 1);
        return this;
    }
    public int cartCount() { return driver.findElements(badge).isEmpty() ? 0 : Integer.parseInt(text(badge)); }
    public CartPage openCart() { click(cart); return new CartPage(driver).waitUntilLoaded(); }
    public LoginPage logout() { click(menu); click(logout); return new LoginPage(driver).waitUntilLoaded(); }
    private String cardXPath(String name) {
        return "//div[@data-test='inventory-item' and .//*[@data-test='inventory-item-name' and text()="
                + xpathLiteral(name) + "]]";
    }
    static String slug(String name) { return name.toLowerCase(java.util.Locale.ROOT).replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", ""); }
    private static String xpathLiteral(String value) {
        if (!value.contains("'")) return "'" + value + "'";
        return "concat('" + value.replace("'", "',\"'\",'") + "')";
    }
}
