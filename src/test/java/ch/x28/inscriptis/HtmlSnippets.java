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

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.stream.Stream;

/**
 * @author Manuel Schmidt
 */
public class HtmlSnippets {

	private static final Path PATH;
	private static final Map<String, Set<String>> NAMES;
	private static final String EXT_HTML = ".html";
	private static final String EXT_TXT = ".txt";

	static {
		try {
			PATH = Paths.get(HtmlSnippets.class.getClassLoader().getResource("snippets").toURI());
			NAMES = new HashMap<>();
			StringJoiner glob = new StringJoiner(",", "*{", "}");

			String[] exts = {
					EXT_HTML,
					EXT_TXT
			};

			for (String ext : exts) {
				NAMES.put(ext, new TreeSet<>());
				glob.add(ext);
			}

			try (DirectoryStream<Path> paths = Files.newDirectoryStream(PATH, glob.toString())) {
				for (Path path : paths) {
					String filename = path.getFileName().toString();
					int extIndex = filename.lastIndexOf('.');
					String name = filename.substring(0, extIndex);
					String ext = filename.substring(extIndex);
					NAMES.get(ext).add(name);
				}
			}
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getHtmlContent(String name) {
		return getContent(name, EXT_HTML);
	}

	public static Stream<String> getHtmlNames() {
		return getNames(EXT_HTML);
	}

	public static String getTextContent(String name) {
		return getContent(name, EXT_TXT);
	}

	public static Stream<String> getTextNames() {
		return getNames(EXT_TXT);
	}

	private static boolean exists(String name, String ext) {
		return NAMES.get(ext).contains(name);
	}

	private static String getContent(String name, String ext) {

		String filename = name + ext;

		if (!exists(name, ext)) {
			throw new RuntimeException("Snippet file \"" + filename + "\" does not exist");
		}

		Path path = PATH.resolve(filename);

		try {
			return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static Stream<String> getNames(String ext) {
		return NAMES.get(ext).stream();
	}

}
