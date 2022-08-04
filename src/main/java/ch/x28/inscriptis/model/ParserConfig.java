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

import ch.x28.inscriptis.CssProfile;

/**
 * The ParserConfig object encapsulates configuration options and custom CSS definitions used by inscriptis for
 * translating HTML to text.
 *
 * @author Sascha Wolski
 * @author Matthias Hewelt
 * @author Manuel Schmidt
 */
public class ParserConfig {

	private final CssProfile css;
	private boolean displayImages = false;
	private boolean deduplicateCaptions = false;
	private boolean displayLinks = false;
	private boolean displayAnchors = false;
	private String tableCellSeparator = "  ";

	/**
	 * Creates a new parser configuration with {@link CssProfile#relaxed}.
	 */
	public ParserConfig() {
		this.css = CssProfile.relaxed();
	}

	/**
	 * Creates a new parser configuration with the given {@link CssProfile}.
	 *
	 * @param cssProfile a custom CSS definition, otherwise {@link CssProfile#relaxed}.
	 */
	public ParserConfig(CssProfile cssProfile) {
		this.css = cssProfile;
	}

	/**
	 * Return the configured {@link CssProfile}.
	 *
	 * @return the configured {@link CssProfile}.
	 */
	public CssProfile getCss() {
		return css;
	}

	/**
	 * Return the separator to use between table cells.
	 *
	 * @return the separator to use between table cells.
	 */
	public String getTableCellSeparator() {
		return tableCellSeparator;
	}

	/**
	 * Whether to deduplicate captions such as image titles (many newspaper include images and video previews with
	 * identical titles).
	 *
	 * @return {@code true} of deduplicate captions, otherwise {@code false}.
	 */
	public boolean isDeduplicateCaptions() {
		return deduplicateCaptions;
	}

	/**
	 * Whether to display anchors (e.g. <code>[here](#here)</code>).
	 *
	 * @return {@code true} of display anchors, otherwise {@code false}.
	 */
	public boolean isDisplayAnchors() {
		return displayAnchors;
	}

	/**
	 * Whether to include images <code>alt</code> or <code>title</code> attribute values as text. If an image has both
	 * <code>alt</code> and <code>title</code> attribute the <code>alt</code> value will be used.
	 *
	 * @return {@code true} to include images, otherwise {@code false}.
	 */
	public boolean isDisplayImages() {
		return displayImages;
	}

	/**
	 * Whether to display link targets (e.g. <code>[Python](https://www.python.org)</code>).
	 *
	 * @return {@code true} to display links, otherwise {@code false}
	 */
	public boolean isDisplayLinks() {
		return displayLinks;
	}

	/**
	 * Whether to deduplicate captions such as image titles (many newspaper include images and video previews with
	 * identifical titles).
	 *
	 * @param deduplicateCaptions if set to true, successive caption duplicates won't be rendered.
	 */
	public void setDeduplicateCaptions(boolean deduplicateCaptions) {
		this.deduplicateCaptions = deduplicateCaptions;
	}

	/**
	 * Whether to display anchors (e.g. <code>[here](#here)</code>).
	 *
	 * @param displayAnchors if true, anchors will be rendered.
	 */
	public void setDisplayAnchors(boolean displayAnchors) {
		this.displayAnchors = displayAnchors;
	}

	/**
	 * Whether to include images <code>alt</code> or <code>title</code> attribute values as text. If an image has both
	 * <code>alt</code> and <code>title</code> attribute the <code>alt</code> value will be used.
	 *
	 * @param displayImages when true, images <code>alt</code> or <code>title</code> will be rendered. Otherwise no
	 *            information about images will be rendered.
	 */
	public void setDisplayImages(boolean displayImages) {
		this.displayImages = displayImages;
	}

	/**
	 * Whether to display link targets (e.g. <code>[Python](https://www.python.org)</code>).
	 *
	 * @param displayLinks if true, link targets will be rendered.
	 */
	public void setDisplayLinks(boolean displayLinks) {
		this.displayLinks = displayLinks;
	}

	/**
	 * Set the separator to use between table cells.
	 *
	 * @param tableCellSeparator the separator to use between table cells.
	 */
	public void setTableCellSeparator(String tableCellSeparator) {
		this.tableCellSeparator = tableCellSeparator;
	}

}
