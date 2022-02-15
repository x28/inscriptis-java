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

import ch.x28.inscriptis.HtmlProperties.WhiteSpace;

/**
 * Representation of a text block within the HTML canvas.
 *
 * @author Manuel Schmidt
 */
public class Block {

	private static final char NO_BREAK_SPACE = '\u00a0';

	private int idx;
	private final Prefix prefix;
	private final StringBuilder contentBuilder;
	private boolean collapsableWhitespace;

	/**
	 * The current block of text.
	 * <p>
	 * A block usually refers to one line of output text.
	 * <dl>
	 * <dt>Note:</dt>
	 * <dd>If pre-formatted content is merged with a block, it may also contain multiple lines.</dd>
	 * </dl>
	 *
	 * @param idx the current block's start index
	 * @param prefix prefix used within the current block.
	 */
	public Block(int idx, Prefix prefix) {

		this.idx = idx;
		this.prefix = prefix;
		this.contentBuilder = new StringBuilder();
		this.collapsableWhitespace = true;
	}

	public void addIdx(int length) {
		idx += length;
	}

	public String getContent() {

		if (!collapsableWhitespace) {
			return contentBuilder.toString();
		}

		int length = contentBuilder.length();
		if (length > 0 && contentBuilder.charAt(length - 1) == ' ') {
			contentBuilder.setLength(length - 1);
			idx--;
		}
		return contentBuilder.toString();
	}

	public int getIdx() {
		return idx;
	}

	public Prefix getPrefix() {
		return prefix;
	}

	public boolean hasIdx(int idx) {
		return this.idx == idx;
	}

	public boolean isEmpty() {
		return contentBuilder.length() == 0;
	}

	/**
	 * Merges the given text with the current block.
	 *
	 * @param text the text to merge
	 * @param whitespace whitespace handling
	 */
	public void merge(String text, WhiteSpace whitespace) {

		if (whitespace == WhiteSpace.PRE) {
			mergePreText(text);
		} else {
			mergeNormalText(text);
		}
	}

	/**
	 * Return a new block that follows the current one.
	 *
	 * @return the new block
	 */
	public Block newBlock() {

		prefix.setConsumed(false);
		return new Block(idx + 1, prefix);
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	void setCollapsableWhitespace(boolean collapsableWhitespace) {
		this.collapsableWhitespace = collapsableWhitespace;
	}

	private void add(CharSequence text) {

		contentBuilder.append(text);
		addIdx(text.length());
	}

	/**
	 * Merge the given text with the current block.
	 *
	 * @param text the text to merge
	 */
	private void mergeNormalText(String text) {

		StringBuilder textBuilder = new StringBuilder();

		for (char c : text.toCharArray()) {
			if (!Character.isWhitespace(c) && c != NO_BREAK_SPACE) {
				textBuilder.append(c);
				collapsableWhitespace = false;
			} else if (!collapsableWhitespace) {
				textBuilder.append(' ');
				collapsableWhitespace = true;
			}
		}

		if (textBuilder.length() > 0) {
			if (isEmpty()) {
				add(prefix.getFirst());
			}
			add(textBuilder);
		}
	}

	/**
	 * Merge the given pre-formatted text with the current block.
	 *
	 * @param text the text to merge
	 */
	private void mergePreText(String text) {

		StringBuilder textBuilder = prefix.getFirst();
		if (!text.isEmpty()) {
			String prefixedText = text.replace("\n", "\n" + prefix.getRest());
			textBuilder.append(prefixedText);
			add(textBuilder);
		}
		collapsableWhitespace = false;
	}

}
