package codepoet.venalartificer;

import codepoet.venalartificer.exception.VenalArtificerException;
import java.io.FileNotFoundException;
import java.util.Map;

public class TemplateBuilder {

	private static final String EXTENSION = ".ven";
	private final TemplateReader reader = new TemplateReader();

	private String path;

	public TemplateBuilder(final String path) {
		this.path = path;
	}

	public static void main(String[] args) {

	}

	public String render(final String name, final Map<String, Object> data) {

		try {
			String template = reader.read(path + "/" + name + EXTENSION);

			//TODO: Map Data to Template
			return template;
		} catch (FileNotFoundException ex) {
			throw new VenalArtificerException("Template File Not Found");
		}
	}

}
