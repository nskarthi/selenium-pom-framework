package org.selenium.pom.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.selenium.pom.base.ElementActions;
import org.selenium.pom.pages.HomePage;
import org.selenium.pom.pages.StorePage;

public class HeaderMenu {

    private final By homeMenuLink = By.cssSelector("#menu-item-1226 a");
	private final By storeMenuLink = By.cssSelector("#menu-item-1227 a");
    private final By menMenuLink = By.cssSelector("#menu-item-1228 a");
    private final By womenMenuLink = By.cssSelector("#menu-item-1229 a");
    private final By accessoriesMenuLink = By.cssSelector("#menu-item-1230 a");
    private final By accountMenuLink = By.cssSelector("#menu-item-1237 a");
    private final By aboutMenuLink = By.cssSelector("#menu-item-1232 a");
    private final By contactusMenuLink = By.cssSelector("#menu-item-1233 a");

    private ElementActions actions;
    private WebDriver driver;

	public HeaderMenu(WebDriver driver) {
		this.driver = driver;
		actions = new ElementActions(driver);
	}

    public HomePage navigateToHomePage() {
        actions.click(homeMenuLink);
        return new HomePage(driver);
    }
	
    public StorePage navigateToStorePage() {
        actions.click(storeMenuLink);
        return new StorePage(driver);
    }

    /* 
    public MenPage navigateToMenPage() {
        actions.click(menMenuLink);
        return new MenPage(driver);
    }
    
    public WomenPage navigateToWomenPage() {
        actions.click(womenMenuLink);
        return new WomenPage(driver);
    }
    
    public AccessoriesPage navigateToStore() {
        actions.click(accessoriesMenuLink);
        return new AccessoriesPage(driver);
    }
    
    public AccountPage navigateToAccountPage() {
        actions.click(accountMenuLink);
        return new AccountPage(driver);
    }
    
    public AboutPage navigateToAboutPage() {
        actions.click(aboutMenuLink);
        return new AboutPage(driver);
    }
    
    public ContactusPage navigateToContactusPage() {
        actions.click(contactusMenuLink);
        return new ContactusPage(driver);
    }
    
    */
    
}
