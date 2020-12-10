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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents a line to render.
 *
 * @author Sascha Wolski
 * @author Matthias Hewelt
 */
class Line {

	private int marginBefore = 0;
	private int marginAfter = 0;
	private String prefix = "";
	private String suffix = "";
	private String content = "";
	private String listBullet = "";
	private int padding = 0;

	public void addContent(String content) {
		this.content += content;
	}

	public String getContent() {
		return content;
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

	public int getPadding() {
		return padding;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	/**
	 * @return the text representation of the current line.
	 */
	public String getText() {

		List<String> text = new ArrayList<>();

		if (!content.contains("\0")) {
			// standard text without any `WhiteSpace#PRE` formatted text.
			text.addAll(Arrays.asList(content.trim().split("\\s+")));
		} else {
			// content containing `WhiteSpace#PRE` formatted text
			content = content.replace("\0\0", "");

			String basePadding = StringUtils.repeat(" ", padding);

			int i = 0;
			for (String data : content.split("\0")) {
				if (i++ % 2 == 0) {
					// handle standard content
					// python extend filters empty elements
					List<String> d = Stream.of(data.trim().split("\\s+"))
						.filter(str -> !str.isEmpty())
						.collect(Collectors.toList());

					text.addAll(d);
				} else {
					// handle `WhiteSpace#PRE` formatted content.
					text.add(data.replaceAll("\n", "\n" + basePadding));
				}
			}
		}

		StringBuilder result = new StringBuilder()
			.append(StringUtils.repeat("\n", marginBefore))
			.append(StringUtils.repeat(" ", Math.max(0, padding - listBullet.length())))
			.append(listBullet)
			.append(prefix)
			.append(String.join(" ", text))
			.append(suffix)
			.append(StringUtils.repeat("\n", marginAfter));

		return result.toString();
	}

	/**
	 * Set the String that will be used as a bullet symbol in a list.
	 *
	 * @param listBullet the bullet to be used in a list.
	 */
	public void setListBullet(String listBullet) {
		this.listBullet = listBullet;
	}

	/**
	 * Set the amount of empty lines that will be added after the lines content.
	 *
	 * @param marginAfter the number of empty lines
	 */
	public void setMarginAfter(int marginAfter) {
		this.marginAfter = marginAfter;
	}

	/**
	 * Set the amount of empty lines that will be added before the lines content.
	 *
	 * @param marginBefore the number of empty lines.
	 */
	public void setMarginBefore(int marginBefore) {
		this.marginBefore = marginBefore;
	}

	/**
	 * Set the amount of horizontal padding (spaces) that will be used to intend the lines content. If a list bullet is
	 * used, the actual padding will be reduced by the amount of characters of this list bullet. This means the list
	 * bullet is handled as part of the padding.
	 *
	 * @param padding the amount of spaces to be added.
	 */
	public void setPadding(int padding) {
		this.padding = padding;
	}

	/**
	 * Set the String value that will be added in front of the lines content.
	 *
	 * @param prefix the string value to be added.
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Set the String value that will be added behind the lines content.
	 *
	 * @param suffix the string value to be added.
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}
