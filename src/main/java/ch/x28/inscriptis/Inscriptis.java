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
import java.util.Stack;
import java.util.stream.Collectors;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.x28.inscriptis.HtmlProperties.Display;
import ch.x28.inscriptis.HtmlProperties.WhiteSpace;

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
	private static final HtmlElement DEFAULT_ELEMENT = new HtmlElement();

	private final ParserConfig config;

	private final Stack<HtmlElement> currentTag;
	private final Stack<Line> currentLine;
	private final Stack<Line> nextLine;
	/**
	 * The canvases used for displaying text. cleanTextLines[0] refers to the root canvas; tables write into child
	 * canvases that are created for every table line and merged with the root canvas at the end of a table.
	 */
	private Stack<List<String>> cleanTextLines;

	private Stack<Table> currentTable;
	private Stack<Object> liCounter;
	private int liLevel = 0;
	private String lastCaption;
	private String linkTarget;

	/**
	 * Translates the given W3C document to its corresponding text representation by using the default
	 * {@link ParserConfig} with {@link CssProfile#RELAXED}.
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

		currentTag = new Stack<>();
		currentLine = new Stack<>();
		nextLine = new Stack<>();

		currentTag.push(this.config.getCss().get("body"));
		currentLine.push(new Line());
		nextLine.push(new Line());

		// The canvases used for displaying text
		// cleanTextLines[0] refers to the root canvas; tables write into child
		// canvases that are created for every table line and merged with the
		// root canvas at the end of a table
		cleanTextLines = new Stack<>();
		cleanTextLines.push(new ArrayList<>());

		currentTable = new Stack<>();
		liCounter = new Stack<>();
		liLevel = 0;
		lastCaption = null;

		// used if ParserConfig#displayLinks is enabled
		linkTarget = "";

		parseHtmlTree(document);

		if (currentLine.peek() != null) {
			writeLine(false);
		}
	}

	/**
	 * Return the text representation of the HTML content.
	 *
	 * @return the text representation of the HTML content
	 */
	public String getText() {

		String text = cleanTextLines.stream()
			.flatMap(lines -> lines.stream())
			.collect(Collectors.joining("\n"));

		return StringUtils.stripTrailing(text);
	}

	/**
	 * Apply the attributes to the given HTML element.
	 *
	 * @param attributes the list of attributes
	 * @param htmlElement the HTML element for which the attributes are parsed
	 */
	private HtmlElement applyAttributes(NamedNodeMap attributes, HtmlElement htmlElement) {

		for (int index = 0; index < attributes.getLength(); index++) {
			Node attribute = attributes.item(index);
			String name = attribute.getNodeName();
			String value = attribute.getNodeValue();

			switch (name) {
				case "style":
					CssParse.attrStyle(value, htmlElement);
					break;
				case "align":
					CssParse.attrHorizontalAlign(value, htmlElement);
					break;
				case "valign":
					CssParse.attrVerticalAlign(value, htmlElement);
					break;
				default:
					break;
			}
		}

		return htmlElement;
	}

	private void endA() {

		if (!linkTarget.isEmpty()) {
			currentLine.peek().addContent(String.format("](%s)", linkTarget));
		}
	}

	private void endOl() {

		liLevel--;
		liCounter.pop();
	}

	private void endTable() {

		if (!currentTable.isEmpty() && currentTable.peek().isTdOpen()) {
			endTd();
		}

		writeLine(false);

		Table table = currentTable.pop();
		writeLineVerbatim(table.getText());
	}

	private void endTd() {

		if (!currentTable.isEmpty() && currentTable.peek().isTdOpen()) {
			currentTable.peek().setTdOpen(false);
			writeLine(true);
			cleanTextLines.pop();
			currentLine.pop();
			nextLine.pop();
		}
	}

	private void endUl() {

		liLevel -= 1;
		liCounter.pop();
	}

	/**
	 * @return The bullet that corresponds to the given index.
	 */
	private String getBullet(int index) {
		return UL_COUNTER[index % UL_COUNTER.length];
	}

	/**
	 * Handle text belonging to HTML tags.
	 *
	 * @param data the text to process.
	 */
	private void handleData(String data) {

		HtmlElement curTag = currentTag.peek();
		if (curTag.getDisplay() == Display.NONE) {
			return;
		}

		// protect pre areas
		if (curTag.getWhitespace() == WhiteSpace.PRE) {
			data = "\0" + data + "\0";
		}

		// add prefix, if present
		data = curTag.getPrefix() + data + curTag.getSuffix();

		// determine whether to add this content to a table column or to a standard line
		currentLine.peek().addContent(data);
	}

	/**
	 * Handle HTML end tags.
	 *
	 * @param node the HTML end tag to process.
	 */
	private void handleEndTag(Node node) {

		HtmlElement curTag = currentTag.pop();
		nextLine.peek().setPadding(currentLine.peek().getPadding() - curTag.getPadding());
		currentLine.peek().setMarginAfter(Math.max(currentLine.peek().getMarginAfter(), curTag.getMarginAfter()));

		// flush text after display:block elements
		if (curTag.getDisplay() == Display.BLOCK) {
			// propagate the new padding to the current line, if nothing has been written
			if (!writeLine(false)) {
				currentLine.peek().setPadding(nextLine.peek().getPadding());
			}
		}

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
		HtmlElement htmlElement = config.getCss().getOrDefault(tag, DEFAULT_ELEMENT);
		HtmlElement curTag = currentTag.peek().getRefinedHtmlElement(htmlElement);
		applyAttributes(node.getAttributes(), curTag);

		currentTag.push(curTag);

		nextLine.peek().setPadding(currentLine.peek().getPadding() + curTag.getPadding());

		// flush text before display: block elements
		if (curTag.getDisplay() == Display.BLOCK) {
			if (!writeLine(false)) {
				int marginBefore = cleanTextLines.get(0).isEmpty()
					? 0
					: Math.max(currentLine.peek().getMarginBefore(), curTag.getMarginBefore());

				currentLine.peek().setMarginBefore(marginBefore);
				currentLine.peek().setPadding(nextLine.peek().getPadding());
			} else {
				currentLine.peek().setMarginAfter(Math.max(currentLine.peek().getMarginAfter(), curTag.getMarginAfter()));
			}
		}

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
		writeLine(true);
	}

	/**
	 * Parses the HTML tree.
	 *
	 * @param document the W3C document
	 */
	private void parseHtmlTree(Node node) {

		if (node.getNodeType() != Node.DOCUMENT_NODE &&
			node.getNodeType() != Node.ELEMENT_NODE &&
			node.getNodeType() != Node.TEXT_NODE) {
			return;
		}

		if (node.getNodeType() == Node.ELEMENT_NODE) {
			handleStartTag(node);
		}

		if (node.getNodeType() == Node.TEXT_NODE) {
			String text = node.getNodeValue();
			if (text != null && !text.isEmpty()) {
				handleData(text);
			}
		}

		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			parseHtmlTree(children.item(i));
		}

		if (node.getNodeType() == Node.ELEMENT_NODE) {
			handleEndTag(node);
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
			currentLine.peek().addContent("[");
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
			currentLine.peek().addContent(String.format("[%s]", imageText));
			lastCaption = imageText;
		}
	}

	private void startLi() {

		writeLine(false);

		Object bullet = liLevel > 0 ? liCounter.peek() : "* ";

		if (bullet instanceof Integer) {
			Integer bulletNumber = (Integer) liCounter.pop();
			liCounter.push(bulletNumber + 1);
			currentLine.peek().setListBullet(bulletNumber + ". ");
		} else {
			currentLine.peek().setListBullet(bullet.toString());
		}
	}

	private void startOl() {

		liCounter.push(1);
		liLevel++;
	}

	private void startTable() {
		currentTable.push(new Table());
	}

	private void startTd() {

		if (currentTable.isEmpty()) {
			return;
		}

		Table curTable = currentTable.peek();

		// check whether we need to cleanup a <td> tag that has not been closed yet
		if (curTable.isTdOpen()) {
			endTd();
		}

		// open td tag
		cleanTextLines.push(new ArrayList<>());
		currentLine.push(new Line());
		nextLine.push(new Line());

		curTable.addCell(
			cleanTextLines.peek(),
			currentTag.peek().getAlign(),
			currentTag.peek().getValign());

		curTable.setTdOpen(true);
	}

	private void startTr() {

		if (currentTable.isEmpty()) {
			return;
		}

		Table curTable = currentTable.peek();

		// check whether we need to cleanup a <td> tag that has not been closed yet
		if (curTable.isTdOpen()) {
			endTd();
		}

		curTable.addRow();
	}

	private void startUl() {

		liLevel++;
		liCounter.push(getBullet(liLevel - 1));
	}

	/**
	 * Writes the current line to the buffer, provided that there is any data to write.
	 *
	 * @param force if true, data will be written even if it's empty.
	 * @return {@code true}, if a line has been written, otherwise {@code false}.
	 */
	private boolean writeLine(boolean force) {

		// only write the line if it contains relevant content
		if (!force && StringUtils.isBlank(currentLine.peek().getContent())) {
			currentLine.peek().setMarginBefore(Math.max(currentLine.peek().getMarginBefore(), currentTag.peek().getMarginBefore()));
			return false;
		}

		String line = currentLine.peek().getText();
		cleanTextLines.peek().add(line);

		currentLine.pop();
		currentLine.push(nextLine.pop());
		nextLine.push(new Line());

		return true;
	}

	/**
	 * Writes the current buffer without any modifications.
	 *
	 * @param text the text to write.
	 */
	private void writeLineVerbatim(String text) {
		cleanTextLines.peek().add(text);
	}

}
