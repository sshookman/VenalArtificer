package codepoet.venalartificer;

import codepoet.venalartificer.exception.VenalArtificerException;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

//TODO: Get it working, then clean this crap up
public class TemplateBuilder {

	private static final String NEGATION = "!";
	private static final String OPEN_TAG = "{{";
	private static final String CLOSE_TAG = "}}";
	private static final String OPEN_IF = OPEN_TAG + "#if";
	private static final String CLOSE_IF = OPEN_TAG + "if#" + CLOSE_TAG;
	private static final String OPEN_FOR = OPEN_TAG + "#for";
	private static final String CLOSE_FOR = OPEN_TAG + "for#" + CLOSE_TAG;

	private final TemplateReader reader = new TemplateReader();

	private String path;

	public TemplateBuilder(final String path) {
		this.path = path;
	}

	public String render(final String name, final Map<String, Object> data) {

		try {
			String template = reader.read(path + "/" + name);
			template = buildConditionals(template, data);
			template = buildLoops(template, data);
			template = buildValues(template, data);

			System.out.println(template);
			return template;
		} catch (FileNotFoundException ex) {
			throw new VenalArtificerException("Template File Not Found");
		}
	}

	private String buildConditionals(String template, final Map<String, Object> data) {
		if (!template.contains(OPEN_IF)) {
			return template;
		}

		Integer start = template.indexOf(OPEN_IF);
		Integer end = template.indexOf(CLOSE_IF) + CLOSE_IF.length();
		String conditional = template.substring(start, end);

		String content = conditional.substring(conditional.indexOf(CLOSE_TAG) + CLOSE_TAG.length(), conditional.indexOf(CLOSE_IF));
		String variable = conditional.substring(OPEN_IF.length() + 1, conditional.indexOf(CLOSE_TAG));
		Boolean show = variable.startsWith(NEGATION) ? !((Boolean) data.get(variable.substring(1))) : (Boolean) data.get(variable);

		if (show) {
			template = template.substring(0, start) + content + template.substring(end);
		} else {
			template = template.substring(0, start) + template.substring(end);
		}

		return buildConditionals(template, data);
	}

	private String buildLoops(String template, final Map<String, Object> data) {
		if (!template.contains(OPEN_FOR)) {
			return template;
		}

		Integer start = template.indexOf(OPEN_FOR);
		Integer end = template.indexOf(CLOSE_FOR) + CLOSE_FOR.length();
		String loop = template.substring(start, end);

		String content = loop.substring(loop.indexOf(CLOSE_TAG) + CLOSE_TAG.length(), loop.indexOf(CLOSE_FOR));
		String[] variables = loop.substring(OPEN_FOR.length() + 1, loop.indexOf(CLOSE_TAG)).split(":");
		String item = variables[0].trim();
		String list = variables[1].trim();

		List<Map<String, Object>> itemList = (List<Map<String, Object>>) data.get(list);
		StringBuilder contentBuilder = new StringBuilder();
		for (Map<String, Object> itemData : itemList) {
			String replacedContent = buildValues(content, itemData, OPEN_TAG + item + ".", CLOSE_TAG);
			contentBuilder.append(replacedContent);
		}

		template = template.substring(0, start) + contentBuilder.toString() + template.substring(end);
		return buildLoops(template, data);
	}

	private String buildValues(String template, final Map<String, Object> data) {
		return buildValues(template, data, OPEN_TAG, CLOSE_TAG);
	}

	private String buildValues(String template, final Map<String, Object> data, final String startsWith, final String endsWith) {
		if (!template.contains(OPEN_TAG)) {
			return template;
		}

		Integer start = template.indexOf(startsWith);
		Integer end = template.indexOf(endsWith) + endsWith.length();
		String value = template.substring(start, end);

		String variable = value.substring(value.indexOf(startsWith) + startsWith.length(), value.indexOf(endsWith));
		Object datum = data.get(variable);

		template = template.replace(value, datum.toString());
		return buildValues(template, data, startsWith, endsWith);
	}
}
