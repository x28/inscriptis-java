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
import ch.x28.inscriptis.HtmlProperties.WhiteSpace;

/**
 * @author Sascha Wolski
 * @author Matthias Hewelt
 * @author Manuel Schmidt
 */
public class HtmlElementTest {

	@Test
	public void testRefinement() {

		HtmlElement span = new HtmlElement()
			.tag("span")
			.display(Display.INLINE)
			.prefix(" ")
			.suffix(" ")
			.limitWhitespaceAffixes(true);

		HtmlElement pre = new HtmlElement()
			.tag("pre")
			.display(Display.BLOCK)
			.whitespace(WhiteSpace.PRE);

		HtmlElement code = new HtmlElement()
			.tag("code");

		// refinement with pre and whitespaces
		HtmlElement refined = pre.getRefinedHtmlElement(span);
		assertThat(refined.getPrefix()).isEqualTo("");
		assertThat(refined.getSuffix()).isEqualTo("");

		// refinement with code and whitespaces
		refined = code.getRefinedHtmlElement(span);
		assertThat(refined.getPrefix()).isEqualTo(" ");
		assertThat(refined.getSuffix()).isEqualTo(" ");

		// refinement with pre and non-whitespaces
		span.prefix(" 1. ");
		span.suffix("<");
		refined = pre.getRefinedHtmlElement(span);
		assertThat(refined.getPrefix()).isEqualTo(" 1. ");
		assertThat(refined.getSuffix()).isEqualTo("<");

		// refinement with code and non-whitespaces
		refined = code.getRefinedHtmlElement(span);
		assertThat(refined.getPrefix()).isEqualTo(" 1. ");
		assertThat(refined.getSuffix()).isEqualTo("<");
	}

	/**
	 * Tests the string representation of an HtmlElement.
	 */
	@Test
	public void testToString() {

		// given
		HtmlElement htmlElement = new HtmlElement()
			.tag("div")
			.display(Display.INLINE)
			.whitespace(WhiteSpace.PRE);

		// when
		String string = htmlElement.toString();

		// then
		assertThat(string).isEqualTo("HtmlElement ["
			+ "tag=div, "
			+ "display=INLINE, "
			+ "whitespace=PRE, "
			+ "prefix=, "
			+ "suffix=, "
			+ "marginBefore=0, "
			+ "marginAfter=0, "
			+ "padding=0, "
			+ "limitWhitespaceAffixes=false, "
			+ "align=LEFT, "
			+ "valign=MIDDLE]");
	}

}
