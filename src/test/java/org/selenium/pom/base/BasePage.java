package org.selenium.pom.base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.selenium.pom.pages.components.MenuComponent;

public abstract class BasePage<T extends BasePage<T>> extends LoadableComponent<T> {
	protected WebDriver driver;
	protected WebDriverWait wait;
	protected ElementActions actions;
    public MenuComponent menu; // Common to all pages

	public BasePage(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		this.actions = new ElementActions(driver);
        this.menu = new MenuComponent(driver);
	}

	/**
	 * Selenium requires this. If isLoaded() throws an Error, this method is
	 * executed to navigate to the page.
	 */
	@Override
	protected abstract void load();

	/**
	 * Selenium requires this. Use it to check if the page is ready. Use Assertions
	 * or specific element checks. If this fails, it MUST throw an Error (not an
	 * Exception).
	 */
	@Override
	protected abstract void isLoaded() throws Error;

	/**
	 * Optional helper: If you want a method that returns 'this' after ensuring it's
	 * loaded, you can wrap Selenium's get().
	 */
	@SuppressWarnings("unchecked")
	public T init() {
		return (T) get();
	}

}
