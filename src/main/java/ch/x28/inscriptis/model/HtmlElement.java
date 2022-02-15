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

import ch.x28.inscriptis.HtmlProperties.Display;
import ch.x28.inscriptis.HtmlProperties.HorizontalAlignment;
import ch.x28.inscriptis.HtmlProperties.VerticalAlignment;
import ch.x28.inscriptis.HtmlProperties.WhiteSpace;
import ch.x28.inscriptis.StringUtils;
import ch.x28.inscriptis.model.canvas.Canvas;

/**
 * Data structures for handling HTML Elements.
 * <p>
 * The HtmlElement class stores properties and metadata of HTML elements.
 *
 * @author Sascha Wolski
 * @author Matthias Hewelt
 * @author Manuel Schmidt
 */
public class HtmlElement {

	public static final HtmlElement DEFAULT = new HtmlElement();

	/**
	 * The canvas to which the HtmlElement writes its content.
	 */
	private Canvas canvas;
	/**
	 * Tag name of the given HtmlElement.
	 */
	private String tag = "/";
	/**
	 * Specifies a prefix that to insert before the tag's content.
	 */
	private String prefix = "";
	/**
	 * A suffix to append after the tag's content.
	 */
	private String suffix = "";
	/**
	 * {@link Display} strategy used for the content.
	 */
	private Display display = Display.INLINE;
	/**
	 * Vertical margin before the tag's content.
	 */
	private int marginBefore = 0;
	/**
	 * Vertical margin after the tag's content.
	 */
	private int marginAfter = 0;
	/**
	 * Horizontal padding inline before the tag's content.
	 */
	private int paddingInline = 0;
	/**
	 * List Bullet.
	 */
	private String listBullet = "";
	/**
	 * {@link WhiteSpace} handling strategy.
	 */
	private WhiteSpace whitespace = null;
	/**
	 * Limit printing of whitespace affixes to elements with normal whitespace handling.
	 */
	private boolean limitWhitespaceAffixes = false;
	/**
	 * The element's horizontal alignment.
	 */
	private HorizontalAlignment align = HorizontalAlignment.LEFT;
	/**
	 * The element's vertical alignment.
	 */
	private VerticalAlignment valign = VerticalAlignment.MIDDLE;
	/**
	 * The margin after of the previous HtmlElement.
	 */
	private int previousMarginAfter;

	public HtmlElement() {
	}

	public HtmlElement align(HorizontalAlignment align) {

		this.align = align;
		return this;
	}

	public HtmlElement canvas(Canvas canvas) {

		this.canvas = canvas;
		return this;
	}

	/**
	 * @return a clone of the current HtmlElement
	 */
	@Override
	public HtmlElement clone() {

		return new HtmlElement()
			.tag(tag)
			.display(display)
			.whitespace(whitespace)
			.prefix(prefix)
			.suffix(suffix)
			.marginBefore(marginBefore)
			.marginAfter(marginAfter)
			.paddingInline(paddingInline)
			.listBullet(listBullet)
			.limitWhitespaceAffixes(limitWhitespaceAffixes)
			.align(align)
			.valign(valign);
	}

	public HtmlElement display(Display display) {

		this.display = display;
		return this;
	}

	public HorizontalAlignment getAlign() {
		return align;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public Display getDisplay() {
		return display;
	}

	public String getListBullet() {
		return listBullet;
	}

	public int getMarginAfter() {
		return marginAfter;
	}

	public int getMarginBefore() {
		return marginBefore;
	}

	public int getPaddingInline() {
		return paddingInline;
	}

	public String getPrefix() {
		return prefix;
	}

	public int getPreviousMarginAfter() {
		return previousMarginAfter;
	}

	/**
	 * Computes the new HTML element based on the previous one.
	 * <p>
	 * <b>Adaptations:</b>
	 * <p>
	 * marginTop: additional margin required when considering marginBottom of the previous element
	 *
	 * @param htmlElement the new HtmlElement to be applied to the current context.
	 * @return the refined element with the context applied.
	 */
	public HtmlElement getRefinedHtmlElement(HtmlElement htmlElement) {

		htmlElement.canvas = canvas;

		// inherit `display:none` attributes and ignore further refinements
		if (display == Display.NONE) {
			htmlElement.display = Display.NONE;
			return htmlElement;
		}

		// no whitespace set => inherit
		if (htmlElement.whitespace == null && whitespace != null) {
			htmlElement.whitespace = whitespace;
		}

		// do not display whitespace only affixes in Whitespace.PRE areas if `limitWhitespaceAffixes` is set.
		if (htmlElement.limitWhitespaceAffixes && whitespace == WhiteSpace.PRE) {
			if (StringUtils.isBlank(htmlElement.prefix)) {
				htmlElement.prefix = "";
			}
			if (StringUtils.isBlank(htmlElement.suffix)) {
				htmlElement.suffix = "";
			}
		}

		if (htmlElement.display == Display.BLOCK && display == Display.BLOCK) {
			htmlElement.previousMarginAfter = marginAfter;
		}

		return htmlElement;
	}

	public String getSuffix() {
		return suffix;
	}

	public String getTag() {
		return tag;
	}

	public VerticalAlignment getValign() {
		return valign;
	}

	public WhiteSpace getWhitespace() {
		return whitespace;
	}

	public boolean has(Display display) {
		return this.display == display;
	}

	public boolean hasListBullet() {
		return !listBullet.isEmpty();
	}

	public boolean isLimitWhitespaceAffixes() {
		return limitWhitespaceAffixes;
	}

	public HtmlElement limitWhitespaceAffixes(boolean limitWhitespaceAffixes) {

		this.limitWhitespaceAffixes = limitWhitespaceAffixes;
		return this;
	}

	public HtmlElement listBullet(String listBullet) {

		this.listBullet = listBullet;
		return this;
	}

	public HtmlElement marginAfter(int marginAfter) {

		this.marginAfter = marginAfter;
		return this;
	}

	public HtmlElement marginBefore(int marginBefore) {

		this.marginBefore = marginBefore;
		return this;
	}

	public HtmlElement paddingInline(int paddingInline) {

		this.paddingInline = paddingInline;
		return this;
	}

	public HtmlElement prefix(String prefix) {

		this.prefix = prefix;
		return this;
	}

	public HtmlElement previousMarginAfter(int previousMarginAfter) {

		this.previousMarginAfter = previousMarginAfter;
		return this;
	}

	public HtmlElement suffix(String suffix) {

		this.suffix = suffix;
		return this;
	}

	public HtmlElement tag(String tag) {

		this.tag = tag;
		return this;
	}

	@Override
	public String toString() {
		return "HtmlElement [tag=" + tag +
			", display=" + display +
			", whitespace=" + whitespace +
			", prefix=" + prefix +
			", suffix=" + suffix +
			", marginBefore=" + marginBefore +
			", marginAfter=" + marginAfter +
			", paddingInline=" + paddingInline +
			", listBullet=" + listBullet +
			", limitWhitespaceAffixes=" + limitWhitespaceAffixes +
			", align=" + align +
			", valign=" + valign + "]";
	}

	public HtmlElement valign(VerticalAlignment valign) {

		this.valign = valign;
		return this;
	}

	public HtmlElement whitespace(WhiteSpace whitespace) {

		this.whitespace = whitespace;
		return this;
	}

	/**
	 * Write the given HTML text to the element's canvas.
	 *
	 * @param text the text to write
	 */
	public void write(String text) {

		if (text == null || display == Display.NONE) {
			return;
		}

		canvas.write(this, prefix + text + suffix, null);
	}

	/**
	 * Write the given text with {@code Whitespace.PRE} to the canvas.
	 *
	 * @param text the text to write
	 */
	public void writeVerbatimText(String text) {

		if (text == null) {
			return;
		}

		if (display == Display.BLOCK) {
			canvas.openBlock(this);
		}

		canvas.write(this, text, WhiteSpace.PRE);

		if (display == Display.BLOCK) {
			canvas.closeBlock(this);
		}
	}

}
