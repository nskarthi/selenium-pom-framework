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

	public void clickHomeLink() {
		// TODO Auto-generated method stub
		
	}

}
