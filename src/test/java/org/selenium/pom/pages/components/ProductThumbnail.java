package org.selenium.pom.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.selenium.pom.base.BasePage;
import org.selenium.pom.pages.CartPage;

public class ProductThumbnail extends BasePage {

	public ProductThumbnail(WebDriver driver) {
		super(driver);
	}

	private By getViewCartLocator(String productName) {
		// Targets the "View Cart" link that appears specifically after a product is
		// added
		return By.cssSelector("a.added[aria-label*='" + productName + "']+a");
	}

	private By getAddToCartBtnLocator(String productName) {
		// Instead of hardcoding "Blue Shoes", generate the locator on the fly
		return By.cssSelector("a[aria-label*='" + productName + "']");
	}

	public ProductThumbnail clickAddToCartBtn(String productName) {
		By locator = getAddToCartBtnLocator(productName);
		actions.click(locator);
		actions.waitForAttributeToContain(locator, "class", "added");
		return this;
	}

	public CartPage clickViewCartOfAProduct(String productName) {
		actions.click(getViewCartLocator(productName));
		return new CartPage(driver);
	}
	
	@Override
	protected void isLoaded() throws Error {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void load() {
		// TODO Auto-generated method stub
		
	}
	
}
