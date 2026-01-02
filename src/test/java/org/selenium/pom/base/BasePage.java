package org.selenium.pom.base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage<T> {
	protected WebDriver driver;
	protected WebDriverWait wait;
    protected ElementActions actions;

	public BasePage(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.actions = new ElementActions(driver);
	}

	public void waitForPageToLoad() {
		try {
			isLoaded();
		} catch (Error e) {
			load();
			isLoaded();
		}
	}

    public abstract T load(); // Logic to navigate to the page
	public abstract void isLoaded() throws Error; // Logic to verify the page is ready
}
