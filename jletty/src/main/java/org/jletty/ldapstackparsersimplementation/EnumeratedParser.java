/*
 * Created on 09-oct-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.jletty.ldapstackldapops.BerTags;

/**
 * @author Ruben
 * 
 */
abstract class EnumeratedParser extends TLVParser {
	private int value;

	protected final boolean parseContents(ByteBuffer buffer) {
		this.value = buffer.get();
		return true;
	}

	protected final void notifyListeners() {
		notifyListeners(this.value);
	}

	protected final void checkContentsLength(int tlvLength) {
		if (getTlvLength() != 1) {
			throw new ParserException("Length should be one");
		}
	}

	protected final void resetInternal() {
		return;
	}

	protected final boolean checkExpectedTag(byte tag) {
		return BerTags.ENUMERATED == tag;
	}

	protected abstract void notifyListeners(int valueToNotify);
}
