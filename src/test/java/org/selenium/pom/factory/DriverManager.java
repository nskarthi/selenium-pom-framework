package org.selenium.pom.factory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

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

        // Optional: Hide the "Chrome is being controlled by automated software" info bar
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

		// 4. Return the configured driver
        // String projectPath = System.getProperty("user.dir");
        //System.setProperty("webdriver.chrome.driver", projectPath + "/drivers/chromedriver.exe");
		WebDriver driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

		return driver;
	}
}
