package org.selenium.pom.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.selenium.pom.base.BaseTest;
import org.selenium.pom.model.Product;
import org.selenium.pom.pages.CartPage;
import org.selenium.pom.pages.StorePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AddToCartTest extends BaseTest {
	@Test
	public void addToCartFromStorePage() throws IOException, InterruptedException {
		List<Integer> listOfProducts = Arrays.asList(1198);
		List<String> cartItems = new ArrayList<>();

		for (int productId : listOfProducts) {
			Product product = new Product(productId);
			cartItems.add(product.getName());
		}

		CartPage cartPage = new StorePage(getDriver()).get().addItemsToCart(cartItems)
				.clickViewCartOfAProduct(cartItems.get(0)).get();

		// Validate if all the products are added to the cart
		Assert.assertTrue(cartPage.areAllProductsInCart(cartItems),
				"One or more expected products were missing from the cart!");
	}
}
