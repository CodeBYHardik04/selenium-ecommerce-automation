package dev.hardik.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private final By username = By.id("user-name");
    private final By password = By.id("password");
    private final By submit = By.id("login-button");
    private final By error = By.cssSelector("[data-test='error']");
    public LoginPage(WebDriver driver) { super(driver); }
    public InventoryPage login(String user, String pass) {
        enterCredentials(user, pass);
        navigate(submit, "/inventory.html");
        return new InventoryPage(driver).waitUntilLoaded();
    }
    public LoginPage attemptLogin(String user, String pass) {
        enterCredentials(user, pass);
        clickAndAwait(submit, d -> d.findElements(error).stream().anyMatch(e -> e.isDisplayed()));
        return this;
    }
    private void enterCredentials(String user, String pass) { type(username, user); type(password, pass); }
    public String errorMessage() { return text(error); }
    public LoginPage waitUntilLoaded() { onPath("/"); visible(submit); return this; }
    public boolean isDisplayed() { return waitUntilLoaded().visible(submit).isDisplayed(); }
}
