/*
 * $Id: AttributeValuesParser.java,v 1.2 2006/02/27 18:23:00 ecerulm Exp $
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jletty.ldapstackldapops.AttributeValuesListener;
import org.jletty.ldapstackldapops.BEROctetStringListener;
import org.jletty.ldapstackldapops.BerTags;

/**
 * @author $Author: ecerulm $
 */
public final class AttributeValuesParser extends TLVParser {

	static Logger log = Logger.getLogger(AttributeValuesParser.class);

	private AttributeValuesListener _listener;

	private int octetsRemaining;

	private final Parser valueParser = new BEROctetStringParser(
			new BEROctetStringListener() {
				public void data(byte[] value) {
					AttributeValuesParser.this.values.add(value);
				}
			});

	private List values = new LinkedList();

	/**
	 * @param l
	 */
	public AttributeValuesParser(AttributeValuesListener l) {
		this._listener = l;
	}

	/**
	 * @see org.jletty.ldapstackparsers.implementation.TLVParser#getExpectedTag()
	 */
	protected boolean checkExpectedTag(byte tag) {
		return (BerTags.SETOF == tag); // SET OF
	}

	protected boolean parseContents(ByteBuffer buffer) {
		while (this.octetsRemaining > 0) {
			int tmp = buffer.remaining();
			boolean completed = this.valueParser.parse(buffer);
			this.octetsRemaining = this.octetsRemaining - (tmp - buffer.remaining());
			if (!completed) {
				return false;
			}
		}
		return true;
	}

	protected void notifyListeners() {
		byte[][] tmp = (byte[][]) this.values.toArray(new byte[this.values.size()][]);
		this._listener.data(tmp);
	}

	protected void checkContentsLength(int tlvLength) {
		if (tlvLength < 0) {
			throw new ParserException("Indefinite length not supported");
		}
		this.octetsRemaining = tlvLength;
	}

	protected void resetInternal() {
		// reset variables
		this.values.clear();

		// resetParsers
		this.valueParser.reset();
	}
}