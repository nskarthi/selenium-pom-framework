package org.selenium.pom.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.selenium.pom.base.BasePage;

/**
 * Main Checkout Page that orchestrates sub-sections.
 */
public class CheckoutPage extends BasePage<CheckoutPage> {
	public static final String PAGE_TITLE = "Cart – AskOmDch";

	private final By checkoutPageHeader = By.name("has-text-align-center");
	private final By returningCustomerloginLink = By.className("showlogin");
	private final By couponCodeLink = By.className("showcoupon");
	private final By usernameTextField = By.name("username");
	private final By passwordTextField = By.name("password");
	private final By remembermeCheckBox = By.name("rememberme");
	private final By loginButton = By.cssSelector("button[name='login']");
	private final By lostPasswordLink = By.linkText("Lost your password?");

	public BillingSection billing;
	public ShippingSection shipping;
	public PaymentSection payment;

	public CheckoutPage(WebDriver driver) {
		super(driver);
		this.billing = new BillingSection(driver);
		this.payment = new PaymentSection(driver);
		this.shipping = new ShippingSection(driver);
	}

	public String getPageTitle() {
		return actions.getPageTitle(PAGE_TITLE);
	}

	public void clickReturningCustomerLoginLink() {
		actions.click(returningCustomerloginLink);
	}

	public void enterUsername(String username) {
		actions.type(usernameTextField, username);
	}

	public void enterPassword(String password) {
		actions.type(passwordTextField, password);
	}
	
	public void clickLoginButton() {
		actions.click(loginButton);
	}

	public void loginAsReturningCustomer(String username, String password) {
		clickReturningCustomerLoginLink();
		enterUsername(username);
		enterPassword(password);
		clickLoginButton();
	}

	@Override
	protected void load() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void isLoaded() throws Error {
		String url = driver.getCurrentUrl();
		if (!url.contains("checkout")) {
			throw new Error("Checkout Page not loaded. Current URL: " + url);
		}
	}

}
