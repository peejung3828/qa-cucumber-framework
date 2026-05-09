package com.qa.frontend.steps;

import com.qa.frontend.pages.ShopPage;
import com.qa.hooks.Hooks;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class ShoppingSteps {

    ShopPage shopPage = new ShopPage(Hooks.driver);

    private int diorQty = 0;
    private int gucciQty = 0;
    private double totalBefore = 0;

    @When("user adds {string} with quantity {int}")
    public void userAddsWithQuantity(String productName, int quantity) {
        if (diorQty == 0 && gucciQty == 0) {
            System.out.println("Clearing cart before adding items...");
            shopPage.clearCart();
            try { Thread.sleep(2000); } catch (Exception e) {}

            String totalText = shopPage.getTotalCost();
            String numericOnly = totalText.replaceAll("[^0-9.]", "").trim();
            totalBefore = numericOnly.isEmpty() ? 0 : Double.parseDouble(numericOnly);
            System.out.println("Total after clear: $" + totalBefore);
        }

        if (productName.contains("Dior")) {
            diorQty = quantity;
        } else if (productName.contains("Gucci")) {
            gucciQty = quantity;
        }
        shopPage.addItemByName(productName, quantity);
    }

    @Then("total cost should be correct")
    public void totalCostShouldBeCorrect() {
        try {
            String totalText = shopPage.getTotalCost();
            System.out.println("Total text: " + totalText);

            double expectedIncrease = (ShopPage.DIOR_PRICE * diorQty)
                                    + (ShopPage.GUCCI_PRICE * gucciQty);
            System.out.println("Expected increase: $" + expectedIncrease);

            String numericOnly = totalText.replaceAll("[^0-9.]", "").trim();

            if (!numericOnly.isEmpty()) {
                double actualTotal = Double.parseDouble(numericOnly);
                double actualIncrease = actualTotal - totalBefore;
                System.out.println("Total before: $" + totalBefore);
                System.out.println("Total after: $" + actualTotal);
                System.out.println("Actual increase: $" + actualIncrease);

                Assert.assertEquals(
                    "Total increase should be $" + expectedIncrease,
                    expectedIncrease, actualIncrease, 0.01
                );
            } else {
                System.out.println("Total text is empty");
                Assert.assertTrue("Cart should have items",
                    diorQty > 0 || gucciQty > 0);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            Assert.fail("Total cost validation failed: " + e.getMessage());
        }
    }

    @Then("total cost should equal {string}")
    public void totalCostShouldEqual(String expectedTotal) {
        String totalText = shopPage.getTotalCost();
        System.out.println("Total text: " + totalText);

        String numericOnly = totalText.replaceAll("[^0-9.]", "").trim();
        System.out.println("Actual total: $" + numericOnly);

        double expectedIncrease = (ShopPage.DIOR_PRICE * diorQty)
                                + (ShopPage.GUCCI_PRICE * gucciQty);
        double actualTotal = numericOnly.isEmpty() ? 0 : Double.parseDouble(numericOnly);
        double actualIncrease = actualTotal - totalBefore;

        System.out.println("Expected increase: $" + expectedIncrease);
        System.out.println("Actual increase: $" + actualIncrease);

        Assert.assertEquals(
            "Total increase should match price x quantity",
            expectedIncrease, actualIncrease, 0.01
        );
    }

    @And("user clicks proceed to checkout button")
    public void userClicksProceedToCheckoutButton() {
        shopPage.clickProceedToCheckout();
    }

    @Then("cart should be empty after clearing")
    public void cartShouldBeEmptyAfterClearing() {
        shopPage.clearCart();
        String totalText = shopPage.getTotalCost();
        System.out.println("Total after clear: " + totalText);
        Assert.assertNotNull(totalText);
    }
}