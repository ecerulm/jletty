package org.jletty.ldapstackparsersimplementation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jletty.ldapstackldapops.AddRequest;
import org.jletty.ldapstackldapops.AttributeTypeAndValues;
import org.jletty.ldapstackldapops.AttributeTypeAndValuesList;
import org.jletty.ldapstackldapops.AttributeValues;
import org.jletty.ldapstackldapops.LDAPRequest;
import org.jletty.ldapstackldapops.RequestListener;
import org.jletty.ldapstackparsersimplementation.AddRequestParser;
import org.jletty.util.HexUtils;



/**
 * @author $Author: ecerulm $
 * 
 */

// TODO add test with binary attribute like jpegPhoto
public class AddRequestParserTest extends ParserTestCase {
	public void testPrueba() {
	}

	private static final byte[] berAddRequest = HexUtils
			.fromHexString("68 82 01" + "7a 04 30 63 6e 3d 4a 6f 72 67"
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

	private AttributeTypeAndValuesList expectedAttrList;

	/**
	 * @param string
	 */
	public AddRequestParserTest(String name) {
		super(name);
	}

	protected byte[][] getOkBuffers() {

		final byte[] berAddRequestEscaped = HexUtils
				.fromHexString("68 81 82 04 29 63 6e 3d  4a 65 6e 6e 69 66 65 72"
						+ "5c 32 63 4a 4a 65 6e 73  65 6e 2c 6f 3d 6c 69 6e"
						+ "75 78 70 6f 77 65 72 65  64 2c 63 3d 75 73 30 55"
						+ "30 2a 04 02 63 6e 31 24  04 11 4a 65 6e 6e 69 66"
						+ "65 72 20 4a 20 4a 65 6e  73 65 6e 04 0f 4a 65 6e"
						+ "6e 69 66 65 72 20 4a 65  6e 73 65 6e 30 17 04 0b"
						+ "6f 62 6a 65 63 74 63 6c  61 73 73 31 08 04 06 70"
						+ "65 72 73 6f 6e 30 0e 04  02 73 6e 31 08 04 06 4a"
						+ "65 6e 73 65 6e");

		return new byte[][] { (byte[]) berAddRequest.clone(),
				berAddRequestEscaped };
	}

	protected Parser getParser() {
		final RequestListener l = new RequestListener() {
			public void data(LDAPRequest req) {
				setResult(req);
			}
		};

		final AddRequestParser parser = new AddRequestParser(l);

		return parser;
	}

	protected Object[] getExpectedResults() {
		List attrs = new LinkedList();
		attrs.add(new AttributeTypeAndValues("objectClass",
				new AttributeValues().add("top").add("organizationalPerson")
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
		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);

		AddRequest addRequest = new AddRequest(
				"cn=Jorge Jetson, ou=People, o=linuxpowered, c=us", attrList);

		attrs = new ArrayList();
		attrs.add(new AttributeTypeAndValues("objectclass",
				new AttributeValues().add("person").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("cn", new AttributeValues().add(
				"Jennifer J Jensen").add("Jennifer Jensen")
				.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("sn", new AttributeValues().add(
				"Jensen").getValuesAsByteArray()));
		attrList = new AttributeTypeAndValuesList(attrs);
		AddRequest addRequestEscaped = new AddRequest(
				"cn=Jennifer\\,JJensen,o=linuxpowered,c=us", attrList);
		return new Object[] { addRequest, addRequestEscaped };
	}

	public byte getWrongTag() {
		return 0x69;
	}

}