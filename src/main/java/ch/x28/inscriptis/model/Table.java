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

import java.util.Stack;
import java.util.StringJoiner;

/**
 * An HTML table.
 *
 * @author Sascha Wolski
 * @author Matthias Hewelt
 * @author Manuel Schmidt
 */
public class Table {

	/**
	 * the table's rows.
	 */
	private final Stack<TableRow> rows;

	public Table() {
		this.rows = new Stack<>();
	}

	/**
	 * Add a new TableCell to the table's last row.
	 * <dl>
	 * <dt>Note:</dt>
	 * <dd>If no row exists yet, a new row is created.</dd>
	 * </dl>
	 *
	 * @param tableCell the table cell
	 */
	public void addCell(TableCell tableCell) {

		if (rows.isEmpty()) {
			rows.add(new TableRow());
		}
		rows.peek().getColumns().add(tableCell);
	}

	/**
	 * Add an empty Row to the table.
	 */
	public void addRow() {
		rows.add(new TableRow());
	}

	/**
	 * Return and render the text of the given table.
	 *
	 * @return the text
	 */
	public String getText() {

		if (rows.isEmpty()) {
			return "\n";
		}

		setRowHeight();
		setColumnWidth();

		StringJoiner joiner = new StringJoiner("\n");
		for (TableRow row : rows) {
			joiner.add(row.getText());
		}
		return joiner.toString() + "\n";
	}

	/**
	 * Set the column width for all table cells in the table.
	 */
	private void setColumnWidth() {

		// determine maximum number of columns
		int maxColumns = 0;
		for (TableRow row : rows) {
			int columns = row.size();
			if (columns > maxColumns) {
				maxColumns = columns;
			}
		}

		for (int c = 0; c < maxColumns; c++) {
			// determine the required column width for the current column
			int maxColumnWidth = 0;
			for (TableRow row : rows) {
				if (row.size() > c) {
					int columnWidth = row.getColumn(c).getWidth();
					if (columnWidth > maxColumnWidth) {
						maxColumnWidth = columnWidth;
					}
				}
			}

			// set column width for all table cells in the current column
			for (TableRow row : rows) {
				if (row.size() > c) {
					row.getColumn(c).setWidth(maxColumnWidth);
				}
			}
		}
	}

	/**
	 * Set the cell height for all table cells in the table.
	 */
	private void setRowHeight() {

		for (TableRow row : rows) {
			int maxRowHeight = 0;
			for (TableCell cell : row.getColumns()) {
				int rowHeight = cell.normalizeBlocks();
				if (rowHeight > maxRowHeight) {
					maxRowHeight = rowHeight;
				}
			}
			for (TableCell cell : row.getColumns()) {
				cell.setHeight(maxRowHeight);
			}
		}
	}

}
