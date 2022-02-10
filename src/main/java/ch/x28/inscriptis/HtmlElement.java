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

import ch.x28.inscriptis.HtmlProperties.Display;
import ch.x28.inscriptis.HtmlProperties.HorizontalAlignment;
import ch.x28.inscriptis.HtmlProperties.VerticalAlignment;
import ch.x28.inscriptis.HtmlProperties.WhiteSpace;

/**
 * The HtmlElement class stores the CSS properties.
 *
 * @author Sascha Wolski
 * @author Matthias Hewelt
 * @author Manuel Schmidt
 */
class HtmlElement {

	/**
	 * Name of the given HtmlElement
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
	private Display display = null;
	/**
	 * Vertical margin before the tag's content.
	 */
	private int marginBefore = 0;
	/**
	 * Vertical margin after the tag's content.
	 */
	private int marginAfter = 0;
	/**
	 * Horizontal padding before the tag's content.
	 */
	private int padding = 0;
	/**
	 * {@link WhiteSpace} handling strategy.
	 */
	private WhiteSpace whitespace = null;
	/**
	 * Limit printing of whitespace affixes to elements with `normal` whitespace handling.
	 */
	private boolean limitWhitespaceAffixes = false;
	/**
	 * Horizontal Alignment.
	 */
	private HorizontalAlignment align = HorizontalAlignment.LEFT;
	/**
	 * Vertical Alignment.
	 */
	private VerticalAlignment valign = VerticalAlignment.MIDDLE;

	public HtmlElement align(HorizontalAlignment align) {

		this.align = align;
		return this;
	}

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
			.padding(padding)
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

	public Display getDisplay() {
		return display;
	}

	public int getMarginAfter() {
		return marginAfter;
	}

	public int getMarginBefore() {
		return marginBefore;
	}

	public int getPadding() {
		return padding;
	}

	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param htmlElement the new HtmlElement to be applied to the current context.
	 * @return the refined element with the context applied.
	 */
	public HtmlElement getRefinedHtmlElement(HtmlElement htmlElement) {

		HtmlElement refinedElement = htmlElement.clone();

		// inherit display:none attributes
		if (display == Display.NONE) {
			refinedElement.display = Display.NONE;
		}

		// no whitespace set => inherit
		if (refinedElement.whitespace == null && whitespace != null) {
			refinedElement.whitespace = whitespace;
		}

		// do not display whitespace only affixes in Whitespace.PRE areas if `limitWhitespaceAffixes` is set.
		if (refinedElement.limitWhitespaceAffixes && whitespace == WhiteSpace.PRE) {
			if (StringUtils.isBlank(refinedElement.prefix)) {
				refinedElement.prefix = "";
			}
			if (StringUtils.isBlank(refinedElement.suffix)) {
				refinedElement.suffix = "";
			}
		}

		return refinedElement;
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

	public boolean isLimitWhitespaceAffixes() {
		return limitWhitespaceAffixes;
	}

	public HtmlElement limitWhitespaceAffixes(boolean limitWhitespaceAffixes) {

		this.limitWhitespaceAffixes = limitWhitespaceAffixes;
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

	public HtmlElement padding(int padding) {

		this.padding = padding;
		return this;
	}

	public HtmlElement prefix(String prefix) {

		this.prefix = prefix;
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
			", padding=" + padding +
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

}
