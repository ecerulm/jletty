/*
 * Created on 21-sep-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.DeleteRequest;
import org.jletty.ldapstackldapops.LDAPRequest;
import org.jletty.ldapstackldapops.RequestListener;
import org.jletty.ldapstackparsersimplementation.DeleteRequestParser;
import org.jletty.util.HexUtils;



/**
 * @author Ruben
 *  
 */
public class DeleteRequestParserTest extends ParserTestCase {
	/**
	 * @param string
	 */
	public DeleteRequestParserTest(String name) {

		super(name);
	}

	public void testPrueba() {
	}

	protected byte[] getOkBuffer() {

		final String tmp = "      4a 1c 63"
				+ "6e 3d 52 75 62 65 6e 2c  6f 3d 6c 69 6e 75 78 70"
				+ "6f 77 65 72 65 64 2c 63  3d 75 73";
		//delete cn=Ruben,o=linuxpowered,c=us

		return HexUtils.fromHexString(tmp);
	}

	protected Object getExpectedResult() {
		DeleteRequest deleteRequest = new DeleteRequest(
				"cn=Ruben,o=linuxpowered,c=us");
		return deleteRequest;
	}

	protected Parser getParser() {
		RequestListener listener = new RequestListener() {
			public void data(LDAPRequest req) {
				setResult(req);
			}
		};
		DeleteRequestParser parser = new DeleteRequestParser(listener);
		return parser;
	}

}