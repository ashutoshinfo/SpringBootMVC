package info.ashutosh.utility;

import java.math.BigDecimal;
import java.util.UUID;

public class CommonMethod {

	public static Long stringToLong(String stringLong) {

		if (stringLong == null || stringLong.trim().isEmpty()) {
			return null;
		}

		try {
			return Long.parseLong(stringLong.trim());
		} catch (Exception e) {
			return null;
		}
	}

	public static Integer stringToInteger(final String stringInteger) {

		if (stringInteger == null || stringInteger.trim().isEmpty()) {
			return null;
		}

		try {
			return Integer.parseInt(stringInteger.trim());
		} catch (Exception e) {
			return null;
		}
	}

	public static Double stringToDouble(final String stringDouble) {

		if (stringDouble == null || stringDouble.trim().isEmpty()) {
			return null;
		}

		try {
			return Double.parseDouble(stringDouble.trim());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * This Method return Trimmed String or null.
	 * 
	 * @param String
	 * @return String Trimmed String
	 */
	public static String trimString(String str) {

		if (str != null && !str.trim().isEmpty()) {
			return str.trim();
		}
		return null;
	}

	public static BigDecimal stringToBigDecimal(String stringBigDecimal) {
		if (stringBigDecimal == null || stringBigDecimal.trim().isEmpty()) {
			return null;
		}

		try {
			return new BigDecimal(stringBigDecimal.trim());
		} catch (NumberFormatException e) {
			return null; // Handle the exception as needed
		}
	}

	public static long generateUniqueLong() {
		UUID uuid = UUID.randomUUID();
		long mostSignificantBits = uuid.getMostSignificantBits();
		long leastSignificantBits = uuid.getLeastSignificantBits();

		long uniqueLong = mostSignificantBits ^ leastSignificantBits;
		uniqueLong &= Long.MAX_VALUE; // Ensure positive value

		return uniqueLong;
	}

}