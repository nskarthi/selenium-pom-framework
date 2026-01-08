package org.selenium.pom.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.selenium.pom.base.BasePage;

/**
 * Main Checkout Page that orchestrates sub-sections.
 */
public class AccountLoginAndRegistration extends BasePage<AccountLoginAndRegistration> {
	public static final String PAGE_TITLE = "Account – AskOmDch";

	// Login
	private final By loginUsername = By.id("reg_username");
	private final By loginPassword = By.className("reg_password");
	private final By rememberMeCheckbox = By.name("register");
	private final By loginButton = By.name("register");
	private final By lostPasswordLink = By.linkText("Lost your password?");
	
	// New Registration
	private final By registerUsername = By.id("reg_username");
	private final By registerEmail = By.id("reg_email");
	private final By registerPassword = By.className("reg_password");
	private final By registerButton = By.name("register");

	public AccountLoginAndRegistration(WebDriver driver) {
		super(driver);
	}

	public String getPageTitle() {
		return actions.getPageTitle(PAGE_TITLE);
	}

	public void enterLoginId(String username) {
		actions.type(loginUsername, username);
	}

	public void enterLoginPassword(String password) {
		actions.type(loginPassword, password);
	}

	public void clickLoginButton() {
		actions.click(loginButton);
	}

	public void login(String username, String password, boolean rememberMe) {
		enterLoginId(username);
		enterLoginPassword(password);
		
		if(rememberMe)
			actions.click(rememberMeCheckbox);

		clickLoginButton();
	}

	public void enterRegistrationId(String username) {
		actions.type(registerUsername, username);
	}

	public void enterRegistrationEmail(String email) {
		actions.type(registerEmail, email);
	}
	
	public void enterRegistrationPassword(String password) {
		actions.type(registerPassword, password);
	}

	public void clickRegisterButton() {
		actions.click(registerButton);
	}

	public AccountPage register(String username, String email, String password) {
		enterRegistrationId(username);
		enterRegistrationEmail(email);
		enterRegistrationPassword(password);
		clickRegisterButton();

		return new AccountPage(driver);
	}

	@Override
	protected void load() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void isLoaded() throws Error {
		String url = driver.getCurrentUrl();
		if (!url.contains("account")) {
			throw new Error("Account Login And Registration Page not loaded. Current URL: " + url);
		}
	}

}
