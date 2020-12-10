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

import java.util.HashMap;
import java.util.Map;

import ch.x28.inscriptis.HtmlProperties.Display;
import ch.x28.inscriptis.HtmlProperties.WhiteSpace;

/**
 * Standard CSS profiles shipped with Inscriptis.
 *
 * @author Sascha Wolski
 * @author Matthias Hewelt
 */
public class CssProfile {

	/**
	 * This profile corresponds to the defaults used by Firefox
	 */
	public static CssProfile STRICT;
	/**
	 * This profile is more suited for text analytics, since it ensures that whitespaces are inserted between
	 * {@code span} and {@code div} elements preventing cases where two words stick together.
	 */
	public static CssProfile RELAXED;

	static {
		Map<String, HtmlElement> strict = new HashMap<>();
		strict.put("body", new HtmlElement("body", Display.INLINE, WhiteSpace.NORMAL));
		strict.put("head", new HtmlElement("head", Display.NONE));
		strict.put("link", new HtmlElement("link", Display.NONE));
		strict.put("meta", new HtmlElement("meta", Display.NONE));
		strict.put("script", new HtmlElement("script", Display.NONE));
		strict.put("title", new HtmlElement("title", Display.NONE));
		strict.put("style", new HtmlElement("style", Display.NONE));

		strict.put("p", new HtmlElement("p", Display.BLOCK, 1, 1));
		strict.put("figure", new HtmlElement("figure", Display.BLOCK, 1, 1));
		strict.put("h1", new HtmlElement("h1", Display.BLOCK, 1, 1));
		strict.put("h2", new HtmlElement("h2", Display.BLOCK, 1, 1));
		strict.put("h3", new HtmlElement("h3", Display.BLOCK, 1, 1));
		strict.put("h4", new HtmlElement("h4", Display.BLOCK, 1, 1));
		strict.put("h5", new HtmlElement("h5", Display.BLOCK, 1, 1));
		strict.put("h6", new HtmlElement("h6", Display.BLOCK, 1, 1));

		strict.put("ul", new HtmlElement("ul", Display.BLOCK, 0, 0, 4));
		strict.put("ol", new HtmlElement("ol", Display.BLOCK, 0, 0, 4));
		strict.put("li", new HtmlElement("li", Display.BLOCK));

		strict.put("address", new HtmlElement("address", Display.BLOCK));
		strict.put("article", new HtmlElement("article", Display.BLOCK));
		strict.put("aside", new HtmlElement("aside", Display.BLOCK));
		strict.put("div", new HtmlElement("div", Display.BLOCK));
		strict.put("footer", new HtmlElement("footer", Display.BLOCK));
		strict.put("header", new HtmlElement("header", Display.BLOCK));
		strict.put("hgroup", new HtmlElement("hgroup", Display.BLOCK));
		strict.put("layer", new HtmlElement("layer", Display.BLOCK));
		strict.put("main", new HtmlElement("main", Display.BLOCK));
		strict.put("nav", new HtmlElement("nav", Display.BLOCK));
		strict.put("figcaption", new HtmlElement("figcaption", Display.BLOCK));
		strict.put("blockquote", new HtmlElement("blockquote", Display.BLOCK));

		strict.put("q", new HtmlElement("q", "\"", "\""));

		// Handling of <pre>
		strict.put("pre", new HtmlElement("pre", Display.BLOCK, WhiteSpace.PRE));
		strict.put("xmp", new HtmlElement("xmp", Display.BLOCK, WhiteSpace.PRE));
		strict.put("listing", new HtmlElement("listing", Display.BLOCK, WhiteSpace.PRE));
		strict.put("plaintext", new HtmlElement("plaintext", Display.BLOCK, WhiteSpace.PRE));

		Map<String, HtmlElement> relaxed = new HashMap<>(strict);
		relaxed.put("div", new HtmlElement("div", Display.BLOCK, 2));
		relaxed.put("span", new HtmlElement("span", Display.INLINE, " ", " ", true));

		STRICT = new CssProfile(strict);
		RELAXED = new CssProfile(relaxed);
	}

	private Map<String, HtmlElement> settings;

	private CssProfile(Map<String, HtmlElement> settings) {
		this.settings = settings;
	}

	public HtmlElement get(String tag) {
		return settings.get(tag);
	}

	public HtmlElement getOrDefault(String tag, HtmlElement defaultElement) {

		HtmlElement htmlElement = settings.get(tag);
		if (htmlElement != null) {
			return htmlElement;
		}

		return defaultElement;
	}
}
