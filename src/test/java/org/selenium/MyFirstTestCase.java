package org.selenium;

import java.util.List;

import org.selenium.pom.base.BaseTest;
import org.selenium.pom.pages.CartPage;
import org.selenium.pom.pages.HomePage;
import org.selenium.pom.pages.StorePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MyFirstTestCase extends BaseTest {
	@Test
	public void sampleTest() throws InterruptedException {
		// 1. Initialize and Navigate
		HomePage homePage = new HomePage(driver).load();
		StorePage storePage = homePage.clickStoreLink();

		// 2. Perform Search and Validate Header
		storePage.searchForProduct("blue");

		Assert.assertEquals(storePage.getSearchHeaderText(), "Search results: “blue”", "Search header text mismatch!");
		Assert.assertEquals(storePage.getCartCount(), "0", "Cart should be empty at start");

		// 3. Validate Price before adding
		String price = storePage.getProductPrice("Blue Shoes");
		Assert.assertEquals(price, "$45.00", "Product price is incorrect");

		// 4. Add to Cart and Validate Count
		// Chaining ensures we wait for the 'added' state inside the method
		storePage.addProductToCart("Blue Shoes");

		Thread.sleep(5000);

		// Assert.assertEquals(storePage.getCartCount(), "1", "Cart count did not update
		// after adding item");

		// 5. Navigate to Cart
		// clickViewCartOfAProduct navigates to Cart page and returns new
		// CartPage(driver)
		CartPage cartPage = storePage.clickViewCartOfAProduct("Blue Shoes");

		List<String> productNames = cartPage.getProductNamesInCart();
		Assert.assertTrue(productNames.contains("Blue Shoes"), "Product not found in cart table!");

		/*
		 * List<CartItem> cartItems = cartPage.getCartTableContents();
		 * 
		 * for (CartItem cartItem : cartItems) { System.out.println(cartItem.getName() +
		 * "\t" + cartItem.getPrice() + "\t" + cartItem.getQuantity() + "\t" +
		 * cartItem.getSubtotal()); }
		 */

		List<List<String>> actualCartData = cartPage.getCartTableData();

		for (List<String> cartRow : actualCartData) {
			if (cartRow.size() >= 6) {
				String iName = cartRow.get(2); // Index 2: Product Name
				String iPrice = cartRow.get(3); // Index 3: Price
				String iQuantity = cartRow.get(4); // Index 4: Quantity
				String iSubtotal = cartRow.get(5); // Index 5: Subtotal
				System.out.println(iName + "\t" + iPrice + "\t" + iQuantity + "\t" + iSubtotal);
			}
		}
		System.out.println(cartPage.isProductInCart("Blue Shoes"));
		System.out.println(cartPage.isProductInCart("Denim Blue Jeans"));

		System.out.println(cartPage.getProductQuantity("Blue Shoes"));
		System.out.println(cartPage.getProductQuantity("Denim Blue Jeans"));

	}
}
