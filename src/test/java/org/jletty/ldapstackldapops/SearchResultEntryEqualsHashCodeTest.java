package org.jletty.ldapstackldapops;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import org.jletty.ldapstackparsersimplementation.SearchRequestParserTest;


import org.jletty.schema.AttributeType;
import org.jletty.schema.AttributeUsage;
import org.jletty.schema.AttributeValue;
import org.jletty.schema.Schema;
import org.jletty.schema.Syntax;
import org.jletty.schema.Syntaxes;
import org.jletty.schemaparser.Parser;
import org.jletty.util.EqualsHashCodeTestCase;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

public class SearchResultEntryEqualsHashCodeTest extends EqualsHashCodeTestCase {


	@BeforeClass
	public static void setUp() throws Exception {
		//ensure that the Schema is populated before creating instances (instances are created during super.setUp() invocation.
		Schema schema = Schema.getInstance();
		schema.clear();
		AttributeType nameAttrType = new AttributeType("2.5.4.41",
				new String[] { "name" }, "", false, "", "", "",
				"caseIgnoreSubstringsMatch", "1.3.6.1.4.1.1466.115.121.1.15",
				32768, false, false, false, AttributeUsage.USER_APPLICATIONS);
		schema.addAttributeType(nameAttrType);
		InputStream schemaStream = SearchResultEntryEqualsHashCodeTest.class.getResourceAsStream(
		"/org/jletty/db/schema/schemas/core.schema");
		assertNotNull(schemaStream);
		
		Parser.parse(schemaStream, schema);
		schema.resolveDependencies();
	}

	protected Object createInstance() throws Exception {
		AttributeTypeAndValuesList attributeList = new AttributeTypeAndValuesList();
		attributeList.addAll(createAttrsForO_org1_C_US());
		return new SearchResultEntry("c=US", attributeList);
	}

	protected Object createNotEqualInstance() throws Exception {
		AttributeTypeAndValuesList attributeList = new AttributeTypeAndValuesList();
		attributeList.addAll(createAttrsForC_US());
		return new SearchResultEntry("c=US1", attributeList);
	}

	private Attributes createAttrsForC_US() throws LdapUndefinedAttributeType,
			NamingException, UnsupportedEncodingException {
		Attributes attrs;
		attrs = new BasicAttributes();
		attrs.put(new BasicAttribute("c", "US"));
		attrs.put(new BasicAttribute("objectClass", "country"));
		attrs.put(new BasicAttribute("searchGuide", "2.5.6.2#(c$EQ)"));
		attrs.put(new BasicAttribute("description",
				"This is a country description"));
		attrs = convertAttrsValuesToOctetStringable(attrs);
		return attrs;
	}

	private static Attributes convertAttrsValuesToOctetStringable(
			Attributes inputAttrs) throws LdapUndefinedAttributeType,
			NamingException, UnsupportedEncodingException {
		Attributes attrs = new BasicAttributes(true); // case-ignore
		// Iterator it = attrList.iterator();
		NamingEnumeration all = inputAttrs.getAll();
		while (all.hasMoreElements()) {
			Attribute attrTaV = (Attribute) all.nextElement();
			final String attrDesc = attrTaV.getID();
			Attribute attr = new BasicAttribute(attrDesc);
			final AttributeType attributeType = Schema.getInstance()
					.getAttributeType(attrDesc);
			if (attributeType == null) {

				throw new LdapUndefinedAttributeType(attrDesc);
			}
			Syntax syntax = Syntaxes.getSyntax(attributeType.getSyntax());
			List tmp = new ArrayList();
			NamingEnumeration all2 = attrTaV.getAll();
			while (all2.hasMoreElements()) {
				String element = (String) all2.nextElement();
				tmp.add(element.getBytes("UTF-8"));
			}
			byte[][] values = (byte[][]) tmp.toArray(new byte[tmp.size()][]);

			if (values.length < 1) {
				throw new LdapInvalidAttributeSyntaxException("attribute "
						+ attrDesc + " cannot be empty");
			}

			for (int i = 0; i < values.length; i++) {
				try {
					byte[] value = values[i];
					AttributeValue matchable = syntax.get(value);
					attr.add(matchable);
				} catch (LdapInvalidAttributeSyntaxException e) {
					final LdapInvalidAttributeSyntaxException toThrow = new LdapInvalidAttributeSyntaxException(
							"value #" + i + " invalid per syntax");
					toThrow.initCause(e);
					throw toThrow;
				}
			}
			attrs.put(attr);
		}
		return attrs;
	}

	private Attributes createAttrsForCN_person1_O_org1_c_US()
			throws UnsupportedEncodingException, LdapUndefinedAttributeType,
			NamingException {
		Attributes attrs;
		attrs = new BasicAttributes();
		attrs.put("cn", "person1");
		attrs.put("objectclass", "person");
		attrs.put("sn", "personsn1");
		attrs.put("userPassword", "{crypt}X5/DBrWPOQQaI");
		attrs.put("telephoneNumber", "+41 1 268 1540");
		attrs.put("seeAlso", "CN=Beverly Pyke, O=ISODE Consortium, C=GB");
		attrs.put("description", "the person1 description");
		attrs = convertAttrsValuesToOctetStringable(attrs);
		return attrs;
	}

	private Attributes createAttrsForO_org1_C_US()
			throws UnsupportedEncodingException, LdapUndefinedAttributeType,
			NamingException {
		Attributes attrs;
		attrs = new BasicAttributes();
		attrs.put("o", "org1");
		attrs.put("objectClass", "organization");
		attrs.put("userPassword", "{crypt}X5/DBrWPOQQaI");
		attrs.put("searchGuide", "2.5.6.4#(o$EQ)");
		attrs.put("seeAlso", "CN=Beverly Pyke, O=ISODE Consortium, C=GB");
		attrs.put("businessCategory",
				"description of the business performed by the organization");
		attrs.put("x121Address", "031344159782738"); // An X.121 address
		// contains between 1
		// and 15 decimal
		// digits, such as
		// 031344159782738.
		attrs.put("registeredAddress", "1234 Main St.$Anytown, CA 12345$USA");
		attrs.put("destinationIndicator", "Stow, Ohio, USA"); // The country
		// and city associated
		// with the
		// entry needed
		// to provide
		// Public
		// Telegram
		// Service.
		// Generally
		// used in
		// conjunction
		// with
		// registeredAddress.
		attrs.put("preferredDeliveryMethod", "telephone");
		attrs.put("telexNumber", "817379, ch, ehhg");
		attrs.put("teletexTerminalIdentifier", "415-555-2233");
		attrs.put("telephoneNumber", "+41 1 268 1540");
		// attrs.put("internationaliSDNNumber","+SO 812467"); // Sun Directory
		// Server 5.2 uses IA5String for internationaliSDNNumber
		attrs.put("internationaliSDNNumber", "812467");
		attrs.put("facsimileTelephoneNumber", "+41 1 268 1540");
		attrs.put("street", "Limmatquai 138");
		attrs.put("postOfficeBox", "P.O. Box 1234");
		attrs.put("postalCode", "44224");
		attrs.put("postalAddress", "1234 Ridgeway Drive$Santa Clara, CA$99555");
		attrs.put("physicalDeliveryOfficeName", "Santa Clara");
		attrs.put("st", "California");
		attrs.put("l", "Santa Clara");
		attrs.put("description", "the organization description");
		attrs = convertAttrsValuesToOctetStringable(attrs);
		return attrs;
	}

}
