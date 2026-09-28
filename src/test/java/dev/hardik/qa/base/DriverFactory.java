package dev.hardik.qa.base;

import dev.hardik.qa.config.Config;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import java.util.logging.Level;

public final class DriverFactory {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private DriverFactory() {}
    public static WebDriver get() {
        WebDriver driver = DRIVER.get();
        if (driver == null) throw new IllegalStateException("WebDriver has not been started for this thread");
        return driver;
    }
    public static void start() {
        if (DRIVER.get() != null) throw new IllegalStateException("WebDriver already started");
        boolean headless = Boolean.parseBoolean(Config.get("headless"));
        WebDriver driver;
        switch (Config.get("browser").toLowerCase(java.util.Locale.ROOT)) {
            case "chrome" -> {
                if (System.getProperty("webdriver.chrome.driver") == null) WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                if (headless) options.addArguments("--headless=new", "--window-size=1440,900", "--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                LoggingPreferences browserLogs = new LoggingPreferences();
                browserLogs.enable(LogType.BROWSER, Level.ALL);
                options.setCapability("goog:loggingPrefs", browserLogs);
                String proxy = System.getProperty("browser.proxy");
                if (proxy != null && !proxy.isBlank()) options.addArguments("--proxy-server=" + proxy);
                driver = new ChromeDriver(options);
            }
            case "firefox" -> {
                if (System.getProperty("webdriver.gecko.driver") == null) WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = new FirefoxOptions();
                if (headless) options.addArguments("-headless");
                driver = new FirefoxDriver(options);
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + Config.get("browser"));
        }
        DRIVER.set(driver);
        driver.manage().timeouts().pageLoadTimeout(Config.pageLoadTime());
    }
    public static void stop() {
        WebDriver driver = DRIVER.get();
        try { if (driver != null) driver.quit(); }
        finally { DRIVER.remove(); }
    }
}
