/*
 * Created on 24-sep-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.AttributeValues;
import org.jletty.ldapstackldapops.Modification;
import org.jletty.ldapstackldapops.ModificationType;
import org.jletty.ldapstackldapops.SequenceOfModificationsListener;
import org.jletty.ldapstackparsersimplementation.SequenceOfModificationsParser;
import org.jletty.util.HexUtils;



public class SequenceofModificationsParserTest extends ParserTestCase {

	public void testPrueba() {
	}

	protected Object getExpectedResult() {
		final AttributeValues replaceMailValues = new AttributeValues();
		replaceMailValues.add("modme@example.com".getBytes());

		Modification replaceMail = new Modification(ModificationType.REPLACE,
				"mail", replaceMailValues.getValuesAsByteArray());

		final AttributeValues addTitleValues = new AttributeValues();
		addTitleValues.add("Grand Poobah".getBytes());
		Modification addTitle = new Modification(ModificationType.ADD, "title",
				addTitleValues.getValuesAsByteArray());

		Modification deleteDescription = new Modification(
				ModificationType.DELETE, "description", new AttributeValues().getValuesAsByteArray());

		return new Object[] { replaceMail, addTitle, deleteDescription };
	}

	protected byte[] getOkBuffer() {
		final byte[] buffer = HexUtils.fromHexString("30"
				+ "56 30 20 0a 01 02 30 1b  04 04 6d 61 69 6c 31 13"
				+ "04 11 6d 6f 64 6d 65 40  65 78 61 6d 70 6c 65 2e"
				+ "63 6f 6d 30 1c 0a 01 00  30 17 04 05 74 69 74 6c"
				+ "65 31 0e 04 0c 47 72 61  6e 64 20 50 6f 6f 62 61"
				+ "68 30 14 0a 01 01 30 0f  04 0b 64 65 73 63 72 69"
				+ "70 74 69 6f 6e 31 00");
		return buffer;
	}

	protected Parser getParser() {
		final SequenceOfModificationsListener listener = new SequenceOfModificationsListener() {
			public void data(Modification[] value) {
				setResult(value);
			}
		};

		final SequenceOfModificationsParser parser = new SequenceOfModificationsParser(
				listener);
		return parser;
	}

}