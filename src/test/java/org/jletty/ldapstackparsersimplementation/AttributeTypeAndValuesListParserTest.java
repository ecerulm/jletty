/*
 * $Id: AttributeTypeAndValuesListParserTest.java,v 1.1 2006/02/12 19:18:49 ecerulm Exp $ Created on May 23, 2004
 */
package org.jletty.ldapstackparsersimplementation;

import java.util.ArrayList;
import java.util.List;

import org.jletty.ldapstackldapops.AttributeTypeAndValues;
import org.jletty.ldapstackldapops.AttributeTypeAndValuesList;
import org.jletty.ldapstackldapops.AttributeTypeAndValuesListListener;
import org.jletty.ldapstackldapops.AttributeValues;
import org.jletty.ldapstackparsersimplementation.AttributeTypeAndValuesListParser;
import org.jletty.util.HexUtils;

import junit.framework.TestCase;


/**
 * @author $Author: ecerulm $
 *  
 */
public class AttributeTypeAndValuesListParserTest extends ParserTestCase {

	public void testPrueba() {
	}

	AttributeTypeAndValuesList attrList = null;

	public void testEquality() {
		List l = createAttributeListEq1();
		AttributeTypeAndValuesList eq1 = new AttributeTypeAndValuesList(l);

		l = createAttributeListEq2();
		AttributeTypeAndValuesList eq2 = new AttributeTypeAndValuesList(l);

		assertEquals(eq1, eq2);

	}

	/**
	 * @see TestCase#setUp()
	 */
	//	protected void setUp() throws Exception {
	//		super.setUp();
	//		attrList = null;
	//		List l = new ArrayList();
	//		AttributeValues values;
	//		values = new AttributeValues();
	//		values.add("top");
	//		values.add("organizationalPerson");
	//		l.add(new AttributeTypeAndValues("objectClass", values));
	//		values = new AttributeValues();
	//		values.add("Jorge Jetson");
	//		l.add(new AttributeTypeAndValues("cn", values));
	//		values = new AttributeValues();
	//		values.add("Jetson");
	//		l.add(new AttributeTypeAndValues("sn", values));
	//		values = new AttributeValues();
	//		values.add("System Administrator");
	//		l.add(new AttributeTypeAndValues("title", values));
	//		values = new AttributeValues();
	//		values.add("{MD5aEaBZG/SVbRccU/UYbzxCg==");
	//		l.add(new AttributeTypeAndValues("userPassword", values));
	//		values = new AttributeValues();
	//		values.add("000-000-1234");
	//		l.add(new AttributeTypeAndValues("telephoneNumber", values));
	//		values = new AttributeValues();
	//		values.add("000-111-1234");
	//		l.add(new AttributeTypeAndValues("facsimileTelephoneNumber", values));
	//		values = new AttributeValues();
	//		values.add("My address");
	//		l.add(new AttributeTypeAndValues("postalAddress", values));
	//		values = new AttributeValues();
	//		values.add("System Admin");
	//		l.add(new AttributeTypeAndValues("description", values));
	//		values = new AttributeValues();
	//		values.add("My location");
	//		l.add(new AttributeTypeAndValues("l", values));
	//		expectedResult = new AttributeTypeAndValuesList(l);
	//	}
	protected Parser getParser() {
		final AttributeTypeAndValuesListParser parser = new AttributeTypeAndValuesListParser(
				new AttributeTypeAndValuesListListener() {

					public void data(AttributeTypeAndValuesList value) {
						setResult(value);
					}
				});
		return parser;
	}

	protected Object getExpectedResult() {
		List l = new ArrayList();
		AttributeValues values;
		values = new AttributeValues();
		values.add("top");
		values.add("organizationalPerson");
		l.add(new AttributeTypeAndValues("objectClass", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("Jorge Jetson");
		l.add(new AttributeTypeAndValues("cn", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("Jetson");
		l.add(new AttributeTypeAndValues("sn", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("System Administrator");
		l.add(new AttributeTypeAndValues("title", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("{MD5aEaBZG/SVbRccU/UYbzxCg==");
		l.add(new AttributeTypeAndValues("userPassword", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("000-000-1234");
		l.add(new AttributeTypeAndValues("telephoneNumber", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("000-111-1234");
		l.add(new AttributeTypeAndValues("facsimileTelephoneNumber", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("My address");
		l.add(new AttributeTypeAndValues("postalAddress", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("System Admin");
		l.add(new AttributeTypeAndValues("description", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("My location");
		l.add(new AttributeTypeAndValues("l", values.getValuesAsByteArray()));

		return new AttributeTypeAndValuesList(l);
	}

	private List createAttributeListEq1() {
		List l = new ArrayList();
		AttributeValues values;
		values = new AttributeValues();
		values.add("top");
		values.add("organizationalPerson");
		l.add(new AttributeTypeAndValues("objectClass", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("Jorge Jetson");
		l.add(new AttributeTypeAndValues("cn", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("Jetson");
		l.add(new AttributeTypeAndValues("sn",values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("System Administrator");
		l.add(new AttributeTypeAndValues("title", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("{MD5aEaBZG/SVbRccU/UYbzxCg==");
		l.add(new AttributeTypeAndValues("userPassword", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("000-000-1234");
		l.add(new AttributeTypeAndValues("telephoneNumber", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("000-111-1234");
		l.add(new AttributeTypeAndValues("facsimileTelephoneNumber", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("My address");
		l.add(new AttributeTypeAndValues("postalAddress", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("System admin");
		l.add(new AttributeTypeAndValues("description", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("My location");
		l.add(new AttributeTypeAndValues("l", values.getValuesAsByteArray()));
		return l;
	}

	private List createAttributeListEq2() {
		List l;
		l = new ArrayList();
		AttributeValues values;
		values = new AttributeValues();
		values.add("top");
		values.add("organizationalPerson");
		l.add(new AttributeTypeAndValues("objectClass", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("Jorge Jetson");
		l.add(new AttributeTypeAndValues("cn", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("Jetson");
		l.add(new AttributeTypeAndValues("sn", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("System Administrator");
		l.add(new AttributeTypeAndValues("title", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("{MD5aEaBZG/SVbRccU/UYbzxCg==");
		l.add(new AttributeTypeAndValues("userPassword", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("000-000-1234");
		l.add(new AttributeTypeAndValues("telephoneNumber", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("000-111-1234");
		l.add(new AttributeTypeAndValues("facsimileTelephoneNumber", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("My address");
		l.add(new AttributeTypeAndValues("postalAddress", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("System admin");
		l.add(new AttributeTypeAndValues("description", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("My location");
		l.add(new AttributeTypeAndValues("l", values.getValuesAsByteArray()));
		return l;
	}

	protected byte[] getOkBuffer() {
		final byte[] berAttributeList = HexUtils
				.fromHexString("30 82 01 44 30 2a 04 0b 6f"
						+ "62 6a 65 63 74 43 6c 61 73 73"
						+ "31 1b 04 03 74 6f 70 04 14 6f"
						+ "72 67 61 6e 69 7a 61 74 69 6f"
						+ "6e 61 6c 50 65 72 73 6f 6e 30"
						+ "14 04 02 63 6e 31 0e 04 0c 4a"
						+ "6f 72 67 65 20 4a 65 74 73 6f"
						+ "6e 30 0e 04 02 73 6e 31 08 04"
						+ "06 4a 65 74 73 6f 6e 30 1f 04"
						+ "05 74 69 74 6c 65 31 16 04 14"
						+ "53 79 73 74 65 6d 20 41 64 6d"
						+ "69 6e 69 73 74 72 61 74 6f 72"
						+ "30 2e 04 0c 75 73 65 72 50 61"
						+ "73 73 77 6f 72 64 31 1e 04 1c"
						+ "7b 4d 44 35 61 45 61 42 5a 47"
						+ "2f 53 56 62 52 63 63 55 2f 55"
						+ "59 62 7a 78 43 67 3d 3d 30 21"
						+ "04 0f 74 65 6c 65 70 68 6f 6e"
						+ "65 4e 75 6d 62 65 72 31 0e 04"
						+ "0c 30 30 30 2d 30 30 30 2d 31"
						+ "32 33 34 30 2a 04 18 66 61 63"
						+ "73 69 6d 69 6c 65 54 65 6c 65"
						+ "70 68 6f 6e 65 4e 75 6d 62 65"
						+ "72 31 0e 04 0c 30 30 30 2d 31"
						+ "31 31 2d 31 32 33 34 30 1d 04"
						+ "0d 70 6f 73 74 61 6c 41 64 64"
						+ "72 65 73 73 31 0c 04 0a 4d 79"
						+ "20 61 64 64 72 65 73 73 30 1d"
						+ "04 0b 64 65 73 63 72 69 70 74"
						+ "69 6f 6e 31 0e 04 0c 53 79 73"
						+ "74 65 6d 20 41 64 6d 69 6e 30"
						+ "12 04 01 6c 31 0d 04 0b 4d 79"
						+ "20 6c 6f 63 61 74 69 6f 6e");
		return berAttributeList;
	}

}