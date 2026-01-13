package org.selenium.pom.dataproviders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.selenium.pom.model.Product;
import org.selenium.pom.utils.JacksonUtils;
import org.testng.annotations.DataProvider;

public class DataProviders {

	@DataProvider(name = "getProducts", parallel = true)
	public Object[] getProducts() throws IOException {
		return JacksonUtils.deserializeJson("products.json", Product[].class);
	}

	@DataProvider(name = "getFeaturedProducts", parallel = true)
	public Object[] getFeaturedProducts() throws IOException {
		Product[] products = JacksonUtils.deserializeJson("products.json", Product[].class);
		List<Product> featuredProducts = new ArrayList<>();
		for (Product p : products) {
			if (p.isFeaturedProduct())
				featuredProducts.add(p);
		}
		return featuredProducts.toArray();
	}
}
