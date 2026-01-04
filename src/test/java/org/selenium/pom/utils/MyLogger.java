package org.selenium.pom.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 1. THE LOGGER: Instead of System.out.println, we use Log4j. This can be
 * configured to print to the console AND save to a file.
 */
public class MyLogger {
	private static final Logger logger = LogManager.getLogger(MyLogger.class);

	public static void info(String message) {
		logger.info(message);
	}

	public static void debug(String message) {
		logger.debug(message);
	}

	public static void error(String message) {
		logger.error(message);
	}
}
