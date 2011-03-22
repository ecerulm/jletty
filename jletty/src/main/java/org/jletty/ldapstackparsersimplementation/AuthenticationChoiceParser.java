/*
 * $Id: AuthenticationChoiceParser.java,v 1.3 2006/02/27 18:23:00 ecerulm Exp $
 *
 */

//AuthenticationChoice ::= CHOICE {
//simple                  [0] OCTET STRING,
//                       -- 1 and 2 reserved
//sasl                    [3] SaslCredentials }
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.jletty.ldapstackldapops.AuthenticationChoiceListener;
import org.jletty.ldapstackldapops.BEROctetStringListener;
import org.jletty.ldapstackldapops.BerTags;

/**
 * @author $Author: ecerulm $
 * 
 * 
 */

final class AuthenticationChoiceParser extends TLVParser {

	private byte[] simplePasswd;

	private BEROctetStringListener simplePasswdListener = new BEROctetStringListener() {
		public void data(byte[] value) {
			AuthenticationChoiceParser.this.simplePasswd = value;
		}
	};

	private Parser simpleParser = new BEROctetStringParser(this.simplePasswdListener);

	// private AuthenticationChoiceParserState state =
	// AuthenticationChoiceParserState.R_CONTENTS;

	private AuthenticationChoiceListener listener;

	/**
	 * @param listeners
	 */
	public AuthenticationChoiceParser(AuthenticationChoiceListener listener) {
		this.listener = listener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rubenlaguna.ldap.TLVParser#parseContents(java.nio.ByteBuffer)
	 */
	protected boolean parseContents(ByteBuffer buffer) {

		return this.simpleParser.parse(buffer);

	}

	protected void checkContentsLength(int tlvLength) {
		if (tlvLength < 0) {
			// -1
			throw new ParserException("Incorrect  length for this type "
					+ tlvLength);
		}
		if (tlvLength == 0) {
			this.simplePasswd = new byte[0];
		}
		this.simpleParser.implicit(tlvLength);
	}

	protected void notifyListeners() {
		// TODO currently only supports simplePassword
		this.listener.data(this.simplePasswd);
	}

	protected boolean checkExpectedTag(byte tag) {
		return ((BerTags.CONTEXT_SPECIFIC) | BerTags.TAG_VALUE_00) == tag;
	}

	/**
	 * @see com.rubenlaguna.ldapstack.TLVParser#reset()
	 */
	protected void resetInternal() {
		// reset variables

		// reset parsers
		this.simpleParser.reset();
	}
}