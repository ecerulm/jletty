/*
 * Created on 24-ene-2005
 *
 */
package org.jletty.db.schema;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.jletty.schema.AttributeType;
import org.jletty.schema.AttributeUsage;
import org.jletty.schema.ObjectClass;
import org.jletty.schema.Schema;
import org.jletty.schemaparser.SchemaLexer;
import org.jletty.schemaparser.SchemaParser;

import junit.framework.TestCase;
import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.TokenStreamSelector;

/**
 * @author Ruben
 * 
 */
public abstract class SchemaTestCase extends TestCase {

	protected Schema schema;

	private AttributeType nameAttrType;

	protected void setUp() throws Exception {
		super.setUp();
		schema = Schema.getInstance();
		schema.clear();
		nameAttrType = new AttributeType("2.5.4.41", new String[] { "name" },
				"", false, "", "", "", "caseIgnoreSubstringsMatch",
				"1.3.6.1.4.1.1466.115.121.1.15", 32768, false, false, false,
				AttributeUsage.USER_APPLICATIONS);
		schema.addAttributeType(nameAttrType);
		

	}

	protected AttributeType parseAttribute(final String fileName)
			throws RecognitionException, TokenStreamException {
		SchemaParser parser = createSchemaParser(fileName);
		AttributeType at = parser.attributetype(schema);
		// schema.resolveDependencies();
		return at;
	}

	protected ObjectClass parseObjectClass(final String fileName)
			throws RecognitionException, TokenStreamException {
		SchemaParser parser = createSchemaParser(fileName);
		ObjectClass at = parser.objectclass(schema);
		// schema.resolveDependencies();
		return at;
	}

	protected SchemaParser createSchemaParser(String filename) {
		InputStream is = getClass().getResourceAsStream(filename);

		// create filter lexer
		SchemaLexer mainLexer = new SchemaLexer(is);
		// create valuelexer attache to input state of filterlexer
		// FilterValueLexer valueLexer = new FilterValueLexer(mainLexer
		// .getInputState());
		TokenStreamSelector selector = new TokenStreamSelector();

		selector.addInputStream(mainLexer, "main");
		// selector.addInputStream(valueLexer, "valueLexer");
		SchemaParser parser = new SchemaParser(selector);
		parser.setSelector(selector);
		selector.select("main");
		return parser;
	}

	protected void checkAttributeRegisteredInSchema(AttributeType at,
			final List names) {
		for (Iterator iter = names.iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			assertEquals(
					"The attribute is not registered in the Schema with the name '"
							+ name + "'", at, schema.getAttributeType(name));
		}
	}

	protected void checkObjectclassInSchema(ObjectClass ob,
			final List names) {
		for (Iterator iter = names.iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			assertEquals(
					"The objectclass "+ob+" is not registered in the Schema with the name '"
							+ name + "'", ob, schema.getObjectClass(name));
		}
	}


}
