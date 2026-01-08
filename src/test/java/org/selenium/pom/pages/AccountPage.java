package org.selenium.pom.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.selenium.pom.base.BasePage;

public class AccountPage extends BasePage<AccountPage> {
	public static final String PAGE_TITLE = "Account – AskOmDch";

	private final By logoutLink = By.linkText("Log out");
	private final By welcomeText = By.cssSelector(".woocommerce-notices-wrapper + p");
	
	public AccountPage(WebDriver driver) {
		super(driver);
	}

	public String GetWelcomeText() {
		return actions.getContents(welcomeText);
	}

	public AccountLoginAndRegistration logout() {
		actions.click(logoutLink);
		return new AccountLoginAndRegistration(driver);
	}
	
	@Override
	protected void load() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void isLoaded() throws Error {
		String url = driver.getCurrentUrl();
		if (!url.contains("account")) {
			throw new Error("Account Page not loaded. Current URL: " + url);
		}
	}

}
