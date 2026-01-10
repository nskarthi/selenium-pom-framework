package org.selenium.pom.base;

import org.openqa.selenium.WebDriver;
import org.selenium.pom.factory.DriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

public class BaseTest {
    private ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    // Internal helper to set the driver
    private void setDriver(WebDriver driver) {
        this.driver.set(driver);
    }

    // Public getter for all test classes to use
    public WebDriver getDriver() {
        return this.driver.get();
    }

    @Parameters("browser")
    @BeforeMethod
    public void startDriver(String browser) {
        setDriver(new DriverManager().initializeDriver(browser));
    }

    @AfterMethod
    public void quitDriver() {
        // Safe access via the getter
        if (getDriver() != null) {
            getDriver().quit();
            driver.remove(); // Important: prevents memory leaks in ThreadLocal
        }
    }
}
