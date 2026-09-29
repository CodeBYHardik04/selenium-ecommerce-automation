package dev.hardik.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {
    private final By first = By.id("first-name");
    private final By last = By.id("last-name");
    private final By postal = By.id("postal-code");
    private final By next = By.id("continue");
    private final By error = By.cssSelector("[data-test='error']");
    public CheckoutPage(WebDriver driver) { super(driver); }
    public CheckoutPage waitUntilLoaded() { onPath("/checkout-step-one.html"); visible(first); return this; }
    public CheckoutPage enterInformation(String firstName, String lastName, String postalCode) {
        type(first, firstName); type(last, lastName); type(postal, postalCode); return this;
    }
    public CheckoutPage continueExpectingError() {
        clickAndAwait(next, d -> d.findElements(error).stream().anyMatch(e -> e.isDisplayed()));
        return this;
    }
    public OverviewPage continueToOverview() {
        navigate(next, "/checkout-step-two.html");
        return new OverviewPage(driver).waitUntilLoaded();
    }
    public String errorMessage() { return text(error); }
}
