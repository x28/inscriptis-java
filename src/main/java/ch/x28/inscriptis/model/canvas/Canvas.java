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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.x28.inscriptis.HtmlProperties.Display;
import ch.x28.inscriptis.HtmlProperties.WhiteSpace;
import ch.x28.inscriptis.StringUtils;
import ch.x28.inscriptis.model.HtmlElement;

/**
 * Elements used for rendering (parts) of the canvas. The class represents the drawing board to which the HTML page is
 * serialized.
 *
 * The text Canvas on which Inscriptis writes the HTML page.
 *
 * @author Manuel Schmidt
 */
public class Canvas {

	/**
	 * The current margin to the previous block (this is required to ensure that the `margin_after` and `margin_before`
	 * constraints of HTML block elements are met).
	 */
	private int margin;
	/**
	 * A list of TextSnippets that will be consolidated into a block, once the current block is completed.
	 */
	protected Block currentBlock;
	/**
	 * A list of finished blocks (i.e., text lines). Each block spawns at least one line.
	 */
	protected List<String> blocks;

	/**
	 * Contain the completed blocks. Each block spawns at least a line
	 */
	public Canvas() {

		this.margin = 1000; // margin to the previous block
		this.currentBlock = new Block(0, new Prefix());
		this.blocks = new ArrayList<>();
	}

	public void addBlock(String block) {
		blocks.add(block);
	}

	public void addBlocks(String... blocks) {
		Collections.addAll(this.blocks, blocks);
	}

	/**
	 * Close the given HtmlElement by writing its bottom margin.
	 *
	 * @param tag the HTML block element to close
	 */
	public void closeBlock(HtmlElement tag) {

		if (tag.getMarginAfter() > margin) {
			int requiredNewlines = tag.getMarginAfter() - margin;
			currentBlock.addIdx(requiredNewlines);
			addBlock(StringUtils.repeat("\n", requiredNewlines - 1));
			margin = tag.getMarginAfter();
		}
	}

	/**
	 * Register that the given tag tag is closed.
	 *
	 * @param tag the tag to close
	 */
	public void closeTag(HtmlElement tag) {

		if (tag.has(Display.BLOCK)) {
			// write missing bullets, if no content has been written so far.
			if (!flushInline() && tag.hasListBullet()) {
				writeUnconsumedBullet();
			}
			currentBlock.getPrefix().removeLastPrefix();
			closeBlock(tag);
		}
	}

	/**
	 * Attempt to flush the content in currentBlock into a new block.
	 * <dl>
	 * <dt>Note:</dt>
	 * <dd>
	 * <ul>
	 * <li>If currentBlock does not contain any content (or only whitespaces) no changes are made.</li>
	 * <li>Otherwise the content of currentBlock is added to blocks and a new currentBlock is initialized.</li>
	 * </ul>
	 * </dd>
	 * </dl>
	 *
	 * @return {@code true} if the attempt was successful, {@code false} otherwise
	 */
	public boolean flushInline() {

		if (!currentBlock.isEmpty()) {
			addBlock(currentBlock.getContent());
			currentBlock = currentBlock.newBlock();
			margin = 0;
			return true;
		}

		return false;
	}

	public String getBlock(int index) {
		return blocks.get(index);
	}

	public List<String> getBlocks() {
		return blocks;
	}

	public Block getCurrentBlock() {
		return currentBlock;
	}

	/**
	 * Return the length of the current line's left margin.
	 *
	 * @return the length
	 */
	public int getLeftMargin() {
		return currentBlock.getPrefix().getCurrentPadding();
	}

	/**
	 * Provide a text representation of the Canvas.
	 *
	 * @return the text representation
	 */
	public String getText() {

		flushInline();
		return String.join("\n", blocks);
	}

	/**
	 * Open an HTML block element.
	 *
	 * @param tag the HTML block element to open
	 */
	public void openBlock(HtmlElement tag) {

		// write missing bullets, if no content has been written
		if (!flushInline() && tag.hasListBullet()) {
			writeUnconsumedBullet();
		}
		currentBlock.getPrefix().registerPrefix(tag.getPaddingInline(), tag.getListBullet());

		// write the block margin
		int requiredMargin = Math.max(tag.getPreviousMarginAfter(), tag.getMarginBefore());
		if (requiredMargin > margin) {
			int requiredNewlines = requiredMargin - margin;
			currentBlock.addIdx(requiredNewlines);
			addBlock(StringUtils.repeat("\n", requiredNewlines - 1));
			margin = requiredMargin;
		}
	}

	/**
	 * Register that a tag is opened.
	 *
	 * @param tag the tag to open
	 */
	public void openTag(HtmlElement tag) {

		if (tag.has(Display.BLOCK)) {
			openBlock(tag);
		}
	}

	/**
	 * Write the given text to the current block.
	 *
	 * @param tag the HTML block element to write to
	 * @param text the text to write
	 * @param whitespace the whitespace
	 */
	public void write(HtmlElement tag, String text, WhiteSpace whitespace) {
		currentBlock.merge(text, whitespace != null ? whitespace : tag.getWhitespace());
	}

	public void writeNewline() {

		if (!flushInline()) {
			addBlock("");
			currentBlock = currentBlock.newBlock();
		}
	}

	/**
	 * Write unconsumed bullets to the blocks list.
	 */
	public void writeUnconsumedBullet() {

		String bullet = currentBlock.getPrefix().getUnconsumedBullet();
		if (!bullet.isEmpty()) {
			addBlock(bullet);
			currentBlock.addIdx(bullet.length());
			currentBlock = currentBlock.newBlock();
			margin = 0;
		}
	}

}
