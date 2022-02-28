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

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;

/**
 * @author Sascha Wolski
 * @author Matthias Hewelt
 * @author Manuel Schmidt
 */
public class InscriptisTest {

	/**
	 * Converts an HTML string to text, optionally including and deduplicating image captions, displaying link targets
	 * and using either the standard or extended indentation strategy.
	 *
	 * @param htmlContent the HTML string to be converted to text.
	 * @return The text representation of the HTML content.
	 */
	private static String getText(String htmlContent) {
		return getText(htmlContent, new ParserConfig());
	}

	/**
	 * Converts an HTML string to text, optionally including and deduplicating image captions, displaying link targets
	 * and using either the standard or extended indentation strategy.
	 *
	 * @param htmlContent the HTML string to be converted to text.
	 * @param config an optional ParserConfig object.
	 * @return The text representation of the HTML content.
	 */
	private static String getText(String htmlContent, ParserConfig config) {

		if (StringUtils.isBlank(htmlContent)) {
			return "";
		}

		htmlContent = htmlContent.trim();

		Document document = W3CDom.convert(Jsoup.parse(htmlContent));
		Inscriptis inscriptis = new Inscriptis(document, config);

		return inscriptis.getText();

	}

	@Test
	public void testBr() {

		//given
		String html = "<html><body><br>"
			+ "first</p></body></html>";

		// when
		// then
		assertThat(getText(html)).isEqualTo("\nfirst");
	}

	@Test
	public void testContent() {

		// given
		// when
		// then
		assertThat(getText("<html><body>first</body></html>")).isEqualTo("first");
	}

	@Test
	public void testDisplayAnchors() {

		// given
		String html = "<html>\n"
			+ "  <body>\n"
			+ "    <a name=\"first\">first</a>\n"
			+ "    <a href=\"second\">second</a>\n"
			+ "  </body>\n"
			+ "</html>";

		// when
		ParserConfig config = new ParserConfig();
		config.setDisplayAnchors(true);

		String text = getText(html, config);

		//then
		assertThat(text).isEqualTo("[first](first) second");
	}

	@Test
	public void testDisplayImages() {

		// given
		String html = "<html>\n"
			+ "  <body>\n"
			+ "    <img src=\"test1\" alt=\"Ein Test Bild\" title=\"Hallo\" />\n"
			+ "    <img src=\"test2\" alt=\"Ein Test Bild\" title=\"Juhu\" />\n"
			+ "    <img src=\"test3\" alt=\"Ein zweites Bild\" title=\"Echo\" />\n"
			+ "  </body>\n"
			+ "</html>";

		// when
		ParserConfig config = new ParserConfig();
		config.setDisplayImages(true);

		String text = getText(html, config);

		//then
		assertThat(text).isEqualTo("[Ein Test Bild] [Ein Test Bild] [Ein zweites Bild]");
	}

	@Test
	public void testDisplayImagesDeduplicated() {

		// given
		String html = "<html>\n"
			+ "  <body>\n"
			+ "    <img src=\"test1\" alt=\"Ein Test Bild\" title=\"Hallo\" />\n"
			+ "    <img src=\"test2\" alt=\"Ein Test Bild\" title=\"Juhu\" />\n"
			+ "    <img src=\"test3\" alt=\"Ein zweites Bild\" title=\"Echo\" />\n"
			+ "  </body>\n"
			+ "</html>";

		// when
		ParserConfig config = new ParserConfig();
		config.setDisplayImages(true);
		config.setDeduplicateCaptions(true);

		String text = getText(html, config);

		//then
		assertThat(text).isEqualTo("[Ein Test Bild] [Ein zweites Bild]");
	}

	@Test
	public void testDisplayLinks() {

		// given
		String html = "<html>\n"
			+ "  <body>\n"
			+ "    <a href=\"first\">first</a>\n"
			+ "    <a href=\"second\">second</a>\n"
			+ "    <a name=\"third\">third</a>\n"
			+ "  </body>\n"
			+ "</html>";

		// when
		ParserConfig config = new ParserConfig();
		config.setDisplayLinks(true);

		String text = getText(html, config);

		//then
		assertThat(text).isEqualTo("[first](first) [second](second) third");
	}

	@Test
	public void testDisplayLinksAndAnchors() {

		// given
		String html = "<html>\n"
			+ "  <body>\n"
			+ "    <a name=\"first\">first</a>\n"
			+ "    <a href=\"second\">second</a>\n"
			+ "    <a href=\"third\">third</a>\n"
			+ "  </body>\n"
			+ "</html>";

		// when
		ParserConfig config = new ParserConfig();
		config.setDisplayLinks(true);
		config.setDisplayAnchors(true);

		String text = getText(html, config);

		//then
		assertThat(text).isEqualTo("[first](first) [second](second) [third](third)");
	}

	@Test
	public void testDivs() {
		// given
		ParserConfig config = new ParserConfig(CssProfile.STRICT);

		// when
		// then
		assertThat(getText("<body>Thomas<div>Anton</div>Maria</body>", config)).isEqualTo("Thomas\nAnton\nMaria");
		assertThat(getText("<body>Thomas<div>Anna <b>läuft</b> weit weg.</div>", config)).isEqualTo("Thomas\nAnna läuft weit weg.");
		assertThat(getText("<body>Thomas <ul><li><div>Anton</div>Maria</ul></body>", config)).isEqualTo("Thomas\n  * Anton\n    Maria");
		assertThat(getText("<body>Thomas <ul><li>  <div>Anton</div>Maria</ul></body>", config)).isEqualTo("Thomas\n  * Anton\n    Maria");
		assertThat(getText("<body>Thomas <ul><li> a  <div>Anton</div>Maria</ul></body>", config)).isEqualTo("Thomas\n  * a\n    Anton\n    Maria");
	}

	@Test
	public void testEmptyAndCorrupt() {

		// given
		// when
		// then
		assertThat(getText("test")).isEqualTo("test");
		assertThat(getText("  ")).isEqualTo("");
		assertThat(getText("")).isEqualTo("");
		assertThat(getText("<<<")).isEqualTo("<<<"); // not equal to python version
	}

	@Test
	public void testForgottenTdCloseTagOneLine() {

		// given
		String html = ("<body>hallo<table><tr><td>1<td>2</tr></table>echo</body>");

		// when
		// then
		assertThat(getText(html)).isEqualTo("hallo\n1  2\necho");
	}

	@Test
	public void testForgottenTdCloseTagTwoLines() {

		// given
		String html = ("<body>hallo<table><tr><td>1<td>2<tr><td>3<td>4</table>echo</body>");

		// when
		// then
		assertThat(getText(html)).isEqualTo("hallo\n1  2\n3  4\necho");
	}

	@ParameterizedTest
	@MethodSource("ch.x28.inscriptis.HtmlSnippets#getTextNames")
	public void testHtmlSnippets(String name) {

		// given
		String htmlContent = HtmlSnippets.getHtmlContent(name);
		String textContent = HtmlSnippets.getTextContent(name);
		htmlContent = "<html><body>" + htmlContent + "</body></html>";
		textContent = StringUtils.stripTrailing(textContent);

		// when
		ParserConfig config = new ParserConfig(CssProfile.STRICT);
		String result = getText(htmlContent, config);

		// then
		assertThat(result).isEqualTo(textContent);
	}

	@Test
	public void testLimitWhitespaceAffixes() {

		// given
		String html = "<html>\n"
			+ "  <body>\n"
			+ "    hallo<span>echo</span>\n"
			+ "    <pre>\n"
			+ "def <span>hallo</span>():\n"
			+ "   print(\"echo\")\n"
			+ "    </pre>\n"
			+ "  </body>\n"
			+ "</html>";

		// when
		String text = getText(html);

		//then
		assertThat(text).isEqualTo("hallo echo\ndef hallo():\n   print(\"echo\")"); // not equal to python version
	}

	@Test
	public void testMarginBefore() {

		// given
		// when
		// then
		assertThat(getText("<html><body><p>first</p></body></html>")).isEqualTo("first");
	}

	@Test
	public void testMarginBeforeWithLinebreak() {

		// given
		String html = "<html><body>first<p>"
			+ "second</p></body></html>";

		// when
		// then
		assertThat(getText(html)).isEqualTo("first\nsecond");
	}

	/**
	 * Ensures that two successive <code>&lt;a&gt;text&lt;/a&gt;</code> contain a space between each other, if there is
	 * a linebreak or space between the tags.
	 */
	@Test
	public void testSuccessiveA() {

		// given
		String htmlNoNewLine = "<html><body><a href=\"first\">first</a><a href=\"second\">second</a></body></html>";
		String htmlWithNewLine = "<html><body><a href=\"first\">first</a>\n<a href=\"second\">second</a></body></html>";

		// when
		// then
		assertThat(getText(htmlNoNewLine)).isEqualTo("firstsecond");
		assertThat(getText(htmlWithNewLine)).isEqualTo("first second");
	}

	@Test
	public void testWhiteSpace() {

		// given
		ParserConfig config = new ParserConfig(CssProfile.STRICT);

		// when
		// then
		assertThat(getText("<body><span style=\"white-space: normal\"><i>1</i>2\n3</span></body>", config)).isEqualTo("12 3");
		assertThat(getText("<body><span style=\"white-space: nowrap\"><i>1</i>2\n3</span></body>", config)).isEqualTo("12 3");
		assertThat(getText("<body><span style=\"white-space: pre\"><i>1</i>2\n3</span></body>", config)).isEqualTo("12\n3");
		assertThat(getText("<body><span style=\"white-space: pre-line\"><i>1</i>2\n3</span></body>", config)).isEqualTo("12\n3");
		assertThat(getText("<body><span style=\"white-space: pre-wrap\"><i>1</i>2\n3</span></body>", config)).isEqualTo("12\n3");
	}

	/**
	 * Ensures that xml declaration are correctly stripped.
	 */
	@Test
	public void testXmlDeclaration() {

		// given
		// when
		// then
		assertThat(getText("<?xml version=\"1.0\" encoding=\"UTF-8\" ?> Hallo?>")).isEqualTo("Hallo?>");
	}
}
