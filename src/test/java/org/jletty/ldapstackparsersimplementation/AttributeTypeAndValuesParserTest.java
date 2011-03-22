/*
 * $Id: AttributeTypeAndValuesParserTest.java,v 1.1 2006/02/12 19:18:49 ecerulm Exp $
 * Created on May 20, 2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.AttributeTypeAndValues;
import org.jletty.ldapstackldapops.AttributeTypeAndValuesListener;
import org.jletty.ldapstackldapops.AttributeValues;
import org.jletty.ldapstackparsersimplementation.AttributeTypeAndValuesParser;
import org.jletty.util.HexUtils;



/**
 * @author $Author: ecerulm $
 * 
 *  
 */
public class AttributeTypeAndValuesParserTest extends ParserTestCase {
	public void testPrueba() {
	}

	protected Parser getParser() {
		final AttributeTypeAndValuesListener l = new AttributeTypeAndValuesListener() {

			public void data(AttributeTypeAndValues value) {
				//System.out.println("Received value " + value);
				setResult(value);
			}
		};
		final Parser parser = new AttributeTypeAndValuesParser(l);
		return parser;
	}

	protected byte[][] getOkBuffers() {
		final byte[] berAttributeLocation = HexUtils.fromHexString("30"
				+ "12 04 01 6c 31 0d 04 0b 4d 79"
				+ "20 6c 6f 63 61 74 69 6f 6e");

		final byte[] berAttributeOC = HexUtils.fromHexString("30 2a 04 0b 6f"
				+ "62 6a 65 63 74 43 6c 61 73 73"
				+ "31 1b 04 03 74 6f 70 04 14 6f"
				+ "72 67 61 6e 69 7a 61 74 69 6f"
				+ "6e 61 6c 50 65 72 73 6f 6e");
		return new byte[][] { berAttributeOC, berAttributeLocation };
	}
	


	protected Object[] getExpectedResults() {
		AttributeTypeAndValues attributeOC = new AttributeTypeAndValues(
				"objectClass", new AttributeValues().add("top").add(
						"organizationalPerson").getValuesAsByteArray());
		AttributeTypeAndValues attributeLocation = new AttributeTypeAndValues(
				"l", new AttributeValues().add("My location").getValuesAsByteArray());
		return new Object[] { attributeOC, attributeLocation };
	}
}