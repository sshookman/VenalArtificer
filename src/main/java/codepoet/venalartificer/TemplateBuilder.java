package codepoet.venalartificer;

import static codepoet.venalartificer.TemplateConstants.*;
import static codepoet.venalartificer.TemplateUtil.*;
import codepoet.venalartificer.exception.VenalArtificerException;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

//TODO: Get it working, then clean this crap up
public class TemplateBuilder {

	private final TemplateReader templateReader = new TemplateReader();
	private String path;

	public TemplateBuilder(final String path) {
		this.path = path;
	}

	public String render(final String fileName, final Map<String, Object> data) {
		try {
			String template = templateReader.read(path, fileName);
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

		Integer start = getNextStartIndex(template, OPEN_IF);
		Integer end = getNextEndIndex(template, CLOSE_IF);
		String tagBlock = template.substring(start, end);

		String content = getTaggedContent(tagBlock, CLOSE_IF);
		String variable = tagBlock.substring(OPEN_IF.length() + 1, tagBlock.indexOf(CLOSE_TAG));
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

		Integer start = getNextStartIndex(template, OPEN_FOR);
		Integer end = getNextEndIndex(template, CLOSE_FOR);
		String tagBlock = template.substring(start, end);

		String content = getTaggedContent(tagBlock, CLOSE_FOR);
		String[] variables = tagBlock.substring(OPEN_FOR.length() + 1, tagBlock.indexOf(CLOSE_TAG)).split(":");
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

	private String buildValues(String template, final Map<String, Object> data) {
		return buildValues(template, data, OPEN_TAG, CLOSE_TAG);
	}
}
