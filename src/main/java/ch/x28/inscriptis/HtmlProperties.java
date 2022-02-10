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
 * @author Sascha Wolski
 * @author Matthias Hewelt
 * @author Manuel Schmidt
 */
class HtmlProperties {

	/**
	 * This enum specifies whether content will be rendered as inline, block or none (i.e. not rendered).
	 */
	public enum Display {

		INLINE(1),
		BLOCK(2),
		NONE(3);

		private final int value;

		private Display(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

	}

	/**
	 * This enum specifies the horizontal alignment.
	 */
	public enum HorizontalAlignment {

		LEFT('<'),
		RIGHT('>'),
		CENTER('^');

		private final char value;

		private HorizontalAlignment(char value) {
			this.value = value;
		}

		public char getValue() {
			return value;
		}

	}

	/**
	 * This enum specifies the vertical alignment.
	 */
	public enum VerticalAlignment {

		TOP(1),
		MIDDLE(2),
		BOTTOM(3);

		private final int value;

		private VerticalAlignment(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

	}

	/**
	 * This enum specifies the whitespace handling used for an HTML element as outlined in the Cascading Style Sheets
	 * specification.
	 *
	 * @NORMAL Sequences of whitespaces will be collapsed into a single one.
	 * @PRE Sequences of whitespaces will preserved.
	 */
	public enum WhiteSpace {

		NORMAL(1),
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
