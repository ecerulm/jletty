/*
 * Created on 10-sep-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import java.util.HashSet;
import java.util.Set;

import org.jletty.ldapstackldapops.AndFilter;
import org.jletty.ldapstackldapops.ApproxMatchFilter;
import org.jletty.ldapstackldapops.EqMatchFilter;
import org.jletty.ldapstackldapops.ExtensibleMatchFilter;
import org.jletty.ldapstackldapops.Filter;
import org.jletty.ldapstackldapops.GreaterOrEqualFilter;
import org.jletty.ldapstackldapops.LessOrEqualFilter;
import org.jletty.ldapstackldapops.NotFilter;
import org.jletty.ldapstackldapops.OrFilter;
import org.jletty.ldapstackldapops.PresentFilter;
import org.jletty.ldapstackldapops.SetOfFiltersListener;
import org.jletty.ldapstackldapops.SubstringFilter;
import org.jletty.ldapstackldapops.SubstringValue;
import org.jletty.ldapstackparsersimplementation.SetOfFiltersParser;
import org.jletty.util.HexUtils;



/**
 * @author Ruben
 * 
 */
public class SetOfFiltersParserTest extends ParserTestCase {

	public void testPrueba() {
	}

	protected byte[] getOkBuffer() {

		final String tmp = "31 81 82 a0 1b a3 0b 04 02"
+"63 6e 04 05 52 75 62 65  6e a4 0c 04 02 73 6e 30"
+"06 81 04 61 67 75 6e a1  29 a5 11 04 0b 64 6e 51"
+"75 61 6c 69 66 69 65 72  04 02 32 34 a6 14 04 0b"
+"64 6e 51 75 61 6c 69 66  69 65 72 04 05 31 35 30"
+"30 30 a2 07 87 05 74 69  74 6c 65 a8 0e 04 02 63"
+"6e 04 08 63 72 69 73 74  69 6e 61 a9 1f 81 0f 63"
+"61 73 65 49 67 6e 6f 72  65 4d 61 74 63 68 82 02"
+"63 6e 83 05 61 64 6d 69  6e 84 01 ff";

		// 01 00 01 01 00 a0 7c a0 1b a3 0b 04 02 63 6e 04
		// 05 52 75 62 65 6e a4 0c 04 02 73 6e 30 06 81 04
		// 61 67 75 6e a1 1b a5 09 04 03 61 67 65 04 02 32
		// 34 a6 0e 04 05 6d 6f 6e 65 79 04 05 31 35 30 30
		// 30 a2 07 87 05 74 69 74 6c 65 a8 16 04 0a 67 69
		// 72 6c 66 72 69 65 6e 64 04 08 63 72 69 73 74 69
		// 6e 61 a9 1f 81 0f 63 61 73 65 49 67 6e 6f 72 65
		// 4d 61 74 63 68 82 02 63 6e 83 05 61 64 6d 69 6e
		// 84 01 ff 30 00

		// # filter:
		// (&(&(cn=Ruben)(sn=*agun*))(|(age>=24)(money<=15000))(!(title=*))(girlf
		// riend~=cristina)(cn:dn:caseIgnoreMatch:=admin))
		byte[] buffer = HexUtils.fromHexString(tmp);

		return buffer;
	}

	protected Object getExpectedResult() {
		// # filter:
		// (&(cn=Ruben)(sn=*agun*)(age>=24)(money<=15000)(title=*)(girlfriend~=cristina))
		Set tmp = new HashSet();
		EqMatchFilter eqMatchFilter = new EqMatchFilter("cn", "Ruben"
				.getBytes());
		SubstringFilter substringFilter = new SubstringFilter("sn",
				new SubstringValue(null, new String[] { "agun" }, null));

		tmp.add(new AndFilter(new Filter[] { eqMatchFilter, substringFilter }));

		GreaterOrEqualFilter greaterOrEqualFilter = new GreaterOrEqualFilter(
				"dnQualifier", "24".getBytes());
		LessOrEqualFilter lessOrEqualFilter = new LessOrEqualFilter("dnQualifier",
				"15000".getBytes());
		tmp.add(new OrFilter(new Filter[] { greaterOrEqualFilter,
				lessOrEqualFilter }));

		tmp.add(new NotFilter(new PresentFilter("title")));
		tmp.add(new ApproxMatchFilter("cn", "cristina".getBytes()));
		tmp.add(new ExtensibleMatchFilter("cn", "caseIgnoreMatch", "admin"
				.getBytes(), true));
		Filter[] expected = (Filter[]) tmp.toArray(new Filter[0]);
		return expected;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rubenlaguna.ldapstack.parsers.implementation.NewParserTestCase#getParser()
	 */
	protected Parser getParser() {
		SetOfFiltersListener listener = new SetOfFiltersListener() {

			public void data(Filter[] set) {
				setResult(set);
			}
		};
		SetOfFiltersParser parser = new SetOfFiltersParser(listener);
		return parser;
	}

}