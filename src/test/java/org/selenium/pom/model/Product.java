package org.selenium.pom.model;

import java.io.IOException;
import java.util.List;

import org.selenium.pom.utils.JacksonUtils;

public class Product {
	private int id;
	private String name;

	public Product() {
	}

	public Product(int id) throws IOException {
		Product[] products = JacksonUtils.deserializeJson("products.json", Product[].class);
		for (Product product : products) {
			if (product.getId() == id) {
				this.id = id;
				this.name = product.getName();
			}
		}
	}

	public Product(List<String> listOfProductNames) throws IOException {
		Product[] products = JacksonUtils.deserializeJson("products.json", Product[].class);

		for (String productName : listOfProductNames) {
			for (Product product : products) {
				if (productName.equalsIgnoreCase(product.getName())) {
					this.id = product.getId();
					this.name = productName;
				}
			}
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
