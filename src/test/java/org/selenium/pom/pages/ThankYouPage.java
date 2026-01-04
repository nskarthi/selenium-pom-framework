package org.selenium.pom.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.selenium.pom.base.BasePage;

public class ThankYouPage extends BasePage<ThankYouPage> {
    public static final String PAGE_TITLE = "Checkout – AskOmDch";

	private final By thankyouPageHeader = By.className("has-text-align-center");
	private final By thankyouPageConfirmationMessage = By.className("woocommerce-thankyou-order-received");

	public ThankYouPage(WebDriver driver) {
		super(driver);
		// Trigger the LoadableComponent lifecycle. This internally calls isLoaded() to
		// verify page state
		get();
	}

    public String getPageTitle() {
        return actions.getPageTitle(PAGE_TITLE);
    }
	
	public String getConfirmationMessage() {
		return driver.findElement(thankyouPageConfirmationMessage).getText();
	}

	@Override
	protected void load() {
		// TODO Auto-generated method stub
	}

	/**
	 * This method is automatically called by the get() method in the
	 * LoadableComponent class.
	 */
	@Override
	protected void isLoaded() throws Error {
		if (!actions.waitForUrlToContain("order-received")) {
			throw new Error("OrderReceivedPage failed to load. URL: " + driver.getCurrentUrl());
		}
	}
}
