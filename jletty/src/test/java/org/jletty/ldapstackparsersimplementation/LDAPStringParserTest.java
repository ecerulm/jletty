/*
 * $Id: LDAPStringParserTest.java,v 1.1 2006/02/12 19:18:49 ecerulm Exp $
 * Created on Jun 25, 2004
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.LDAPStringListener;
import org.jletty.ldapstackparsersimplementation.LDAPStringParser;
import org.jletty.ldapstackparsersimplementation.ParserException;
import org.jletty.util.HexUtils;



/**
 * @author $Author: ecerulm $
 *  
 */
public class LDAPStringParserTest extends ParserTestCase {

	public void testParseWrongUTF8() {
		final byte[] bufferWrongEncoding = HexUtils
				.fromHexString("04 0b 80 62 6a 65 63 74 63 6c 61 73 73");
		final Parser parser = new LDAPStringParser(new LDAPStringListener() {
			public void data(String value) {
				fail("It should throw an exception but parsed ->" + value);
			}
		});
		try {
			parser.parse(bufferWrongEncoding);
			fail("It should throw a parser exception");
		} catch (ParserException e) {
			// ignore
		}

	}

	public void testPrueba() {
	}

	protected Parser getParser() {
		final Parser parser = new LDAPStringParser(new LDAPStringListener() {
			public void data(String value) {
				setResult(value);
			}
		});
		return parser;
	}

	protected Object getExpectedResult() {
		return "objectclass";
	}

	protected byte[] getOkBuffer() {
		final byte[] buffer = HexUtils
				.fromHexString("04 0b 6f 62 6a 65 63 74 63 6c 61 73 73");

		return buffer;
	}
}