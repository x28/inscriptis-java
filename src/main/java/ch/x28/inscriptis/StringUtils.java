/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.x28.inscriptis;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sascha Wolski
 * @author Matthias Hewelt
 * @author Manuel Schmidt
 */
public class StringUtils {

	/**
	 * Append a string several times to the specified string builder.
	 * <p>
	 * If the provided string is empty or {@code repetitions} is less than 1 nothing is appended to the string builder.
	 *
	 * @param builder the string builder to which the string is repeatedly appended
	 * @param str the string value to be appended several times.
	 * @param repetitions how many times the string should be appended.
	 */
	public static void append(StringBuilder builder, String str, int repetitions) {

		if (str != null && !str.isEmpty() && repetitions > 0) {
			for (int i = 0; i < repetitions; i++) {
				builder.append(str);
			}
		}
	}

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
	 * Check if a string is null or has length zero.
	 *
	 * @param str the string to be checked
	 * @return true if str is null or of length zero.
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * Add as many spaces equally distributed to the front and end of the string as needed to increase its length to the
	 * provided target length. If the amount of spaces that will be added is an odd number, the left over space will be
	 * added to the end of the string.
	 *
	 * @param str the string to be padded.
	 * @param targetLength the final length of the string.
	 * @return a string surrounded with as many space characters as required. Will return null if the provided string is
	 *         null. Will return the original string, if the provided target length isn't greater then the original
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
		StringBuilder builder = new StringBuilder();
		append(builder, " ", frontSpaces);
		builder.append(str);
		append(builder, " ", endSpaces);
		return builder.toString();
	}

	/**
	 * Add as many spaces in front of the string as needed to increase its length to the provided target length.
	 *
	 * @param str the string to be padded.
	 * @param targetLength the final length of the string.
	 * @return a string prefixed with as many space characters as required. Will return null if the provided string is
	 *         null. Will return the original string, if the provided target length isn't greater then the original
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
		StringBuilder builder = new StringBuilder();
		append(builder, " ", spacesNeeded);
		builder.append(str);
		return builder.toString();
	}

	/**
	 * Add as many spaces to the end of the string as needed to increase its length to the provided target length.
	 *
	 * @param str the string to be padded.
	 * @param targetLength the final length of the string.
	 * @return a string suffixed with as many space characters as required. Will return null if the provided string is
	 *         null. Will return the original string, if the provided target length isn't greater then the original
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
		StringBuilder builder = new StringBuilder(str);
		append(builder, " ", spacesNeeded);
		return builder.toString();
	}

	/**
	 * Repeat a string several times.
	 *
	 * @param str the string value to be repeated several times.
	 * @param repetitions how many times the string should be repeated.
	 * @return A new string consisting of {@code repetitions} times the value of {@code str<}. Return null if the
	 *         provided string is null. Return an empty string if the provided string is empty or repetitions is less
	 *         than 1.
	 */
	public static String repeat(String str, int repetitions) {

		if (str == null) {
			return null;
		}
		if (str.isEmpty() || repetitions < 1) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		append(builder, str, repetitions);
		return builder.toString();
	}

	/**
	 * Split a string by a separator char. <br>
	 * Example:
	 * <p>
	 * <code>
	 *   split("hello:world", ':') // [["hello"],["world"]]<br>
	 * </code>
	 * </p>
	 *
	 * @param str the string to be split.
	 * @param separator the separator char.
	 * @return a string array containing the splits. Return null if the provided string is null. Return a string array
	 *         with the original string as element when the separator is not found.
	 */
	public static String[] split(String str, char separator) {

		if (str == null) {
			return null;
		}
		int index = 0;
		List<String> result = new ArrayList<>();
		while (str.length() > 0) {
			index = str.indexOf(separator);
			if (index == -1) {
				break;
			}
			result.add(str.substring(0, index));
			index++;
			str = index < str.length() ? str.substring(index) : "";
		}
		result.add(str.substring(0));
		return result.toArray(new String[0]);
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
	 * @param separator the separator char.
	 * @param maxSplits the max amount of splits being performed.
	 * @return a string array containing the splits. Return null if the provided string is null. Return a string array
	 *         with the original string as element when the separator is not found or the number of max splits is lower
	 *         than 1.
	 */
	public static String[] split(String str, char separator, int maxSplits) {

		if (str == null) {
			return null;
		}
		int index = 0;
		int splits = 0;
		List<String> result = new ArrayList<>();
		while (splits < maxSplits && str.length() > 0) {
			index = str.indexOf(separator);
			if (index == -1) {
				break;
			}
			result.add(str.substring(0, index));
			index++;
			str = index < str.length() ? str.substring(index) : "";
		}
		result.add(str.substring(0));
		return result.toArray(new String[0]);
	}

	/**
	 * Remove any trailing whitespace characters.
	 *
	 * @param str the string to be processed.
	 * @return the string without any trailing whitespace characters.
	 */
	public static String stripTrailing(String str) {

		if (str == null) {
			return null;
		}
		int index = str.length();
		while (index != 0 && Character.isWhitespace(str.charAt(index - 1))) {
			index--;
		}
		return str.substring(0, index);
	}

	/**
	 * Remove the given trailing character.
	 *
	 * @param str the string to be processed.
	 * @param stripChar the trailing char to strip
	 * @return the string without the trailing character.
	 */
	public static String stripTrailing(String str, char stripChar) {

		if (str == null) {
			return null;
		}
		int index = str.length();
		while (index != 0 && str.charAt(index - 1) == stripChar) {
			index--;
		}
		return str.substring(0, index);
	}

}
