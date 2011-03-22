/*
 * $Id: ScopeParserTest.java,v 1.1 2006/02/12 19:18:49 ecerulm Exp $
 * Created on Jun 9, 2004
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.Scope;
import org.jletty.ldapstackldapops.ScopeListener;
import org.jletty.ldapstackparsersimplementation.ParserException;
import org.jletty.ldapstackparsersimplementation.ScopeParser;
import org.jletty.util.HexUtils;



/**
 * @author $Author: ecerulm $
 *  
 */
public class ScopeParserTest extends ParserTestCase {

	public void testPrueba() {
	}

	public void testWrongValue() {
		final ScopeListener listener = new ScopeListener() {
			public void data(Scope value) {
				fail("It should throw an exception but parsed ->" + value);
			}
		};
		ScopeParser parser = new ScopeParser(listener);
		final byte[] berWrongValue = HexUtils.fromHexString("0a 01 03");
		try {
			parser.parse(berWrongValue);
			fail("It should throw an exception");
		} catch (ParserException e) {
			// ignore
		}
	}

	protected Parser getParser() {
		final ScopeListener listener = new ScopeListener() {
			public void data(Scope value) {
				setResult(value);
			}
		};

		ScopeParser parser = new ScopeParser(listener);
		return parser;
	}

	protected byte[][] getOkBuffers() {
		final byte[] berBaseObject = HexUtils.fromHexString("0a 01 00");
		final byte[] berOneLevel = HexUtils.fromHexString("0a 01 01");
		final byte[] berWholeSubtree = HexUtils.fromHexString("0a 01 02");
		return new byte[][] { berBaseObject, berOneLevel, berWholeSubtree };
	}

	protected Object[] getExpectedResults() {

		return new Object[] { Scope.BASE_OBJECT, Scope.ONE_LEVEL,
				Scope.WHOLE_SUBTREE };
	}
}