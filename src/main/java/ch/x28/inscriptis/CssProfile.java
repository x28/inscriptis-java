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

import static ch.x28.inscriptis.HtmlProperties.Display.*;
import static ch.x28.inscriptis.HtmlProperties.WhiteSpace.*;

import java.util.HashMap;
import java.util.Map;

import ch.x28.inscriptis.model.HtmlElement;

/**
 * Standard CSS profiles shipped with Inscriptis.
 *
 * @author Sascha Wolski
 * @author Matthias Hewelt
 * @author Manuel Schmidt
 */
public class CssProfile {

	private final Map<String, Setting> settings;
	private Map<String, HtmlElement> elements;

	/**
	 * This profile is more suited for text analytics, since it ensures that whitespaces are inserted between
	 * {@code span} and {@code div} elements preventing cases where two words stick together.
	 *
	 * @return the relaxed profile
	 */
	public static CssProfile relaxed() {

		return strict()
			.put("div", e -> e.display(BLOCK).paddingInline(2))
			.put("span", e -> e.display(INLINE).prefix(" ").suffix(" ").limitWhitespaceAffixes(true));
	}

	/**
	 * This profile corresponds to the defaults used by Firefox.
	 *
	 * @return the strict profile
	 */
	public static CssProfile strict() {

		return new CssProfile()
			.put("body", e -> e.display(INLINE).whitespace(NORMAL))

			.put("head", e -> e.display(NONE))
			.put("link", e -> e.display(NONE))
			.put("meta", e -> e.display(NONE))
			.put("script", e -> e.display(NONE))
			.put("title", e -> e.display(NONE))
			.put("style", e -> e.display(NONE))

			.put("p", e -> e.display(BLOCK).marginBefore(1).marginAfter(1))
			.put("figure", e -> e.display(BLOCK).marginBefore(1).marginAfter(1))

			.put("h1", e -> e.display(BLOCK).marginBefore(1).marginAfter(1))
			.put("h2", e -> e.display(BLOCK).marginBefore(1).marginAfter(1))
			.put("h3", e -> e.display(BLOCK).marginBefore(1).marginAfter(1))
			.put("h4", e -> e.display(BLOCK).marginBefore(1).marginAfter(1))
			.put("h5", e -> e.display(BLOCK).marginBefore(1).marginAfter(1))
			.put("h6", e -> e.display(BLOCK).marginBefore(1).marginAfter(1))

			.put("ul", e -> e.display(BLOCK).marginBefore(0).marginAfter(0).paddingInline(4))
			.put("ol", e -> e.display(BLOCK).marginBefore(0).marginAfter(0).paddingInline(4))
			.put("li", e -> e.display(BLOCK))

			.put("address", e -> e.display(BLOCK))
			.put("article", e -> e.display(BLOCK))
			.put("aside", e -> e.display(BLOCK))
			.put("div", e -> e.display(BLOCK))
			.put("footer", e -> e.display(BLOCK))
			.put("header", e -> e.display(BLOCK))
			.put("hgroup", e -> e.display(BLOCK))
			.put("layer", e -> e.display(BLOCK))
			.put("main", e -> e.display(BLOCK))
			.put("nav", e -> e.display(BLOCK))
			.put("figcaption", e -> e.display(BLOCK))

			.put("blockquote", e -> e.display(BLOCK))

			.put("q", e -> e.prefix("\"").suffix("\""))

			// Handling of <pre>
			.put("pre", e -> e.display(BLOCK).whitespace(PRE))
			.put("xmp", e -> e.display(BLOCK).whitespace(PRE))
			.put("listing", e -> e.display(BLOCK).whitespace(PRE))
			.put("plaintext", e -> e.display(BLOCK).whitespace(PRE));
	}

	public CssProfile() {
		this(new HashMap<>());
	}

	private CssProfile(Map<String, Setting> settings) {
		this.settings = settings;
	}

	public HtmlElement get(String tag) {

		if (elements == null) {
			refresh();
		}
		return elements.get(tag);
	}

	public HtmlElement getOrDefault(String tag, HtmlElement defaultElement) {

		if (elements == null) {
			refresh();
		}
		return elements.getOrDefault(tag, defaultElement);
	}

	public CssProfile put(String tag, Setting setting) {

		settings.put(tag, setting);

		// reset elements since settings have changed
		elements = null;

		return this;
	}

	private void refresh() {

		elements = new HashMap<>();

		for (Map.Entry<String, Setting> entry : settings.entrySet()) {
			String tag = entry.getKey();
			Setting setting = entry.getValue();

			HtmlElement element = new HtmlElement();
			setting.apply(element);

			elements.put(tag, element);
		}
	}

	@FunctionalInterface
	private interface Setting {

		void apply(HtmlElement element);

	}

}
