package org.jletty.schema;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jletty.schemaparser.SchemaLexer;
import org.jletty.schemaparser.SchemaParser;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.TokenStreamSelector;

/*
 * Created on 07-ene-2005
 *
 */

/**
 * @author Ruben
 * 
 */
public final class Schema {

	private Map attributeTypesByName = new HashMap();

	private Map attributeTypesByOid = new HashMap();

	private Map objectclasses = new HashMap();

	private static Schema theInstance;

	private Schema() {
		clear();
	}

	public static Schema getInstance() {
		if (theInstance == null) {
			theInstance = new Schema();
		}
		return theInstance;
	}

	public void clear() {
		this.attributeTypesByName.clear();
		this.attributeTypesByOid.clear();
		this.objectclasses.clear();
		addObjectclass(new ObjectClass("1.3.6.1.3.1", new String[] { "top" },
				"top class", null, false, ObjectClassType.ABSTRACT, null, null));
	}

	public void addAttributeType(AttributeType attrType) {
		// TODO test already exists
		String[] names = attrType.getNames();
		this.attributeTypesByOid.put(attrType.getNumericoid(), attrType);
		for (int i = 0; i < names.length; i++) {
			this.attributeTypesByName.put(names[i].toLowerCase(), attrType);
		}
	}

	public void addObjectclass(ObjectClass ob) {
		// TODO test already exists
		String[] names = ob.getNames();
		for (int i = 0; i < names.length; i++) {
			this.objectclasses.put(names[i].toLowerCase(), ob);
		}
	}

	public AttributeType getAttributeType(String name) {
		if (null == name) {
			return null;
		}
		AttributeType toReturn = (AttributeType) this.attributeTypesByName.get(name
				.toLowerCase());
		if (toReturn == null) {
			toReturn = (AttributeType) this.attributeTypesByOid.get(name
					.toLowerCase());
		}
		return toReturn;
	}

	public void resolveDependencies() {
		resolveAttributeDependencies();
		resolveObjectclassDependencies();
	}

	/**
	 * 
	 */
	private void resolveObjectclassDependencies() {
		{
			Collection values = this.objectclasses.values();
			for (Iterator iter = values.iterator(); iter.hasNext();) {
				ObjectClass oc = (ObjectClass) iter.next();
				String[] superClasses = oc.getSuperClasses();
				for (int i = 0; i < superClasses.length; i++) {
					String scName = superClasses[i];
					ObjectClass superClass = getObjectClass(scName);
					if (null == superClass) {
						throw new RuntimeException("Unresolved dependency "
								+ oc + " depends on " + scName
								+ " but it isn't defined");
					}
					oc.inherit(superClass);
				}
			}
		}
	}

	/**
	 * 
	 */
	private void resolveAttributeDependencies() {
		{
			Collection values = this.attributeTypesByName.values();
			for (Iterator iter = values.iterator(); iter.hasNext();) {
				AttributeType at = (AttributeType) iter.next();
				final String superAttrName = at.getSuperAttr();
				if (!"".equals(superAttrName) && superAttrName != null) {
					// has a superclass
					AttributeType superAttr = getAttributeType(superAttrName);
					if (null == superAttr) {
						throw new RuntimeException("Unresolved dependency "
								+ at + " depends on " + superAttrName
								+ " but it isn't defined");
					}
					at.inherit(superAttr);
				}
			}
		}
	}

	/**
	 * @param name
	 * @return
	 */
	public ObjectClass getObjectClass(String name) {

		return (ObjectClass) this.objectclasses.get(name.toLowerCase());
	}

	/**
	 * @param resourceName
	 * @return
	 * @throws RecognitionException
	 * @throws TokenStreamException
	 */
	public Schema loadSchemaFromResource(String resourceName)
			throws RecognitionException, TokenStreamException {
		Schema schema = Schema.getInstance();
		SchemaParser schemaParser = createSchemaParser(resourceName);
		schemaParser.schema(schema);
		schema.resolveDependencies();
		return schema;
	}

	protected SchemaParser createSchemaParser(String resourceName) {
		InputStream is = getClass().getResourceAsStream(resourceName);
		if (is == null) {
			throw new RuntimeException("No resouce found with name "
					+ resourceName);
		}
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

	public AttributeType getAttributeTypeByOid(String oid) {
		if (null == oid) {
			return null;
		}
		AttributeType toReturn = (AttributeType) this.attributeTypesByOid.get(oid);
		return toReturn;
	}

}
