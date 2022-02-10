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
import java.util.StringJoiner;

/**
 * A single row within a table.
 *
 * @author Sascha Wolski
 * @author Matthias Hewelt
 * @author Manuel Schmidt
 */
class Row {

	private final List<TableCell> columns = new ArrayList<>();

	private static List<List<String>> zipLongest(List<List<String>> lists, String fillValue) {

		// determine longest list
		int maxListLength = 0;
		for (List<String> list : lists) {
			maxListLength = Math.max(maxListLength, list.size());
		}

		List<List<String>> resultLists = new ArrayList<>();

		for (int listElementIndex = 0; listElementIndex < maxListLength; listElementIndex++) {
			List<String> subList = new ArrayList<>();
			for (List<String> list : lists) {
				String element = list.size() > listElementIndex
					? list.get(listElementIndex)
					: fillValue;

				subList.add(element);
			}
			resultLists.add(subList);
		}
		return resultLists;
	}

	/**
	 * Computes the list of lines in the cell specified by the column_idx.
	 *
	 * @param columnIndex The column index of the cell.
	 * @return The list of lines in the cell specified by the column_idx or an empty list if the column does not exist.
	 */
	public List<String> getCellLines(int columnIndex) {

		if (columnIndex >= columns.size()) {
			return new ArrayList<>(0);
		}

		return columns.get(columnIndex).getCellLines();
	}

	public List<TableCell> getColumns() {
		return columns;
	}

	/**
	 * @return A rendered string representation of the given row.
	 */
	public String getText() {

		List<List<String>> lines = new ArrayList<>();
		for (TableCell column : columns) {
			lines.add(column.getCellLines());
		}

		StringJoiner joiner = new StringJoiner("\n");
		for (List<String> list : zipLongest(lines, " ")) {
			joiner.add(String.join("  ", list));
		}

		return joiner.toString();
	}

}
