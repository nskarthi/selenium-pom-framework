package org.selenium.pom.utils;

/**
 * Utility for common String manipulations and checks. Using static methods so
 * you don't need to instantiate it.
 */
public class StringUtils {

	/**
	 * Checks if a string is neither null nor empty (after trimming).
	 * 
	 * @param value The string to check
	 * @return true if the string has content
	 */
	public static boolean isNotEmpty(String value) {
		return value != null && !value.trim().isEmpty();
	}

	/**
	 * Checks if a string is null or empty. Useful for assertions or skipping logic.
	 */
	public static boolean isEmpty(String value) {
		return !isNotEmpty(value);
	}
}
