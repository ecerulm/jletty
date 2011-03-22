/*
 * Created on 22-feb-2004
 * 
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.BEROctetStringListener;
import org.jletty.ldapstackparsersimplementation.BEROctetStringParser;
import org.jletty.util.HexUtils;



/**
 * @author rlm
 *  
 */
public class BEROctetStringParserTest extends ParserTestCase {

	public void testPrueba() {
	}

	protected Parser getParser() {
		final BEROctetStringListener l = new BEROctetStringListener() {
			public void data(byte[] value) {
				setResult(value);
			}
		};

		final Parser parser = new BEROctetStringParser(l);
		return parser;
	}

	protected byte[][] getOkBuffers() {
		//		 cn=Directory Manager
		final byte[] berOctetString = HexUtils
				.fromHexString("0414636e3d4469726563746f7279204d616e61676572");
		//		 cn = Ruben;
		final byte[] berOctetString2 = HexUtils
				.fromHexString("0408636e3d527562656e");
		final byte[] berOctetStringEmpty = HexUtils
				.fromHexString("0400");

		return new byte[][] { berOctetString, berOctetString2, berOctetStringEmpty };

	}

	protected Object[] getExpectedResults() {
		return new Object[] { "cn=Directory Manager".getBytes(),
				"cn=Ruben".getBytes(),"".getBytes() };
	}

}