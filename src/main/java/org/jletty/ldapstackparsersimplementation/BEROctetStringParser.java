/*
 * $Id: BEROctetStringParser.java,v 1.2 2006/02/27 18:23:00 ecerulm Exp $
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jletty.ldapstackldapops.BEROctetStringListener;
import org.jletty.ldapstackldapops.BerTags;

/**
 * @author $Author: ecerulm $
 */
public class BEROctetStringParser extends TLVParser implements Parser {

	private byte[] m_value;

	private BEROctetStringListener listener;

	private int octetsLeft = 0;

	private int tlvLength = 0;

	/**
	 * @param l
	 */
	public BEROctetStringParser(BEROctetStringListener l) {
		this();
		this.listener = l;
	}

	/**
	 * 
	 */
	public BEROctetStringParser() {
		// default for use by subclasses
	}

	public boolean parseContents(ByteBuffer buffer) {

		if (this.octetsLeft == this.tlvLength) {
			// AllocMem
			this.m_value = new byte[this.tlvLength];
		}

		int available = buffer.remaining();

		// we will never receive more bytes than
		// number of bytes need

		buffer.get(this.m_value, (this.tlvLength - this.octetsLeft), available);
		this.octetsLeft = this.octetsLeft - available;
		if (this.octetsLeft > 0) {
			return false;
		}
		return true;
	}

	protected final void notifyListeners() {
		notifyListener(this.m_value);
	}

	/**
	 * Notify the listener. This is the default behavior subclass can override
	 * this method
	 * 
	 * @param value
	 */
	protected void notifyListener(byte[] value) {
		this.listener.data(value);
	}

	protected void resetInternal() {
		// Reset variables
		this.m_value = null;
		this.octetsLeft = 0;
		this.tlvLength = 0;
		// Reset parser
		// It a primitive parser and doesn't depend on other parsers
	}

	protected boolean checkExpectedTag(byte tag) {
		return BerTags.OCTETSTRING == tag;
	}

	protected void checkContentsLength(int tlvLength) {
		if (tlvLength < 0) {
			// -1 or 0
			throw new ParserException("Incorrect  length for this type "
					+ tlvLength);
		}
		if (tlvLength == 0) {
			this.m_value = new byte[0];
		}
		this.tlvLength = this.octetsLeft = tlvLength;
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
				.appendSuper(super.toString()).append("m_value", this.m_value)
				.toString();
	}

}