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

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author Sascha Wolski
 * @author Matthias Hewelt
 * @author Manuel Schmidt
 */
public class LineTest {

	@Test
	public void testGetText() {

		// given
		Line line = new Line();
		line.addContent("Ehre sei Gott!");

		// when
		// then
		assertThat(line.getText()).isEqualTo("Ehre sei Gott!");

	}

	@Test
	public void testListBulletWithoutPadding() {

		// given
		Line line = new Line();
		line.addContent("Ehre sei Gott!");
		line.setListBullet("* ");

		// when
		// then
		assertThat(line.getText()).isEqualTo("* Ehre sei Gott!");
	}

	@Test
	public void testListBulletWithPadding() {

		// given
		Line line = new Line();
		line.addContent("Ehre sei Gott!");
		line.setListBullet("* ");
		line.setPadding(3);

		// when
		// then
		assertThat(line.getText()).isEqualTo(" * Ehre sei Gott!");
	}

	@Test
	public void testMargin() {

		// given
		Line line = new Line();
		line.addContent("Ehre sei Gott!");
		line.setMarginBefore(1);
		line.setMarginAfter(2);

		// when
		// then
		assertThat(line.getText()).isEqualTo("\nEhre sei Gott!\n\n");
	}

	@Test
	public void testPrefixAndSuffix() {

		// given
		Line line = new Line();
		line.addContent("Ehre sei Gott!");
		line.setPrefix(">>");
		line.setSuffix("<<");

		// when
		// then
		assertThat(line.getText()).isEqualTo(">>Ehre sei Gott!<<");
	}

}
