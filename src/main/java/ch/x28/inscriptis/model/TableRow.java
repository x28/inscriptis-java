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
 * A single row within a table.
 *
 * @author Sascha Wolski
 * @author Matthias Hewelt
 * @author Manuel Schmidt
 */
public class TableRow {

	private final Stack<TableCell> columns;
	private final String cellSeparator;

	public TableRow(String cellSeparator) {

		this.columns = new Stack<>();
		this.cellSeparator = cellSeparator;
	}

	public String getCellSeparator() {
		return cellSeparator;
	}

	public TableCell getColumn(int index) {
		return columns.get(index);
	}

	public Stack<TableCell> getColumns() {
		return columns;
	}

	/**
	 * Return a text representation of the TableRow.
	 *
	 * @return the text representation
	 */
	public String getText() {

		if (columns.isEmpty()) {
			return "";
		}

		int height = columns.peek().getHeight();
		StringJoiner[] joiners = new StringJoiner[height];

		for (int c = 0; c < size(); c++) {
			for (int i = 0; i < height; i++) {
				StringJoiner joiner;
				if (c == 0) {
					joiner = new StringJoiner(cellSeparator);
					joiners[i] = joiner;
				} else {
					joiner = joiners[i];
				}
				joiner.add(getColumn(c).getBlock(i));
			}
		}

		StringJoiner lineJoiner = new StringJoiner("\n");
		for (StringJoiner joiner : joiners) {
			lineJoiner.add(joiner.toString());
		}
		return lineJoiner.toString();
	}

	/**
	 * Compute and return the width of the current row.
	 *
	 * @return the computed width
	 */
	public int getWidth() {

		if (columns.isEmpty()) {
			return 0;
		}

		int width = cellSeparator.length() * (columns.size() - 1);
		for (TableCell cell : columns) {
			width += cell.getWidth();
		}
		return width;
	}

	public int size() {
		return columns.size();
	}

}
