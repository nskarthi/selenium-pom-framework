package org.selenium.pom.model;

import java.io.IOException;
import java.util.List;

import org.selenium.pom.utils.JacksonUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {
	private int id;
	private String name;

    @JsonProperty("isFeaturedProduct") // This forces the mapping
	private boolean isFeaturedProduct;

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

	public boolean isFeaturedProduct() {
		return isFeaturedProduct;
	}

	public void isFeaturedProduct(boolean isFeaturedProduct) {
		this.isFeaturedProduct = isFeaturedProduct;
	}

	public Product() {
	}

	public Product(int id) throws IOException {
		Product[] products = JacksonUtils.deserializeJson("products.json", Product[].class);
		for (Product product : products) {
			if (product.getId() == id) {
				this.id = id;
				this.name = product.getName();
				this.isFeaturedProduct = product.isFeaturedProduct();
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
					this.isFeaturedProduct = product.isFeaturedProduct();
				}
			}
		}
	}
}
