package codepoet.venalartificer;

import static codepoet.venalartificer.TemplateConstants.CLOSE_TAG;

public class TemplateUtil {

	public static String getTaggedContent(final String tagBlock, final String closeTag) {
		return tagBlock.substring(tagBlock.indexOf(CLOSE_TAG) + CLOSE_TAG.length(), tagBlock.indexOf(closeTag));
	}

	public static int getNextStartIndex(final String template, final String value) {
		return template.indexOf(value);
	}

	public static int getNextEndIndex(final String template, final String value) {
		return template.indexOf(value) + value.length();
	}
}
