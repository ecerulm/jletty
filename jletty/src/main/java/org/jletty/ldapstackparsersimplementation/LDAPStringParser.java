/*
 * $Id: LDAPStringParser.java,v 1.2 2006/02/27 18:23:00 ecerulm Exp $
 * Created on Jun 25, 2004
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.LDAPStringListener;

/**
 * @author $Author: ecerulm $
 * 
 */
public class LDAPStringParser extends TLVParser implements Parser {

	private LDAPStringListener listener;

	private final CharsetDecoder decoder = Charset.forName("UTF-8")
			.newDecoder();

	private int octetsLeft = 0;

	private CharBuffer charBuffer;

	protected String theString = "";

	/**
	 * @param listener
	 */
	public LDAPStringParser(LDAPStringListener listener) {
		super();
		this.listener = listener;
	}

	public boolean parseContents(ByteBuffer buffer) {
		int remaining = buffer.remaining();
		this.decoder.decode(buffer, this.charBuffer, (this.octetsLeft == remaining));
		this.octetsLeft = this.octetsLeft - remaining;
		if (this.octetsLeft > 0) {
			return false;
		}

		this.decoder.flush(this.charBuffer);
		this.charBuffer.rewind();
		this.theString = this.charBuffer.toString();
		return true;

	}

	protected void checkContentsLength(int tlvLength) {
		if (tlvLength < 0) {
			throw new ParserException("Cannot parse with indefinite length");
		}

		this.octetsLeft = tlvLength;
		if (tlvLength == 0) {
			this.theString = "";
		}

		this.charBuffer = CharBuffer.allocate(tlvLength);
	}

	protected boolean checkExpectedTag(byte tag) throws ParserException {
		return (BerTags.OCTETSTRING == tag);
	}

	protected void resetInternal() {
		this.decoder.reset();
	}

	protected void notifyListeners() {
		this.listener.data(this.theString);
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(
				"ldapstringparser").appendSuper(super.toString()).toString();
	}

}