/*
 * Created on 21-sep-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import java.util.HashSet;

import org.jletty.ldapstackldapops.DerefAliases;
import org.jletty.ldapstackldapops.LDAPRequest;
import org.jletty.ldapstackldapops.PresentFilter;
import org.jletty.ldapstackldapops.RequestListener;
import org.jletty.ldapstackldapops.Scope;
import org.jletty.ldapstackldapops.SearchRequest;
import org.jletty.ldapstackparsersimplementation.SearchRequestParser;
import org.jletty.util.HexUtils;



/**
 * @author Ruben
 *  
 */
public class SearchRequestParserTest extends ParserTestCase {
	/**
	 * @param string
	 */
	public SearchRequestParserTest(String name) {
		super(name);
	}

	public void testPrueba() {
	}

	protected byte[] getOkBuffer() {

		final String tmp = "                                        63 33 04"
				+ "         13 6f 3d 6c 69 6e 75 78  70 6f 77 65 72 65 64 2c"
				+ "         63 3d 75 73 0a 01 02 0a  01 00 02 01 00 02 01 00"
				+ "         01 01 00 87 0b 6f 62 6a  65 63 74 63 6c 61 73 73"
				+ "         30 00";
		//ldapsearch -b "o=linuxpowered,c=us"(objectclass=*)

		return HexUtils.fromHexString(tmp);
	}

	protected Object getExpectedResult() {
		SearchRequest searchRequest = new SearchRequest("o=linuxpowered,c=us",
				Scope.WHOLE_SUBTREE, DerefAliases.NEVER_DEREF_ALIASES, 0, 0,
				false, new PresentFilter("objectclass"), new HashSet());
		return searchRequest;
	}

	protected Parser getParser() {
		RequestListener listener = new RequestListener() {
			public void data(LDAPRequest req) {
				setResult(req);
			}
		};
		SearchRequestParser parser = new SearchRequestParser(listener);
		return parser;
	}

}