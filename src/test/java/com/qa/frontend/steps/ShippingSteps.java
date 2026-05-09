package com.qa.frontend.steps;

import com.qa.frontend.pages.ShippingPage;
import com.qa.frontend.pages.ShopPage;
import com.qa.hooks.Hooks;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import org.junit.Assert;
import java.util.Map;

public class ShippingSteps {

    ShippingPage shippingPage = new ShippingPage(Hooks.driver);

    @Given("user has added items to cart and proceeded to checkout")
public void userHasAddedItemsToCartAndProceededToCheckout() {
    ShopPage shopPage = new ShopPage(Hooks.driver);
    shopPage.clearCart();
    shopPage.addItemByName("Dior J'adore", 2);
    shopPage.addItemByName("Gucci Bloom Eau de", 3);
    shopPage.clickProceedToCheckout();
}

    @Given("user has completed checkout with shipping details")
    public void userHasCompletedCheckoutWithShippingDetails(DataTable dataTable) {
        Map<String, String> details = dataTable.asMap(String.class, String.class);
        fillShippingForm(details);
        shippingPage.clickSubmitOrder();
    }

    @When("user fills shipping details")
    public void userFillsShippingDetails(DataTable dataTable) {
        Map<String, String> details = dataTable.asMap(String.class, String.class);
        fillShippingForm(details);
    }

    private void fillShippingForm(Map<String, String> details) {
        if (details.containsKey("phone")) {
            shippingPage.fillPhone(details.get("phone"));
        }
        if (details.containsKey("street")) {
            shippingPage.fillStreet(details.get("street"));
        }
        if (details.containsKey("city")) {
            shippingPage.fillCity(details.get("city"));
        }
        if (details.containsKey("country")) {
            shippingPage.selectCountry(details.get("country"));
        }
    }

    @Then("user should be able to submit order successfully")
    public void userShouldBeAbleToSubmitOrderSuccessfully() {
        shippingPage.clickSubmitOrder();
        Assert.assertTrue(shippingPage.isOrderSuccessful());
    }

    @Then("user should not be able to submit order")
    public void userShouldNotBeAbleToSubmitOrder() {
        shippingPage.clickSubmitOrder();
        Assert.assertFalse(shippingPage.isOrderSuccessful());
    }

    @Then("address should be displayed in format {string}")
    public void addressShouldBeDisplayedInFormat(String format) {
        String address = shippingPage.getAddressText();
        Assert.assertFalse(address.isEmpty());
        System.out.println("Address: " + address);
    }

    @And("address should be {string}")
    public void addressShouldBe(String expectedAddress) {
        String actualAddress = shippingPage.getAddressText();
        Assert.assertEquals(expectedAddress, actualAddress);
    }

    @And("user submits the order")
    public void userSubmitsTheOrder() {
        shippingPage.clickSubmitOrder();
    }
}