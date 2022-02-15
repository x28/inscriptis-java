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

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import ch.x28.inscriptis.HtmlProperties.Display;
import ch.x28.inscriptis.HtmlProperties.WhiteSpace;

/**
 * @author Sascha Wolski
 * @author Matthias Hewelt
 * @author Manuel Schmidt
 */
public class HtmlElementTest {

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
			+ "paddingInline=0, "
			+ "listBullet=, "
			+ "limitWhitespaceAffixes=false, "
			+ "align=LEFT, "
			+ "valign=MIDDLE]");
	}

	@Nested
	public class RefinementTest {

		@Nested
		public class NonWhitespaceTest {

			@Test
			public void testCode() {

				// given
				HtmlElement code = new HtmlElement()
					.tag("code");

				HtmlElement span = new HtmlElement()
					.tag("span")
					.display(Display.INLINE)
					.prefix(" 1. ")
					.suffix("<")
					.limitWhitespaceAffixes(true);

				// when
				HtmlElement refinedSpan = span.clone();
				code.getRefinedHtmlElement(refinedSpan);

				// then
				assertThat(refinedSpan.getPrefix()).isEqualTo(" 1. ");
				assertThat(refinedSpan.getSuffix()).isEqualTo("<");
			}

			@Test
			public void testPre() {

				// given
				HtmlElement pre = new HtmlElement()
					.tag("pre")
					.display(Display.BLOCK)
					.whitespace(WhiteSpace.PRE);

				HtmlElement span = new HtmlElement()
					.tag("span")
					.display(Display.INLINE)
					.prefix(" 1. ")
					.suffix("<")
					.limitWhitespaceAffixes(true);

				// when
				HtmlElement refinedSpan = span.clone();
				pre.getRefinedHtmlElement(refinedSpan);

				// thne
				assertThat(refinedSpan.getPrefix()).isEqualTo(" 1. ");
				assertThat(refinedSpan.getSuffix()).isEqualTo("<");
			}

		}

		@Nested
		public class WhitespaceTest {

			@Test
			public void testCode() {

				// given
				HtmlElement code = new HtmlElement()
					.tag("code");

				HtmlElement span = new HtmlElement()
					.tag("span")
					.display(Display.INLINE)
					.prefix(" ")
					.suffix(" ")
					.limitWhitespaceAffixes(true);

				// when
				HtmlElement refinedSpan = span.clone();
				code.getRefinedHtmlElement(refinedSpan);

				// thne
				assertThat(refinedSpan.getPrefix()).isEqualTo(" ");
				assertThat(refinedSpan.getSuffix()).isEqualTo(" ");
			}

			@Test
			public void testPre() {

				// given
				HtmlElement pre = new HtmlElement()
					.tag("pre")
					.display(Display.BLOCK)
					.whitespace(WhiteSpace.PRE);

				HtmlElement span = new HtmlElement()
					.tag("span")
					.display(Display.INLINE)
					.prefix(" ")
					.suffix(" ")
					.limitWhitespaceAffixes(true);

				// when
				HtmlElement refinedSpan = span.clone();
				pre.getRefinedHtmlElement(refinedSpan);

				// then
				assertThat(refinedSpan.getPrefix()).isEqualTo("");
				assertThat(refinedSpan.getSuffix()).isEqualTo("");
			}

		}

	}

}
