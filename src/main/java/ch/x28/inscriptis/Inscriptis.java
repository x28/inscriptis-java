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

import java.util.Stack;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.x28.inscriptis.model.Attribute;
import ch.x28.inscriptis.model.HtmlElement;
import ch.x28.inscriptis.model.ParserConfig;
import ch.x28.inscriptis.model.Table;
import ch.x28.inscriptis.model.TableCell;
import ch.x28.inscriptis.model.canvas.Canvas;

/**
 * The Inscriptis class translates a W3C document to its corresponding text representation.
 * <p>
 * <b>Example</b>
 *
 * <pre>
 * <code>
 * Document document = &lt;W3C document&gt;;
 * Inscriptis inscriptis = new Inscriptis(document);
 * String text = inscriptis.getText();
 * </code>
 * </pre>
 *
 * @author Sascha Wolski
 * @author Matthias Hewelt
 * @author Manuel Schmidt
 */
public class Inscriptis {

	private static final String[] UL_COUNTER = { "* ", "+ ", "o ", "- " };

	private final ParserConfig config;
	private final Canvas canvas;
	private final Stack<HtmlElement> tags;

	private Stack<Table> currentTable;
	private Stack<Object> liCounter;
	private String lastCaption;
	private String linkTarget;

	/**
	 * Translates the given W3C document to its corresponding text representation by using the default
	 * {@link ParserConfig} with {@link CssProfile#relaxed}.
	 *
	 * @param document the W3C document to convert
	 */
	public Inscriptis(Document document) {
		this(document, new ParserConfig());
	}

	/**
	 * Translates the given W3C document to its corresponding text representation by using the specified
	 * {@link ParserConfig}.
	 *
	 * @param document the W3C document to convert
	 * @param config an optional ParserConfig configuration object
	 */
	public Inscriptis(Document document, ParserConfig config) {

		this.config = config;
		canvas = new Canvas();

		tags = new Stack<>();
		tags.push(config.getCss().get("body").clone().canvas(canvas));

		currentTable = new Stack<>();
		liCounter = new Stack<>();
		lastCaption = null;

		// used if ParserConfig#displayLinks is enabled
		linkTarget = "";

		parseHtmlTree(document);
	}

	/**
	 * Return the text extracted from the HTML page.
	 *
	 * @return the text
	 */
	public String getText() {
		return canvas.getText();
	}

	private void endA() {

		if (!linkTarget.isEmpty()) {
			tags.peek().write(String.format("](%s)", linkTarget));
		}
	}

	private void endOl() {
		liCounter.pop();
	}

	private void endTable() {

		if (!currentTable.isEmpty()) {
			endTd();
		}

		Table table = currentTable.pop();
		HtmlElement beforeTableTag = tags.get(tags.size() - 2);

		HtmlElement curTag = tags.peek();
		String outOfTableText = curTag.getCanvas().getText().trim();
		Canvas beforeTableCanvas = beforeTableTag.getCanvas();

		if (!outOfTableText.isEmpty()) {
			beforeTableTag.write(outOfTableText);
			beforeTableCanvas.writeNewline();
		}

		beforeTableTag.writeVerbatimText(table.getText());
		beforeTableCanvas.flushInline();
	}

	private void endTd() {

		if (!currentTable.isEmpty()) {
			HtmlElement curTag = tags.peek();
			curTag.getCanvas().closeTag(curTag);
		}
	}

	private void endUl() {
		liCounter.pop();
	}

	/**
	 * Return the bullet that corresponds to the given index.
	 *
	 * @return the bullet
	 */
	private String getBullet() {
		return UL_COUNTER[liCounter.size() % UL_COUNTER.length];
	}

	/**
	 * Handle HTML end tags.
	 *
	 * @param node the HTML end tag to process.
	 */
	private void handleEndTag(Node node) {

		String tag = node.getNodeName();

		switch (tag) {
			case "table":
				endTable();
				break;
			case "ul":
				endUl();
				break;
			case "ol":
				endOl();
				break;
			case "th":
			case "td":
				endTd();
				break;
			case "a":
				if (config.isDisplayAnchors() || config.isDisplayLinks()) {
					endA();
				}
				break;
		}
	}

	/**
	 * Handle HTML start tags.
	 *
	 * @param node the HTML start tag to process.
	 */
	private void handleStartTag(Node node) {

		// use the css to handle tags known to it
		String tag = node.getNodeName();
		HtmlElement htmlElement = config.getCss().getOrDefault(tag, HtmlElement.DEFAULT).clone();
		HtmlElement curTag = tags.peek().getRefinedHtmlElement(htmlElement).tag(tag);
		Attribute.applyAttributes(node.getAttributes(), curTag);

		tags.push(curTag);

		switch (tag) {
			case "table":
				startTable();
				break;
			case "tr":
				startTr();
				break;
			case "th":
			case "td":
				startTd();
				break;
			case "ul":
				startUl();
				break;
			case "ol":
				startOl();
				break;
			case "li":
				startLi();
				break;
			case "br":
				newline();
				break;
			case "a":
				if (config.isDisplayAnchors() || config.isDisplayLinks()) {
					startA(node.getAttributes());
				}
				break;
			case "img":
				if (config.isDisplayImages()) {
					startImg(node.getAttributes());
				}
				break;
		}
	}

	private void newline() {
		tags.peek().getCanvas().writeNewline();
	}

	/**
	 * Parse the HTML tree.
	 *
	 * @param document the W3C document
	 */
	private void parseHtmlTree(Node node) {

		// ignore comments
		if (node.getNodeType() != Node.DOCUMENT_NODE &&
			node.getNodeType() != Node.ELEMENT_NODE &&
			node.getNodeType() != Node.TEXT_NODE) {
			return;
		}

		if (node.getNodeType() == Node.ELEMENT_NODE) {
			handleStartTag(node);
			HtmlElement curTag = tags.peek();
			curTag.getCanvas().openTag(curTag);
		}

		if (node.getNodeType() == Node.TEXT_NODE) {
			String text = node.getNodeValue();
			tags.peek().write(text);
		}

		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			parseHtmlTree(children.item(i));
		}

		if (node.getNodeType() == Node.ELEMENT_NODE) {
			handleEndTag(node);
			HtmlElement prevTag = tags.pop();
			prevTag.getCanvas().closeTag(prevTag);
		}
	}

	private void startA(NamedNodeMap attributes) {

		linkTarget = "";

		if (config.isDisplayLinks()) {
			Node hrefAttribute = attributes.getNamedItem("href");
			linkTarget = hrefAttribute != null
				? hrefAttribute.getNodeValue()
				: "";
		}

		if (config.isDisplayAnchors() && linkTarget.isEmpty()) {
			Node nameAttribute = attributes.getNamedItem("name");
			linkTarget = nameAttribute != null
				? nameAttribute.getNodeValue()
				: "";
		}

		if (!linkTarget.isEmpty()) {
			tags.peek().write("[");
		}
	}

	private void startImg(NamedNodeMap attributes) {

		String imageText = "";

		Node altNode = attributes.getNamedItem("alt");
		if (altNode != null) {
			imageText = altNode.getNodeValue();
		} else {
			Node titleNode = attributes.getNamedItem("title");
			if (titleNode != null) {
				imageText = titleNode.getNodeValue();
			}
		}

		if (!imageText.isEmpty() && !(config.isDeduplicateCaptions() && imageText.equals(lastCaption))) {
			tags.peek().write(String.format("[%s]", imageText));
			lastCaption = imageText;
		}
	}

	private void startLi() {

		Object bullet = !liCounter.isEmpty() ? liCounter.peek() : "* ";

		if (bullet instanceof Integer) {
			Integer bulletNumber = (Integer) liCounter.pop();
			liCounter.push(bulletNumber + 1);
			tags.peek().listBullet(bulletNumber + ". ");
		} else {
			tags.peek().listBullet(bullet.toString());
		}

		tags.peek().write("");
	}

	private void startOl() {
		liCounter.push(1);
	}

	private void startTable() {

		tags.peek().canvas(new Canvas());
		currentTable.push(new Table());
	}

	private void startTd() {

		if (!currentTable.isEmpty()) {
			Table curTable = currentTable.peek();

			// open td tag
			HtmlElement curTag = tags.peek();
			TableCell tableCell = new TableCell(curTag.getAlign(), curTag.getValign());
			curTag.canvas(tableCell);
			curTable.addCell(tableCell);
		}
	}

	private void startTr() {

		if (!currentTable.isEmpty()) {
			currentTable.peek().addRow();
		}
	}

	private void startUl() {
		liCounter.push(getBullet());
	}

}
