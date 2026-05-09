package com.qa.frontend.pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class ShopPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public static final double DIOR_PRICE = 89.99;
    public static final double GUCCI_PRICE = 79.99;

    public ShopPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public void clearCart() {
        try {
            System.out.println("Starting cart clear...");
            int maxAttempts = 20;
            int attempt = 0;
            while (attempt < maxAttempts) {
                List<WebElement> removeButtons = driver.findElements(
                    By.cssSelector("button.btn-danger")
                );
                if (removeButtons.isEmpty()) {
                    System.out.println("Cart is empty after " + attempt + " removals");
                    break;
                }
                System.out.println("Removing item " + (attempt + 1));
                removeButtons.get(0).click();
                try { Thread.sleep(1500); } catch (Exception e) {}
                attempt++;
            }
        } catch (Exception e) {
            System.out.println("clearCart error: " + e.getMessage());
        }
    }

    public void addItemByName(String productName, int quantity) {
        // Step 1: กด ADD TO CART 1 ครั้ง
        String xpath;
        if (productName.contains("Dior")) {
            xpath = "//*[contains(text(),'Dior J')]"
                  + "/following::button[contains(text(),'ADD TO CART')][1]";
        } else if (productName.contains("Gucci")) {
            xpath = "//*[contains(text(),'Gucci Bloom')]"
                  + "/following::button[contains(text(),'ADD TO CART')][1]";
        } else {
            xpath = "//*[contains(text(),'" + productName + "')]"
                  + "/following::button[contains(text(),'ADD TO CART')][1]";
        }

        List<WebElement> buttons = driver.findElements(By.xpath(xpath));
        if (!buttons.isEmpty()) {
            buttons.get(0).click();
            try {
                WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(3));
                Alert alert = alertWait.until(ExpectedConditions.alertIsPresent());
                alert.accept();
            } catch (Exception e) {
                // No alert = item added OK
            }
        }
        try { Thread.sleep(1000); } catch (Exception e) {}

        // Step 2: แก้ Quantity
        if (quantity > 1) {
            try {
                List<WebElement> qtyInputs = driver.findElements(
                    By.cssSelector("input.cart-quantity-input")
                );
                System.out.println("Found " + qtyInputs.size() + " quantity inputs");

                if (!qtyInputs.isEmpty()) {
                    int inputIndex = productName.contains("Dior") ? 0 : qtyInputs.size() - 1;
                    WebElement qtyInput = qtyInputs.get(inputIndex);

                    qtyInput.click();
                    qtyInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
                    qtyInput.sendKeys(String.valueOf(quantity));

                    ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].dispatchEvent(new Event('change', {bubbles:true}))",
                        qtyInput
                    );
                    ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].dispatchEvent(new Event('input', {bubbles:true}))",
                        qtyInput
                    );

                    try { Thread.sleep(2000); } catch (Exception e) {}
                    System.out.println("Set quantity " + quantity + " for " + productName);
                    System.out.println("Current value: " + qtyInput.getAttribute("value"));
                }
            } catch (Exception e) {
                System.out.println("Error setting quantity: " + e.getMessage());
            }
        }
    }

    public String getTotalCost() {
        try {
            WebElement totalElement = driver.findElement(
                By.cssSelector(".cart-total, #cart-total, .total")
            );
            String text = totalElement.getText();
            System.out.println("Cart total element text: " + text);
            return text;
        } catch (Exception e) {
            try {
                WebElement totalElement = driver.findElement(
                    By.xpath("//*[contains(@class,'cart-total') or contains(@class,'total-price')]")
                );
                String text = totalElement.getText();
                System.out.println("Total via xpath: " + text);
                return text;
            } catch (Exception ex) {
                System.out.println("Total not found: " + ex.getMessage());
                return "";
            }
        }
    }

    public void clickProceedToCheckout() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button.btn-purchase")
        ));
        btn.click();
    }

    public boolean isShopPageDisplayed() {
        try {
            wait.until(ExpectedConditions.urlContains("auth_ecommerce"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}