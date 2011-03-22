/*
 * $Id: SubstringFilterParserTest.java,v 1.1 2006/02/12 19:18:49 ecerulm Exp $
 * Created on Jun 30, 2004
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.Filter;
import org.jletty.ldapstackldapops.FilterListener;
import org.jletty.ldapstackldapops.SubstringFilter;
import org.jletty.ldapstackldapops.SubstringValue;
import org.jletty.ldapstackparsersimplementation.ParserException;
import org.jletty.ldapstackparsersimplementation.SubstringFilterParser;
import org.jletty.util.HexUtils;



/**
 * @author $Author: ecerulm $
 *  
 */
public class SubstringFilterParserTest extends ParserTestCase {

	/**
	 * @param string
	 */
	public SubstringFilterParserTest(String string) {
		super(string);
	}

	public SubstringFilterParserTest() {
		super();
	}

	public void testPrueba() {
	}

	protected Parser getParser() {
		final SubstringFilterParser parser = new SubstringFilterParser(
				new FilterListener() {
					public void data(Filter filter) {
						setResult(filter);
					}
				});
		return parser;
	}

	protected Parser getExceptionParser() {
		final SubstringFilterParser parser = new SubstringFilterParser(
				new FilterListener() {
					public void data(Filter filter) {
						fail("It should throw an exception but parsed -> "
								+ filter);
					}
				});
		return parser;
	}

	protected byte[][] getOkBuffers() {
		byte[] bufferInitial = HexUtils
				.fromHexString("a4 12 04 0b 6f 62 6a 65 63 74 63 6c 61 73 73 30 03 80 01 74 ");
		byte[] bufferAny = HexUtils
				.fromHexString("a4 12 04 0b 6f 62 6a 65 63 74 63 6c 61 73 73 30 03 81 01 6f "); //objectclass=*o*
		byte[] bufferFinal = HexUtils
				.fromHexString("a4 12 04 0b 6f 62 6a 65 63 74 63 6c 61 73 73 30 03 82 01 6e "); //objectclass=*n
		byte[] bufferInitialAny = HexUtils
				.fromHexString("a4 0c 04 02 63 6e 30 06 80 01 4a 81 01 6f  "); // (cn=J*o*)
		byte[] bufferInitialAnyFinal = HexUtils
				.fromHexString("a4 0f 04 02 63 6e 30 09 80 01 4a 81 01 6f 82 01 6e  "); //(cn=J*o*n)
		byte[] bufferAnyAny = HexUtils
				.fromHexString("a4 15 04 0b 6f 62 6a 65 63 74 63 6c 61 73 73 30 06 81 01 6f 81 01 4f ");
		//two anys *o*O*
		byte[] bufferAnyFinal = HexUtils
				.fromHexString("a4 15 04 0b 6f 62 6a 65 63 74 63 6c 61 73 73 30 06 81 01 6f 82 01 6e  "); //(cn=*o*n)

		return new byte[][] { bufferInitial, bufferAny, bufferFinal,
				bufferInitialAny, bufferInitialAnyFinal, bufferAnyAny,
				bufferAnyFinal };
	}

	protected byte[][] getBuffersThatShouldThrowAnException() {
		byte[] wrongBufferInitial = HexUtils
		.fromHexString("a4 12 04 0b 6f 62 6a 65 63 74 63 6c 61 73 73 31 03 80 01 74 ");
	
		return new byte[][]{wrongBufferInitial};
	}

	protected Object[] getExpectedResults() {

		SubstringFilter expectedInitial = new SubstringFilter("objectclass",
				new SubstringValue("t",new String[0],null));

		SubstringFilter expectedAny = new SubstringFilter("objectclass",
				new SubstringValue(null, new String[]{"o"},null));

		SubstringFilter expectedFinal = new SubstringFilter("objectclass",
				new SubstringValue(null,new String[0],"n"));

		SubstringFilter expectedInitialAny = new SubstringFilter("cn",
				new SubstringValue("J",new String[] {"o"},null));

		SubstringFilter expectedInitialAnyFinal = new SubstringFilter("cn",
				new SubstringValue("J",new String[] {"o"},"n"));

		SubstringFilter expectedAnyAny = new SubstringFilter("objectclass",
				new SubstringValue(null,new String[] {"o","O"},null));
		
		SubstringFilter expectedAnyFinal = new SubstringFilter("objectclass",
				new SubstringValue(null,new String[] {"o"},"n"));

		return new Object[] { expectedInitial, expectedAny, expectedFinal,
				expectedInitialAny, expectedInitialAnyFinal, expectedAnyAny,
				expectedAnyFinal };
	}

	public void testParseInitialInitial() {
		// [initial] any [final] no se puede
		// tener varios initial seguidos
		byte[] buffer = HexUtils
				.fromHexString("a4 15 04 0b 6f 62 6a 65 63 74 63 6c 61 73 73 30 06 80 01 74 80 01 54");
		boolean completed = false;
		Parser parser = getExceptionParser();
		try {
			completed = parser.parse(buffer);
			fail("It should throw an exception");
		} catch (ParserException e) {
			//ok it should throw an exception
		}
		assertFalse(completed);
	}

	public void testParseAnyInitial() {
		// [initial] any [final] no se puede
		// tener varios any seguido de un initial
		byte[] buffer = HexUtils
				.fromHexString("a4 15 04 0b 6f 62 6a 65 63 74 63 6c 61 73 73 30 06 81 01 74 80 01 54");
		boolean completed = false;
		Parser parser = getExceptionParser();
		try {
			completed = parser.parse(buffer);
			fail("It should throw an exception");
		} catch (ParserException e) {
			//ok it should throw an exception
		}
		assertFalse(completed);
	}

	public void testParseFinalFinal() {
		// [initial] any [final] no se puede
		// tener varios dos final
		byte[] buffer = HexUtils
				.fromHexString("a4 15 04 0b 6f 62 6a 65 63 74 63 6c 61 73 73 30 06 82 01 74 82 01 54");
		boolean completed = false;
		Parser parser = getExceptionParser();
		try {
			completed = parser.parse(buffer);
			fail("It should throw an exception");
		} catch (ParserException e) {
			//ok it should throw an exception
		}
		assertFalse(completed);
	}

	public void testParseFinalAny() {
		//		 [initial] any [final] no se puede
		// tener varios un any despues de un final
		byte[] buffer = HexUtils
				.fromHexString("a4 15 04 0b 6f 62 6a 65 63 74 63 6c 61 73 73 30 06 82 01 74 81 01 54");
		boolean completed = false;
		Parser parser = getExceptionParser();
		try {
			completed = parser.parse(buffer);
			fail("It should throw an exception");
		} catch (ParserException e) {
			//ok it should throw an exception
		}
		assertFalse(completed);
	}

	public void testParseFinalInitial() {
		//		 [initial] any [final] no se puede
		// tener varios un initial despues de un final
		byte[] buffer = HexUtils
				.fromHexString("a4 15 04 0b 6f 62 6a 65 63 74 63 6c 61 73 73 30 06 82 01 74 80 01 54");
		boolean completed = false;
		Parser parser = getExceptionParser();
		try {
			completed = parser.parse(buffer);
			fail("It should throw an exception");
		} catch (ParserException e) {
			//ok it should throw an exception
		}
		assertFalse(completed);
	}

	public void testWrongStreamAndContinue() {
		byte[] bufferWrong = HexUtils
				.fromHexString("a4 0f 04 02 63 6e 30 09 80 01 4a 81 01 6f 85 01 6e"); //(cn=J*o*n)

		boolean completed = false;
		Parser parser = getExceptionParser();
		try {
			completed = parser.parse(bufferWrong);
			fail("It should throw an exception");
		} catch (ParserException e) {
			//OK it should throw an exception
		}
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(new SubstringFilterParserTest("testPerformance"));
	}

}