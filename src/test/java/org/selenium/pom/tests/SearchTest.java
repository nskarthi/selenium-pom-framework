package org.selenium.pom.tests;

import java.io.IOException;

import org.selenium.pom.base.BaseTest;
import org.selenium.pom.pages.StorePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SearchTest extends BaseTest {

	@Test
	public void searchWithPartialMatch() throws IOException, InterruptedException {
		String searchFor = "Blue";
		StorePage storePage = new StorePage(getDriver()).get().searchForProduct(searchFor).clickSearchButton();
		Assert.assertTrue(storePage.getPageHeading().toLowerCase().contains(searchFor.toLowerCase()),
				"Expected '" + searchFor + "' but found: " + storePage.getPageHeading());
	}
}
