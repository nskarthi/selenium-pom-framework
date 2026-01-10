package org.selenium.pom.base;

import org.openqa.selenium.WebDriver;
import org.selenium.pom.factory.DriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

public class BaseTest {

	private WebDriver driver;
	
	public WebDriver getDriver() {
		return driver;
	}
	
	@Parameters("browser")
	@BeforeMethod
	public void startDriver(String browser) {
		driver = new DriverManager().initializeDriver(browser);
	}

	@AfterMethod
	public void quitDriver() {
		driver.quit();
	}
}
