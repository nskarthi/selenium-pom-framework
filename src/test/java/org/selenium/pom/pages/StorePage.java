package org.selenium.pom.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.selenium.pom.base.BasePage;
import org.selenium.pom.pages.components.HeaderMenu;
import org.selenium.pom.pages.components.ProductThumbnail;

public class StorePage extends BasePage<StorePage> {
    public static final String PAGE_TITLE = "Products – AskOmDch";

	private final By searchTextbox = By.id("woocommerce-product-search-field-0");
	private final By searchButton = By.cssSelector("button[value='Search']");
	private final By searchResultHeading = By.cssSelector(".woocommerce-products-header__title.page-title");

	private HeaderMenu headerMenu;
	private ProductThumbnail productThumbnail;

	public HeaderMenu getHeaderMenu() {
		return headerMenu;
	}

	public ProductThumbnail getProductThumbnail() {
		return productThumbnail;
	}

	public StorePage(WebDriver driver) {
		super(driver);
	}

    public String getPageHeading() {
        return actions.getContents(searchResultHeading);
    }

    public String getPageTitle() {
        return actions.getPageTitle(PAGE_TITLE);
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

	// DATA RETRIEVAL

	public String getProductPrice(String productName) {
		return actions.getContents(getPriceLocator(productName));
	}

	public String getSearchHeaderText() {
		return actions.getContents(searchResultHeading);
	}

	// --- PRIVATE DYNAMIC LOCATORS ---

	private By getPriceLocator(String productName) {
		// Uses the XPath axis to find the price relative to the "Add to Cart" button
		return By.xpath("//a[contains(@aria-label, '" + productName + "')]/preceding-sibling::span[@class='price']");
	}

	// --- LOADABLE COMPONENT OVERRIDES ---
	protected void load() {
		load("/store");
	}

	@Override
	protected void isLoaded() throws Error {
        String url = driver.getCurrentUrl();
        if (!url.contains("store")) {
            throw new Error("Store Page not loaded. Current URL: " + url);
        }
	}

}
