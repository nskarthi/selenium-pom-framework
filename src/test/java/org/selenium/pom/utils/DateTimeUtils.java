package org.selenium.pom.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
	public static String getTimestamp() {
		// Format: YearMonthDay_HourMinuteSecond (e.g., 20260115_061805)
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		return LocalDateTime.now().format(formatter);
	}
}
