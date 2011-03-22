/*
 * $Id: BERIntegerParser.java,v 1.3 2006/02/27 18:24:46 ecerulm Exp $
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.jletty.ldapstackldapops.BERIntegerListener;
import org.jletty.ldapstackldapops.BerTags;

/**
 * @author $Author: ecerulm $
 */
public class BERIntegerParser extends TLVParser implements Parser {

	/**
	 * an <code>int</code> in java is 32-bits so
	 * <code>MAX_INTEGER_LENGTH</code> is 4
	 */
	private static final int MAX_INTEGER_LENGTH = 4;

	private int tlvLength;

	private boolean negative = false;

	protected int m_value = 0;

	private BERIntegerListener listener;

	/**
	 * <code>octetsLeft</code> is used to check how many bytes are supposed to
	 * still be read to complete this TLV. It works with the declared size in
	 * the TLV
	 */
	private int octetsLeft = 0;

	BERIntegerParser(BERIntegerListener receiver) {
		this.listener = receiver;
	}

	protected BERIntegerParser() {
	};

	/**
	 * @param berInteger1
	 */
	public final boolean parseContents(ByteBuffer buffer) {

		int octet = 0;
		while (this.octetsLeft > 0) {
			if (!buffer.hasRemaining()) {
				return false;
			}
			octet = buffer.get();
			if (this.octetsLeft == this.tlvLength) {
				if ((octet & 0x80) > 0) {
					this.negative = true;
				}
			}
			if (this.negative) {
				this.m_value = (this.m_value << 8) + (octet ^ 0xFF) & 0xFF;
			} else {
				this.m_value = (this.m_value << 8) + (octet & 0xFF);
			}
			this.octetsLeft--;
		}
		if (this.negative) { /* convert to 2's complement */
			this.m_value = (this.m_value + 1) * -1;
		}

		return true;
	}

	/**
	 * @see org.jletty.ldapstackparsers.implementation.TLVParser#notifyListeners()
	 */
	protected void notifyListeners() {
		this.listener.data(this.m_value);
	}

	/**
	 * @see TLVParser#resetInternal()
	 */
	protected void resetInternal() {
		// reset variables
		this.m_value = 0;
		this.negative = false;

		// reset parsers
		// It a primitive type parse so it doesn't rely on other parsers
	}

	/**
	 * @see com.rubenlaguna.ldap.TLVParser#getExpectedTag()
	 */
	protected boolean checkExpectedTag(byte tag) {
		return (BerTags.INTEGER == tag);
	}

	/**
	 * @see TLVParser#checkContentsLength(int)
	 */
	protected void checkContentsLength(final int tlvLength) {
		super.checkContentsLength(tlvLength);
		if (tlvLength > MAX_INTEGER_LENGTH) {
			int cl = tlvLength;
			reset();
			throw new ParserException("Length " + cl
					+ " too big for INTEGER (4 bytes)");
		}
		this.tlvLength = this.octetsLeft = tlvLength;

	}
}