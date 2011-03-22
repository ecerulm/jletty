/*
 * $Id: AttributeDescriptionListParser.java,v 1.3 2006/02/27 18:23:00 ecerulm Exp $
 * Created on Jun 16, 2004
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.jletty.ldapstackldapops.AttributeDescriptionListListener;
import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.LDAPStringListener;
import org.jletty.util.Style;

/**
 * @author $Author: ecerulm $
 * 
 */
public class AttributeDescriptionListParser extends TLVParser {

	/**
	 * @author $Author: ecerulm $
	 * 
	 */

	private AttributeDescriptionListListener listener;

	private List toReturn = new ArrayList();

	/**
	 * <code>octetsLeft</code> is used to check how many bytes are supposed to
	 * still be read to complete this TLV. It works with the declared size in
	 * the TLV
	 */
	private int octetsLeft = -1;

	private Parser attrDescParser = new LDAPStringParser(
			new LDAPStringListener() {
				public void data(String value) {
					AttributeDescriptionListParser.this.toReturn.add(value);
				}
			});

	/**
	 * @param listener
	 */
	public AttributeDescriptionListParser(
			AttributeDescriptionListListener listener) {

		this.listener = listener;
	}

	protected boolean checkExpectedTag(byte tag) {
		return (BerTags.SEQUENCEOF == tag);
	}

	protected boolean parseContents(ByteBuffer buffer) {
		boolean completed = false;
		while (this.octetsLeft > 0) {
			int tmp = buffer.remaining();
			completed = this.attrDescParser.parse(buffer);
			this.octetsLeft -= tmp - buffer.remaining();
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
		this.listener.data(this.toReturn);
	}

	protected void resetInternal() {
		this.toReturn = new ArrayList();
		this.attrDescParser.reset();
	}

	protected void checkContentsLength(int tlvLength) {
		if (tlvLength < 0) {
			// -1
			throw new ParserException("Incorrect  length for this type "
					+ tlvLength);
		}
		this.octetsLeft = getTlvLength();
		if (this.octetsLeft == 0) {
			this.toReturn = Collections.EMPTY_LIST;
		}
	}

	public String toString() {
		return new ToStringBuilder(this, Style.getDefaultStyle()).appendSuper(
				super.toString()).append("toReturn", this.toReturn).append(
				"octetsLeft", this.octetsLeft).append("attrDescParser",
				this.attrDescParser).toString();
	}

}