package codepoet.venalartificer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class TemplateBuilderTest {

	private static final TemplateBuilder TEMPLATE_BUILDER = new TemplateBuilder("templates");

	@Test
	public void testRender() {
		Map<String, Object> item = new HashMap<>();
		item.put("prop1", 1);
		item.put("prop2", 2);
		item.put("prop3", 3);

		List<Map<String, Object>> items = new ArrayList<>();
		items.add(item);
		items.add(item);
		items.add(item);

		Map<String, Object> data = new HashMap<>();
		data.put("num", 123);
		data.put("dub", 12.3);
		data.put("text", "Hello World");
		data.put("show", true);
		data.put("items", items);

		String response = TEMPLATE_BUILDER.render("test-template", data);
		assertNotNull(response);
		assertEquals(EXPECTED_RESPONSE, response);
	}

	private static final String EXPECTED_RESPONSE = ""
			+ "This is plain text\n"
			+ "Integer 123\n"
			+ "Double 12.3\n"
			+ "Text Hello World\n"
			+ "This shows up\n"
			+ "\n"
			+ "Item Prop 1 1\n"
			+ "Item Prop 2 2\n"
			+ "Item Prop 3 3\n"
			+ "Item Prop 1 1\n"
			+ "Item Prop 2 2\n"
			+ "Item Prop 3 3\n"
			+ "Item Prop 1 1\n"
			+ "Item Prop 2 2\n"
			+ "Item Prop 3 3\n";
}
