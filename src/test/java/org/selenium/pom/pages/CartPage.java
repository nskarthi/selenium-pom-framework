package org.selenium.pom.pages;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.selenium.pom.base.BasePage;

public class CartPage extends BasePage<CartPage> {
	public static final String PAGE_TITLE = "AskOmDch – Become a Selenium automation expert!";

	private final By cartPageHeader = By.className("has-text-align-center");
	private final By productNamesColumn = By.cssSelector(".woocommerce-cart-form__cart-item td.product-name");
	private final By priceOfItemsColumn = By.cssSelector(".woocommerce-cart-form__cart-item td.product-price");
	private final By cartTableRowLocator = By.cssSelector(".woocommerce-cart-form__cart-item.cart_item");
	private final By cartTableColumnLocator = By.tagName("td");
	private final By proceedToCheckoutButton = By.cssSelector(".checkout-button");

	public CartPage(WebDriver driver) {
		super(driver);
	}

	public String getPageTitle() {
		return actions.getPageTitle(PAGE_TITLE);
	}

	public List<String> getProductNamesInCart() {
		return actions.getElementTextList(productNamesColumn);
	}

	public List<String> getItemsPriceInCart() {
		return actions.getElementTextList(priceOfItemsColumn);
	}

	// Obsolete - use below function
	public List<List<String>> getCartTableData_obsolete() {
		return actions.getTableData(cartTableRowLocator);
	}

	/**
	 * Retrieves the cart data by passing a custom "cell parser" logic to the
	 * generic ElementActions class.
	 */
	public List<List<String>> getCartTableData() {
		// Define the site-specific parsing logic using a Lambda expression
		return actions.getTableData(cartTableRowLocator, (cell) -> {
			String cellClass = cell.getAttribute("class");

			// 1. Handle WooCommerce Quantity Inputs
			if (cellClass.contains("product-quantity")) {
				try {
					return cell.findElement(By.tagName("input")).getAttribute("value");
				} catch (Exception e) {
					return "0";
				}
			}

			// 2. Performance: Skip expensive getText() for non-data columns
			if (cellClass.contains("product-remove") || cellClass.contains("product-thumbnail")) {
				return "";
			}

			// 3. Default: Return trimmed text for Name, Price, Subtotal
			return cell.getText().trim();
		});
	}

	/**
	 * Function to verify if a product exists in the cart.
	 */
	public boolean isProductInCart(String expectedProductName) {
		List<List<String>> tableData = getCartTableData();

		// Loop through each row of the table
		for (List<String> row : tableData) {
			// Loop through each cell in the current row
			for (String cellText : row) {
				// Check if cell text matches expected product name (case-insensitive)
				if (cellText.equalsIgnoreCase(expectedProductName)) {
					return true; // Match found, exit immediately
				}
			}
		}
		return false; // No match found after checking the entire table
	}

	/**
	 * Function to verify if all the products are in the cart.
	 */
	public boolean areAllProductsInCart(List<String> expectedProductNames) {
		// 1. Read the table data ONCE from the browser
		List<List<String>> tableData = getCartTableData();

		// 2. Flatten the 2D table into a single Set for O(1) lookups
		// Set of lowercase strings for fast, case-insensitive matching
		Set<String> allCellTexts = new HashSet<>();
		for (List<String> row : tableData) {
			for (String cell : row) {
				if (cell != null) {
					allCellTexts.add(cell.toLowerCase().trim());
				}
			}
		}

		// 3. Check if every expected product exists in our local Set
		for (String productName : expectedProductNames) {
			if (!allCellTexts.contains(productName.toLowerCase().trim())) {
				// Fails as soon as one product is missing
				return false;
			}
		}
		return true; // All products found
	}

	/**
	 * Get a specific product's quantity from the parsed table.
	 */
	public String getProductQuantity(String expectedProductName) {
		List<List<String>> tableData = getCartTableData();
		for (List<String> row : tableData) {
			for (String cellText : row) {
				if (cellText.equalsIgnoreCase(expectedProductName)) {
					return row.get(4);
				}
			}
		}
		return "0";
	}

	public CheckoutPage clickProceedToCheckout() {
		actions.click(proceedToCheckoutButton);
		return new CheckoutPage(driver);
	}

	@Override
	protected void load() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void isLoaded() throws Error {
		String url = driver.getCurrentUrl();
		if (!url.contains("cart")) {
			throw new Error("Cart Page not loaded. Current URL: " + url);
		}
	}

}
