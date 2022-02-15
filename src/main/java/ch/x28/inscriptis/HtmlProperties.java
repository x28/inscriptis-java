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

/**
 * Provide properties used for rendering HTML pages.
 *
 * @author Sascha Wolski
 * @author Matthias Hewelt
 * @author Manuel Schmidt
 */
public class HtmlProperties {

	/**
	 * Specify whether content will be rendered as inline, block or none.
	 * <dl>
	 * <dt>Note:</dt>
	 * <dd>A display attribute on none indicates, that the content should not be rendered at all.</dd>
	 * </dl>
	 */
	public enum Display {
		INLINE,
		BLOCK,
		NONE
	}

	/**
	 * Specify the content's horizontal alignment.
	 */
	public enum HorizontalAlignment {

		/**
		 * Left alignment of the block's content.
		 */
		LEFT,
		/**
		 * Right alignment of the block's content.
		 */
		RIGHT,
		/**
		 * Center the block's content.
		 */
		CENTER;

	}

	/**
	 * Specify the content's vertical alignment.
	 */
	public enum VerticalAlignment {

		/**
		 * Align all content at the top.
		 */
		TOP,
		/**
		 * Align all content in the middle.
		 */
		MIDDLE,
		/**
		 * Align all content at the bottom.
		 */
		BOTTOM;

	}

	/**
	 * Specify the HTML element's whitespace handling.
	 * <p>
	 * Inscriptis supports the following handling strategies outlined in the
	 * <a href="https://www.w3.org/TR/CSS1/">Cascading Style Sheets</a> specification.
	 */
	public enum WhiteSpace {

		/**
		 * Collapse multiple whitespaces into a single one.
		 */
		NORMAL(1),
		/**
		 * Collapse multiple whitespaces into a single one.
		 */
		PRE(3);

		private final int value;

		private WhiteSpace(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

	}

}
