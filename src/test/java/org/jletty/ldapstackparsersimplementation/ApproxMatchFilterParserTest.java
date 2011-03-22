/*
 * Created on 03-oct-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.ApproxMatchFilter;
import org.jletty.ldapstackldapops.Filter;
import org.jletty.ldapstackldapops.FilterListener;
import org.jletty.ldapstackparsersimplementation.ApproxMatchFilterParser;
import org.jletty.util.HexUtils;



/**
 * @author Ruben
 *  
 */
public class ApproxMatchFilterParserTest extends ParserTestCase {

	public void testPrueba() {
	}

	protected byte[] getOkBuffer() {
		byte[] tmp = HexUtils.fromHexString("a8 0b 04 02 63 6e 04 05  6a 6f 72 67 65");
		return (byte[]) tmp.clone();
	}

	protected Object getExpectedResult() {

		return new ApproxMatchFilter("cn", "jorge".getBytes());
	}

	protected Parser getParser() {
		FilterListener listener = new FilterListener() {
			public void data(Filter f) {
				setResult(f);
			}
		};
		ApproxMatchFilterParser approxMatchFilterParser = new ApproxMatchFilterParser(
				listener);
		return approxMatchFilterParser;
	}
}
