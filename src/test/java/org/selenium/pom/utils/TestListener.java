package org.selenium.pom.utils;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * 2. THE LISTENER: This class monitors the test. If you want to log data about
 * the test state without cluttering the @Test method, you do it here.
 */
class TestListener implements ITestListener {

	@Override
	public void onTestStart(ITestResult result) {
		MyLogger.info("Starting Test: " + result.getMethod().getMethodName());
	}

	@Override
	public void onTestFailure(ITestResult result) {
		MyLogger.error("TEST FAILED: " + result.getName());
		// This is where you would put: CaptureScreenshot(driver);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		MyLogger.info("Test Passed: " + result.getName());
	}
}
