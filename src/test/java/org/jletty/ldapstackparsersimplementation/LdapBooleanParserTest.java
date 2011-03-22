package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.LDAPBooleanListener;
import org.jletty.util.HexUtils;

public class LdapBooleanParserTest extends ParserTestCase {

	protected Parser getParser() {
		final LDAPBooleanParser parser = new LDAPBooleanParser(
				new LDAPBooleanListener() {
					public void data(boolean b) {
						setResult(Boolean.valueOf(b));
					}
				});
		return parser;
	}

	protected byte[][] getBuffersThatShouldThrowAnException() {
		final byte[] berWrongTag = HexUtils.fromHexString("02 01 FF");
		final byte[] berWrongValue = HexUtils.fromHexString("01 01 03");
		return new byte[][] { berWrongTag, berWrongValue };
	}

	protected Object[] getExpectedResults() {
		return new Object[] { Boolean.valueOf(false), Boolean.valueOf(true) };
	}

	protected byte[][] getOkBuffers() {
		final byte[] berTrue = HexUtils.fromHexString("01 01 FF");
		final byte[] berFalse = HexUtils.fromHexString("01 01 00");

		return new byte[][] { berFalse, berTrue };
	}

	public void testPrueba() {

	}

}
