package org.selenium.pom.tests;

import java.io.IOException;

import org.selenium.pom.api.actions.CartApi;
import org.selenium.pom.api.actions.SignUpApi;
import org.selenium.pom.base.BaseTest;
import org.selenium.pom.model.Product;
import org.selenium.pom.model.User;
import org.selenium.pom.pages.CheckoutPage;
import org.selenium.pom.utils.FakerUtils;
import org.testng.Assert;

public class LoginTest extends BaseTest {

	public void loginDuringCheckout() throws IOException, InterruptedException {
		String username = "demouser" + FakerUtils.generateRandomNumber();
		User user = new User().
				setUsername(username).
				setPassword("password").
				setEmail(username + "@test.com");
		
		SignUpApi signUpApi = new SignUpApi();
		signUpApi.register(user);

		CartApi cartApi = new CartApi();
		Product product = new Product(1215);
		cartApi.addToCart(product.getId(), 1);

		CheckoutPage checkoutPage = new CheckoutPage(getDriver()).get();
		injectCookiesToBrowser(cartApi.getCookies());
		checkoutPage.loadUrl();
		checkoutPage.loginAsReturningCustomer(user.getUsername(), user.getPassword());

		Assert.assertEquals(checkoutPage.getProductName(), product.getName());
	}
}
