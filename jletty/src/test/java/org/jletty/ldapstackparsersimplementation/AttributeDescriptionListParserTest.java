/*
 * $Id: AttributeDescriptionListParserTest.java,v 1.1 2006/02/12 19:18:49 ecerulm Exp $
 * Created on Jun 16, 2004
 */
package org.jletty.ldapstackparsersimplementation;

import java.util.ArrayList;
import java.util.List;

import org.jletty.ldapstackldapops.AttributeDescriptionListListener;
import org.jletty.ldapstackparsersimplementation.AttributeDescriptionListParser;
import org.jletty.util.HexUtils;



/**
 * @author $Author: ecerulm $
 *  
 */
public class AttributeDescriptionListParserTest extends ParserTestCase {

	public void testPrueba() {
	}

	protected Parser getParser() {
		final AttributeDescriptionListListener listener = new AttributeDescriptionListListener() {
			public void data(List value) {
				setResult(value);
			}
		};

		final AttributeDescriptionListParser parser = new AttributeDescriptionListParser(
				listener);

		return parser;

	}

	protected byte[][] getOkBuffers() {
		final byte[] berOk = HexUtils
				.fromHexString("30 08 04 02 63 6e 04 02 73 6e");
		final byte[] berEmpty = HexUtils.fromHexString("30 00");

		return new byte[][] { berOk, berEmpty };
	}

	protected Object[] getExpectedResults() {
		List expected = new ArrayList();
		expected.add("cn");
		expected.add("sn");
		return new Object[] { expected, new ArrayList() };
	}
}