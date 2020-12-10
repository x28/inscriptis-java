package ch.x28.inscriptis;

import java.util.ArrayList;
import java.util.List;

class StringUtils {

	/**
	 * Check if a string is null, has length zero or consists of whitespace characters only.
	 *
	 * @param str the string to be checked
	 * @return true if str is null or of length zero or consists of whitespace characters only.
	 */
	public static boolean isBlank(String str) {

		if (str == null || str.length() == 0) {
			return true;
		}

		for (char c : str.toCharArray()) {
			if (!Character.isWhitespace(c)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Adds as many spaces equally distributed to the front and end of the string as needed to increase its length to
	 * the provided target length. If the amount of spaces that will be added is an odd number, the left over space will
	 * be added to the end of the string.
	 *
	 * @param str the string to be padded.
	 * @param targetLength the final length of the string.
	 * @return a string surrounded with as many space characters as required. Will return null if the providede string
	 *         is null. Will return the original string, if the providede target length isn't greater then the original
	 *         strings length.
	 */
	public static String padCenter(String str, int targetLength) {

		if (str == null) {
			return null;
		}

		int spacesNeeded = targetLength - str.length();
		if (spacesNeeded <= 0) {
			return str;
		}

		int frontSpaces = spacesNeeded / 2;
		int endSpaces = spacesNeeded - frontSpaces;

		return repeat(" ", frontSpaces).concat(str).concat(repeat(" ", endSpaces));
	}

	/**
	 * Adds as many spaces in front of the string as needed to increase its length to the provided target length.
	 *
	 * @param str the string to be padded.
	 * @param targetLength the final length of the string.
	 * @return a string prefixed with as many space characters as required. Will return null if the providede string is
	 *         null. Will return the original string, if the providede target length isn't greater then the original
	 *         strings length.
	 */
	public static String padLeft(String str, int targetLength) {

		if (str == null) {
			return null;
		}

		int spacesNeeded = targetLength - str.length();
		if (spacesNeeded <= 0) {
			return str;
		}

		return repeat(" ", spacesNeeded).concat(str);
	}

	/**
	 * Adds as many spaces to the end of the string as needed to increase its length to the provided target length.
	 *
	 * @param str the string to be padded.
	 * @param targetLength the final length of the string.
	 * @return a string suffixed with as many space characters as required. Will return null if the providede string is
	 *         null. Will return the original string, if the providede target length isn't greater then the original
	 *         strings length.
	 */
	public static String padRight(String str, int targetLength) {

		if (str == null) {
			return null;
		}

		int spacesNeeded = targetLength - str.length();
		if (spacesNeeded <= 0) {
			return str;
		}

		return str.concat(repeat(" ", spacesNeeded));
	}

	/**
	 * Repeat a string several times.
	 *
	 * @param str the string value to be repeated several times.
	 * @param repetitions how many times the string should be repeated.
	 * @return A new string consisting of <code>repetitions</code> times the value of <code>str</code>. Returns null if
	 *         the provided string is null. Returns an empty string if the provided string is empty of repetitions is <
	 *         1.
	 */
	public static String repeat(String str, int repetitions) {

		if (str == null) {
			return null;
		}

		if (str.length() < 1 || repetitions < 1) {
			return "";
		}

		StringBuffer result = new StringBuffer();
		for (int i = 0; i < repetitions; i++) {
			result.append(str);
		}

		return result.toString();
	}

	/**
	 * Split a string by a separator char for a specified amount of times. <br>
	 * Example:
	 * <p>
	 * <code>
	 *   split("hello:world", ':', 1) // [["hello"],["world"]]<br>
	 *   split("hello:world", ':', 2) // [["hello"],["world"]]<br>
	 *   split(":helloworld", ':', 1) // [[""],["helloworld"]]<br>
	 *   split(":hello:world", ':', 1) // [[""],["hello:world"]]<br>
	 *   split(":hello:world", ':', 2) // [[""],["hello"],[":world"]]<br>
	 *   split("helloworld:", ':', 1) // [["helloworld"],[""]]<br>
	 * </code>
	 * </p>
	 *
	 * @param str the string to be split.
	 * @param c the separator char.
	 * @param maxSplits the max amount of splits being performed.
	 * @return a string array containing the splits. Returns null if the provided string is null. Returns a string array
	 *         with the original string as element when the separator is not found or the number of max splits is lower
	 *         than 1.
	 */
	public static String[] split(String str, char separator, int maxSplits) {

		if (str == null) {
			return null;
		}

		int separatorIndex = 0;
		int splits = 0;
		String remaining = str;

		List<String> result = new ArrayList<>();

		while (splits < maxSplits && remaining.length() > 0) {

			separatorIndex = remaining.indexOf(separator);
			if (separatorIndex == -1) {
				break;
			}

			result.add(remaining.substring(0, separatorIndex));

			separatorIndex++;

			if (separatorIndex < remaining.length()) {
				remaining = remaining.substring(separatorIndex);
			} else {
				remaining = "";
			}
		}

		result.add(remaining.substring(0));

		return result.toArray(new String[0]);
	}

	/**
	 * Removes any trailing whitespace characters.
	 *
	 * @param str the string to be processed.
	 * @return the string without any trailing whitespace characters.
	 */
	public static String stripTrailing(String str) {

		if (str == null) {
			return null;
		}

		int index = str.length() - 1;

		boolean isWhitespaceCharacter = true;
		while (index >= 0 && isWhitespaceCharacter) {
			isWhitespaceCharacter = Character.isWhitespace(str.charAt(index));

			if (isWhitespaceCharacter) {
				index--;
			}
		}

		return str.substring(0, index + 1);
	}
}
