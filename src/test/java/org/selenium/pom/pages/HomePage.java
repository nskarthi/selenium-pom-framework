package org.selenium.pom.pages;

import org.openqa.selenium.WebDriver;
import org.selenium.pom.base.BasePage;
import org.selenium.pom.pages.components.HeaderMenu;
import org.selenium.pom.pages.components.ProductThumbnail;

public class HomePage extends BasePage<HomePage> {
    public static final String PAGE_TITLE = "AskOmDch – Become a Selenium automation expert!";
	private HeaderMenu headerMenu;
	private ProductThumbnail productThumbnail;

	public HeaderMenu getHeaderMenu() {
		return headerMenu;
	}

	public ProductThumbnail getProductThumbnail() {
		return productThumbnail;
	}

	public HomePage(WebDriver driver) {
		super(driver);
		headerMenu = new HeaderMenu(driver);
		productThumbnail = new ProductThumbnail(driver);
	}

    public String getPageTitle() {
        return actions.getPageTitle(PAGE_TITLE);
    }

	@Override
	protected void isLoaded() throws Error {
		String url = driver.getCurrentUrl();
		if (!url.contains("askomdch.com")) {
			throw new Error("Home Page not loaded. Current URL: " + url);
		}
	}

	@Override
	protected void load() {
		load("/");
	}
}
