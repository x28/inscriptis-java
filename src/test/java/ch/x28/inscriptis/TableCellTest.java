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

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.x28.inscriptis.HtmlProperties.HorizontalAlignment;
import ch.x28.inscriptis.HtmlProperties.VerticalAlignment;

/**
 * @author Sascha Wolski
 * @author Matthias Hewelt
 * @author Manuel Schmidt
 */
public class TableCellTest {

	@Test
	public void testHorizontalCenterCellFormatting() {

		//given
		List<String> canvas = new ArrayList<>();
		TableCell cell = new TableCell(canvas, HorizontalAlignment.CENTER, VerticalAlignment.TOP, 16, null);

		// when
		canvas.add("Ehre sei Gott!");

		// then
		assertThat(cell.getCellLines()).containsExactly(" Ehre sei Gott! ");
	}

	@Test
	public void testHorizontalLeftCellFormatting() {

		//given
		List<String> canvas = new ArrayList<>();
		TableCell cell = new TableCell(canvas, HorizontalAlignment.LEFT, VerticalAlignment.TOP, 16, null);

		// when
		canvas.add("Ehre sei Gott!");

		// then
		assertThat(cell.getCellLines()).containsExactly("Ehre sei Gott!  ");
	}

	@Test
	public void testHorizontalRightCellFormatting() {

		//given
		List<String> canvas = new ArrayList<>();
		TableCell cell = new TableCell(canvas, HorizontalAlignment.RIGHT, VerticalAlignment.TOP, 16, null);

		// when
		canvas.add("Ehre sei Gott!");

		// then
		assertThat(cell.getCellLines()).containsExactly("  Ehre sei Gott!");
	}

	@Test
	public void testVerticalBottomCellFormatting() {

		// given
		List<String> canvas = new ArrayList<>();
		TableCell cell = new TableCell(canvas, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM, 16, 4);

		// when
		canvas.add("Ehre sei Gott!");

		// then
		assertThat(cell.getCellLines()).containsExactly(
			"                ",
			"                ",
			"                ",
			"Ehre sei Gott!  ");
	}

	@Test
	public void testVerticalMiddleCellFormatting() {

		// given
		List<String> canvas = new ArrayList<>();
		TableCell cell = new TableCell(canvas, HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE, 16, 4);

		// when
		canvas.add("Ehre sei Gott!");

		// then
		assertThat(cell.getCellLines()).containsExactly(
			"                ",
			"Ehre sei Gott!  ",
			"                ",
			"                ");
	}

	@Test
	public void testVerticalTopCellFormatting() {

		// given
		List<String> canvas = new ArrayList<>();
		TableCell cell = new TableCell(canvas, HorizontalAlignment.LEFT, VerticalAlignment.TOP, 16, 4);

		// when
		canvas.add("Ehre sei Gott!");

		// then
		assertThat(cell.getCellLines()).containsExactly(
			"Ehre sei Gott!  ",
			"                ",
			"                ",
			"                ");
	}

}
