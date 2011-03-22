package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.LDAPRequest;
import org.jletty.ldapstackldapops.ModifyRDNRequest;
import org.jletty.ldapstackldapops.RequestListener;
import org.jletty.ldapstackparsersimplementation.ModifyRDNRequestParser;
import org.jletty.util.HexUtils;



public class ModifyRDNRequestParserTest extends ParserTestCase {

	public ModifyRDNRequestParserTest(String name) {
		super(name);
	}

	public void testPrueba() {
	};

	public void testResetNewRdn() {
		Parser p = getParser();
		resultReceived = null;
		final byte[] buf1 = HexUtils
				.fromHexString(" 6C 55 04 23 63 6E 3D 52 75 62 65"
						+ "6E 20 4C 61 67 75 6E 61 2C 6F 3D 6C 69 6E 75 78"
						+ "70 6F 77 65 72 65 64 2C 63 3D 75 73 04 14 63 6E"
						+ "3D 43 72 69 73 74 69"); // in middle of newRdn
		assertFalse(p.parse(buf1));
		assertNull(resultReceived);
		p.reset();

		//complete request
		final byte[] buf2 = HexUtils
				.fromHexString(" 6C 55 04 23 63 6E 3D 52 75 62 65"
						+ "6E 20 4C 61 67 75 6E 61 2C 6F 3D 6C 69 6E 75 78"
						+ "70 6F 77 65 72 65 64 2C 63 3D 75 73 04 14 63 6E"
						+ "3D 43 72 69 73 74 69 6E 61 20 43 75 61 64 72 61"
						+ "64 6F 01 01 FF 80 15 6F 3D 77 69 6E 64 6F 77 73"
						+ "70 6F 77 65 72 65 64 2C 63 3D 75 73 __ __ __ __");
		assertTrue(p.parse(buf2));
		assertNotNull(resultReceived);
		
		final Object res2 = new ModifyRDNRequest(
				"cn=Ruben Laguna,o=linuxpowered,c=us", "cn=Cristina Cuadrado",
				true, "o=windowspowered,c=us");
		assertEquals(res2,resultReceived);
		
	}

	protected byte[][] getOkBuffers() {
		final byte[] berModifyRDNRequest = HexUtils
				.fromHexString(" 6C 55 04 23 63 6E 3D 52 75 62 65"
						+ "6E 20 4C 61 67 75 6E 61 2C 6F 3D 6C 69 6E 75 78"
						+ "70 6F 77 65 72 65 64 2C 63 3D 75 73 04 14 63 6E"
						+ "3D 43 72 69 73 74 69 6E 61 20 43 75 61 64 72 61"
						+ "64 6F 01 01 FF 80 15 6F 3D 77 69 6E 64 6F 77 73"
						+ "70 6F 77 65 72 65 64 2C 63 3D 75 73 __ __ __ __");
		final byte[] berModifyRDNRequestWithoutSuperior = HexUtils
				.fromHexString("6C 3E 04 23 63 6E 3D 52 75 62 65"
						+ "6E 20 4C 61 67 75 6E 61 2C 6F 3D 6C 69 6E 75 78"
						+ "70 6F 77 65 72 65 64 2C 63 3D 75 73 04 14 63 6E"
						+ "3D 43 72 69 73 74 69 6E 61 20 43 75 61 64 72 61"
						+ "64 6F 01 01 FF __ __ __ __ __ __ __ __ __ __ __");
		return new byte[][] { berModifyRDNRequest,
				berModifyRDNRequestWithoutSuperior };
	}

	protected Object[] getExpectedResults() {
		final Object modifyRDNRequest = new ModifyRDNRequest(
				"cn=Ruben Laguna,o=linuxpowered,c=us", "cn=Cristina Cuadrado",
				true, "o=windowspowered,c=us");
		final Object modifyRDNRequestNoSuperior = new ModifyRDNRequest(
				"cn=Ruben Laguna,o=linuxpowered,c=us", "cn=Cristina Cuadrado",
				true);

		return new Object[] { modifyRDNRequest, modifyRDNRequestNoSuperior };
	}

	protected Parser getParser() {
		final RequestListener listener = new RequestListener() {
			public void data(LDAPRequest value) {
				setResult(value);
			}
		};
		final ModifyRDNRequestParser parser = new ModifyRDNRequestParser(
				listener);
		return parser;
	}

	protected byte[][] getBuffersThatShouldThrowAnException() {
		byte[] wrongNewSuperiorTag = HexUtils
				.fromHexString(" 6C 55 04 23 63 6E 3D 52 75 62 65"
						+ "6E 20 4C 61 67 75 6E 61 2C 6F 3D 6C 69 6E 75 78"
						+ "70 6F 77 65 72 65 64 2C 63 3D 75 73 04 14 63 6E"
						+ "3D 43 72 69 73 74 69 6E 61 20 43 75 61 64 72 61"
						+ "64 6F 01 01 FF 81 15 6F 3D 77 69 6E 64 6F 77 73"
						+ "70 6F 77 65 72 65 64 2C 63 3D 75 73 __ __ __ __");
		return new byte[][] { wrongNewSuperiorTag };
	}

}