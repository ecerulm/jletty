/*
 * Created on 18-ago-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.EqMatchFilter;
import org.jletty.ldapstackldapops.Filter;
import org.jletty.ldapstackldapops.FilterListener;
import org.jletty.ldapstackldapops.NotFilter;
import org.jletty.util.HexUtils;

/**
 * Test case (note that test methods are inherited from ParserTestCase)
 * 
 * @author Administrator
 * 
 */
public class NotFilterParserTest extends ParserTestCase {
	// TODO add more test and expected results
	public void testPrueba() {
		// placeholder to make eclipse junit recognize this as a runnable unit
		// test
	}

	// [start]
	protected final byte[] getOkBuffer() {
		final String notFilterBuffer = "a2 0d a3 0b 04 02 63 6e 04 05  52 75 62 65 6e";
		// filter: (!(cn=ruben))
		return HexUtils.fromHexString(notFilterBuffer);
	}

	protected Object getExpectedResult() {
		EqMatchFilter eqMatchFilter = new EqMatchFilter("cn", "Ruben"
				.getBytes());
		NotFilter toReturn = new NotFilter(eqMatchFilter);
		return toReturn;
	}

	protected Parser getParser() {
		FilterListener listener = new FilterListener() {
			public void data(Filter value) {
				setResult(value);
			}
		};
		Parser parser = new NotFilterParser(listener);
		return parser;
	}

	protected byte getWrongTag() {
		return (byte) 0xa4;
	}

	// [end]

}