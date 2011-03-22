/*
 * $Id: ExtensibleMatchFilterParserTest.java,v 1.1 2006/02/12 19:18:49 ecerulm Exp $
 * Created on Jun 24, 2004
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.ExtensibleMatchFilter;
import org.jletty.ldapstackldapops.Filter;
import org.jletty.ldapstackldapops.FilterListener;
import org.jletty.ldapstackparsersimplementation.ExtensibleMatchFilterParser;
import org.jletty.util.HexUtils;



/**
 * @author $Author: ecerulm $
 * 
 */
public class ExtensibleMatchFilterParserTest extends ParserTestCase {

	public void testPrueba() {
	}

	protected byte[] getOkBuffer() {
		final String tmp = "a9 1b 81 0f 63 61 73"
				+ "65 49 67 6e 6f 72 65 4d  61 74 63 68 82 01 63 83 02 55 53 84 01 ff";
		byte[] toReturn = HexUtils.fromHexString(tmp);
		return toReturn;
	}

	protected Object getExpectedResult() {
		ExtensibleMatchFilter filter = new ExtensibleMatchFilter("c",
				"caseIgnoreMatch", "US".getBytes(), true);
		return filter;
	}

	protected Parser getParser() {
		FilterListener listener = new FilterListener() {
			public void data(Filter f) {
				setResult(f);
			}
		};
		Parser parser = new ExtensibleMatchFilterParser(listener);
		return parser;
	}

protected byte[][] getBuffersThatShouldThrowAnException() {
		byte[] wrongMatchRuleTag = HexUtils.fromHexString("a9 1b 82 0f 63 61 73"
				+ "65 49 67 6e 6f 72 65 4d  61 74 63 68 82 01 63 83 02 55 53 84 01 ff");
		byte[] wrongAttrDescTag = HexUtils.fromHexString("a9 1b 81 0f 63 61 73"
				+ "65 49 67 6e 6f 72 65 4d  61 74 63 68 83 01 63 83 02 55 53 84 01 ff");
		byte[] wrongMatchValTag = HexUtils.fromHexString("a9 1b 81 0f 63 61 73"
				+ "65 49 67 6e 6f 72 65 4d  61 74 63 68 82 01 63 84 02 55 53 84 01 ff");
		byte[] wrongRdnAttrsTag = HexUtils.fromHexString("a9 1b 81 0f 63 61 73"
				+ "65 49 67 6e 6f 72 65 4d  61 74 63 68 82 01 63 83 02 55 53 85 01 ff");
		return new byte[][] {wrongMatchRuleTag,wrongAttrDescTag,wrongMatchValTag,wrongRdnAttrsTag};
	}}