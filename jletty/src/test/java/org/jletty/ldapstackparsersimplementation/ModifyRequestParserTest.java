/*
 * Created on 21-sep-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.AttributeValues;
import org.jletty.ldapstackldapops.LDAPRequest;
import org.jletty.ldapstackldapops.Modification;
import org.jletty.ldapstackldapops.ModificationType;
import org.jletty.ldapstackldapops.ModifyRequest;
import org.jletty.ldapstackldapops.RequestListener;
import org.jletty.ldapstackparsersimplementation.ModifyRequestParser;
import org.jletty.util.HexUtils;



/**
 * @author Ruben
 *  
 */
public class ModifyRequestParserTest extends ParserTestCase {
	public ModifyRequestParserTest(String name) {
		super(name);

	}

	public void testPrueba() {
	}

	protected byte[] getOkBuffer() {

		final String tmp = "                               66 78 04 "
				+ "1e 63 6e 3d 4d 6f 64 69  66 79 20 4d 65 2c 64 63"
				+ "3d 65 78 61 6d 70 6c 65  2c 64 63 3d 63 6f 6d 30"
				+ "56 30 20 0a 01 02 30 1b  04 04 6d 61 69 6c 31 13"
				+ "04 11 6d 6f 64 6d 65 40  65 78 61 6d 70 6c 65 2e"
				+ "63 6f 6d 30 1c 0a 01 00  30 17 04 05 74 69 74 6c"
				+ "65 31 0e 04 0c 47 72 61  6e 64 20 50 6f 6f 62 61"
				+ "68 30 14 0a 01 01 30 0f  04 0b 64 65 73 63 72 69"
				+ "70 74 69 6f 6e 31 00";
		//dn: cn=Modify Me,dc=example,dc=com
		//		changetype: modify
		//		replace: mail
		//		mail: modme@example.com
		//		-
		//		add: title
		//		title: Grand Poobah
		//		-
		//		delete: description
		//		-

		return HexUtils.fromHexString(tmp);
	}

	protected Object getExpectedResult() {
		AttributeValues replaceMailValues = new AttributeValues();
		replaceMailValues.add("modme@example.com".getBytes());
		Modification replaceMail = new Modification(ModificationType.REPLACE,
				"mail", replaceMailValues.getValuesAsByteArray());
		AttributeValues addTitleValues = new AttributeValues();
		addTitleValues.add("Grand Poobah".getBytes());
		Modification addTitle = new Modification(ModificationType.ADD, "title",
				addTitleValues.getValuesAsByteArray());
		Modification deleteDescription = new Modification(
				ModificationType.DELETE, "description", new AttributeValues().getValuesAsByteArray());
		Modification[] modificationList = new Modification[] { replaceMail,
				addTitle, deleteDescription };
		ModifyRequest modifyRequest = new ModifyRequest(
				"cn=Modify Me,dc=example,dc=com", modificationList);
		return modifyRequest;
	}

	protected Parser getParser() {
		RequestListener listener = new RequestListener() {
			public void data(LDAPRequest req) {
				setResult(req);
			}
		};
		ModifyRequestParser parser = new ModifyRequestParser(listener);
		return parser;
	}

}