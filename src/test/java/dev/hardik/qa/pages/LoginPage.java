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
        submit(user, pass);
        return new InventoryPage(driver);
    }
    public LoginPage attemptLogin(String user, String pass) { submit(user, pass); return this; }
    private void submit(String user, String pass) { type(username, user); type(password, pass); click(submit); }
    public String errorMessage() { return text(error); }
    public boolean isDisplayed() { return visible(submit).isDisplayed(); }
}
