package dev.hardik.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ConfirmationPage extends BasePage {
    private final By heading = By.cssSelector("[data-test='complete-header']");
    private final By message = By.cssSelector("[data-test='complete-text']");
    public ConfirmationPage(WebDriver driver) { super(driver); }
    public String heading() { return text(heading); }
    public String message() { return text(message); }
}
