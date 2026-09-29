package dev.hardik.qa.pages;

import dev.hardik.qa.config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.net.URI;
import java.time.Duration;
import java.util.logging.Logger;

public abstract class BasePage {
    private static final Logger LOG = Logger.getLogger(BasePage.class.getName());
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Config.waitTime());
    }
    protected WebElement visible(By locator) { return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)); }
    protected WebElement clickable(By locator) {
        return wait.until(d -> d.findElements(locator).stream()
                .filter(e -> e.isDisplayed() && e.isEnabled()).findFirst().orElse(null));
    }
    protected void click(By locator) {
        WebElement element = clickable(locator);
        new Actions(driver).moveToElement(element).click().perform();
    }
    // A click command can complete without the application reacting, especially in headless Chrome.
    // Retry through the DOM only when the page-specific expected effect did not occur.
    protected void clickAndAwait(By locator, ExpectedCondition<Boolean> effect) {
        click(locator);
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3)).until(effect);
            return;
        } catch (TimeoutException noEffect) {
            if (Boolean.TRUE.equals(effect.apply(driver))) return;
            LOG.warning("Pointer click had no observed effect on " + locator + " at " + driver.getCurrentUrl()
                    + "; retrying a DOM click");
        }
        ((JavascriptExecutor) driver).executeScript("arguments[0].click()", clickable(locator));
        wait.until(effect);
    }
    protected void navigate(By locator, String path) {
        clickAndAwait(locator, d -> path.equals(URI.create(d.getCurrentUrl()).getPath()));
    }
    protected void type(By locator, String text) { WebElement element = visible(locator); element.clear(); element.sendKeys(text); }
    protected String text(By locator) { return visible(locator).getText(); }
    protected void onPath(String path) {
        try {
            wait.until(d -> path.equals(URI.create(d.getCurrentUrl()).getPath()));
        } catch (TimeoutException e) {
            throw new AssertionError("Expected page path " + path + " after navigation, but browser remained at "
                    + driver.getCurrentUrl(), e);
        }
    }
}
