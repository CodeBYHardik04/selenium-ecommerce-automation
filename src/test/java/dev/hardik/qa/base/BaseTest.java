package dev.hardik.qa.base;

import dev.hardik.qa.config.Config;
import dev.hardik.qa.pages.LoginPage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.start();
        DriverFactory.get().get(Config.get("base.url"));
    }
    @AfterMethod(alwaysRun = true)
    public void tearDown() { DriverFactory.stop(); }
    protected LoginPage loginPage() { return new LoginPage(DriverFactory.get()); }
    protected dev.hardik.qa.pages.InventoryPage loggedIn() {
        return loginPage().login("standard_user", "secret_sauce");
    }
}
