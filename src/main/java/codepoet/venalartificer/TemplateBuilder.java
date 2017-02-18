package codepoet.venalartificer;

import java.util.Map;

public class TemplateBuilder {

	private String templateLocation;

	public TemplateBuilder(final String templateLocation) {
		this.templateLocation = templateLocation;
	}

	public String render(final String template, final Map<String, Object> data) {
		return template;
	}
}
