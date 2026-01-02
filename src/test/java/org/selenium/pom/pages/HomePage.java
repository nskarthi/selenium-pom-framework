package org.selenium.pom.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.selenium.pom.base.BasePage;

public class HomePage extends BasePage<Object> {
	private final By storeMenuLink = By.cssSelector("#menu-item-1227 a");

	public HomePage(WebDriver driver) {
		super(driver);
	}

	public StorePage clickStoreLink() {
		isLoaded();
		actions.click(storeMenuLink);
		return new StorePage(driver);
	}

	@Override
	public HomePage load() {
		driver.get("https://askomdch.com/");
		return this;
	}

	@Override
	public void isLoaded() throws Error {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(storeMenuLink));
		} catch (TimeoutException e) {
			throw new Error("Login Page did not load: Store Manu Link is not visible.");
		}
	}
}
