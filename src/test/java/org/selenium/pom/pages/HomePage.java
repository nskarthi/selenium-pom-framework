package org.selenium.pom.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.selenium.pom.base.BasePage;
import org.selenium.pom.pages.components.MenuComponent;

public class HomePage extends BasePage<HomePage> {
    public static final String PAGE_TITLE = "AskOmDch – Become a Selenium automation expert!";
	private final By storeMenuLink = By.cssSelector("#menu-item-1227 a");
	MenuComponent menu;

	public HomePage(WebDriver driver) {
		super(driver);
		menu = new MenuComponent(driver);
	}

	public StorePage clickStoreLink() {
		isLoaded();
		actions.click(storeMenuLink);
		return new StorePage(driver);
	}

    public String getPageTitle() {
        return actions.getPageTitle(PAGE_TITLE);
    }
	
	@Override
	protected void load() {
		driver.get("https://askomdch.com/");
	}

	// Syntax error, insert "Finally" to complete BlockStatements

	@Override
	protected void isLoaded() throws Error {
		String url = driver.getCurrentUrl();
		if (!url.contains("askomdch.com")) {
			throw new Error("Home Page not loaded. Current URL: " + url);
		}
	}
}
