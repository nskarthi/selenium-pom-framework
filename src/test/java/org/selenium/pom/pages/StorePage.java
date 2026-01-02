package org.selenium.pom.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.selenium.pom.base.BasePage;

public class StorePage extends BasePage<Object> {

	private final By searchTextbox = By.id("woocommerce-product-search-field-0");
	private final By searchButton = By.cssSelector("button[value='Search']");
	private final By searchResultHeading = By.cssSelector(".woocommerce-products-header__title.page-title");
	private final By cartIconCount = By.cssSelector("div#ast-desktop-header span.count");

	/*
	 * By addBlueShoesToCart =
	 * By.cssSelector("a[aria-label='Add “Blue Shoes” to your cart']"); // By
	 * addedToCartBlueShoes = // By.cssSelector(
	 * ".button.product_type_simple.add_to_cart_button.ajax_add_to_cart.added"); By
	 * addedToCartBlueShoes = By.cssSelector("a.added[aria-label*='Blue Shoes']");
	 * By priceOfBlueJeans = By
	 * .xpath("//a[@aria-label='Add “Blue Shoes” to your cart']/preceding-sibling::span[@class='price']"
	 * ); By viewCartLinkOfBlueJeans =
	 * By.cssSelector("a.added[aria-label*='Blue Shoes']+a");
	 */

	public StorePage(WebDriver driver) {
		super(driver);
	}

	// SEARCH ACTIONS
	public StorePage searchForProduct(String searchText) {
		return enterSearchText(searchText).clickSearchButton();
	}

	public StorePage enterSearchText(String searchText) {
		actions.type(searchTextbox, searchText);
		return this;
	}

	public StorePage clickSearchButton() {
		actions.click(searchButton);
		return this;
	}

	// --- PRODUCT ACTIONS ---

	public StorePage addProductToCart(String productName) {
		By locator = getAddToCartBtn(productName);
		actions.click(locator);
		actions.waitForAttributeToContain(locator, "class", "added");
		return this;
	}

	// Intent-based: Combines clicking and waiting for the next state
	public CartPage clickViewCartOfAProduct(String productName) {
		actions.click(getViewCartLocator(productName));
		return new CartPage(driver);
	}

	// DATA RETRIEVAL

	public String getProductPrice(String productName) {
		return actions.getContent(getPriceLocator(productName));
	}

	public String getCartCount() {
		return actions.getContent(cartIconCount);
	}

	public String getSearchHeaderText() {
		return actions.getContent(searchResultHeading);
	}

	// --- PRIVATE DYNAMIC LOCATORS ---

	private By getPriceLocator(String productName) {
		// Uses the XPath axis to find the price relative to the "Add to Cart" button
		return By.xpath("//a[contains(@aria-label, '" + productName + "')]/preceding-sibling::span[@class='price']");
	}

	private By getViewCartLocator(String productName) {
		// Targets the "View Cart" link that appears specifically after a product is
		// added
		return By.cssSelector("a.added[aria-label*='" + productName + "']+a");
	}

	private By getAddToCartBtn(String productName) {
		// Instead of hardcoding "Blue Shoes", generate the locator on the fly
		return By.cssSelector("a[aria-label*='" + productName + "']");
	}

	// --- LOADABLE COMPONENT OVERRIDES ---
	@Override
	public StorePage load() {
		return this;
	}

	@Override
	public void isLoaded() throws Error {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(searchTextbox));
		} catch (TimeoutException e) {
			throw new Error("Store Page did not load: Search field not found.");
		}
	}

}
