package org.selenium.pom.flows;

import java.util.List;

import org.selenium.pom.model.BillingModel;
import org.selenium.pom.model.ShippingModel;
import org.selenium.pom.pages.CartPage;
import org.selenium.pom.pages.CheckoutPage;
import org.selenium.pom.pages.HomePage;
import org.selenium.pom.pages.StorePage;
import org.testng.Assert;

public class CheckoutFlow {
	public static final String PAGE_TITLE = "AskOmDch – Become a Selenium automation expert!";

	private final HomePage homePage;

	public CheckoutFlow(HomePage homePage) {
		this.homePage = homePage;
	}

	/**
	 * Reusable flow to take a guest from the Home Page to the Checkout Page with
	 * items in their cart.
	 */
	public CheckoutPage navigateToCheckoutWithProducts(String searchKey, List<String> itemsToAdd) {
		StorePage storePage = homePage.headerMenu.navigateToStorePage();
		storePage.searchForProduct(searchKey);

		CartPage cartPage = storePage.addItemsToCart(itemsToAdd).
				clickViewCartOfAProduct(itemsToAdd.get(0));

		// Validate if all the products are added to the cart
		Assert.assertTrue(cartPage.areAllProductsInCart(itemsToAdd));
		return cartPage.clickProceedToCheckout();
	}

	/**
	 * Reusable flow to fill out all billing and shipping details.
	 * 
	 * @throws InterruptedException
	 */
	public void fillGuestDetails(CheckoutPage checkoutPage, BillingModel billingData, ShippingModel shippingData)
			throws InterruptedException {
		checkoutPage.billing.fillBillingDetails(billingData);

		if (billingData.isShipToDifferentAddress() && shippingData != null) {
			System.out.println("filling shipping address");
			checkoutPage.shipping.fillShippingDetails(shippingData);
		}
	}

}
