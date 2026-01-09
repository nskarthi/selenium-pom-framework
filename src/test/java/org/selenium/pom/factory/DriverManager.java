package org.selenium.pom.factory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

public class DriverManager {

	public WebDriver initializeDriver() {
		// 1. Create a map to store the browser preferences
		Map<String, Object> prefs = new HashMap<String, Object>();

		// 2. Disable the specific autofill and password manager features
		// This prevents the "Save Address" and "Save Password" popups
		prefs.put("autofill.profile_enabled", false);
		prefs.put("autofill.credit_card_enabled", false);
		prefs.put("credentials_enable_service", false);
		prefs.put("profile.password_manager_enabled", false);

		// 3. Initialize ChromeOptions and add the preferences
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", prefs);

		// Optional: Hide the "Chrome is being controlled by automated software" info
		// bar
		options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });

		// 4. Return the configured driver
		// String projectPath = System.getProperty("user.dir");
		// System.setProperty("webdriver.chrome.driver", projectPath +
		// "/drivers/chromedriver.exe");
		WebDriver driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

		return driver;
	}

	/**
	 * Initializes the WebDriver based on a browser name.
	 * 
	 * @param browser The browser to initialize (chrome, firefox, edge)
	 * @return Configured WebDriver instance
	 */
	public WebDriver initializeDriver(String browser) {
		WebDriver driver;

		switch (browser.toLowerCase()) {
		case "chrome":
			driver = new ChromeDriver(getChromeOptions());
			break;

		case "firefox":
			driver = new FirefoxDriver(getFirefoxOptions());
			break;

		case "edge":
			driver = new EdgeDriver(getEdgeOptions());
			break;

		default:
			throw new IllegalArgumentException("Invalid browser name: " + browser);
		}

		driver.manage().window().maximize();
		// Best practice: Keep implicit wait at 0 and use Explicit Waits (WebDriverWait)
		// in Page Objects
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

		return driver;
	}

	private ChromeOptions getChromeOptions() {
		// 1. Create a map to store the browser preferences
		Map<String, Object> prefs = new HashMap<>();

		// 2. Disable the specific autofill and password manager features
		// This prevents the "Save Address" and "Save Password" popups
		prefs.put("autofill.profile_enabled", false);
		prefs.put("autofill.credit_card_enabled", false);
		prefs.put("credentials_enable_service", false);
		prefs.put("profile.password_manager_enabled", false);

		// 3. Initialize ChromeOptions and add the preferences
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", prefs);
		// Optional: Hide the "Chrome is being controlled by automated software" info
		// bar from the browser
		options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
		return options;
	}

	private FirefoxOptions getFirefoxOptions() {
		// 1. Create browser options
		FirefoxOptions options = new FirefoxOptions();
		FirefoxProfile profile = new FirefoxProfile();

		// 2. Disable the specific autofill and password manager features
		// This prevents the "Save Address" and "Save Password" popups
		profile.setPreference("signon.rememberSignons", false);
		profile.setPreference("dom.forms.autocomplete.formautofill", false);
		profile.setPreference("browser.formfill.enable", false);

		// Prevent "First Run" tabs and welcome screens
		options.addArguments("--disable-infobars");
		options.setProfile(profile);
		return options;
	}

	private EdgeOptions getEdgeOptions() {
		// 1. Create a map to store the browser preferences
		Map<String, Object> prefs = new HashMap<>();

		// 2. Disable the specific autofill and password manager features
		// This prevents the "Save Address" and "Save Password" popups
		prefs.put("autofill.profile_enabled", false);
		prefs.put("autofill.credit_card_enabled", false);
		prefs.put("credentials_enable_service", false);
		prefs.put("profile.password_manager_enabled", false);

		// 3. Initialize EdgeOptions and add the preferences
		EdgeOptions options = new EdgeOptions();
		options.setExperimentalOption("prefs", prefs);
		// Hide the "Edge is being controlled..." info bar from the browser
		options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
		return options;
	}
}
