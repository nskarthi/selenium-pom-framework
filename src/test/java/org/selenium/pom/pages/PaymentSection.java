package org.selenium.pom.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.selenium.pom.base.BasePage;

public class PaymentSection extends BasePage<PaymentSection> {
	private final By directBankTransfer = By.id("payment_method_bacs");
	private final By cashOnDelivery = By.id("payment_method_cod");
	private final By placeOrderButton = By.name("woocommerce_checkout_place_order");
	
	public PaymentSection(WebDriver driver) {
		super(driver);
	}
	
	public PaymentSection selectDirectBankTransfer() {
		actions.click(directBankTransfer);
		return this;
	}
	
	public PaymentSection selectCashOnDelivery() {
		actions.click(cashOnDelivery);
		return this;
	}
	
    public ThankYouPage placeOrder() {
    	actions.click(placeOrderButton);
    	return new ThankYouPage(driver);
    }
	
	@Override
	protected void load() {
		// TODO Auto-generated method stub
	}
	@Override
	protected void isLoaded() throws Error {
        String url = driver.getCurrentUrl();
        if (!url.contains("askomdch.com")) {
            throw new Error("Payment Section not loaded. Current URL: " + url);
        }
	}
}
