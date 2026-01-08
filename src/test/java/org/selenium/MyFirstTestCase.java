package org.selenium;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.selenium.pom.base.BaseTest;
import org.selenium.pom.flows.CheckoutFlow;
import org.selenium.pom.model.BillingModel;
import org.selenium.pom.model.PaymentModel;
import org.selenium.pom.model.Product;
import org.selenium.pom.model.ShippingModel;
import org.selenium.pom.pages.BillingSection;
import org.selenium.pom.pages.CartPage;
import org.selenium.pom.pages.CheckoutPage;
import org.selenium.pom.pages.HomePage;
import org.selenium.pom.pages.StorePage;
import org.selenium.pom.pages.ThankYouPage;
import org.selenium.pom.utils.JacksonUtils;
import org.selenium.pom.utils.MyLogger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MyFirstTestCase extends BaseTest {

	//@Test
	public void guestCheckoutUsingDirectBankTransfer() throws InterruptedException {
		// 1. Data Setup
		String searchKey = "blue";
		List<String> itemsToAdd = Arrays.asList("Blue Shoes", "Blue Shoes", "Blue Shoes", "Denim Blue Jeans",
				"Denim Blue Jeans", "Blue Denim Shorts", "Blue Tshirt", "Blue Tshirt");

		// 2. Navigation & Initial Validation
		// Using .get() ensures HomePage is loaded before we interact with it
		HomePage homePage = new HomePage(driver).get();
		StorePage storePage = homePage.clickStoreLink();

		storePage.searchForProduct(searchKey);

		Assert.assertEquals(storePage.getSearchHeaderText(), "Search results: “" + searchKey + "”");
		Assert.assertEquals(storePage.getCartCount(), "0", "Cart should be empty at the start of the test");

		// 3. Product Price Validation
		String productToVerify = "Blue Shoes";
		Assert.assertEquals(storePage.getProductPrice(productToVerify), "$45.00");

		// 4. Add Items to Cart
		for (String item : itemsToAdd) {
			storePage.addProductToCart(item);
		}

		Assert.assertEquals(storePage.getCartCount(), String.valueOf(itemsToAdd.size()),
				"Cart count did not match the number of added items");

		// 5. Cart Review
		CartPage cartPage = storePage.clickViewCartOfAProduct(productToVerify);

		// Assertions on Cart Contents
		for (String item : itemsToAdd) {
			Assert.assertTrue(cartPage.isProductInCart(item), item + " should be in cart");
		}

		Assert.assertEquals(cartPage.getProductQuantity("Blue Shoes"), "3", "Quantity for Blue Shoes is incorrect");

		// Log table data (Optional: move to a custom listener or logger)
		List<List<String>> actualCartData = cartPage.getCartTableData();
		MyLogger.info("Validating cart contents for " + itemsToAdd.size() + " items.");

		for (List<String> cartRow : actualCartData) {
			if (cartRow.size() >= 6) {
				String iName = cartRow.get(2); // Index 2: Product Name
				String iPrice = cartRow.get(3); // Index 3: Price
				String iQuantity = cartRow.get(4); // Index 4: Quantity
				String iSubtotal = cartRow.get(5); // Index 5: Subtotal
				System.out.println(iName + "\t" + iPrice + "\t" + iQuantity + "\t" + iSubtotal);
			}
		}

		// 6. Checkout Process
		CheckoutPage checkoutPage = cartPage.clickProceedToCheckout();

		BillingModel billingData = new BillingModel().setFirstname("Jane").setLastname("Doe")
				.setAddress1("123 Street").setCity("San Francisco").setZip("94102")
				.setEmail("jane@example.com").setShipToDifferentAddress(true);

		checkoutPage.billing.fillBillingDetails(billingData);

		if (billingData.isShipToDifferentAddress()) {
			checkoutPage.shipping
					.fillShippingDetails(new ShippingModel().setFirstname("John").setLastname("Recipient"));
		}

		Thread.sleep(5000);
		// The ThankYouPage constructor (via BasePage) will handle isLoaded() verification
		ThankYouPage thankYouPage = checkoutPage.payment.selectDirectBankTransfer().placeOrder();

		// 7. Final Validation
		Assert.assertEquals(thankYouPage.getConfirmationMessage(), "Thank you. Your order has been received.");
	}

	@Test
    public void testGuestCheckoutOfOneProductUsingDirectBankTransfer() throws IOException {
        // Setup Data
		BillingModel billingAddress = JacksonUtils.deserializeJson("billing_testdata.json", BillingModel.class);
		List<Integer> listOfProducts = Arrays.asList(1215);
		List<String> cartItems = new ArrayList<>();
		
		for(int productId : listOfProducts) {
			Product product = new Product(productId);
			cartItems.add(product.getName());
		}

        // Use the Reusable Flow
        CheckoutFlow flow = new CheckoutFlow(new HomePage(driver).get());
        CheckoutPage checkoutPage = flow.navigateToCheckoutWithProducts("blue", cartItems);

        flow.fillGuestDetails(checkoutPage, billingAddress, null);

        // Test Specific Action: Place Order
        ThankYouPage thankYouPage = checkoutPage.payment.selectDirectBankTransfer().placeOrder();
        Assert.assertEquals(thankYouPage.getConfirmationMessage(), "Thank you. Your order has been received.");
        System.out.println(thankYouPage.getConfirmationMessage());
    }

	@Test
    public void testGuestCheckoutOfManyProductUsingDirectBankTransfer() throws IOException {
        // Setup Data
		BillingModel billingAddress = JacksonUtils.deserializeJson("billing_testdata.json", BillingModel.class);
		List<Integer> listOfProducts = Arrays.asList(1215, 1209, 1209, 1205);
		List<String> cartItems = new ArrayList<>();
		
		for(int productId : listOfProducts) {
			Product product = new Product(productId);
			cartItems.add(product.getName());
		}

        // Use the Reusable Flow
        CheckoutFlow flow = new CheckoutFlow(new HomePage(driver).get());
        CheckoutPage checkoutPage = flow.navigateToCheckoutWithProducts("blue", cartItems);

        flow.fillGuestDetails(checkoutPage, billingAddress, null);

        // Test Specific Action: Place Order
        ThankYouPage thankYouPage = checkoutPage.payment.selectDirectBankTransfer().placeOrder();
        Assert.assertEquals(thankYouPage.getConfirmationMessage(), "Thank you. Your order has been received.");
        System.out.println(thankYouPage.getConfirmationMessage());
    }
	
	//@Test
    public void testGuestCheckoutUsingDirectBankTransfer_map() throws IOException {
        // 1. Get the file stream
		InputStream is = getClass().getClassLoader().getResourceAsStream("billing_testdata.json");
        if (is == null) {
            throw new RuntimeException("JSON file not found in resources!");
        }

		Map<String, BillingModel> testDataMap;
		
        // 2. Call the utility (simplified to not require passing an empty map)
        testDataMap = JacksonUtils.deserializeJson_map(is);

        // 3. Access the data
        BillingModel billingAddress = testDataMap.get("tc_001");
        
        if (billingAddress != null) {
            System.out.println("Running Test 1 with First Name: " + billingAddress.toString());
        }

        // Use the Reusable Flow
        CheckoutFlow flow = new CheckoutFlow(new HomePage(driver).get());
        List<String> items = Arrays.asList("Blue Shoes", "Blue Shoes", "Blue Denim Shorts");

        CheckoutPage checkoutPage = flow.navigateToCheckoutWithProducts("blue", items);
        flow.fillGuestDetails(checkoutPage, billingAddress, null);

        // Test Specific Action: Place Order
        ThankYouPage thankYouPage = checkoutPage.payment.selectDirectBankTransfer().placeOrder();
        Assert.assertEquals(thankYouPage.getConfirmationMessage(), "Thank you. Your order has been received.");
        System.out.println(thankYouPage.getConfirmationMessage());
    }
	
	//@Test
    public void testGuestCheckoutUsingDirectBankTransfer_HardCoded() {
        // Setup Data
        List<String> items = Arrays.asList("Blue Shoes", "Blue Shoes", "Blue Denim Shorts");
        BillingModel billing = new BillingModel().setFirstname("Jane").setLastname("Doe").setAddress1("123 Street")
                .setCity("San Francisco").setZip("94102").setEmail("jane@example.com");

        // Use the Reusable Flow
        CheckoutFlow flow = new CheckoutFlow(new HomePage(driver).get());
        CheckoutPage checkoutPage = flow.navigateToCheckoutWithProducts("blue", items);

        flow.fillGuestDetails(checkoutPage, billing, null);

        // Test Specific Action: Place Order
        ThankYouPage thankYouPage = checkoutPage.payment.selectDirectBankTransfer().placeOrder();
        Assert.assertEquals(thankYouPage.getConfirmationMessage(), "Thank you. Your order has been received.");
    }

    //@Test
    public void testLoggedInUserCheckoutUsingDirectBankTransfer(){
        // Setup Data
        List<String> items = Arrays.asList("Blue Shoes", "Blue Shoes", "Blue Denim Shorts");
        BillingModel billing = new BillingModel().setFirstname("Jane").setLastname("Doe").setAddress1("123 Street")
                .setCity("San Francisco").setZip("94102").setEmail("jane@example.com")
                .setOrderNotes("Leave at front door").setShipToDifferentAddress(true);
		ShippingModel shipping = new ShippingModel().setFirstname("John").setAddress1("Different Place 456");

        // Use the Reusable Flow
        CheckoutFlow flow = new CheckoutFlow(new HomePage(driver).get());
        CheckoutPage checkoutPage = flow.navigateToCheckoutWithProducts("blue", items);

        checkoutPage.loginAsReturningCustomer("chetan", "Test@1234");
        
        flow.fillGuestDetails(checkoutPage, billing, shipping);

        // Test Specific Action: Place Order
        ThankYouPage thankYouPage = checkoutPage.payment.selectDirectBankTransfer().placeOrder();
        Assert.assertEquals(thankYouPage.getConfirmationMessage(), "Thank you. Your order has been received.");
    }

	//@Test
    public void guestStopsAtPlaceOrder() {
        List<String> items = Arrays.asList("Blue Shoes");
        BillingModel billing = new BillingModel().setFirstname("John").setLastname("Doe").setAddress1("456 Ave")
                .setCity("LA").setZip("90001").setEmail("john@example.com");

        CheckoutFlow flow = new CheckoutFlow(new HomePage(driver).get());
        CheckoutPage checkoutPage = flow.navigateToCheckoutWithProducts("blue", items);

        flow.fillGuestDetails(checkoutPage, billing, null);

        // Test Specific Requirement: Just go back home instead of placing order
        HomePage homePage = checkoutPage.menu.navigateToHomePage();
        Assert.assertEquals(homePage.getPageTitle(), HomePage.PAGE_TITLE);
	}
	
	public void testMissingEmailError() {
		CheckoutPage checkout = new CheckoutPage(driver);

		// APPROACH: Take a valid model and "break" only what you need
		// Here, we create a valid user but intentionally leave Email as empty string
		// Explicitly clear it
		// BillingModel invalidData = BillingModel.validGuest().setEmail("");

		// checkout.billing.fillBillingDetails(invalidData);
		// checkout.payment.clickPlaceOrder();

		// Assert email error appears...
	}

	public void testInvalidZipCode() {
		CheckoutPage checkout = new CheckoutPage(driver);

		// You can also build it from scratch for granular tests
		// Invalid zip code
		BillingModel shortZip = new BillingModel().setFirstname("Test").setZip("12");

		checkout.billing.fillBillingDetails(shortZip);
		// Assert zip error appears...
	}

	public void guestCheckoutTest() {
		BillingModel billing = new BillingModel().setFirstname("Alice").setLastname("Smith")
				.setShipToDifferentAddress(false);

		PaymentModel payment = new PaymentModel().setPaymentMethod("cod");

		CheckoutPage checkoutPage = new CheckoutPage(driver);
		// checkoutPage.billing.fillBillingDetails(billing).finalizeOrder(payment);
	}

	public void happyPath() {
		BillingModel billingData = new BillingModel().setFirstname("Jane").setEmail("jane@example.com")
				.setShipToDifferentAddress(true);

		ShippingModel shipping = new ShippingModel().setFirstname("John").setAddress1("Different Place 456");

		/*
		 * checkoutPage.billing.fillBillingDetails(billingData); if
		 * (billingData.shipToDifferentAddress) {
		 * checkoutPage.shipping.fillShippingDetails(shippingData); }
		 * checkoutPage.payment.selectPaymentMethod(paymentData);
		 * checkoutPage.placeOrder();
		 */

	}

	// The "Modified Path": You only change what matters for that specific test.
	public void checkoutWithDifferentShipping() {
		BillingModel billing = new BillingModel().setFirstname("Jane").setEmail("jane@example.com")
				.setShipToDifferentAddress(true);

		ShippingModel shipping = new ShippingModel().setFirstname("John").setAddress1("Different Place 456");

		BillingSection billingPage = new BillingSection(driver);
		/*
		 * checkoutPage.setBillingDetails(billing) .setShippingDetails(shipping)
		 * .placeOrder(new PaymentModel().setPaymentMethod("cod"));
		 */
	}
}
