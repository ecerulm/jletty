/*
 * $Id: ModificationTypeParserTest.java,v 1.1 2006/02/12 19:18:49 ecerulm Exp $
 * Created on Jun 9, 2004
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.ModificationType;
import org.jletty.ldapstackldapops.ModificationTypeListener;
import org.jletty.ldapstackparsersimplementation.ModificationTypeParser;
import org.jletty.util.HexUtils;



/**
 * @author $Author: ecerulm $
 *  
 */
public class ModificationTypeParserTest extends ParserTestCase {

	public void testPrueba() {
	}

	protected byte[][] getBuffersThatShouldThrowAnException() {
		final byte[] berWrongOperation = HexUtils.fromHexString("0a 01 04");
		return new byte[][]{berWrongOperation};
	}

	protected Object[] getExpectedResults() {
		return new Object[] { ModificationType.ADD, ModificationType.DELETE,
				ModificationType.REPLACE };
	}

	protected byte[][] getOkBuffers() {
		final byte[] berAddOperation = HexUtils.fromHexString("0a 01 00");

		final byte[] berDeleteOperation = HexUtils.fromHexString("0a 01 01");

		final byte[] berReplaceOperation = HexUtils.fromHexString("0a 01 02");

		return new byte[][] { berAddOperation, berDeleteOperation,
				berReplaceOperation };
	}

	protected Parser getParser() {
		final ModificationTypeListener listener = new ModificationTypeListener() {
			public void data(ModificationType value) {
				setResult(value);
			}
		};

		ModificationTypeParser parser = new ModificationTypeParser(listener);
		return parser;
	}

}