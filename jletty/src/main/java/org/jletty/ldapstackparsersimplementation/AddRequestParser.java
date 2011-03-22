/*
 * $Id: AddRequestParser.java,v 1.2 2006/02/27 18:23:00 ecerulm Exp $
 */

// AddRequest ::= [APPLICATION 8] SEQUENCE {
//                   entry LDAPDN,
//                   attributes AttributeList
//                 }
// LDAPDN ::= LDAPString
//
// AttributeList ::= SEQUENCE OF SEQUENCE {
//                      type AttributeDescription,
//                      vals SET OF AttributeValue
//                   }
// AttributeDescription ::= LDAPString
// AttributeValue ::= OCTETSTRING
//
//The LDAPString is a notational convenience to indicate that, although
//strings of LDAPString type encode as OCTET STRING types, the ISO
//10646 [13] character set (a superset of Unicode) is used, encoded
//following the UTF-8 algorithm [14]. Note that in the UTF-8 algorithm
//characters which are the same as ASCII (0x0000 through 0x007F) are
//represented as that same ASCII character in a single byte. The other
//byte values are used to form a variable-length encoding of an
//arbitrary character.
//
//     LDAPString ::= OCTET STRING
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.jletty.ldapstackldapops.AddRequest;
import org.jletty.ldapstackldapops.AttributeTypeAndValuesList;
import org.jletty.ldapstackldapops.AttributeTypeAndValuesListListener;
import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.LDAPStringListener;
import org.jletty.ldapstackldapops.RequestListener;

/**
 * @author $Author: ecerulm $
 */
public final class AddRequestParser extends TLVParser {

	/**
	 * <code>dn</code> is a temporal variable holding the dn of the request.
	 * When the parsing is complete this value is used to compose the
	 * {@link org.jletty.ldapstackldapops.AddRequest}
	 */
	private String dn = null;

	private AttributeTypeAndValuesList attrList;

	private State readingValueState = new ParserStateBase(
			"Reading DN value (string)") {
		private LDAPStringListener listener = new LDAPStringListener() {
			public void data(String value) {
				AddRequestParser.this.dn = value;
			}
		};

		private LDAPStringParser entryDnParser = new LDAPStringParser(this.listener);

		public boolean parse(ByteBuffer buffer) {
			final boolean completed = this.entryDnParser.parse(buffer);
			if (completed) {
				AddRequestParser.this.state = AddRequestParser.this.readingAttrListState;
			}
			return completed;
		}

		public void resetInternal() {
			this.entryDnParser.reset();
		}

	};

	private State readingAttrListState = new ParserStateBase(
			"Reading attribute list") {
		private AttributeTypeAndValuesListParser attrListParser = new AttributeTypeAndValuesListParser(
				new AttributeTypeAndValuesListListener() {
					public void data(AttributeTypeAndValuesList value) {
						AddRequestParser.this.attrList = value;
					}
				});

		public boolean parse(ByteBuffer buffer) {
			final boolean completed = this.attrListParser.parse(buffer);
			if (completed) {
				AddRequestParser.this.state = AddRequestParser.this.finalState;
			}
			return completed;
		}

		public void resetInternal() {
			this.attrListParser.reset();
		}

	};

	private State finalState = new FinalParserState();

	/**
	 * <code>state</code> represent the internal state of the parser. To know
	 * which state are valid refer
	 * 
	 */
	private State state = this.readingValueState;

	/**
	 * <code>listener</code> is the receiver of the parsed
	 * {@link org.jletty.ldapstackldapops.AddRequest}s
	 * 
	 * @see org.jletty.ldapstackldapops.AddRequest
	 */
	private RequestListener listener;

	public AddRequestParser(RequestListener l) {
		this.listener = l;
	}

	protected boolean parseContents(ByteBuffer buffer) {

		while (!this.state.equals(this.finalState)) {
			boolean completed = this.state.parse(buffer);
			if (!completed) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @see org.jletty.ldapstackparsers.implementation.TLVParser#notifyListeners()
	 */
	protected void notifyListeners() {
		this.listener.data(new AddRequest(this.dn, this.attrList));
	}

	/**
	 * @see org.jletty.ldapstackparsers.implementation.TLVParser#getExpectedTag()
	 */
	protected final boolean checkExpectedTag(byte tag) {
		// [APPLICATION 8] AddRequest
		return (BerTags.APPLICATION_8 == tag);
	}

	protected final void resetInternal() {
		// reset variables
		this.attrList = null;
		this.dn = null;
		this.state.resetInternal();
		this.state = this.readingValueState;
	}
}