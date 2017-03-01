package codepoet.venalartificer;

import codepoet.venalartificer.exception.VenalArtificerException;
import java.io.FileNotFoundException;
import java.util.Map;

public class TemplateBuilder {

	private static final String NEGATION = "!";
	private static final String OPEN_TAG = "{{";
	private static final String CLOSE_TAG = "}}";
	private static final String OPEN_IF = OPEN_TAG + "#if";
	private static final String CLOSE_IF = OPEN_TAG + "if#" + CLOSE_TAG;
	private final TemplateReader reader = new TemplateReader();

	private String path;

	public TemplateBuilder(final String path) {
		this.path = path;
	}

	public String render(final String name, final Map<String, Object> data) {

		try {
			String template = reader.read(path + "/" + name);

			//TODO: Map Data to Template
			template = buildConditionals(template, data);
			//Handle for
			//Handle values

			System.out.println(template);
			return template;
		} catch (FileNotFoundException ex) {
			throw new VenalArtificerException("Template File Not Found");
		}
	}

	public String buildConditionals(String template, final Map<String, Object> data) {
		if (!template.contains(OPEN_IF)) {
			return template;
		}

		Integer start = template.indexOf(OPEN_IF);
		Integer end = template.indexOf(CLOSE_IF) + CLOSE_IF.length();
		String conditional = template.substring(start, end);
		String content = conditional.substring(conditional.indexOf(CLOSE_TAG) + CLOSE_TAG.length(),
				conditional.indexOf(CLOSE_IF));
		String variable = conditional.substring(OPEN_IF.length() + 1, conditional.indexOf(CLOSE_TAG));
		Boolean show = variable.startsWith(NEGATION) ? !((Boolean) data.get(variable.substring(1))) : (Boolean) data.get(variable);

		if (show) {
			template = template.substring(0, start) + content + template.substring(end);
		} else {
			template = template.substring(0, start) + template.substring(end);
		}

		return buildConditionals(template, data);
	}
}
