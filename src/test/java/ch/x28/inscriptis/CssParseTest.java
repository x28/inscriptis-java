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

/**
 * @author Sascha Wolski
 * @author Matthias Hewelt
 */
public class CssParseTest {

	@Test
	public void testDisplayBlockWithPadding() {

		// given
		CssProfile css = CssProfile.STRICT;

		// when
		HtmlElement htmlElement = CssParse.getStyleAttribute("padding-left: 8px; display: block", css.get("div"));
		// then
		assertThat(htmlElement.getPadding()).isEqualTo(1);
		assertThat(htmlElement.getDisplay()).isEqualTo(Display.BLOCK);
	}

	@Test
	public void testDisplayInlineWithMarginBefore() {

		// given
		CssProfile css = CssProfile.STRICT;

		// when
		HtmlElement htmlElement = CssParse.getStyleAttribute("margin-top: 8em; display: inline", css.get("div"));

		// then
		assertThat(htmlElement.getMarginBefore()).isEqualTo(8);
		assertThat(htmlElement.getDisplay()).isEqualTo(Display.INLINE);
	}

	@Test
	public void testStyleUnitParsing() {

		// given
		// when
		HtmlElement htmlElement = CssParse.getStyleAttribute("margin-top:2.666666667em;margin-bottom: 2.666666667em", new HtmlElement());

		// then
		assertThat(htmlElement.getMarginBefore()).isEqualTo(3);
		assertThat(htmlElement.getMarginAfter()).isEqualTo(3);
	}

}
