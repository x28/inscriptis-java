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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Handle HTML attributes such as `align`, and `valign`.
 * <p>
 * This class handles HTML attributes by mapping them to the corresponding methods in the {@link CssParse} class.
 *
 * @author Manuel Schmidt
 */
public class Attribute {

	private static final Map<String, BiConsumer<String, HtmlElement>> HTML_ATTRIBUTE_MAPPING;

	static {
		Map<String, BiConsumer<String, HtmlElement>> map = new HashMap<>();
		map.put("style", CssParse::attrStyle);
		map.put("align", CssParse::attrHorizontalAlign);
		map.put("valign", CssParse::attrVerticalAlign);

		HTML_ATTRIBUTE_MAPPING = Collections.unmodifiableMap(map);
	}

	/**
	 * Apply the attributes to the given HTML element.
	 *
	 * @param attributes the list of attributes
	 * @param htmlElement the HTML element for which the attributes are parsed
	 * @return the HTML element
	 */
	public static HtmlElement applyAttributes(NamedNodeMap attributes, HtmlElement htmlElement) {

		for (int index = 0; index < attributes.getLength(); index++) {
			Node attribute = attributes.item(index);
			BiConsumer<String, HtmlElement> consumer = HTML_ATTRIBUTE_MAPPING.get(attribute.getNodeName());
			if (consumer != null) {
				consumer.accept(attribute.getNodeValue(), htmlElement);
			}
		}

		return htmlElement;
	}

}
