/*
 * $Id: AttributeTypeAndValuesParser.java,v 1.2 2006/02/27 18:23:00 ecerulm Exp $
 */

//
// SEQUENCE {
//     type AttributeDescription,
//     vals SET OF AttributeValue
// }
//
// AttributeDescription ::= LDAPString
// The LDAPString is a notational
// convenience to indicate that, although strings of LDAPString type encode as
// OCTET STRING types, the ISO 10646 [13] character set (a superset of Unicode)
// is used, encoded following the UTF-8 algorithm [14]. Note that in the UTF-8
// algorithm characters which are the same as ASCII (0x0000 through 0x007F) are
// represented as that same ASCII character in a single byte. The other byte
// values are used to form a variable-length encoding of an arbitrary character.
//
// LDAPString ::= OCTET STRING
//
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.jletty.ldapstackldapops.AttributeTypeAndValues;
import org.jletty.ldapstackldapops.AttributeTypeAndValuesListener;
import org.jletty.ldapstackldapops.AttributeValuesListener;
import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.LDAPStringListener;

/**
 * @author $Author: ecerulm $
 */
public final class AttributeTypeAndValuesParser extends TLVParser {

	/**
	 * <code>listener</code> is the receiver of the parsed values
	 */
	private AttributeTypeAndValuesListener listener = null;

	public AttributeTypeAndValuesParser(AttributeTypeAndValuesListener l) {
		this.listener = l;
	}

	/**
	 * <code>attrDesc</code> is a temporary variable used to store the parsed
	 * attributes in this TLV. when parsing is finished the attrDesc are used to
	 * create a new {@link AttributeTypeAndValues}
	 */
	private String attrDesc;

	/**
	 * <code>attrDescParser</code> is a parser used to parse the attribute
	 * description part of this TLV AttributeDescription ::= LDAPString
	 */
	private Parser attrDescParser = new LDAPStringParser(
			new LDAPStringListener() {

				public void data(String value) {
					AttributeTypeAndValuesParser.this.attrDesc = value;
				}
			});

	protected byte[][] attrVals;

	private AttributeValuesParser attrValsParser = new AttributeValuesParser(
			new AttributeValuesListener() {
				public void data(final byte[][] values) {
					AttributeTypeAndValuesParser.this.attrVals = values;
				}
			});

	private Parser parser = this.attrDescParser;

	/**
	 * @see org.jletty.ldapstackparsers.implementation.TLVParser#getExpectedTag()
	 */
	protected boolean checkExpectedTag(byte tag) {
		return BerTags.SEQUENCEOF == tag; // SEQUENCE OF
	}

	protected boolean parseContents(final ByteBuffer buffer) {

		while (true) {

			boolean completed = this.parser.parse(buffer);

			if (!completed) {
				return false;
			}
			if (this.parser == this.attrDescParser) {
				this.parser = this.attrValsParser;
			} else {
				return true;
			}
		}

	}

	protected void notifyListeners() {
		this.listener.data(new AttributeTypeAndValues(this.attrDesc,
				this.attrVals));
	}

	protected void resetInternal() {
		// vars reset
		this.attrDesc = "";
		this.attrVals = null;

		// parsers reset
		this.parser.reset();
		this.parser = this.attrDescParser;
	}

}