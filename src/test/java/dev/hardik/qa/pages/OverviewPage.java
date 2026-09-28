package dev.hardik.qa.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OverviewPage extends BasePage {
    private final By names = By.cssSelector("[data-test='inventory-item-name']");
    private final By total = By.cssSelector("[data-test='total-label']");
    private final By finish = By.id("finish");
    public OverviewPage(WebDriver driver) { super(driver); }
    public OverviewPage waitUntilLoaded() { onPath("/checkout-step-two.html"); visible(total); return this; }
    public List<String> productNames() { visible(names); return driver.findElements(names).stream().map(e -> e.getText()).toList(); }
    public String total() { return text(total); }
    public ConfirmationPage finish() { click(finish); return new ConfirmationPage(driver).waitUntilLoaded(); }
}
