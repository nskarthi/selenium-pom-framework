package org.selenium.pom.base;

import java.util.List;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.selenium.pom.factory.DriverManager;
import org.selenium.pom.utils.CookieUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import io.restassured.http.Cookies;

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
        System.out.println("CURRENT THREAD: " + Thread.currentThread().getId());
    }

    @AfterMethod
    public void quitDriver() {
        // Safe access via the getter
        if (getDriver() != null) {
            System.out.println("CURRENT THREAD: " + Thread.currentThread().getId());
            getDriver().quit();
            driver.remove(); // Important: prevents memory leaks in ThreadLocal
        }
    }
    
    public void injectCookiesToBrowser(Cookies cookies) {
    	List<Cookie> seleniumCookies = new CookieUtils().convertRestAssuredCookiesToSeleniumCookies(cookies);
    	for(Cookie cookie : seleniumCookies) {
            //System.out.println(cookie.toString());
    		getDriver().manage().addCookie(cookie);
    	}
    }
}
