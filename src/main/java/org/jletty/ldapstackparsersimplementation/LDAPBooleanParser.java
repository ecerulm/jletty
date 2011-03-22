/*
 * Created on 19-sep-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.LDAPBooleanListener;
import org.jletty.util.HexUtils;

/**
 * @author Ruben
 * 
 */
public class LDAPBooleanParser extends TLVParser implements Parser {
	private boolean value = false;

	private LDAPBooleanListener listener;

	private static byte TRUE = (byte) 0xFF;

	private static byte FALSE = (byte) 0x00;

	/**
	 * @param listener
	 */
	public LDAPBooleanParser(LDAPBooleanListener listener) {
		this.listener = listener;
	}

	protected boolean parseContents(ByteBuffer buffer) {
		byte b = buffer.get();
		// LDAP definition of boolean encoding it's different from standard
		// berboolean.
		// ber boolean say that if contents are non-zero then it's true
		// ldap boolean say that the only allowed contents are 0x00(false) or
		// 0xFF (true)
		if (b == FALSE) {
			this.value = false;
		} else if (b == TRUE) {
			this.value = true;
		} else {
			throw new ParserException(
					"Incorrent value for LDAP boolean. Allowed values are 0x00 or 0xFF but was "
							+ HexUtils.toHexString(b));
		}
		return true;
	}

	protected boolean checkExpectedTag(byte tag) throws ParserException {
		return (BerTags.UNIVERSAL | BerTags.TAG_VALUE_01) == tag;
	}

	protected void resetInternal() {
		this.value = false;
	}

	protected void notifyListeners() {
		this.listener.data(this.value);
	}

}