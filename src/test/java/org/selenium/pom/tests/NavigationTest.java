package org.selenium.pom.tests;

import java.io.IOException;

import org.selenium.pom.base.BaseTest;
import org.selenium.pom.pages.HomePage;
import org.selenium.pom.pages.StorePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NavigationTest extends BaseTest {

	@Test
	public void testnavigateFromHomeToStoreUsingMainMenu() throws IOException, InterruptedException {
        //StorePage storePage = new HomePage(getDriver()).get().clickStoreMenuLink();
        StorePage storePage = new HomePage(getDriver()).get().headerMenu.navigateToStorePage();
		Assert.assertEquals(storePage.getPageHeading(), "Store");
	}
}
