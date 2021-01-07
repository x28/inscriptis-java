[![Build Status](https://travis-ci.org/x28/inscriptis-java.svg?branch=master)](https://travis-ci.org/x28/inscriptis-java)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/ch.x28.inscriptis/inscriptis/badge.svg)](https://maven-badges.herokuapp.com/maven-central/ch.x28.inscriptis/inscriptis)
[![javadoc](https://javadoc.io/badge2/ch.x28.inscriptis/inscriptis/javadoc.svg)](https://javadoc.io/doc/ch.x28.inscriptis/inscriptis)

# inscriptis - HTML to text conversion library for Java

A Java-based HTML to text conversion library with support for nested tables and a subset of CSS. Please take a look at the [Rendering document](https://github.com/weblyzard/inscriptis/blob/master/RENDERING.md) for a demonstration of Inscriptis conversion quality.

This is a Java port of [inscriptis for Python](https://github.com/weblyzard/inscriptis).

## Getting Started

Here is a quick teaser of an application using inscriptis for Java:

```java
package example;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.w3c.dom.Document;

import ch.x28.inscriptis.Inscriptis;

public class Example {

	public static void main(String[] args) {

		String htmlContent = "<p style=\"margin-top:0px\">Hello World!</p>";

		// use jsoup to parse HTML and convert it to W3C Document (https://jsoup.org)
		Document document = W3CDom.convert(Jsoup.parse(htmlContent));

		Inscriptis inscriptis = new Inscriptis(document);
		String text = inscriptis.getText();

		System.out.println(text); // Hello World!
	}
}
```

## Maven configuration

Add the Maven dependency:

```xml
<dependency>
  <groupId>ch.x28.inscriptis</groupId>
  <artifactId>inscriptis</artifactId>
  <version>1.0</version>
</dependency>
```

## HTML parser

inscriptis requires a W3C document, so it's up to you which parser you choose. Here is a list of parsers that support a W3C document result.

### jsoup
https://jsoup.org/

### nu-validator HTML Parser
https://mvnrepository.com/artifact/nu.validator/htmlparser

## License

inscriptis for Java is an Open Source software released under the Apache License, Version 2.0
