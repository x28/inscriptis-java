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

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.x28.inscriptis.HtmlProperties.Display;
import ch.x28.inscriptis.HtmlProperties.WhiteSpace;

/**
 * Parses CSS specifications and translates them into the corresponding HtmlElements used by Inscriptis for rendering
 * HTML pages.
 *
 * @author Sascha Wolski
 * @author Matthias Hewelt
 */
class CssParse {

	// used to separate value and unit from each other
	private static Pattern RE_UNIT = Pattern.compile("([\\-0-9\\.]+)(\\w+)");

	// used to validate parsed size units
	private static List<String> CSS_RELATIVE_UNITS = Arrays.asList("em", "qem", "rem");

	// used to chose over whiteSpace values
	private static List<String> WHITE_SPACE_NORMAL = Arrays.asList("normal", "nowrap");
	private static List<String> WHITE_SPACE_PRE = Arrays.asList("pre", "pre-line", "pre-wrap");

	/**
	 * @param styleAttribute the attribute value of the given style sheet. Example: display: none
	 * @param htmlElement the HtmlElement to which the given style is applied.
	 *
	 * @return An HtmlElement that merges the given element with the style attributes specified.
	 */
	public static HtmlElement getStyleAttribute(String styleAttribute, HtmlElement htmlElement) {

		HtmlElement customHtmlElement = htmlElement.clone();

		for (String styleDirective : styleAttribute.toLowerCase().split(";")) {
			if (!styleDirective.contains(":")) {
				continue;
			}

			String[] keyValuePair = StringUtils.split(styleDirective, ':', 1);
			if (keyValuePair.length < 2) {
				continue;
			}

			String key = keyValuePair[0].trim();
			String value = keyValuePair[1].trim();

			String fieldName = key.replace("-webkit-", "");

			switch (fieldName) {
				case "display":
					attributeDisplay(value, customHtmlElement);
					break;
				case "margin-top":
					attributeMarginTop(value, customHtmlElement);
					break;
				case "margin-bottom":
					attributeMarginBottom(value, customHtmlElement);
					break;
				case "padding-left":
					attributePaddingLeft(value, customHtmlElement);
					break;
				case "white-space":
					attributeWhiteSpace(value, customHtmlElement);
					break;
				default:
					break;
			}
		}

		return customHtmlElement;
	}

	/**
	 * Set the display value.
	 */
	private static void attributeDisplay(String value, HtmlElement htmlElement) {

		if (htmlElement.getDisplay() == Display.NONE)
			return;

		switch (value) {
			case "block":
				htmlElement.setDisplay(Display.BLOCK);
				break;
			case "none":
				htmlElement.setDisplay(Display.NONE);
				break;
			default:
				htmlElement.setDisplay(Display.INLINE);
		}
	}

	/**
	 * Sets the bottom margin for the given HTML element.
	 */
	private static void attributeMarginBottom(String value, HtmlElement htmlElement) {
		htmlElement.setMarginAfter(getEm(value));
	}

	/**
	 * Sets the top margin for the given HTML element.
	 */
	private static void attributeMarginTop(String value, HtmlElement htmlElement) {
		htmlElement.setMarginBefore(getEm(value));
	}

	/**
	 * Sets the left padding for the given HTML element.
	 */
	private static void attributePaddingLeft(String value, HtmlElement htmlElement) {
		htmlElement.setPadding(getEm(value));
	}

	/**
	 * Set the white-space value.
	 */
	private static void attributeWhiteSpace(String value, HtmlElement htmlElement) {

		if (WHITE_SPACE_NORMAL.contains(value)) {
			htmlElement.setWhitespace(WhiteSpace.NORMAL);
		} else if (WHITE_SPACE_PRE.contains(value)) {
			htmlElement.setWhitespace(WhiteSpace.PRE);
		}
	}

	/**
	 * @param length the length (e.g. 2em, 2px, etc.) as specified in the CSS.
	 * @return the length in em's.
	 */
	private static int getEm(String length) {

		Matcher matcher = RE_UNIT.matcher(length);

		if (matcher.find()) {
			float value = Float.parseFloat(matcher.group(1));
			String unit = matcher.group(2);

			if (!CSS_RELATIVE_UNITS.contains(unit)) {
				return Math.round(value / 8);
			}

			return Math.round(value);
		}

		return 0;
	}
}
