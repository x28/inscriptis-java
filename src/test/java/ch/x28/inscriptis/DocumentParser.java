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

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Manuel Schmidt
 */
public class DocumentParser {

	private static final boolean DEBUG = false;
	private static final boolean PRESERVE_LINE_BREAKS = true;

	public static Document parse(String html) {

		if (PRESERVE_LINE_BREAKS) {
			html = LineBreakEscaper.escape(html);
		}

		W3CDom dom = new W3CDom();
		Document document = dom.fromJsoup(Jsoup.parse(html));

		if (PRESERVE_LINE_BREAKS) {
			html = LineBreakEscaper.unescape(html);
			LineBreakEscaper.unescape(document);
		}

		if (DEBUG) {
			System.out.println(dom.asString(document));
		}

		return document;
	}

	/**
	 * Helper to escape and unescape line breaks since jsoup does not preserve all of them.
	 */
	static class LineBreakEscaper {

		private static final String UNESCAPED = "\n";
		private static final String ESCAPED = "\u0000\n";

		public static String escape(String unescaped) {
			return unescaped.replace(UNESCAPED, ESCAPED);
		}

		public static void unescape(Node node) {

			if (node.getNodeType() == Node.TEXT_NODE) {
				node.setNodeValue(unescape(node.getNodeValue()));
			}

			NodeList children = node.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				unescape(children.item(i));
			}
		}

		public static String unescape(String escaped) {
			return escaped.replace(ESCAPED, UNESCAPED);
		}

	}

}
