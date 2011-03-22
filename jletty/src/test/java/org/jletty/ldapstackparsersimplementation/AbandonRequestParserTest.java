/*
 * Created on 25-sep-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.AbandonRequest;
import org.jletty.ldapstackldapops.LDAPRequest;
import org.jletty.ldapstackldapops.RequestListener;
import org.jletty.ldapstackparsersimplementation.AbandonRequestParser;
import org.jletty.util.HexUtils;



/**
 * @author Ruben
 *  
 */
public class AbandonRequestParserTest extends ParserTestCase {

	/**
	 * @param string
	 */
	
	public AbandonRequestParserTest() {
		super();
	}
	public AbandonRequestParserTest(String name) {

		super(name);
	}

	public void testPrueba() {
	};

	protected byte[] getOkBuffer() {
		final byte[] buffer = HexUtils.fromHexString("50 01 02");
		return buffer;
	}

	protected Object getExpectedResult() {
		final Object tmp = new AbandonRequest(2);
		return tmp;
	}

	protected byte getWrongTag() {
		return 0;
	}

	protected Parser getParser() {
		final RequestListener listener = new RequestListener() {
			public void data(LDAPRequest req) {
				setResult(req);

			}
		};
		final Parser parser = new AbandonRequestParser(listener);
		return parser;
	}

}