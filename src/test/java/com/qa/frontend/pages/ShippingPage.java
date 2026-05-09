package com.qa.frontend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ShippingPage {

    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "phone")
    private WebElement phoneInput;

    @FindBy(name = "street")
    private WebElement streetInput;

    @FindBy(name = "city")
    private WebElement cityInput;

    @FindBy(id = "countries_dropdown_menu")
    private WebElement countryDropdown;

    @FindBy(id = "submitOrderBtn")
    private WebElement submitOrderButton;

    // Success message อยู่ใน div#message
    @FindBy(id = "message")
    private WebElement messageDiv;

    public ShippingPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public void fillPhone(String value) {
        if (value != null && !value.isEmpty()) {
            wait.until(ExpectedConditions.visibilityOf(phoneInput));
            phoneInput.clear();
            phoneInput.sendKeys(value);
        }
    }

    public void fillStreet(String value) {
        if (value != null && !value.isEmpty()) {
            wait.until(ExpectedConditions.visibilityOf(streetInput));
            streetInput.clear();
            streetInput.sendKeys(value);
        }
    }

    public void fillCity(String value) {
        if (value != null && !value.isEmpty()) {
            wait.until(ExpectedConditions.visibilityOf(cityInput));
            cityInput.clear();
            cityInput.sendKeys(value);
        }
    }

    public void selectCountry(String value) {
        if (value != null && !value.isEmpty()) {
            wait.until(ExpectedConditions.visibilityOf(countryDropdown));
            Select select = new Select(countryDropdown);
            select.selectByVisibleText(value);
        }
    }

    public void clickSubmitOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(submitOrderButton));
        submitOrderButton.click();
    }

    public boolean isOrderSuccessful() {
        try {
            // รอ message div แสดงผล
            wait.until(ExpectedConditions.visibilityOf(messageDiv));
            String text = messageDiv.getText();
            System.out.println("Message: " + text);
            return text.contains("Congrats");
        } catch (Exception e) {
            System.out.println("Success message not found: " + e.getMessage());
            return false;
        }
    }

    public String getAddressText() {
        try {
            // ดึง address จาก success message
            // format: "...will be shipped to Street, City - Country"
            wait.until(ExpectedConditions.visibilityOf(messageDiv));
            String text = messageDiv.getText();
            System.out.println("Full message: " + text);

            if (text.contains("shipped to")) {
                return text.substring(text.indexOf("shipped to") + 11).trim();
            }

            // fallback: ดึงจาก form fields
            String street = streetInput.getAttribute("value");
            String city = cityInput.getAttribute("value");
            Select select = new Select(countryDropdown);
            String country = select.getFirstSelectedOption().getText();
            return street + ", " + city + " - " + country;

        } catch (Exception e) {
            System.out.println("Error getting address: " + e.getMessage());
            return "";
        }
    }
}