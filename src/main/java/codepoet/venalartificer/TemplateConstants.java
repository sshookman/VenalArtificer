package codepoet.venalartificer;

public class TemplateConstants {

	public static final String OPEN_TAG = "{{";
	public static final String CLOSE_TAG = "}}";
	public static final String TAG_MARKER = "#";
	public static final String NEGATION = "!";
	public static final String IF = "if";
	public static final String FOR = "for";
	public static final String OPEN_IF = OPEN_TAG + TAG_MARKER + IF;
	public static final String CLOSE_IF = OPEN_TAG + IF + TAG_MARKER + CLOSE_TAG;
	public static final String OPEN_FOR = OPEN_TAG + TAG_MARKER + FOR;
	public static final String CLOSE_FOR = OPEN_TAG + FOR + TAG_MARKER + CLOSE_TAG;
}
