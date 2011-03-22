/*
 * Created on 22-sep-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.RequestListener;
import org.jletty.ldapstackldapops.UnbindRequest;

/**
 * @author Ruben
 * 
 */
public class UnbindRequestParser extends TLVParser {

	public static final byte TAG = BerTags.APPLICATION_2;

	private RequestListener listener;

	public UnbindRequestParser(RequestListener listener) {
		this.listener = listener;
	}

	protected boolean parseContents(ByteBuffer buffer) {
		return true;
	}

	protected boolean checkExpectedTag(byte tag) throws ParserException {

		return (UnbindRequestParser.TAG == tag);
	}

	protected void resetInternal() {
		// nothing to do
	}

	protected void notifyListeners() {
		this.listener.data(UnbindRequest.UNBIND_REQUEST);
	}

	protected void checkContentsLength(int tlvLength) {
		if (getTlvLength() != 0) {
			throw new ParserException(
					"UnbindRequest length should be 0 and it was "
							+ getTlvLength());
		}
		// notifyListeners();
	}
}