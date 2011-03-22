/*
 * Created on 29-feb-2004
 *
  */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.AuthenticationChoiceListener;
import org.jletty.ldapstackparsersimplementation.AuthenticationChoiceParser;
import org.jletty.util.HexUtils;



/**
 * @author rlm
 * 
 */
public class AuthenticationChoiceParserTest extends ParserTestCase {
	protected String _passwd;

	public void testPrueba() {
	}

	protected Parser getParser() {
		final AuthenticationChoiceListener l = new AuthenticationChoiceListener() {
			public void data(byte[] value) {
				setResult(value);
			}
		};

		final AuthenticationChoiceParser parser = new AuthenticationChoiceParser(
				l);

		return parser;
	}

	protected byte[][] getOkBuffers() {
		final byte[] berBindRequest = HexUtils
		.fromHexString("80077365637265746f");
		final byte[] berBindRequestAnonymous = HexUtils
		.fromHexString("8000");

		return new byte[][] {berBindRequest,berBindRequestAnonymous};
	}

	protected Object[] getExpectedResults() {
		return new Object[] {"secreto".getBytes(),"".getBytes()};
	}
}