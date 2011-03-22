/*
 * Created on 22-sep-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.DeleteRequest;
import org.jletty.ldapstackldapops.LDAPStringListener;
import org.jletty.ldapstackldapops.RequestListener;

/**
 * @author Ruben
 * 
 */
public final class DeleteRequestParser extends TLVParser {

	private RequestListener listener;

	protected String dn;

	private LDAPStringListener ldapStringListener = new LDAPStringListener() {
		public void data(String value) {
			DeleteRequestParser.this.dn = value;
		}
	};

	private Parser dnParser = new LDAPStringParser(this.ldapStringListener);

	public DeleteRequestParser(RequestListener listener) {
		this.listener = listener;
	}

	protected boolean parseContents(ByteBuffer buffer) {

		return this.dnParser.parse(buffer);
	}

	protected boolean checkExpectedTag(byte tag) throws ParserException {

		return BerTags.APPLICATION_10 == tag;
	}

	protected void resetInternal() {
		this.dnParser.reset();
	}

	protected void notifyListeners() {
		this.listener.data(new DeleteRequest(this.dn));
	}

	protected void checkContentsLength(int tlvLength) {
		super.checkContentsLength(tlvLength);
		this.dnParser.implicit(tlvLength);
	}
}