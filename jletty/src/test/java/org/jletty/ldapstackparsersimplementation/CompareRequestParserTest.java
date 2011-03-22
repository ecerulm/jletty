/*
 * Created on 25-sep-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.CompareRequest;
import org.jletty.ldapstackldapops.LDAPRequest;
import org.jletty.ldapstackldapops.RequestListener;
import org.jletty.ldapstackparsersimplementation.CompareRequestParser;
import org.jletty.util.HexUtils;



/**
 * @author Ruben
 *  
 */
public class CompareRequestParserTest extends ParserTestCase {

	/**
	 * @param string
	 */
	public CompareRequestParserTest(String name) {
		super(name);
	}

	public void testPrueba() {
	};

	protected byte[] getOkBuffer() {
		final byte[] buffer = HexUtils
				.fromHexString("6E 32 04 1C 63 6E 3D 61 64 6D 69"
						+ "6E 2C 6F 3D 6C 69 6E 75 78 70 6F 77 65 72 65 64"
						+ "2C 63 3D 75 73 30 12 04 02 63 6E 04 0C 52 75 62"
						+ "65 6E 20 4C 61 67 75 6E 61 __ __ __ __ __ __ __");
		return buffer;
	}
	
	

	protected byte[][] getBuffersThatShouldThrowAnException() {
		final byte[] buffer = HexUtils
		.fromHexString("6E 32 04 1C 63 6E 3D 61 64 6D 69"
				+ "6E 2C 6F 3D 6C 69 6E 75 78 70 6F 77 65 72 65 64"
				+ "2C 63 3D 75 73 31 12 04 02 63 6E 04 0C 52 75 62"
				+ "65 6E 20 4C 61 67 75 6E 61 __ __ __ __ __ __ __");
       return new byte[][] {buffer};

	}

	protected Object getExpectedResult() {
		final Object tmp = new CompareRequest("cn=admin,o=linuxpowered,c=us",
				"cn", "Ruben Laguna".getBytes());
		return tmp;
	}

	protected Parser getParser() {
		final RequestListener listener = new RequestListener() {
			public void data(LDAPRequest req) {
				setResult(req);

			}
		};
		final Parser parser = new CompareRequestParser(listener);
		return parser;
	}

}