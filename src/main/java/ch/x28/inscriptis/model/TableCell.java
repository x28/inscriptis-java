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

import java.util.ArrayList;
import java.util.List;

import ch.x28.inscriptis.HtmlProperties.HorizontalAlignment;
import ch.x28.inscriptis.HtmlProperties.VerticalAlignment;
import ch.x28.inscriptis.StringUtils;
import ch.x28.inscriptis.model.canvas.Canvas;

/**
 * A table Cell.
 *
 * @author Sascha Wolski
 * @author Matthias Hewelt
 * @author Manuel Schmidt
 */
public class TableCell extends Canvas {

	private HorizontalAlignment align;
	private VerticalAlignment valign;
	private Integer width;
	/**
	 * the original line widths per line (required to adjust annotations after a reformatting)
	 */
	private int[] lineWidth;
	/**
	 * vertical padding that has been introduced due to vertical formatting rules.
	 */
	private int verticalPadding;

	/**
	 * Create a new table cell.
	 *
	 * @param align the horizontal alignment
	 * @param valign the vertical alignment
	 */
	public TableCell(HorizontalAlignment align, VerticalAlignment valign) {

		this.align = align;
		this.valign = valign;
		this.width = null;
		this.lineWidth = null;
		this.verticalPadding = 0;
	}

	public HorizontalAlignment getAlign() {
		return align;
	}

	/**
	 * Compute the table cell's height.
	 *
	 * @return the cell's current height
	 */
	public int getHeight() {
		return !blocks.isEmpty() ? blocks.size() : 1;
	}

	public int[] getLineWidth() {
		return lineWidth;
	}

	public VerticalAlignment getValign() {
		return valign;
	}

	public int getVerticalPadding() {
		return verticalPadding;
	}

	/**
	 * Compute the table cell's width.
	 *
	 * @return the cell's current width
	 */
	public int getWidth() {

		if (width != null) {
			return width;
		}

		int maxLineWidth = 0;

		for (String block : blocks) {
			for (String line : StringUtils.split(block, '\n')) {
				if (line.length() > maxLineWidth) {
					maxLineWidth = line.length();
				}
			}
		}

		return maxLineWidth;
	}

	/**
	 * Split multi-line blocks into multiple one-line blocks.
	 *
	 * @return the height of the normalized cell
	 */
	public int normalizeBlocks() {

		flushInline();

		List<String> normalized = new ArrayList<>();
		for (String block : blocks) {
			for (String line : StringUtils.split(block, '\n')) {
				normalized.add(line);
			}
		}

		blocks = normalized;

		if (blocks.isEmpty()) {
			addBlock("");
		}

		return blocks.size();
	}

	/**
	 * Set the cell's height to the given value.
	 * <dl>
	 * <dt>Note:</dt>
	 * <dd>Depending on the height and the cell's vertical formatting this might require the introduction of empty
	 * lines.</dd>
	 * </dl>
	 *
	 * @param height the cell's expected height
	 */
	public void setHeight(int height) {

		int diff = height - blocks.size();
		if (diff > 0) {
			int prepend = 0;
			int append = 0;
			if (valign == VerticalAlignment.BOTTOM) {
				prepend = diff;
			} else if (valign == VerticalAlignment.MIDDLE) {
				prepend = diff / 2;
				append = (diff + 1) / 2;
			} else { // VerticalAlignment.TOP or null
				append = diff;
			}

			verticalPadding = prepend;

			for (int i = 0; i < prepend; i++) {
				blocks.add(0, "");
			}
			for (int i = 0; i < append; i++) {
				blocks.add("");
			}
		}
	}

	/**
	 * Set the table's width and apply the cell's horizontal formatting.
	 *
	 * @param width the cell's expected width
	 */
	public void setWidth(int width) {

		this.width = width;

		int size = blocks.size();
		lineWidth = new int[size];

		for (int i = 0; i < size; i++) {
			String line = blocks.get(i);
			// save the original line width before reformatting
			lineWidth[i] = line.length();
			// start reformatting
			if (align == HorizontalAlignment.LEFT) {
				line = StringUtils.padRight(line, width);
			} else if (align == HorizontalAlignment.RIGHT) {
				line = StringUtils.padLeft(line, width);
			} else { // HorizontalAlignment.CENTER or null
				line = StringUtils.padCenter(line, width);
			}
			blocks.set(i, line);
		}
	}

}
