package codepoet.venalartificer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TemplateReader {

	private static final String EXTENSION = ".ven";

	public String read(final String name) throws FileNotFoundException {
		StringBuilder result = new StringBuilder("");
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(name + EXTENSION).getFile());
		Scanner scanner = new Scanner(file);

		while (scanner.hasNextLine()) {
			result.append(scanner.nextLine()).append("\n");
		}

		scanner.close();
		return result.toString();
	}
}
