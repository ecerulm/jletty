package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.BERIntegerListener;
import org.jletty.ldapstackparsersimplementation.BERIntegerParser;
import org.jletty.ldapstackparsersimplementation.ParserException;
import org.jletty.util.HexUtils;



/**
 * @author rlm
 */
public class BERIntegerParserTest extends ParserTestCase {

	public void testPrueba() {
	}

	public void testTooBigInteger() {
		BERIntegerListener l = new BERIntegerListener() {
			public void data(int value) {
				fail("It throw an exception but parsed " + value);
			}
		};

		Parser parser = new BERIntegerParser(l);
		final byte[] berInteger_2147483648 = HexUtils
				.fromHexString("02 05 00 80 00 00 00");
		try {
			boolean completed = parser.parse(berInteger_2147483648);
			assertFalse("It should throw an exception but returned true",
					completed);
			fail("It should throw an exception");

		} catch (ParserException e) {
			// ok it should throw an exception
		}

	}

	public void testIndefiniteLength() {
		BERIntegerListener l = new BERIntegerListener() {
			public void data(int value) {
				fail("It throw an exception but parsed " + value);
			}
		};

		Parser parser = new BERIntegerParser(l);
		final byte[] berInteger_indefiniteLength = HexUtils
				.fromHexString("02 80 01");
		try {
			boolean completed = parser.parse(berInteger_indefiniteLength);
			assertFalse("It should throw an exception but returned true",
					completed);
			fail("It should throw an exception");

		} catch (ParserException e) {
			// ok it should throw an exception
		}

	}

	protected Parser getParser() {
		BERIntegerListener l = new BERIntegerListener() {
			public void data(int value) {
				setResult(new Integer(value));

			}
		};

		Parser parser = new BERIntegerParser(l);

		return parser;
	}

	protected byte[][] getOkBuffers() {
		final byte[] berInteger0 = HexUtils.fromHexString("020100");
		final byte[] berInteger127 = HexUtils.fromHexString("02017F");
		final byte[] berInteger128 = HexUtils.fromHexString("02020080");
		final byte[] berInteger256 = HexUtils.fromHexString("02020100");
		final byte[] berInteger_128 = HexUtils.fromHexString("020180");
		final byte[] berInteger_129 = HexUtils.fromHexString("0202FF7F");
		final byte[] berInteger_2147483647 = HexUtils
				.fromHexString("02 04 7F FF FF FF ");
		//		final byte[] berIntegerTooBig =
		// HexUtils.fromHexString("02050100000000");
		//		final byte[] berIntegerTooBig2 =
		// HexUtils.fromHexString("02820100000000");
		//		final byte[] berIntegerIndefiniteLength = HexUtils
		//				.fromHexString("02800100000000");
		return new byte[][] { berInteger0, berInteger127, berInteger128,
				berInteger256, berInteger_128, berInteger_129,
				berInteger_2147483647 };
	}

	protected Object[] getExpectedResults() {
		final Object integer0 = new Integer(0);
		final Object integer127 = new Integer(127);
		final Object integer128 = new Integer(128);
		final Object integer256 = new Integer(256);
		final Object integer_128 = new Integer(-128);
		final Object integer_129 = new Integer(-129);
		final Object integer_2147483647 = new Integer(2147483647);

		return new Object[] { integer0, integer127, integer128, integer256,
				integer_128, integer_129, integer_2147483647 };
	}

	//	public void testParse() {
	//
	//		parser.parse(berInteger0);
	//		assertEquals(0, _value);
	//
	//		parser.parse(berInteger127);
	//		assertEquals(127, _value);
	//
	//		parser.parse(berInteger128);
	//		assertEquals(128, _value);
	//
	//		parser.parse(berInteger256);
	//		assertEquals(256, _value);
	//
	//		parser.parse(berInteger_128);
	//		assertEquals(-128, _value);
	//
	//		parser.parse(berInteger_129);
	//		assertEquals(-129, _value);
	//
	//	}
	//
	//	public void testParseAsync() {
	//
	//		parseOneByOne(parser, berInteger0, 0);
	//		parseOneByOne(parser, berInteger127, 127);
	//		parseOneByOne(parser, berInteger128, 128);
	//		parseOneByOne(parser, berInteger256, 256);
	//		parseOneByOne(parser, berInteger_128, -128);
	//		parseOneByOne(parser, berInteger_129, -129);
	//	}
	//
	//	public void testParseException() {
	//
	//		byte[][] allcases = new byte[][] { berIntegerWrongTag,
	//				berIntegerTooBig, berIntegerTooBig2, berIntegerIndefiniteLength };
	//
	//		for (int i = 0; i < allcases.length; i++) {
	//			byte[] b = allcases[i];
	//			try {
	//				parser.parse(b);
	//				fail("Parsing buffer " + i + " should trow a ParserException");
	//			} catch (ParserException e) {
	//				logger.debug("Exception", e);
	//			}
	//		}
	//		malformedMessage(berInteger0, parser, new Integer(0));
	//		malformedMessage(berInteger127, parser, new Integer(127));
	//		malformedMessage(berInteger128, parser, new Integer(128));
	//		malformedMessage(berInteger256, parser, new Integer(256));
	//		malformedMessage(berInteger_128, parser, new Integer(-128));
	//		malformedMessage(berInteger_129, parser, new Integer(-129));
	//	}
	//
	//	public void testParseAsyncException() {
	//
	//		byte[][] allcases = new byte[][] { berIntegerWrongTag,
	//				berIntegerTooBig, berIntegerTooBig2, berIntegerIndefiniteLength };
	//
	//		for (int i = 0; i < allcases.length; i++) {
	//			byte[] b = allcases[i];
	//			try {
	//				parseOneByOne(parser, b);
	//				fail("Parsing buffer " + i + " should trow a ParserException");
	//			} catch (ParserException e) {
	//				logger.debug("Exception", e);
	//			}
	//		}
	//
	//	}
	//
	//	private void parseOneByOne(Parser parser, byte[] buffer, int expected) {
	//		parseOneByOne(parser, buffer);
	//		assertEquals(expected, _value);
	//	}

	//	/*
	//	 * (non-Javadoc)
	//	 *
	//	 * @see junit.framework.TestCase#setUp()
	//	 */
	//	protected void setUp() throws Exception {
	//		super.setUp();
	//		l = new BERIntegerListener() {
	//			public void data(int value) {
	//				_value = value;
	//
	//			}
	//		};
	//		parser = new BERIntegerParser(l);
	//	}

	//	/*
	//	 * (non-Javadoc)
	//	 *
	//	 * @see
	// com.rubenlaguna.ldapstack.ParserTestCase#malformed(java.lang.Object)
	//	 */
	//	protected boolean malformed(Object expected) {
	//		return new Integer(_value).equals(expected);
	//	}

}