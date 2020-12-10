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
import java.util.List;

import ch.x28.inscriptis.HtmlProperties.HorizontalAlignment;

/**
 * An HTML table.
 *
 * @author Sascha Wolski
 * @author Matthias Hewelt
 */
class Table {

	private final List<Row> rows = new ArrayList<>();
	private boolean tdOpen = false;

	/**
	 * Adds a new left aligned TableCell to the table's last row. If no row exists yet, a new row is created.
	 */
	public void addCell(List<String> canvas) {
		addCell(canvas, HorizontalAlignment.LEFT);
	}

	/**
	 * Adds a new TableCell to the table's last row. If no row exists yet, a new row is created.
	 */
	public void addCell(List<String> canvas, HorizontalAlignment alignment) {

		if (rows.isEmpty()) {
			rows.add(new Row());
		}

		Row last = rows.get(rows.size() - 1);
		last.getColumns().add(new TableCell(canvas, alignment, null, null));
	}

	/**
	 * Adds an empty Row to the table.
	 */
	public void addRow() {
		rows.add(new Row());
	}

	/**
	 * Compute and set the column width and height for all colls in the table.
	 */
	public void computeColumnWidthAnHeight() {

		// skip tables with no row
		if (rows.isEmpty())
			return;

		// determine row height
		for (Row row : rows) {
			int maxRowHeight = 1;
			for (TableCell col : row.getColumns()) {
				maxRowHeight = Math.max(maxRowHeight, col.getCellLines().size());
			}
			for (TableCell col : row.getColumns()) {
				col.setHeight(maxRowHeight);
			}
		}

		// determine maximum number of columns
		int maxColumns = 0;
		for (Row row : rows) {
			maxColumns = Math.max(maxColumns, row.getColumns().size());
		}

		for (int columnIndex = 0; columnIndex < maxColumns; columnIndex++) {
			// determine max column width by longest cell line per row
			int maxColumnWidth = 0;
			for (Row row : rows) {
				for (String cellLine : row.getCellLines(columnIndex)) {
					maxColumnWidth = Math.max(maxColumnWidth, cellLine.length());
				}
			}

			// set column width in all rows
			for (Row row : rows) {
				if (row.getColumns().size() > columnIndex) {
					row.getColumns().get(columnIndex).setWidth(maxColumnWidth);
				}
			}
		}
	}

	/**
	 * Get a rendered string representation of this table.
	 */
	public String getText() {

		computeColumnWidthAnHeight();

		List<String> rowContents = new ArrayList<>();
		for (Row row : rows) {
			rowContents.add(row.getText());
		}

		return String.join("\n", rowContents);
	}

	public boolean isTdOpen() {
		return tdOpen;
	}

	public void setTdOpen(boolean tdOpen) {
		this.tdOpen = tdOpen;
	}

}
