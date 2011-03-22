/*
 * $Id: DerefAliasesParserTest.java,v 1.1 2006/02/12 19:18:49 ecerulm Exp $
 * Created on Jun 15, 2004
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.DerefAliases;
import org.jletty.ldapstackldapops.DerefAliasesListener;
import org.jletty.ldapstackparsersimplementation.DerefAliasesParser;
import org.jletty.ldapstackparsersimplementation.ParserException;
import org.jletty.util.HexUtils;



/**
 * @author $Author: ecerulm $
 *  
 */
public class DerefAliasesParserTest extends ParserTestCase {

	public void testPrueba() {
	}

	public void testWrongValue() {
		final DerefAliasesListener listener = new DerefAliasesListener() {
			public void data(DerefAliases value) {
				fail("It should throw an exception but parsed " + value);
			}
		};

		DerefAliasesParser parser = new DerefAliasesParser(listener);

		try {
			boolean completed = parser
					.parse(HexUtils.fromHexString("0a 01 04")); // there is no 4
			// in
			// derefAliases)
			fail("It should trhow and exception but returned " + completed);

		} catch (ParserException e) {
			//ok it should throw an exception
			//e.printStackTrace();
		}

	}

	protected Parser getParser() {
		final DerefAliasesListener listener = new DerefAliasesListener() {
			public void data(DerefAliases value) {
				setResult(value);
			}
		};

		DerefAliasesParser parser = new DerefAliasesParser(listener);
		return parser;
	}

	protected byte[][] getOkBuffers() {
		final byte[] berNeverDerefAliases = HexUtils.fromHexString("0a 01 00");

		final byte[] berDerefInSearching = HexUtils.fromHexString("0a 01 01");

		final byte[] berFindingBaseObject = HexUtils.fromHexString("0a 01 02");

		final byte[] berDerefAlways = HexUtils.fromHexString("0a 01 03");

		return new byte[][] { berNeverDerefAliases, berDerefInSearching,
				berFindingBaseObject, berDerefAlways };
	}

	protected Object[] getExpectedResults() {
		return new Object[] { DerefAliases.NEVER_DEREF_ALIASES,
				DerefAliases.DEREF_IN_SEARCHING,
				DerefAliases.DEREF_FINDING_BASE, DerefAliases.DEREF_ALWAYS };
	}

}