package org.selenium.pom.base;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ElementActions {
	private final WebDriverWait wait;
	private WebDriver driver;
	private final JavascriptExecutor js;

	public ElementActions(WebDriver driver) {
		// Optimization: Use a configurable timeout if possible
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		this.driver = driver;
		this.js = (JavascriptExecutor) driver;
	}

	public void type(By locator, String text) {
		System.out.println("inside type");
		// Wait for visibility first then wait for clickability which returns the element
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
		
		// Below can be used as well to force focus to overcome Firefox focus-sticking
    	//js.executeScript("arguments[0].scrollIntoView(true);", element);
    	element.click();
		element.clear();
		element.sendKeys(text);
	}

	/**
	 * Enhanced Click: Handles Staleness and provides a JS fallback if the element
	 * is intercepted by another UI component.
	 */
	public void click(By locator) {
		try {
			wait.ignoring(StaleElementReferenceException.class).until(d -> {
				WebElement element = d.findElement(locator);
				// Ensure it's scrolled into view before clicking
				js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
				element.click();
				return true;
			});
		} catch (ElementClickInterceptedException e) {
			// Fallback to JS Click if a physical click is blocked
			js.executeScript("arguments[0].click();", driver.findElement(locator));
		}
	}

	public String getCssValue(By locator, String property) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator)).getCssValue(property).trim();
	}

	public String getPageTitle(String expectedTitle) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
			wait.until(ExpectedConditions.titleIs(expectedTitle));
		} catch (TimeoutException e) {
			// Log that the title didn't match within the timeout
		}
		return driver.getTitle();
	}

	/**
	 * Centralized wait for URL validation.
	 */
	public boolean waitForUrlToContain(String urlFraction) {
		try {
			return wait.until(ExpectedConditions.urlContains(urlFraction));
		} catch (Exception e) {
			return false;
		}
	}

	public boolean waitForAttributeToContain(By locator, String attribute, String value) {
		try {
			return wait.until(ExpectedConditions.attributeContains(locator, attribute, value));
		} catch (Exception e) {
			return false; // Returns false if timeout occurs
		}
	}

	public void select(By locator, String value) {
		new Select(wait.until(ExpectedConditions.presenceOfElementLocated(locator))).selectByVisibleText(value);
	}

	/**
	 * Standard Select2 Selection Uses normalize-space() in XPath to ignore extra
	 * whitespace/newlines in the HTML.
	 */
	public void selectFromSelect2(By containerLocator, By searchFieldLocator, String itemName) {
		// Open the dropdown
		wait.until(ExpectedConditions.elementToBeClickable(containerLocator)).click();

		// Type into the search box
		WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(searchFieldLocator));
		searchField.sendKeys(itemName);

		// Updated XPath: Finds the list item matching the text exactly, ignoring
		// whitespace
		By itemLocator = By
				.xpath("//li[contains(@class, 'select2-results__option')][normalize-space()='" + itemName + "']");

		wait.until(ExpectedConditions.elementToBeClickable(itemLocator)).click();
	}

	/**
	 * Optimized Country-to-State refresh logic.
	 * 
	 * @param expectNavigation Set to true ONLY if the previous action is guaranteed
	 *                         to trigger a page reload or DOM wipe.
	 */
	public void selectCountryAndWaitForStateRefresh_new(By countryContainer, By searchField, String countryName,
			By stateContainer, boolean expectNavigation) {
		// 1. Get current state element (if it exists) to watch for refresh
		List<WebElement> stateElements = driver.findElements(stateContainer);
		WebElement oldState = stateElements.isEmpty() ? null : stateElements.get(0);

		System.out.println("country: " + countryName);
		// 2. Select the country
		click(countryContainer);
		WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchField));
		input.sendKeys(countryName + Keys.ENTER);

		// 3. Wait for the AJAX reload (Staleness of the old element)
		if (expectNavigation && oldState != null) {
			try {
				wait.until(ExpectedConditions.stalenessOf(oldState));
			} catch (TimeoutException ignored) {
				// Refresh happened faster than Selenium could catch it or didn't happen
			}
		}

		// 4. Final sync for the new state dropdown
		wait.until(ExpectedConditions.elementToBeClickable(stateContainer));
	}

	/**
	 * Selects a country and waits for the dependent State/Province field to refresh
	 * via AJAX. * @param countryContainer The locator for the country Select2
	 * dropdown.
	 * 
	 * @param searchField      The locator for the Select2 search input.
	 * @param countryName      The text to search and select.
	 * @param stateContainer   The locator for the state dropdown that refreshes.
	 * @param expectNavigation Set to true ONLY if the previous action is guaranteed
	 *                         to trigger a page reload or DOM wipe.
	 */
	public void selectCountryAndWaitForStateRefresh(By countryContainer, By searchField, String countryName,
			By stateContainer, boolean expectNavigation) {
		// Access the driver directly from the wait object
		WebElement oldStateElement = null;

		// 1. Identify the current state dropdown to track staleness (AJAX reload)
		try {
			oldStateElement = driver.findElement(stateContainer);
		} catch (NoSuchElementException e) {
			// Element not present yet, which is fine for the first selection
		}

		// 2. Interact with the Select2 Country dropdown
		click(countryContainer);
		WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchField));
		input.sendKeys(countryName + Keys.ENTER);

		// 3. Handle the AJAX refresh synchronization
		if (expectNavigation && oldStateElement != null) {
			try {
				// Wait for the old dropdown to be detached from the DOM
				wait.until(ExpectedConditions.stalenessOf(oldStateElement));
			} catch (TimeoutException e) {
				// If it doesn't become stale, it might have refreshed too fast or failed
			}
		}

		// 4. Wait for the new State element to be ready and scroll it into view
		WebElement newStateElement = wait.until(ExpectedConditions.elementToBeClickable(stateContainer));

		((JavascriptExecutor) driver)
				.executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", newStateElement);
	}

	public String getContents(By locator) {
		// Optimization: Use visibility instead of presence for text to ensure it's
		// rendered
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText().trim();
	}

	public List<String> getElementTextList(By locator) {
		List<String> productNames = new ArrayList<String>();
		List<WebElement> elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));

		for (WebElement element : elements) {
			productNames.add(element.getText().trim());
		}
		return productNames;
	}

	/**
	 * Optimized method to retrieve table data. Addresses performance issues by
	 * reducing redundant DOM searches and handles WooCommerce-specific quantity
	 * inputs.
	 */
	public List<List<String>> getTableData(By rowLocator) {
		List<List<String>> tableData = new ArrayList<>();

		// 1. Get all rows at once to reduce driver calls
		List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(rowLocator));

		for (WebElement row : rows) {
			// Skip the "actions" row (the one with the coupon code)
			if (row.getAttribute("class").contains("actions"))
				continue;

			List<String> rowData = new ArrayList<>();
			// 2. Find all cells in this specific row
			List<WebElement> cells = row.findElements(By.tagName("td"));

			for (WebElement cell : cells) {
				String cellClass = cell.getAttribute("class");
				String cellText = "";

				// 3. SPECIAL HANDLING: Quantity Column
				if (cellClass.contains("product-quantity")) {
					// Instead of getText() which grabs the hidden label,
					// we get the 'value' attribute from the input field.
					WebElement qtyInput = cell.findElement(By.tagName("input"));
					cellText = qtyInput.getAttribute("value");
				}
				// 4. PERFORMANCE & CLEANUP: Ignore empty/icon columns
				else if (cellClass.contains("product-remove") || cellClass.contains("product-thumbnail")) {
					cellText = ""; // Keep placeholder to maintain index, but don't waste time parsing
				} else {
					// Use getText() for standard text columns (Name, Price, Subtotal)
					cellText = cell.getText().trim();
				}

				rowData.add(cellText);
			}

			if (!rowData.isEmpty()) {
				tableData.add(rowData);
			}
		}
		return tableData;
	}

	/**
	 * TRULY GENERIC: Retrieves table data.
	 * 
	 * @param rowLocator The locator for the table rows.
	 * @param cellParser A function that defines how to extract text from a cell.
	 *                   This allows site-specific logic to stay out of this class.
	 */
	public List<List<String>> getTableData(By rowLocator, Function<WebElement, String> cellParser) {
		List<List<String>> tableData = new ArrayList<>();

		// Question 1: Using wait to ensure elements are present and visible
		List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(rowLocator));

		for (WebElement row : rows) {
			// Generic check: skip rows that have no <td> elements (like headers or spacers)
			List<WebElement> cells = row.findElements(By.tagName("td"));
			if (cells.isEmpty())
				continue;

			List<String> rowData = new ArrayList<>();
			for (WebElement cell : cells) {
				// Apply the logic passed in from the Page Object
				rowData.add(cellParser.apply(cell));
			}
			tableData.add(rowData);
		}
		return tableData;
	}

}
