package org.selenium.pom.factory;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverManager {

	public WebDriver initializeDriver() {
		String projectPath = System.getProperty("user.dir");

		//System.setProperty("webdriver.chrome.driver", projectPath + "/drivers/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

		return driver;
	}
}
