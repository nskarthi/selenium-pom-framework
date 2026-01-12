package org.selenium.pom.api.actions;

import org.selenium.pom.model.User;
import org.selenium.pom.utils.FakerUtils;

public class DummyClass {
	public static void main(String[] args) {
		registerUser();
		addToCartAsGuest();
		addToCartAsLoggedInUser();
	}

	private static void addToCartAsLoggedInUser() {
		System.out.println("addToCartAsLoggedInUser");
		String username = "demouser" + FakerUtils.generateRandomNumber();
		User user = new User().
				setUsername(username).
				setPassword("password").
				setEmail(username + "@test.com");
		
		SignUpApi api = new SignUpApi();
		api.register(user);

		System.out.println("REGISTRATION COOKIE: " + api.getCookies());
		CartApi cartApi = new CartApi(api.getCookies());
		cartApi.addToCart(1215, 1);
		System.out.println("CART COOKIE: " + cartApi.getCookies());
	}
	
	private static void addToCartAsGuest() {
		System.out.println("addToCartAsGuest");
		CartApi cartApi = new CartApi();
		cartApi.addToCart(1215, 1);
		System.out.println(cartApi.getCookies());
	}

	public static void registerUser() {
		System.out.println("registerUser");
		String username = "demouser" + FakerUtils.generateRandomNumber();
		User user = new User().
				setUsername(username).
				setPassword("password").
				setEmail(username + "@test.com");
		
		SignUpApi api = new SignUpApi();
		api.register(user);
		System.out.println(api.getCookies());
	}
}
