/*
 * $Id: EqMatchFilterParserTest.java,v 1.1 2006/02/12 19:18:49 ecerulm Exp $
 * Created on Jun 26, 2004
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.EqMatchFilter;
import org.jletty.ldapstackldapops.Filter;
import org.jletty.ldapstackldapops.FilterListener;
import org.jletty.ldapstackparsersimplementation.EqMatchFilterParser;
import org.jletty.util.HexUtils;



/**
 * @author $Author: ecerulm $
 *  
 */
public class EqMatchFilterParserTest extends ParserTestCase {

	public void testPrueba() {
		//placeholder to allow junit identify this as a testcase
	}

	protected byte[] getOkBuffer() {
		return HexUtils
				.fromHexString("a3 12 04 0b 6f 62 6a 65 63 74 63 6c 61 73 73 04 03 74 6f 70");
	}

	protected Object getExpectedResult() {
		return new EqMatchFilter("objectclass", "top".getBytes());
	}

	protected Parser getParser() {
		final EqMatchFilterParser toReturn = new EqMatchFilterParser(
				new FilterListener() {
					public void data(Filter value) {
						setResult(value);
					}
				});
		return toReturn;
	}

	protected byte getWrongTag() {
		return (byte) 0xa0;
	}
}