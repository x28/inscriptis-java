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

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ch.x28.inscriptis.HtmlProperties.Display;
import ch.x28.inscriptis.HtmlProperties.HorizontalAlignment;
import ch.x28.inscriptis.HtmlProperties.VerticalAlignment;

/**
 * @author Sascha Wolski
 * @author Matthias Hewelt
 * @author Manuel Schmidt
 */
public class CssParseTest {

	@Test
	public void testDisplayBlockWithPadding() {

		// given
		CssProfile css = CssProfile.STRICT;
		HtmlElement htmlElement = css.get("div");

		// when
		CssParse.attrStyle("padding-left: 8px; display: block", htmlElement);

		// then
		assertThat(htmlElement.getPadding()).isEqualTo(1);
		assertThat(htmlElement.getDisplay()).isEqualTo(Display.BLOCK);
	}

	@Test
	public void testDisplayInlineWithMarginBefore() {

		// given
		CssProfile css = CssProfile.STRICT;
		HtmlElement htmlElement = css.get("div");

		// when
		CssParse.attrStyle("margin-top: 8em; display: inline", htmlElement);

		// then
		assertThat(htmlElement.getMarginBefore()).isEqualTo(8);
		assertThat(htmlElement.getDisplay()).isEqualTo(Display.INLINE);
	}

	@Test
	public void testParseHorizontalAlign() {

		// given
		HtmlElement htmlElement = new HtmlElement();
		String value = "center";

		// when
		CssParse.attrHorizontalAlign(value, htmlElement);

		// then
		assertThat(htmlElement.getAlign()).isEqualTo(HorizontalAlignment.CENTER);
	}

	@Test
	public void testParseInvalidHorizontalAlign() {

		// given
		HtmlElement htmlElement = new HtmlElement();
		htmlElement.align(HorizontalAlignment.CENTER);
		String invalidValue = "unknown";

		// when
		CssParse.attrHorizontalAlign(invalidValue, htmlElement);

		// then
		assertThat(htmlElement.getAlign()).isEqualTo(HorizontalAlignment.CENTER);
	}

	@Test
	public void testParseInvalidVerticalAlign() {

		// given
		HtmlElement htmlElement = new HtmlElement();
		htmlElement.valign(VerticalAlignment.TOP);
		String invalidValue = "unknown";

		// when
		CssParse.attrVerticalAlign(invalidValue, htmlElement);

		// then
		assertThat(htmlElement.getValign()).isEqualTo(VerticalAlignment.TOP);
	}

	@Test
	public void testParseVerticalAlign() {

		// given
		HtmlElement htmlElement = new HtmlElement();
		String value = "top";

		// when
		CssParse.attrVerticalAlign(value, htmlElement);

		// then
		assertThat(htmlElement.getValign()).isEqualTo(VerticalAlignment.TOP);
	}

	@Test
	public void testStyleUnitParsing() {

		// given
		HtmlElement htmlElement = new HtmlElement();

		// when
		CssParse.attrStyle("margin-top:2.666666667em;margin-bottom: 2.666666667em", htmlElement);

		// then
		assertThat(htmlElement.getMarginBefore()).isEqualTo(3);
		assertThat(htmlElement.getMarginAfter()).isEqualTo(3);
	}

}
