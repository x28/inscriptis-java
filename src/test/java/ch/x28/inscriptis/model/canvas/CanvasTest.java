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
package ch.x28.inscriptis.model.canvas;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.InstanceOfAssertFactories.*;

import java.util.function.Consumer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import ch.x28.inscriptis.HtmlProperties.WhiteSpace;

public class CanvasTest {

	@Nested
	public class BlockTest {

		private Block block;

		@BeforeEach
		public void createBlock() {
			block = new Block(0, new Prefix());
		}

		@AfterEach
		public void destroyBlock() {
			block = null;
		}

		private Consumer<Block> hasCollapsableWhitespace() {

			return block -> assertThat(block)
				.extracting("collapsableWhitespace", as(BOOLEAN))
				.isTrue();
		}

		private Consumer<Block> hasContent(String content) {

			return block -> assertThat(block)
				.extracting("contentBuilder", as(STRING_BUILDER))
				.hasToString(content);
		}

		private Consumer<Block> hasNoCollapsableWhitespace() {

			return block -> assertThat(block)
				.extracting("collapsableWhitespace", as(BOOLEAN))
				.isFalse();
		}

		private Consumer<Block> hasNoContent() {

			return block -> assertThat(block)
				.extracting("contentBuilder", as(STRING_BUILDER))
				.isEmpty();
		}

		@Nested
		public class MergeNormalTest {

			private void merge(String text) {
				block.merge(text, WhiteSpace.NORMAL);
			}

			@Nested
			public class CollapsableWhitespace {

				@BeforeEach
				public void configureBlock() {
					block.setCollapsableWhitespace(true);
				}

				@Test
				public void testEmptyText() {

					// given
					String text = "";

					// when
					merge(text);

					// then
					assertThat(block)
						.satisfies(hasNoContent())
						.satisfies(hasCollapsableWhitespace());
				}

				@Test
				public void testNoWhitespaceText() {

					// given
					String text = "Hallo";

					// when
					merge(text);

					// then
					assertThat(block)
						.satisfies(hasContent("Hallo"))
						.satisfies(hasNoCollapsableWhitespace());
				}

				@Test
				public void testOneWhitespaceText() {

					// given
					String text = " ";

					// when
					merge(text);

					// then
					assertThat(block)
						.satisfies(hasNoContent())
						.satisfies(hasCollapsableWhitespace());
				}

				@Test
				public void testTwoWhitespaceText() {

					// given
					String text = "  ";

					// when
					merge(text);

					// then
					assertThat(block)
						.satisfies(hasNoContent())
						.satisfies(hasCollapsableWhitespace());
				}

				@Test
				public void testWhitespaceText() {

					// given
					String text = " Hallo ";

					// when
					merge(text);

					// then
					assertThat(block)
						.satisfies(hasContent("Hallo "))
						.satisfies(hasCollapsableWhitespace());
				}

			}

			@Nested
			public class NonCollapsableWhitespace {

				@BeforeEach
				public void configureBlock() {
					block.setCollapsableWhitespace(false);
				}

				@Test
				public void testEmptyText() {

					// given
					String text = "";

					// when
					merge(text);

					// then
					assertThat(block)
						.satisfies(hasNoContent())
						.satisfies(hasNoCollapsableWhitespace());
				}

				@Test
				public void testNoWhitespaceText() {

					// given
					String text = "Hallo";

					// when
					merge(text);

					// then
					assertThat(block)
						.satisfies(hasContent("Hallo"))
						.satisfies(hasNoCollapsableWhitespace());
				}

				@Test
				public void testOneWhitespaceText() {

					// given
					String text = " ";

					// when
					merge(text);

					// then
					assertThat(block)
						.satisfies(hasContent(" "))
						.satisfies(hasCollapsableWhitespace());
				}

				@Test
				public void testTwoWhitespaceText() {

					// given
					String text = "  ";

					// when
					merge(text);

					// then
					assertThat(block)
						.satisfies(hasContent(" "))
						.satisfies(hasCollapsableWhitespace());
				}

				@Test
				public void testWhitespaceText() {

					// given
					String text = " Hallo ";

					// when
					merge(text);

					// then
					assertThat(block)
						.satisfies(hasContent(" Hallo "))
						.satisfies(hasCollapsableWhitespace());
				}

			}

		}

	}

	@Nested
	public class PrefixTest {

		@Test
		public void testCombinedPrefix() {

			Prefix prefix = new Prefix();
			prefix.registerPrefix(5, "1. ");
			prefix.registerPrefix(2, "");

			assertThat(prefix.getFirst()).hasToString("    1. ");
			assertThat(prefix.getFirst()).isEmpty();

			prefix.removeLastPrefix();
			assertThat(prefix.getFirst()).isEmpty();

			prefix.removeLastPrefix();
			// final consumption - no prefix
			assertThat(prefix.getFirst()).isEmpty();

			// ensure that there are no interactions between different runs with bullets
			prefix.setConsumed(false);
			prefix.registerPrefix(5, "2. ");
			prefix.registerPrefix(2, "- ");

			assertThat(prefix.getFirst()).hasToString("     - ");
			assertThat(prefix.getFirst()).isEmpty();
			assertThat(prefix.getRest()).hasToString("       ");

			prefix.setConsumed(false);
			prefix.removeLastPrefix();

			assertThat(prefix.getFirst()).hasToString("  2. ");
			assertThat(prefix.getRest()).hasToString("     ");
		}

		@Test
		public void testSimplePrefix() {

			Prefix prefix = new Prefix();
			prefix.registerPrefix(5, "1. ");

			// first use
			assertThat(prefix.getFirst()).hasToString("  1. ");

			// the prefix has been consumed
			assertThat(prefix.getFirst()).isEmpty();

			// prefix used to indent lines separated with newlines
			assertThat(prefix.getRest()).hasToString("     ");
		}

	}

}
