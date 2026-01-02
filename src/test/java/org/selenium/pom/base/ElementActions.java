package org.selenium.pom.base;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ElementActions {
	private final WebDriverWait wait;

	public ElementActions(WebDriver driver) {
		// Optimization: Use a configurable timeout if possible
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	// Optimization: Clear before typing to prevent appending text
	public void type(By locator, String text) {
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		element.clear();
		element.sendKeys(text);
	}

	public void click(By locator) {
		wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
	}

	public String getCssValue(By locator, String property) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator)).getCssValue(property).trim();
	}

	public boolean waitForAttributeToContain(By locator, String attribute, String value) {
		try {
			return wait.until(ExpectedConditions.attributeContains(locator, attribute, value));
		} catch (Exception e) {
			return false; // Returns false if timeout occurs
		}
	}

	public String getContent(By locator) {
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
	 * Optimized method to retrieve table data.
	 * Addresses performance issues by reducing redundant DOM searches 
	 * and handles WooCommerce-specific quantity inputs.
	 */
	public List<List<String>> getTableData(By rowLocator) {
	    List<List<String>> tableData = new ArrayList<>();
	    
	    // 1. Get all rows at once to reduce driver calls
	    List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(rowLocator));
	    
	    for (WebElement row : rows) {
	        // Skip the "actions" row (the one with the coupon code)
	        if (row.getAttribute("class").contains("actions")) continue;

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
	            } 
	            else {
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
     * @param rowLocator The locator for the table rows.
     * @param cellParser A function that defines how to extract text from a cell. 
     * This allows site-specific logic to stay out of this class.
     */
    public List<List<String>> getTableData(By rowLocator, Function<WebElement, String> cellParser) {
        List<List<String>> tableData = new ArrayList<>();

        // Question 1: Using wait to ensure elements are present and visible
        List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(rowLocator));

        for (WebElement row : rows) {
            // Generic check: skip rows that have no <td> elements (like headers or spacers)
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.isEmpty()) continue;

            List<String> rowData = new ArrayList<>();
            for (WebElement cell : cells) {
                // Apply the logic passed in from the Page Object
                rowData.add(cellParser.apply(cell));
            }
            tableData.add(rowData);
        }
        return tableData;
    }
	
	public List<List<String>> getTableData(By rowLocator, By columnLocator) {
		System.out.println("Inside getTableData() function");
		// 1. Wait for and find all rows
		List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(rowLocator));

		// 2. Prepare the outer list
		List<List<String>> tableData = new ArrayList<>();

		int rowCount = 1;
		// 3. Loop through each row
		for (WebElement row : rows) {
			// Find all columns within this specific row
			List<WebElement> columns = row.findElements(columnLocator);
			List<String> rowData = new ArrayList<>();
			System.out.println("Parsing Row: " + (rowCount++));
			// 4. Loop through each column
			int columnCount = 1;
			for (WebElement column : columns) {
				String cellText = column.getText().trim();

				// SMART LOGIC: If text is empty, check if there is an input or textarea inside
				if (cellText.isEmpty()) {
					List<WebElement> inputs = column.findElements(By.tagName("input"));
					if (!inputs.isEmpty()) {
						cellText = inputs.get(0).getAttribute("value");
					}
				}
				System.out.println("Column# " + (columnCount++) + " : " + cellText);
				rowData.add(cellText);
			}

			// Only add rows that actually contain data (skips the 'Actions/Coupon' row)
			if (!rowData.isEmpty() && rowData.size() > 1) {
				tableData.add(rowData);
			}
		}

		return tableData;
	}

	public List<List<String>> getTableData_obsolete(By rowLocator, By columnLocator) {
		// 1. Wait for and find all rows
		List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(rowLocator));

		// 2. Prepare the outer list (the whole table)
		List<List<String>> tableData = new ArrayList<>();

		// 3. Loop through each row
		for (WebElement row : rows) {
			// Find all columns within this specific row
			List<WebElement> columns = row.findElements(columnLocator);
			List<String> rowData = new ArrayList<>();

			// 4. Loop through each column to get the text
			for (WebElement column : columns) {
				rowData.add(column.getText().trim());
			}

			// 5. Add the completed row to our table
			tableData.add(rowData);
		}

		return tableData;
	}

}
