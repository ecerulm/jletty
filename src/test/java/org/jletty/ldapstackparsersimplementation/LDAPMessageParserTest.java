/*
 * Created on 12-abr-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jletty.ldapstackldapops.AbandonRequest;
import org.jletty.ldapstackldapops.AddRequest;
import org.jletty.ldapstackldapops.AttributeTypeAndValues;
import org.jletty.ldapstackldapops.AttributeTypeAndValuesList;
import org.jletty.ldapstackldapops.AttributeValues;
import org.jletty.ldapstackldapops.BindRequest;
import org.jletty.ldapstackldapops.CompareRequest;
import org.jletty.ldapstackldapops.DeleteRequest;
import org.jletty.ldapstackldapops.DerefAliases;
import org.jletty.ldapstackldapops.LDAPMessage;
import org.jletty.ldapstackldapops.LDAPMessageListener;
import org.jletty.ldapstackldapops.Modification;
import org.jletty.ldapstackldapops.ModificationType;
import org.jletty.ldapstackldapops.ModifyRDNRequest;
import org.jletty.ldapstackldapops.ModifyRequest;
import org.jletty.ldapstackldapops.PresentFilter;
import org.jletty.ldapstackldapops.Scope;
import org.jletty.ldapstackldapops.SearchRequest;
import org.jletty.ldapstackldapops.UnbindRequest;
import org.jletty.util.HexUtils;

/**
 * @author rlm
 * 
 */
public class LDAPMessageParserTest extends ParserTestCase {

	protected Object[] getExpectedResults() {
		LDAPMessage expectedBindRequest1 = new LDAPMessage(1, new BindRequest(
				3, "cn=Directory Manager", "secreto".getBytes()));

		LDAPMessage expectedBindRequest2 = new LDAPMessage(1, new BindRequest(
				3, "cn=Ruben", "secreto".getBytes()));
		// LDAPMessage expectedBindResponse = new LDAPMessage(1,new
		// BindResponse(LDAPResultCode.SUCCESS));
		LDAPMessage expectedAddRequest = createAddRequestMessage();
		LDAPMessage expectedSearchRequest = new LDAPMessage(2,
				new SearchRequest("", Scope.WHOLE_SUBTREE,
						DerefAliases.NEVER_DEREF_ALIASES, 0, 0, false,
						new PresentFilter("objectclass"), null));

		LDAPMessage expectedModifyRequestMessage = createModifyRequestMessage();

		final Object modifyRDNRequestMessage = new LDAPMessage(2,
				new ModifyRDNRequest("cn=Ruben Laguna,o=linuxpowered,c=us",
						"cn=Cristina Cuadrado", true, "o=windowspowered,c=us"));
		final Object modifyRDNRequestNoSuperiorMessage = new LDAPMessage(2,
				new ModifyRDNRequest("cn=Ruben Laguna,o=linuxpowered,c=us",
						"cn=Cristina Cuadrado", true));
		final LDAPMessage deleteRequestMessage = new LDAPMessage(2,
				new DeleteRequest("cn=Ruben,o=linuxpowered,c=us"));

		final LDAPMessage unbindRequestMessage = new LDAPMessage(2,
				UnbindRequest.UNBIND_REQUEST);

		final LDAPMessage compareRequestMessage = new LDAPMessage(2,
				new CompareRequest("cn=admin,o=linuxpowered,c=us", "cn",
						"Ruben Laguna".getBytes()));

		final LDAPMessage abandonRequestMessage = new LDAPMessage(3,
				new AbandonRequest(2));

		return new Object[] { expectedBindRequest1, expectedBindRequest2,
				expectedAddRequest, expectedSearchRequest,
				expectedModifyRequestMessage, modifyRDNRequestMessage,
				modifyRDNRequestNoSuperiorMessage, deleteRequestMessage,
				unbindRequestMessage, compareRequestMessage,
				abandonRequestMessage };
	}

	protected byte[][] getOkBuffers() {

		byte[] berBindRequestMessage = HexUtils
				.fromHexString("30 27 02 01 01 60 22 02 01 03 04 14 63 6E 3D 44"
						+ "69 72 65 63 74 6F 72 79 20 4D 61 6E 61 67 65 72"
						+ "80 07 73 65 63 72 65 74 6F");

		byte[] berBindRequestMessage2 = HexUtils
				.fromHexString("30 1B 02 01 01 60 16 02 01 03 04 08 63 6E 3D 52"
						+ "75 62 65 6E 80 07 73 65 63 72 65 74 6F");

		final byte[] berAddRequestMessage = HexUtils
				.fromHexString("30 82 01 81 02 01 05" + "68 82 01"
						+ "7a 04 30 63 6e 3d 4a 6f 72 67"
						+ "65 20 4a 65 74 73 6f 6e 2c 20"
						+ "6f 75 3d 50 65 6f 70 6c 65 2c"
						+ "20 6f 3d 6c 69 6e 75 78 70 6f"
						+ "77 65 72 65 64 2c 20 63 3d 75"
						+ "73 30 82 01 44 30 2a 04 0b 6f"
						+ "62 6a 65 63 74 43 6c 61 73 73"
						+ "31 1b 04 03 74 6f 70 04 14 6f"
						+ "72 67 61 6e 69 7a 61 74 69 6f"
						+ "6e 61 6c 50 65 72 73 6f 6e 30"
						+ "14 04 02 63 6e 31 0e 04 0c 4a"
						+ "6f 72 67 65 20 4a 65 74 73 6f"
						+ "6e 30 0e 04 02 73 6e 31 08 04"
						+ "06 4a 65 74 73 6f 6e 30 1f 04"
						+ "05 74 69 74 6c 65 31 16 04 14"
						+ "53 79 73 74 65 6d 20 41 64 6d"
						+ "69 6e 69 73 74 72 61 74 6f 72"
						+ "30 2e 04 0c 75 73 65 72 50 61"
						+ "73 73 77 6f 72 64 31 1e 04 1c"
						+ "7b 4d 44 35 61 45 61 42 5a 47"
						+ "2f 53 56 62 52 63 63 55 2f 55"
						+ "59 62 7a 78 43 67 3d 3d 30 21"
						+ "04 0f 74 65 6c 65 70 68 6f 6e"
						+ "65 4e 75 6d 62 65 72 31 0e 04"
						+ "0c 30 30 30 2d 30 30 30 2d 31"
						+ "32 33 34 30 2a 04 18 66 61 63"
						+ "73 69 6d 69 6c 65 54 65 6c 65"
						+ "70 68 6f 6e 65 4e 75 6d 62 65"
						+ "72 31 0e 04 0c 30 30 30 2d 31"
						+ "31 31 2d 31 32 33 34 30 1d 04"
						+ "0d 70 6f 73 74 61 6c 41 64 64"
						+ "72 65 73 73 31 0c 04 0a 4d 79"
						+ "20 61 64 64 72 65 73 73 30 1d"
						+ "04 0b 64 65 73 63 72 69 70 74"
						+ "69 6f 6e 31 0e 04 0c 53 79 73"
						+ "74 65 6d 20 41 64 6d 69 6e 30"
						+ "12 04 01 6c 31 0d 04 0b 4d 79"
						+ "20 6c 6f 63 61 74 69 6f 6e");

		final byte[] berSearchRequestMessage = HexUtils
				.fromHexString("30 25 02 01 02 63 20 04  00 0a 01 02 0a 01 00 02"
						+ "     01 00 02 01 00 01 01 00  87 0b 6f 62 6a 65 63 74"
						+ "     63 6c 61 73 73 30 00");
		InputStream is = LDAPMessageParserTest.class
				.getResourceAsStream("modifyRequest.txt");
		final byte[] berModifyRequestMessage = HexUtils.fromHexInputStream(is);

		final byte[] berModifyRDNRequestMessage = HexUtils
				.fromHexString(" 30 5A 02 01 02 6C 55 04 23 63 6E 3D 52 75 62 65"
						+ "6E 20 4C 61 67 75 6E 61 2C 6F 3D 6C 69 6E 75 78"
						+ "70 6F 77 65 72 65 64 2C 63 3D 75 73 04 14 63 6E"
						+ "3D 43 72 69 73 74 69 6E 61 20 43 75 61 64 72 61"
						+ "64 6F 01 01 FF 80 15 6F 3D 77 69 6E 64 6F 77 73"
						+ "70 6F 77 65 72 65 64 2C 63 3D 75 73 __ __ __ __");
		final byte[] berModifyRDNRequestWithoutSuperiorMessage = HexUtils
				.fromHexString(" 30 43 02 01 02 6C 3E 04 23 63 6E 3D 52 75 62 65"
						+ "6E 20 4C 61 67 75 6E 61 2C 6F 3D 6C 69 6E 75 78"
						+ "70 6F 77 65 72 65 64 2C 63 3D 75 73 04 14 63 6E"
						+ "3D 43 72 69 73 74 69 6E 61 20 43 75 61 64 72 61"
						+ "64 6F 01 01 FF __ __ __ __ __ __ __ __ __ __ __");
		final byte[] berDeleteRequestMessage = HexUtils
				.fromHexString(" 30 21 02 01 02 4a 1c 63"
						+ "6e 3d 52 75 62 65 6e 2c  6f 3d 6c 69 6e 75 78 70"
						+ "6f 77 65 72 65 64 2c 63  3d 75 73");

		final byte[] berUnbindRequestMessage = HexUtils
				.fromHexString("30 05 02 01 02 42 00");

		final byte[] berCompareRequest = HexUtils
				.fromHexString("30 37 02 01 02 6E 32 04 1C 63 6E 3D 61 64 6D 69"
						+ "6E 2C 6F 3D 6C 69 6E 75 78 70 6F 77 65 72 65 64"
						+ "2C 63 3D 75 73 30 12 04 02 63 6E 04 0C 52 75 62"
						+ "65 6E 20 4C 61 67 75 6E 61 __ __ __ __ __ __ __");

		final byte[] berAbandonRequestMessage = HexUtils
				.fromHexString("30 06 02 01 03 50 01 02");

		return new byte[][] { berBindRequestMessage, berBindRequestMessage2,
				berAddRequestMessage, berSearchRequestMessage,
				berModifyRequestMessage, berModifyRDNRequestMessage,
				berModifyRDNRequestWithoutSuperiorMessage,
				berDeleteRequestMessage, berUnbindRequestMessage,
				berCompareRequest, berAbandonRequestMessage };
	}

	protected Parser getParser() {
		LDAPMessageListener l = new LDAPMessageListener() {
			public void data(LDAPMessage m) {
				setResult(m);
			}
		};

		LDAPMessageParser parser = new LDAPMessageParser(l);
		return parser;
	}

	// placeholder that makes eclipse junit to identify this test as a junit
	// test
	//
	public void testPrueba() {
	}

	public void testUnreachableThow() {
		LDAPMessageListener l = new LDAPMessageListener() {
			public void data(LDAPMessage m) {
				fail("it should never suceed but returned:\n"+m);
			}
		};

		LDAPMessageParser parser = new LDAPMessageParser(l) {
			protected boolean checkExpectedTag(byte tag) {
				return true;
			};
		};
		byte[] berBindRequestMessageIncorrectTag = HexUtils
		.fromHexString("30 27 02 01 01 70 22 02 01 03 04 14 69 6E 3D 44"
				+ "69 72 65 63 74 6F 72 79 20 4D 61 6E 61 67 65 72"
				+ "80 07 73 65 63 72 65 74 6F");

		try {
			parser.parse(berBindRequestMessageIncorrectTag);
			fail();
		} catch (ParserException e) {
			// ignore it should throw an exception
		}		

	}

	private LDAPMessage createAddRequestMessage() {
		List attrs = new ArrayList();
		attrs.add(new AttributeTypeAndValues("objectClass",
				new AttributeValues().add("organizationalPerson").add("top")
						.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("cn", new AttributeValues().add(
				"Jorge Jetson").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("sn", new AttributeValues().add(
				"Jetson").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("title", new AttributeValues()
				.add("System Administrator").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("userPassword",
				new AttributeValues().add("{MD5aEaBZG/SVbRccU/UYbzxCg==")
						.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("telephoneNumber",
				new AttributeValues().add("000-000-1234")
						.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("facsimileTelephoneNumber",
				new AttributeValues().add("000-111-1234")
						.getValuesAsByteArray()));
		attrs
				.add(new AttributeTypeAndValues("postalAddress",
						new AttributeValues().add("My address")
								.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("description",
				new AttributeValues().add("System Admin")
						.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("l", new AttributeValues().add(
				"My location").getValuesAsByteArray()));

		AttributeTypeAndValuesList l = new AttributeTypeAndValuesList(attrs);
		LDAPMessage expectedAddRequest = new LDAPMessage(5, new AddRequest(
				"cn=Jorge Jetson, ou=People, o=linuxpowered, c=us", l));
		return expectedAddRequest;
	}

	private LDAPMessage createModifyRequestMessage() {
		Modification replaceMail = new Modification(ModificationType.REPLACE,
				"mail", new AttributeValues().add("modme@example.com")
						.getValuesAsByteArray());
		Modification addTitle = new Modification(ModificationType.ADD, "title",
				new AttributeValues().add("Grand Poobah")
						.getValuesAsByteArray());
		byte[] photoBuffer = getBytesFromFile("modme.jpeg");
		Modification addJpegPhoto = new Modification(ModificationType.ADD,
				"jpegPhoto", new AttributeValues().add(photoBuffer)
						.getValuesAsByteArray());
		Modification deleteDescription = new Modification(
				ModificationType.DELETE, "description", new AttributeValues()
						.getValuesAsByteArray());

		Modification[] modificationList = new Modification[] { replaceMail,
				addTitle, addJpegPhoto, deleteDescription };
		LDAPMessage expectedModifyRequest = new LDAPMessage(2,
				new ModifyRequest("cn=Modify Me,o=linuxpowered, c=us",
						modificationList));
		return expectedModifyRequest;
	}

	// Returns the contents of the file in a byte array.
	public static byte[] getBytesFromFile(String resourceName) {
		try {
			InputStream is = LDAPMessageParserTest.class
					.getResourceAsStream(resourceName);

			// Get the size of the file
			long length = is.available();

			// You cannot create an array using a long type.
			// It needs to be an int type.
			// Before converting to an int type, check
			// to ensure that file is not larger than Integer.MAX_VALUE.
			if (length > Integer.MAX_VALUE) {
				// File is too large
			}

			// Create the byte array to hold the data
			byte[] bytes = new byte[(int) length];

			// Read in the bytes
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length
					&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}

			// Ensure all the bytes have been read in
			if (offset < bytes.length) {
				throw new IOException("Could not completely read file "
						+ resourceName);
			}

			// Close the input stream and return bytes
			is.close();
			return bytes;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}