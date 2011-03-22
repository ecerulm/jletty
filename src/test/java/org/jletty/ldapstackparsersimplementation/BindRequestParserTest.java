/*
 * Created on 12-abr-2004
 * 
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.BindRequest;
import org.jletty.ldapstackldapops.LDAPRequest;
import org.jletty.ldapstackldapops.RequestListener;
import org.jletty.ldapstackparsersimplementation.BindRequestParser;
import org.jletty.util.HexUtils;



/**
 * @author rlm
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BindRequestParserTest extends ParserTestCase {

	public void testPrueba() {
	}

	/**
	 * @param string
	 */
	public BindRequestParserTest(String name) {
		super(name);
	}

	protected Parser getParser() {
		final RequestListener l = new RequestListener() {
			public void data(LDAPRequest req) {
				setResult(req);
			}
		};
		final Parser parser = new BindRequestParser(l);

		return parser;
	}

	protected Object[] getExpectedResults() {
		BindRequest bindRequest = new BindRequest(3,
				"cn=admin,o=linuxpowered,c=us", "secret".getBytes());
		BindRequest bindRequestAnonymous = new BindRequest(3,"","".getBytes());
		return new Object[] {bindRequest,bindRequestAnonymous};
	}

	protected byte[][] getOkBuffers() {
		byte[] berBindRequest = HexUtils
				.fromHexString("60 29 02 01 03 04 1C 63 6E 3D 61"
						+ "64 6D 69 6E 2C 6F 3D 6C 69 6E 75 78 70 6F 77 65"
						+ "72 65 64 2C 63 3D 75 73 80 06 73 65 63 72 65 74");
		byte[] berBindAnonymous = HexUtils
				.fromHexString("60 07 02 01 03 04 00 80 00");

		return new byte[][] { berBindRequest, berBindAnonymous };
	}

	protected byte[][] getBuffersThatShouldThrowAnException() {
		byte[] berBindRequestWrongVersion = HexUtils
				.fromHexString("60 29 02 01 04 04 1C 63 6E 3D 61"
						+ "64 6D 69 6E 2C 6F 3D 6C 69 6E 75 78 70 6F 77 65"
						+ "72 65 64 2C 63 3D 75 73 80 06 73 65 63 72 65 74");
		return new byte[][]{berBindRequestWrongVersion};
	}
}