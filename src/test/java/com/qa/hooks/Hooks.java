package com.qa.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;
import java.util.List;

public class Hooks {

    public static WebDriver driver;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-extensions");
        driver = new ChromeDriver(options);

        driver.manage().timeouts()
              .pageLoadTimeout(Duration.ofSeconds(120))
              .implicitlyWait(Duration.ofSeconds(10));

        loadPageWithRetry("https://qa-practice.razvanvancea.ro/auth_ecommerce.html");
    }

    private void loadPageWithRetry(String url) {
        int maxRetry = 3;
        for (int i = 0; i < maxRetry; i++) {
            try {
                driver.get(url);
                System.out.println("Page loaded successfully");
                return;
            } catch (Exception e) {
                System.out.println("Page load attempt " + (i + 1) + " failed");
                if (i == maxRetry - 1) throw e;
                try { Thread.sleep(3000); } catch (Exception ex) {}
            }
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed() && driver != null) {
            try {
                byte[] screenshot = ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Screenshot");
            } catch (Exception e) {
                System.out.println("Screenshot failed: " + e.getMessage());
            }
        }

        // Logout ก่อนปิด Browser เพื่อ clear Server Session
        try {
            List<WebElement> logoutBtn = driver.findElements(
                By.linkText("Log Out")
            );
            if (!logoutBtn.isEmpty()) {
                logoutBtn.get(0).click();
                System.out.println("Logged out successfully");
                try { Thread.sleep(1000); } catch (Exception e) {}
            }
        } catch (Exception e) {
            System.out.println("Logout failed: " + e.getMessage());
        }

        if (driver != null) {
            driver.quit();
        }
    }
}