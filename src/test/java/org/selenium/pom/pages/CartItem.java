package org.selenium.pom.pages;

public class CartItem {
	public String name, price, quantity, subtotal;

	public CartItem(String name, String price, String quantity, String subtotal) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.subtotal = subtotal;
	}

	public String getName() {
		return name;
	}

	public String getPrice() {
		return price;
	}

	public String getQuantity() {
		return quantity;
	}

	public String getSubtotal() {
		return subtotal;
	}
	
}