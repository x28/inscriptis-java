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

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import ch.x28.inscriptis.HtmlProperties.HorizontalAlignment;
import ch.x28.inscriptis.HtmlProperties.VerticalAlignment;
import ch.x28.inscriptis.StringUtils;

/**
 * @author Sascha Wolski
 * @author Matthias Hewelt
 * @author Manuel Schmidt
 */
public class TableTest {

	@Nested
	public class TableCellTest {

		private String[] toLines(List<String> blocks) {
			return StringUtils.split(String.join("\n", blocks), '\n');
		}

		@Nested
		public class Alignment {

			@Nested
			public class Horizontal {

				@Test
				public void testCenter() {

					//given
					TableCell cell = new TableCell(HorizontalAlignment.CENTER, VerticalAlignment.TOP);
					cell.addBlock("Ehre sei Gott!");

					// when
					cell.setWidth(16);

					// then
					assertThat(cell.getBlocks()).containsExactly(" Ehre sei Gott! ");
				}

				@Test
				public void testLeft() {

					//given
					TableCell cell = new TableCell(HorizontalAlignment.LEFT, VerticalAlignment.TOP);
					cell.addBlock("Ehre sei Gott!");

					// when
					cell.setWidth(16);

					// then
					assertThat(cell.getBlocks()).containsExactly("Ehre sei Gott!  ");
				}

				@Test
				public void testRight() {

					//given
					TableCell cell = new TableCell(HorizontalAlignment.RIGHT, VerticalAlignment.TOP);
					cell.addBlock("Ehre sei Gott!");

					// when
					cell.setWidth(16);

					// then
					assertThat(cell.getBlocks()).containsExactly("  Ehre sei Gott!");
				}

			}

			@Nested
			public class Vertical {

				@Test
				public void testBottom() {

					// given
					TableCell cell = new TableCell(HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM);
					cell.addBlock("Ehre sei Gott!");

					// when
					cell.setWidth(16);
					cell.setHeight(4);

					// then
					assertThat(cell.getBlocks()).containsExactly(
						"",
						"",
						"",
						"Ehre sei Gott!  ");
				}

				@Test
				public void testMiddle() {

					// given
					TableCell cell = new TableCell(HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);
					cell.addBlock("Ehre sei Gott!");

					// when
					cell.setWidth(16);
					cell.setHeight(4);

					// then
					assertThat(cell.getBlocks()).containsExactly(
						"",
						"Ehre sei Gott!  ",
						"",
						"");
				}

				@Test
				public void testTop() {

					// given
					TableCell cell = new TableCell(HorizontalAlignment.LEFT, VerticalAlignment.TOP);
					cell.addBlock("Ehre sei Gott!");

					// when
					cell.setWidth(16);
					cell.setHeight(4);

					// then
					assertThat(cell.getBlocks()).containsExactly(
						"Ehre sei Gott!  ",
						"",
						"",
						"");
				}

			}

		}

		@Nested
		public class Height {

			@Test
			public void test() {

				// given
				TableCell cell = new TableCell(HorizontalAlignment.LEFT, VerticalAlignment.TOP);
				cell.addBlock("hallo");

				// when
				cell.normalizeBlocks();

				// then
				assertThat(toLines(cell.getBlocks())).hasSize(cell.getHeight());
			}

			@Test
			public void test2() {

				// given
				TableCell cell = new TableCell(HorizontalAlignment.LEFT, VerticalAlignment.TOP);
				cell.addBlocks("hallo", "echo");

				// when
				cell.normalizeBlocks();

				// then
				assertThat(cell.getHeight()).isEqualTo(2);
			}

			@Test
			public void test3() {

				// given
				TableCell cell = new TableCell(HorizontalAlignment.LEFT, VerticalAlignment.TOP);
				cell.addBlock("hallo\necho");

				// when
				cell.normalizeBlocks();

				// then
				assertThat(cell.getHeight()).isEqualTo(2);
			}

			@Test
			public void test4() {

				// given
				TableCell cell = new TableCell(HorizontalAlignment.LEFT, VerticalAlignment.TOP);
				cell.addBlocks("hallo\necho", "Ehre sei Gott", "Jump\n&\nRun!\n\n\n");

				// when
				cell.normalizeBlocks();

				// then
				assertThat(cell.getHeight()).isEqualTo(9);
				assertThat(toLines(cell.getBlocks())).hasSize(cell.getHeight());
			}

		}

	}

	/**
	 * Test borderline cases for table rows.
	 */
	@Nested
	public class TableRowTest {

		@Test
		public void testEmpty() {

			// given
			// when
			TableRow row = new TableRow();

			// then
			assertThat(row.getWidth()).isZero();
			assertThat(row.getText()).isEmpty();
		}

	}

}
