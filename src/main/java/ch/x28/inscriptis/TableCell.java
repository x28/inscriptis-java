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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ch.x28.inscriptis.HtmlProperties.HorizontalAlignment;
import ch.x28.inscriptis.HtmlProperties.VerticalAlignment;

/**
 * A single Table Cell.
 *
 * @author Sascha Wolski
 * @author Matthias Hewelt
 * @author Manuel Schmidt
 */
class TableCell {

	private List<String> canvas;
	private HorizontalAlignment horizontalAlignment;
	private VerticalAlignment verticalAlignment;
	private Integer width;
	private Integer height;

	/**
	 * Create a new table cell with the given properties
	 */
	public TableCell(
		List<String> canvas,
		HorizontalAlignment horizontalAlignment,
		VerticalAlignment verticalAlignment,
		Integer width,
		Integer height) {

		this.canvas = canvas;
		this.horizontalAlignment = horizontalAlignment;
		this.verticalAlignment = verticalAlignment;
		this.width = width;
		this.height = height;
	}

	public List<String> getCanvas() {
		return canvas;
	}

	/**
	 * @return a list of all the lines stores within the table cell.
	 */
	public List<String> getCellLines() {

		List<String> lines = new ArrayList<>();
		for (String str : this.canvas) {
			String[] split = str.split("\n");
			lines.addAll(Arrays.asList(split));
		}

		this.canvas.clear();
		this.canvas.addAll(lines);

		if (this.height != null && false) {
			for (int i = 0; i < this.height - this.canvas.size(); i++) {
				lines.add("");
			}
		}

		// horizontal alignment
		if (this.width != null && this.width > 0) {
			lines = lines.stream()
				.map(this::alignString)
				.collect(Collectors.toList());
		}

		// vertical alignment
		if (this.height != null && lines.size() < this.height) {
			alignLines(lines);
		}

		return lines;
	}

	public int getHeight() {
		return height;
	}

	public HorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public VerticalAlignment getVerticalAlignment() {
		return verticalAlignment;
	}

	public int getWidth() {
		return width;
	}

	/**
	 * The text lines contained in this table cell.
	 */
	public void setCanvas(List<String> canvas) {
		this.canvas = canvas;
	}

	/**
	 * Set the height (amount of lines) of this table cell.
	 */
	public void setHeight(Integer height) {
		this.height = height;
	}

	/**
	 * Set the horizontal alignment of this table cell.
	 *
	 * @param horizontalAlignment one of <code>CENTER</code>, <code>LEFT</code> or <code>RIGHT</code>
	 */
	public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
	}

	/**
	 * Set the vertical alignment of this table cell.
	 *
	 * @param valign one of <code>TOP</code>, <code>MIDDLE</code> or <code>BOTTOM</code>
	 */
	public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
		this.verticalAlignment = verticalAlignment;
	}

	/**
	 * Set the width of the lines in this table cell.
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}

	private void alignLines(List<String> lines) {

		String emptyLine = width != null ? StringUtils.repeat(" ", width) : "";

		switch (verticalAlignment) {
			case TOP: {
				int size = height - lines.size();
				for (int i = 0; i < size; i++) {
					lines.add(emptyLine);
				}
				return;
			}
			case MIDDLE: {
				int size = (height - lines.size() - 1) / 2;
				for (int i = 0; i < size; i++) {
					lines.add(0, emptyLine);
				}
				size = height - lines.size();
				for (int i = 0; i < size; i++) {
					lines.add(emptyLine);
				}
				return;
			}
			case BOTTOM: {
				int size = height - lines.size();
				for (int i = 0; i < size; i++) {
					lines.add(0, emptyLine);
				}
				return;
			}
			default: {
				return;
			}
		}
	}

	private String alignString(String str) {

		switch (horizontalAlignment) {
			case CENTER:
				return StringUtils.padCenter(str, width);
			case LEFT:
				return StringUtils.padRight(str, width);
			case RIGHT:
				return StringUtils.padLeft(str, width);
			default:
				return StringUtils.padCenter(str, width);
		}
	}

}
