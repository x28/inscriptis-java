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

import java.util.Stack;

import ch.x28.inscriptis.StringUtils;

/**
 * Manage the horizontal prefix (left-indentation, bullets) of canvas lines.
 * <p>
 * Class Prefix manages paddings and bullets that prefix an HTML block.
 * <dl>
 * <dt>Note:</dt>
 * <dd>In Inscriptis an HTML block corresponds to a line in the final output, since new blocks (Display.BLOCK) trigger
 * line breaks while inline content (Display.NORMAL) does not.</dd>
 * </dl>
 *
 * @author Manuel Schmidt
 */
class Prefix {

	/**
	 * The number of characters used for the current left-indentation.
	 */
	private int currentPadding;
	/**
	 * The list of bullets and paddings for the current and all previous tags.
	 */
	private Stack<Item> items;
	/**
	 * Whether the current bullet has already been consumed.
	 */
	private boolean consumed;

	public Prefix() {

		this.currentPadding = 0;
		this.items = new Stack<>();
	}

	public int getCurrentPadding() {
		return currentPadding;
	}

	/**
	 * Return the prefix used at the beginning of a tag.
	 * <dl>
	 * <dt>Note:</dt>
	 * <dd>A new block needs to be prefixed by the current padding and bullet. Once this has happened (i.e., consumed is
	 * set to {@code true}) no further prefixes should be used for a line.</dd>
	 * </dl>
	 *
	 * @return the prefix used at the beginning of a tag.
	 */
	public StringBuilder getFirst() {

		StringBuilder builder = new StringBuilder();
		if (consumed) {
			return builder;
		}

		consumed = true;
		String bullet = popNextBullet();
		StringUtils.append(builder, " ", currentPadding - bullet.length());
		builder.append(bullet);
		return builder;
	}

	/**
	 * Return the prefix used for new lines within a block.
	 * <dl>
	 * <dt>Note:</dt>
	 * <dd>This prefix is used for pre-text that contains newlines. The lines need to be prefixed with the right padding
	 * to preserve the indentation.</dd>
	 * </dl>
	 *
	 * @return the prefix used for new lines within a block.
	 */
	public String getRest() {
		return StringUtils.repeat(" ", currentPadding);
	}

	/**
	 * Yield any yet unconsumed bullet.
	 * <dl>
	 * <dt>Note:</dt>
	 * <dd>This function yields the previous element's bullets, if they have not been consumed yet.</dd>
	 * </dl>
	 *
	 * @return any unconsumed bullet
	 */
	public String getUnconsumedBullet() {

		if (consumed) {
			return "";
		}

		String bullet = popNextBullet();
		if (bullet.isEmpty()) {
			return "";
		}

		int padding = currentPadding - items.peek().padding;
		StringBuilder builder = new StringBuilder();
		StringUtils.append(builder, " ", padding - bullet.length());
		builder.append(bullet);
		return builder.toString();
	}

	public boolean isConsumed() {
		return consumed;
	}

	/**
	 * Pop the next bullet to use, if any bullet is available.
	 *
	 * @return the next bullet
	 */
	public String popNextBullet() {

		int size = items.size();
		int nextBulletIdx = size;

		for (int i = 0; i < size; i++) {
			int idx = size - 1 - i;

			if (!items.get(idx).bullet.isEmpty()) {
				nextBulletIdx = idx;
				break;
			}
		}

		if (nextBulletIdx == size) {
			return "";
		}

		Item current = items.get(nextBulletIdx);
		String bullet = current.bullet;
		current.bullet = "";
		return bullet;
	}

	/**
	 * Register the given prefix.
	 *
	 * @param paddingInline the number of characters used for paddingInline
	 * @param bullet an optional bullet
	 */
	public void registerPrefix(int paddingInline, String bullet) {

		currentPadding += paddingInline;
		items.add(new Item(paddingInline, bullet));
	}

	/**
	 * Remove the last prefix from the list.
	 */
	public void removeLastPrefix() {
		currentPadding -= items.pop().padding;
	}

	public void setConsumed(boolean consumed) {
		this.consumed = consumed;
	}

	public class Item {

		private int padding;
		private String bullet;

		public Item(int padding, String bullet) {

			this.padding = padding;
			this.bullet = bullet;
		}

	}

}
