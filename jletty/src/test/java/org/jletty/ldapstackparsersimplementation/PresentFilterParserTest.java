/*
 * $Id: PresentFilterParserTest.java,v 1.1 2006/02/12 19:18:49 ecerulm Exp $
 * Created on Jun 24, 2004
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.Filter;
import org.jletty.ldapstackldapops.FilterListener;
import org.jletty.ldapstackldapops.PresentFilter;
import org.jletty.ldapstackparsersimplementation.ParserException;
import org.jletty.ldapstackparsersimplementation.PresentFilterParser;
import org.jletty.util.HexUtils;



/**
 * @author $Author: ecerulm $
 *  
 */
public class PresentFilterParserTest extends ParserTestCase {

	public void testPrueba() {
	}

	public void testWrongValue() {
		byte[] wrongTagBuffer = HexUtils
				.fromHexString("87 0b 80 62 6a 65 63 74 63 6c 61 73 73");
		Parser parser = new PresentFilterParser(new FilterListener() {
			public void data(Filter f) {
				fail("It should throw an exception but parser -> " + f);
			}
		});
		try {
			parser.parse(wrongTagBuffer);
			fail("It should throw a ParserException");

		} catch (ParserException e) {
			// ignore
			//e.printStackTrace();
		}

	}

	protected Parser getParser() {
		Parser parser = new PresentFilterParser(new FilterListener() {
			public void data(Filter f) {
				setResult(f);
			}
		});
		return parser;
	}

	protected byte[] getOkBuffer() {
		final byte[] buffer = HexUtils
				.fromHexString("87 0b 6f 62 6a 65 63 74 63 6c 61 73 73");
		return buffer;
	}

	protected Object getExpectedResult() {
		final PresentFilter expectedResult = new PresentFilter("objectclass");
		return expectedResult;
	}
}