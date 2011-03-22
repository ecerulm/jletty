/*
 * $Id: AttributeValuesParserTest.java,v 1.1 2006/02/12 19:18:49 ecerulm Exp $
 * 
 * Created on May 14, 2004
 *  
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.AttributeValues;
import org.jletty.ldapstackldapops.AttributeValuesListener;
import org.jletty.ldapstackparsersimplementation.AttributeValuesParser;
import org.jletty.util.HexUtils;



//TODO add test for binary attributes like jpegphoto
/**
 * @author $Author: ecerulm $
 *  
 */
public class AttributeValuesParserTest extends ParserTestCase {
	public void testPrueba() {
	}
	
//	public void testSameValueTwice() {
//		final AttributeValuesListener l = new AttributeValuesListener() {
//
//			public void data(byte[][] values) {
//				//				System.out.println("Received values " + values);
//				fail("It should throw an exception but parsed " + values);
//			}
//		};
//
//		final Parser parser = new AttributeValuesParser(l);
//		
//		byte[] buffer = HexUtils.fromHexString("31 0a 04 03 74 6f 70 04 03 74 6f 70"); 
//		try {
//			parser.parse(buffer);
//			fail("It should throw an exception");
//			
//		} catch (ParserException e) {
//			// ok
//		}
//		
//		
//	}

	protected Parser getParser() {
		final AttributeValuesListener l = new AttributeValuesListener() {

			public void data(byte[][] values) {
				//				System.out.println("Received values " + values);
				setResult(values);
			}
		};

		final Parser parser = new AttributeValuesParser(l);
		return parser;
	}

	protected byte[][] getOkBuffers() {
		final byte[] berAttributeValues = HexUtils
				.fromHexString("31 1b 04 03 74 6f 70 04 14 6f"
						+ "72 67 61 6e 69 7a 61 74 69 6f"
						+ "6e 61 6c 50 65 72 73 6f 6e");
		final byte[] berAttributeValuesEmpty = HexUtils.fromHexString("31 00");
		return new byte[][] { berAttributeValues, berAttributeValuesEmpty };
	}

	protected Object[] getExpectedResults() {
		byte[][] attributeValues = new AttributeValues().add("top").add(
				"organizationalPerson").getValuesAsByteArray();
		byte[][] attributeValuesEmpty = new AttributeValues().getValuesAsByteArray();
		return new Object[] { attributeValues, attributeValuesEmpty };
	}

}