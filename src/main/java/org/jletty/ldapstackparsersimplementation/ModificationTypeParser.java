/*
 * $Id: ModificationTypeParser.java,v 1.3 2006/02/27 18:23:00 ecerulm Exp $
 * Created on Jun 8, 2004
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.ModificationType;
import org.jletty.ldapstackldapops.ModificationTypeListener;
import org.jletty.util.HexUtils;

/**
 * @author $Author: ecerulm $
 * 
 */
public class ModificationTypeParser extends TLVParser {

	private ModificationTypeListener listener;

	private ModificationType modificationType;

	/**
	 * @param listener
	 */
	public ModificationTypeParser(ModificationTypeListener listener) {

		this.listener = listener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rubenlaguna.ldapstack.parsers.implementation.TLVParser#parseContents(java.nio.ByteBuffer)
	 */
	protected boolean parseContents(ByteBuffer buffer) {
		byte value = buffer.get();
		switch (value) {
		case 0:
			this.modificationType = ModificationType.ADD;
			break;
		case 1:
			this.modificationType = ModificationType.DELETE;
			break;
		case 2:
			this.modificationType = ModificationType.REPLACE;
			break;

		default:
			throw new ParserException("Unknown modification type value ("
					+ HexUtils.toHexString(value) + ")");

		}
		return true;
	}

	/**
	 * @see org.jletty.ldapstackparsers.implementation.TLVParser#notifyListeners()
	 */
	protected void notifyListeners() {
		this.listener.data(this.modificationType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rubenlaguna.ldapstack.parsers.implementation.TLVParser#getExpectedTag()
	 */
	protected boolean checkExpectedTag(byte tag) {
		return BerTags.ENUMERATED == tag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rubenlaguna.ldapstack.parsers.implementation.TLVParser#resetInternal()
	 */
	protected void resetInternal() {
		return;
	}

	protected void checkContentsLength(int tlvLength) {
		if (getTlvLength() != 1) {
			throw new ParserException(
					"Modification type length should be one but was "
							+ getTlvLength());
		}
	}
}