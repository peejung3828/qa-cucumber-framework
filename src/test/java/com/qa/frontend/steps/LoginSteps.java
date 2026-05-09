package com.qa.frontend.steps;

import com.qa.frontend.pages.LoginPage;
import com.qa.frontend.pages.ShopPage;
import com.qa.hooks.Hooks;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class LoginSteps {

    LoginPage loginPage = new LoginPage(Hooks.driver);
    ShopPage shopPage = new ShopPage(Hooks.driver);

    @Given("user is on login page")
    public void userIsOnLoginPage() {
        System.out.println("User is on login page");
    }

    @Given("user is logged in with {string} and {string}")
    public void userIsLoggedInWith(String email, String password) {
        loginPage.login(email, password);
        // Clear cart ทันทีหลัง Login
        System.out.println("Clearing cart after login...");
        shopPage.clearCart();
        System.out.println("Cart cleared after login");
    }

    @When("user enters email {string} and password {string}")
    public void userEntersEmailAndPassword(String email, String password) {
        loginPage.login(email, password);
    }

    @Then("user should be redirected to shop page")
    public void userShouldBeRedirectedToShopPage() {
        Assert.assertTrue(shopPage.isShopPageDisplayed());
    }

    @Then("user should see error message")
    public void userShouldSeeErrorMessage() {
        Assert.assertTrue(loginPage.isErrorMessageDisplayed());
    }
}