package org.selenium.pom.pages;

import static org.selenium.pom.utils.StringUtils.isNotEmpty;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.selenium.pom.base.BasePage;
import org.selenium.pom.model.BillingModel;

public class BillingSection extends BasePage<BillingSection>  {
	private final By billingFirstName = By.name("billing_first_name");
	private final By billingLastName = By.name("billing_last_name");
	private final By billingCompany = By.name("billing_company");

	// CSS selectors for the 'Country' and 'State' Select2 components
    private final By billingCountryContainer = By.id("select2-billing_country-container");
    private final By billingStateContainer = By.id("select2-billing_state-container");

    // The search input field that appears for ALL Select2 dropdowns when active
    private final By select2SearchField = By.cssSelector(".select2-search__field");

	private final By billingAddress1 = By.name("billing_address_1");
	private final By billingAddress2 = By.name("billing_address_2");
	private final By billingCity = By.name("billing_city");
	private final By billingZip = By.name("billing_postcode");
	private final By billingPhone = By.name("billing_phone");
	private final By billingEmail = By.name("billing_email");
	private final By billingCreateAccountChkBox = By.id("createaccount");
	private final By shipToDifferentAddress = By.id("ship-to-different-address-checkbox");
	private final By orderNotes = By.name("order_comments");

	public BillingSection(WebDriver driver) {
		super(driver);
	}

	/**
	 * Fills the billing details based on the provided model. Use this for both
	 * Positive and Negative testing.
	 */
	public BillingSection fillBillingDetails(BillingModel data) {
		// Strings: Only type if the string is NOT empty.
		// This allows us to "skip" fields we don't want to touch in a test.
		if (isNotEmpty(data.firstname)) {
			actions.type(billingFirstName, data.firstname);
		}

		if (isNotEmpty(data.lastname)) {
			actions.type(billingLastName, data.lastname);
		}

		if (isNotEmpty(data.company)) {
			actions.type(billingCompany, data.company);
		}

        if (isNotEmpty(data.country)) {
            // Select Country and handle the internal AJAX refresh for the State dropdown
            actions.selectCountryAndWaitForStateRefresh_new(
                billingCountryContainer, 
                select2SearchField, 
                data.country, 
                billingStateContainer,
                false
            );

            // Now it is safe to select the State
            if (isNotEmpty(data.state)) {
                actions.selectFromSelect2(billingStateContainer, select2SearchField, data.state);
            }
        }

		if (isNotEmpty(data.address1)) {
			actions.type(billingAddress1, data.address1);
		}

		if (isNotEmpty(data.address2)) {
			actions.type(billingAddress2, data.address2);
		}

		if (isNotEmpty(data.city)) {
			actions.type(billingCity, data.city);
		}
		
		if (isNotEmpty(data.email)) {
			actions.type(billingEmail, data.email);
		}

		if (isNotEmpty(data.zip)) {
			actions.type(billingZip, data.zip);
		}

		if (isNotEmpty(data.phone)) {
			actions.type(billingPhone, data.phone);
		}

		if (data.createAnAccount) {
			actions.click(billingCreateAccountChkBox);
		}

		if (data.shipToDifferentAddress) {
			actions.click(shipToDifferentAddress);
		}

		if (isNotEmpty(data.orderNotes)) {
			actions.type(orderNotes, data.orderNotes);
		}
		return this;
	}

	@Override
	protected void load() {
        // Since this is a section of a page, 'load' might navigate to checkout if needed
        // driver.get("https://yourstore.com/checkout");
	}

	@Override
	protected void isLoaded() throws Error {
        String url = driver.getCurrentUrl();
        if (!url.contains("askomdch.com")) {
            throw new Error("Billing Section not loaded. Current URL: " + url);
        }
	}

}
