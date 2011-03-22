/*
 * Created on 18-ago-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;


import org.easymock.MockControl;
import org.jletty.ldapstackldapops.Filter;
import org.jletty.ldapstackldapops.FilterListener;
import org.jletty.ldapstackldapops.GreaterOrEqualFilter;
import org.jletty.util.HexUtils;


/**
 * @author Administrator
 *  
 */
public class greaterOrEqualFilterParserTest extends ParserTestCase {

	public void testPrueba() {
		// placeholder to make eclipse junit recognize this as a runnable unit
		// test
	}

	/**
	 * This test checks funcionality in AttributeValueAssertionParser
	 */
	public void testAttrDescException() {
		FilterListener listener = new FilterListener() {
			public void data(Filter value) {
				setResult(value);
			}
		};

		byte[] buffer = getOkBuffer();
		GreaterOrEqualFilterParser parser = new GreaterOrEqualFilterParser(
				listener);
		final MockControl control = MockControl.createNiceControl(Parser.class);
		final Parser mock = (Parser) control.getMock();
		parser.setAttrDescParser(mock);
		mock.parse(ByteBuffer.allocate(1));
		control.setMatcher(MockControl.ALWAYS_MATCHER);
		control.setThrowable(new ParserException("forged exception"));
		control.replay();

		try {
			boolean b = parser.parse(buffer);
			fail("It should throw an exception but returned " + b +" instead and resultReceived " + resultReceived);
		} catch (ParserException e) {
			//ok exception expected
		}
		testSync();
	}

	/**
	 * This test checks funcionality in AttributeValueAssertionParser
	 */
	public void testAssertionValueException() {
		FilterListener listener = new FilterListener() {
			public void data(Filter value) {
				setResult(value);
			}
		};

		byte[] buffer = getOkBuffer();
		GreaterOrEqualFilterParser parser = new GreaterOrEqualFilterParser(
				listener);
		final MockControl control = MockControl.createNiceControl(Parser.class);
		final Parser mock = (Parser) control.getMock();
		parser.setAssertionValueParser(mock);
		mock.parse(ByteBuffer.allocate(1));
		control.setMatcher(MockControl.ALWAYS_MATCHER);
		control.setThrowable(new ParserException("forged exception"));
		control.replay();

		try {
			parser.parse(buffer);
			fail("It should throw an exception");
		} catch (ParserException e) {
			//ok exception expected
		}
		testSync();
	}

	// [start]
	/**
	 * @return
	 */
	protected final byte[] getOkBuffer() {
		final String dnQualifierGreaterOrEqualJorge = "a5 14  04 0b 64 6e 51 75 61 6c 69 66 69 65 72 04 05 4a 6f 72 67 65";
		
		return HexUtils.fromHexString(dnQualifierGreaterOrEqualJorge);
	}

	/**
	 * @return
	 */
	protected Object getExpectedResult() {
		GreaterOrEqualFilter toReturn = new GreaterOrEqualFilter("dnQualifier", "Jorge"
				.getBytes());
		return toReturn;
	}

	/**
	 * @see org.jletty.ldapstackparsers.implementation.ParserTestCase#getParser()
	 */
	protected Parser getParser() {
		FilterListener listener = new FilterListener() {
			public void data(Filter value) {
				setResult(value);
			}
		};
		Parser parser = new GreaterOrEqualFilterParser(listener);
		return parser;
	}

	protected byte getWrongTag() {

		return (byte) 0xa4;
	}

	// [end]

}