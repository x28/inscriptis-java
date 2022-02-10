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

/**
 * Standard CSS profiles shipped with Inscriptis.
 *
 * @author Sascha Wolski
 * @author Matthias Hewelt
 * @author Manuel Schmidt
 */
public class CssProfile {

	/**
	 * This profile corresponds to the defaults used by Firefox
	 */
	public static final CssProfile STRICT;
	/**
	 * This profile is more suited for text analytics, since it ensures that whitespaces are inserted between
	 * {@code span} and {@code div} elements preventing cases where two words stick together.
	 */
	public static final CssProfile RELAXED;

	static {
		CssProfile.Builder builder = CssProfile.builder()
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

			.put("ul", e -> e.display(BLOCK).marginBefore(0).marginAfter(0).padding(4))
			.put("ol", e -> e.display(BLOCK).marginBefore(0).marginAfter(0).padding(4))
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

		STRICT = builder.build();

		builder
			.put("div", e -> e.display(BLOCK).padding(2))
			.put("span", e -> e.display(INLINE).prefix(" ").suffix(" ").limitWhitespaceAffixes(true));

		RELAXED = builder.build();
	}

	private final Map<String, HtmlElement> elements;

	public static Builder builder() {
		return new Builder();
	}

	private CssProfile() {
		elements = new HashMap<>();
	}

	public HtmlElement get(String tag) {
		return elements.get(tag);
	}

	public HtmlElement getOrDefault(String tag, HtmlElement defaultElement) {
		return elements.getOrDefault(tag, defaultElement);
	}

	public static class Builder {

		private Map<String, Setting> settings;

		private Builder() {
			settings = new HashMap<>();
		}

		public CssProfile build() {

			CssProfile profile = new CssProfile();

			for (Map.Entry<String, Setting> entry : settings.entrySet()) {
				String tag = entry.getKey();
				Setting setting = entry.getValue();

				HtmlElement element = new HtmlElement();
				setting.apply(element);

				profile.elements.put(tag, element);
			}

			return profile;
		}

		public Builder put(String tag, Setting setting) {

			settings.put(tag, setting);
			return this;
		}

	}

	@FunctionalInterface
	private interface Setting {

		void apply(HtmlElement element);

	}

}
