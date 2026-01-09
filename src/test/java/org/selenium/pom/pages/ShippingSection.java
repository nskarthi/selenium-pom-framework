package org.selenium.pom.pages;

import static org.selenium.pom.utils.StringUtils.isNotEmpty;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.selenium.pom.base.BasePage;
import org.selenium.pom.model.ShippingModel;

public class ShippingSection extends BasePage<ShippingSection> {

	private final By shippingFirstName = By.name("shipping_first_name");
	private final By shippingLastName = By.name("shipping_last_name");
	private final By shippingCompany = By.name("shipping_company");

	// CSS selectors for the 'Country' and 'State' Select2 components
	private final By shippingCountryContainer = By.id("select2-shipping_country-container");
	private final By shippingStateContainer = By.id("select2-shipping_state-container");

	// The search input field that appears for ALL Select2 dropdowns when active
	private final By select2SearchField = By.cssSelector(".select2-search__field");

	private final By shippingAddress1 = By.name("shipping_address_1");
	private final By shippingAddress2 = By.name("shipping_address_2");
	private final By shippingCity = By.name("shipping_city");
	private final By shippingState = By.name("shipping_state");
	private final By shippingZip = By.name("shipping_postcode");

	public ShippingSection(WebDriver driver) {
		super(driver);
	}

	/**
	 * Fills the shipping details based on the provided model. Use this for both
	 * Positive and Negative testing.
	 */
	public ShippingSection fillShippingDetails(ShippingModel data) {
		// Strings: Only type if the string is NOT empty.
		// This allows us to "skip" fields we don't want to touch in a test.
		if (isNotEmpty(data.firstname)) {
			actions.type(shippingFirstName, data.firstname);
		}

		if (isNotEmpty(data.lastname)) {
			actions.type(shippingLastName, data.lastname);
		}

		if (isNotEmpty(data.company)) {
			actions.type(shippingCompany, data.company);
		}

		if (isNotEmpty(data.country)) {
	        System.out.println("country");
			// Select Country and handle the internal AJAX refresh for the State dropdown
			actions.selectCountryAndWaitForStateRefresh(shippingCountryContainer, select2SearchField, data.country,
					shippingStateContainer, false);

			// Now it is safe to select the State
			if (isNotEmpty(data.state)) {
		        System.out.println("state");
				actions.selectFromSelect2(shippingStateContainer, select2SearchField, data.state);
			}
		}

		if (isNotEmpty(data.address1)) {
	        System.out.println("address1");
			actions.type(shippingAddress1, data.address1);
		}

		if (isNotEmpty(data.address2)) {
	        System.out.println("address2");
			actions.type(shippingAddress2, data.address2);
		}

		if (isNotEmpty(data.city)) {
	        System.out.println("city");
			actions.type(shippingCity, data.city);
		}

		if (isNotEmpty(data.zip)) {
	        System.out.println("zip");
			actions.type(shippingZip, data.zip);
		}

		return this;
	}

	@Override
	protected void load() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void isLoaded() throws Error {
		// TODO Auto-generated method stub

	}

}
