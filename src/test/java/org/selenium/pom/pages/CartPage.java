package org.selenium.pom.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.selenium.pom.base.BasePage;
import org.testng.Assert;

public class CartPage extends BasePage<Object> {

	private final By cartPageHeader = By.className("has-text-align-center");
	private final By productNamesColumn = By.cssSelector(".woocommerce-cart-form__cart-item td.product-name");
	private final By priceOfItemsColumn = By.cssSelector(".woocommerce-cart-form__cart-item td.product-price");
	private final By cartTableRowLocator = By.cssSelector(".woocommerce-cart-form__cart-item.cart_item");
	private final By cartTableColumnLocator = By.tagName("td");

	public CartPage(WebDriver driver) {
		super(driver);
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
	 * Example of how to use the data to verify a product exists in the cart.
	 */
	public boolean isProductInCart(String expectedProductName) {
		List<List<String>> tableData = getCartTableData();

		return tableData.stream()
				.anyMatch(row -> row.stream().anyMatch(cellText -> cellText.equalsIgnoreCase(expectedProductName)));
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

	// Not used anymore. Use above function
	public List<CartItem> getCartTableContents() {
		// Call the generic function from ElementActions
		List<List<String>> rawTableData = actions.getTableData(cartTableRowLocator, cartTableColumnLocator);

		List<CartItem> cartItems = new ArrayList<>();

		// Loop through the raw data and convert it into CartItem objects
		for (List<String> rowData : rawTableData) {
			// Basic error checking: ensure the row has enough columns
			if (rowData.size() >= 6) {
				String name = rowData.get(2); // Index 2: Product Name
				System.out.println(name);
				String price = rowData.get(3); // Index 3: Price
				System.out.println(price);
				String quantity = rowData.get(4); // Index 4: Quantity
				System.out.println(quantity);
				String subtotal = rowData.get(5); // Index 5: Subtotal

				cartItems.add(new CartItem(name, price, quantity, subtotal));
			}
		}
		return cartItems;
	}

	public void dummyFunc() {
		// Use a combined selector to target the product name TD within any cart item
		// row

		wait.until(ExpectedConditions.visibilityOfElementLocated(productNamesColumn));

		List<WebElement> productNames = driver.findElements(productNamesColumn);
		List<WebElement> productPrices = driver.findElements(priceOfItemsColumn);
		System.out.println("Total items found: " + productNames.size());

		String price = "";
		boolean itemFoundInCart = false;
		int productCount = 0;
		for (WebElement productName : productNames) {
			if (productName.getText().equals("Blue Shoes")) {
				String itemPrice = productPrices.get(productCount).getText().trim();
				if (price.equalsIgnoreCase(itemPrice))
					itemFoundInCart = true;
			}
			productCount++;
		}
		Assert.assertTrue(itemFoundInCart == true);
	}

	@Override
	public Object load() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void isLoaded() throws Error {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(cartPageHeader));
		} catch (TimeoutException e) {
			throw new Error("Cart Page did not load: Cart Page Header is not visible.");
		}
	}

}
