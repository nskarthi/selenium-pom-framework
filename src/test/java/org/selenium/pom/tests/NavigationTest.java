package org.selenium.pom.tests;

import java.io.IOException;

import org.selenium.pom.base.BaseTest;
import org.selenium.pom.pages.HomePage;
import org.selenium.pom.pages.StorePage;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.qameta.allure.Description;

public class NavigationTest extends BaseTest {

	@Description("This is the description")
	@Test(description = "Should be able to navigate from home page to store page using main menu")
	public void testnavigateFromHomeToStoreUsingMainMenu() throws IOException, InterruptedException {
        //StorePage storePage = new HomePage(getDriver()).get().clickStoreMenuLink();
        StorePage storePage = new HomePage(getDriver()).get().getHeaderMenu().navigateToStorePage();
		Assert.assertEquals(storePage.getPageHeading(), "Store");
	}
}
