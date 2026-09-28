package dev.hardik.qa.tests;

import dev.hardik.qa.base.BaseTest;
import dev.hardik.qa.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {
    @Test(groups = "smoke")
    public void validLoginShowsInventory() {
        Assert.assertEquals(loggedIn().heading(), "Products");
    }
    @DataProvider(name = "invalidLogins")
    public Object[][] invalidLogins() {
        return new Object[][] {
                {"locked_out_user", "secret_sauce", "Sorry, this user has been locked out."},
                {"standard_user", "incorrect", "Username and password do not match any user in this service"},
                {"", "secret_sauce", "Username is required"},
                {"standard_user", "", "Password is required"}
        };
    }
    @Test(dataProvider = "invalidLogins", groups = "negative")
    public void invalidLoginShowsActionableError(String user, String password, String expected) {
        LoginPage page = loginPage().attemptLogin(user, password);
        Assert.assertTrue(page.errorMessage().contains(expected), "Login error should explain the rejection");
    }
    @Test(groups = "smoke")
    public void logoutReturnsToLogin() {
        Assert.assertTrue(loggedIn().logout().isDisplayed());
    }
}
