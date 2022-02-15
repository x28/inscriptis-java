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
package ch.x28.inscriptis.model;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.x28.inscriptis.HtmlProperties.Display;
import ch.x28.inscriptis.HtmlProperties.HorizontalAlignment;
import ch.x28.inscriptis.HtmlProperties.VerticalAlignment;
import ch.x28.inscriptis.HtmlProperties.WhiteSpace;
import ch.x28.inscriptis.StringUtils;

/**
 * Parses CSS specifications and translates them into the corresponding HtmlElements used by Inscriptis for rendering
 * HTML pages.
 *
 * @author Sascha Wolski
 * @author Matthias Hewelt
 * @author Manuel Schmidt
 */
public class CssParse {

	// used to separate value and unit from each other
	private static final Pattern RE_UNIT = Pattern.compile("(-?[0-9.]+)(\\w+)");

	// used to validate parsed size units
	private static final List<String> CSS_RELATIVE_UNITS = Arrays.asList("em", "qem", "rem");

	// used to chose over whiteSpace values
	private static final List<String> WHITE_SPACE_NORMAL = Arrays.asList("normal", "nowrap");
	private static final List<String> WHITE_SPACE_PRE = Arrays.asList("pre", "pre-line", "pre-wrap");

	/**
	 * Apply the given display value.
	 */
	static void attrDisplay(String value, HtmlElement htmlElement) {

		if (htmlElement.has(Display.NONE)) {
			return;
		}

		switch (value) {
			case "block":
				htmlElement.display(Display.BLOCK);
				break;
			case "none":
				htmlElement.display(Display.NONE);
				break;
			default:
				htmlElement.display(Display.INLINE);
		}
	}

	/**
	 * Apply the provided horizontal alignment.
	 */
	static void attrHorizontalAlign(String value, HtmlElement htmlElement) {

		try {
			htmlElement.align(HorizontalAlignment.valueOf(value.toUpperCase()));
		} catch (IllegalArgumentException e) {
			// ignore
		}
	}

	/**
	 * Apply the provided bottom margin.
	 */
	static void attrMarginBottom(String value, HtmlElement htmlElement) {
		htmlElement.marginAfter(getEm(value));
	}

	/**
	 * Apply the given top margin.
	 */
	static void attrMarginTop(String value, HtmlElement htmlElement) {
		htmlElement.marginBefore(getEm(value));
	}

	/**
	 * Apply the given left padding.
	 */
	static void attrPaddingLeft(String value, HtmlElement htmlElement) {
		htmlElement.paddingInline(getEm(value));
	}

	/**
	 * Apply the provided style attributes to the given html element.
	 *
	 * @param styleAttribute the attribute value of the given style sheet. Example: display: none
	 * @param htmlElement the HtmlElement to which the given style is applied.
	 */
	static void attrStyle(String styleAttribute, HtmlElement htmlElement) {

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
					attrDisplay(value, htmlElement);
					break;
				case "margin-top":
					attrMarginTop(value, htmlElement);
					break;
				case "margin-bottom":
					attrMarginBottom(value, htmlElement);
					break;
				case "padding-left":
					attrPaddingLeft(value, htmlElement);
					break;
				case "vertical-align":
					attrVerticalAlign(value, htmlElement);
					break;
				case "white-space":
					attrWhiteSpace(value, htmlElement);
					break;
				default:
					break;
			}
		}
	}

	/**
	 * Apply the given vertical alignment.
	 */
	static void attrVerticalAlign(String value, HtmlElement htmlElement) {

		try {
			htmlElement.valign(VerticalAlignment.valueOf(value.toUpperCase()));
		} catch (IllegalArgumentException e) {
			// ignore
		}
	}

	/**
	 * Apply the given white-space value.
	 */
	static void attrWhiteSpace(String value, HtmlElement htmlElement) {

		if (WHITE_SPACE_NORMAL.contains(value)) {
			htmlElement.whitespace(WhiteSpace.NORMAL);
		} else if (WHITE_SPACE_PRE.contains(value)) {
			htmlElement.whitespace(WhiteSpace.PRE);
		}
	}

	/**
	 * @param length the length (e.g. 2em, 2px, etc.) as specified in the CSS.
	 * @return the length in em's.
	 */
	static int getEm(String length) {

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
