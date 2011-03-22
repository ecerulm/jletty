/*
 * Created on 21-sep-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.LDAPRequest;
import org.jletty.ldapstackldapops.RequestListener;
import org.jletty.ldapstackldapops.UnbindRequest;
import org.jletty.ldapstackparsersimplementation.UnbindRequestParser;
import org.jletty.util.HexUtils;



/**
 * @author Ruben
 *  
 */
public class UnbindRequestParserTest extends ParserTestCase {
	/**
	 * @param string
	 */
	public UnbindRequestParserTest(String name) {

		super(name);
	}

	public void testPrueba() {
	}

	protected byte[] getOkBuffer() {

		final String tmp = "42 00";
		//unbind

		return HexUtils.fromHexString(tmp);
	}

	protected Object getExpectedResult() {
		UnbindRequest unbindRequest = UnbindRequest.UNBIND_REQUEST;
		return unbindRequest;
	}

	protected Parser getParser() {
		RequestListener listener = new RequestListener() {
			public void data(LDAPRequest req) {
				setResult(req);
			}
		};
		UnbindRequestParser parser = new UnbindRequestParser(listener);
		return parser;
	}

}