/*
 * Created on 18-ago-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.ApproxMatchFilter;
import org.jletty.ldapstackldapops.EqMatchFilter;
import org.jletty.ldapstackldapops.Filter;
import org.jletty.ldapstackldapops.FilterListener;
import org.jletty.ldapstackldapops.GreaterOrEqualFilter;
import org.jletty.ldapstackldapops.LessOrEqualFilter;
import org.jletty.ldapstackldapops.OrFilter;
import org.jletty.ldapstackldapops.PresentFilter;
import org.jletty.ldapstackldapops.SubstringFilter;
import org.jletty.ldapstackldapops.SubstringValue;
import org.jletty.ldapstackparsersimplementation.OrFilterParser;
import org.jletty.util.HexUtils;



/**
 * @author Administrator
 *  
 */
public class OrFilterParserTest extends ParserTestCase {

	public void testPrueba() {
		// placeholder to make eclipse junit recognize this as a runnable unit
		// test
	}

	// [start]
	/**
	 * @return
	 */
	protected final byte[] getOkBuffer() {
		final String andFilterBuffer = "a1 55 a3 0b  04 02 63 6e 04 05 52 75"
				+ "62 65 6e a4 0c 04 02 73  6e 30 06 81 04 61 67 75"
				+ "6e a5 09 04 03 61 67 65  04 02 32 34 a6 0e 04 05"
				+ "6d 6f 6e 65 79 04 05 31  35 30 30 30 87 05 74 69"
				+ "74 6c 65 a8 16 04 0a 67  69 72 6c 66 72 69 65 6e"
				+ "64 04 08 63 72 69 73 74  69 6e 61";
		//set of filter:
		// ((cn=Ruben)(sn=*agun*)(age>=24)(money<=15000)(title=*)(girlfriend~=cristina))
		return HexUtils.fromHexString(andFilterBuffer);
	}

	/**
	 * @return
	 */
	protected Object getExpectedResult() {
		SubstringFilter substringFilter = new SubstringFilter("sn",
				new SubstringValue(null, new String[] {"agun"},null));
		EqMatchFilter eqMatchFilter = new EqMatchFilter("cn", "Ruben"
				.getBytes());
		GreaterOrEqualFilter greaterOrEqualFilter = new GreaterOrEqualFilter(
				"age", "24".getBytes());
		LessOrEqualFilter lessOrEqual = new LessOrEqualFilter("money", "15000"
				.getBytes());
		PresentFilter presentFilter = new PresentFilter("title");
		ApproxMatchFilter approxMatchFilter = new ApproxMatchFilter(
				"girlfriend", "cristina".getBytes());

		Filter[] setOfFilters = new Filter[] { eqMatchFilter, substringFilter,
				greaterOrEqualFilter, lessOrEqual, presentFilter,
				approxMatchFilter };
		OrFilter toReturn = new OrFilter(setOfFilters);
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
		Parser parser = new OrFilterParser(listener);
		return parser;
	}

	/**
	 * @see org.jletty.ldapstackparsers.implementation.ParserTestCase#getWrongTag()
	 */
	protected byte getWrongTag() {

		return (byte) 0xa4;
	}

	// [end]

}