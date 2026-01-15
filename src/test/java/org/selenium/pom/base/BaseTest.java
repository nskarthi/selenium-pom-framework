package org.selenium.pom.base;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.selenium.pom.factory.DriverManager;
import org.selenium.pom.utils.CookieUtils;
import org.testng.ITestResult;
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

	@Parameters("browser")
	@AfterMethod
	public void quitDriver(String browser, ITestResult result) throws IOException {
		// Safe access via the getter
		if (getDriver() != null) {
			System.out.println("CURRENT THREAD: " + Thread.currentThread().getId());
			
			if (result.getStatus() == ITestResult.FAILURE) {
				File filepath = new File("screenshots" + File.separator + browser + File.separator
						+ result.getTestClass().getRealClass().getSimpleName() + "_" + result.getMethod().getMethodName()
						+ ".png");
				takeScreenshot(filepath);
			}			
			
			getDriver().quit();
			driver.remove(); // Important: prevents memory leaks in ThreadLocal
		}
	}

	public void injectCookiesToBrowser(Cookies cookies) {
		List<Cookie> seleniumCookies = new CookieUtils().convertRestAssuredCookiesToSeleniumCookies(cookies);
		for (Cookie cookie : seleniumCookies) {
			// System.out.println(cookie.toString());
			getDriver().manage().addCookie(cookie);
		}
	}

	public void takeScreenshot(File filepath) throws IOException {
		File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshotFile, filepath);
	}
	
}
