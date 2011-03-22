/*
 * Created on 24-sep-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.Modification;
import org.jletty.ldapstackldapops.ModificationListener;
import org.jletty.ldapstackldapops.SequenceOfModificationsListener;

/**
 * @author Ruben
 * 
 */
public class SequenceOfModificationsParser extends TLVParser {

	private SequenceOfModificationsListener listener;

	private int octetsRemaining;

	private List modifications = new ArrayList();

	private ModificationListener modificationListener = new ModificationListener() {

		public void data(Modification value) {
			SequenceOfModificationsParser.this.modifications.add(value);
		}
	};

	private Parser modificationParser = new ModificationParser(
			this.modificationListener);

	/**
	 * @param listener
	 */
	public SequenceOfModificationsParser(
			SequenceOfModificationsListener listener) {
		this.listener = listener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rubenlaguna.ldapstack.parsers.implementation.TLVParser#parseContents(java.nio.ByteBuffer)
	 */
	protected boolean parseContents(ByteBuffer buffer) {
		while (this.octetsRemaining > 0) {
			int tmp = buffer.remaining();
			boolean completed = this.modificationParser.parse(buffer);
			this.octetsRemaining = this.octetsRemaining - (tmp - buffer.remaining());
			if (!completed) {
				return false;
			}
		}

		return true;
	}

	protected boolean checkExpectedTag(byte tag) throws ParserException {
		return BerTags.SEQUENCEOF == tag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rubenlaguna.ldapstack.parsers.implementation.TLVParser#resetInternal()
	 */
	protected void resetInternal() {

		this.modifications = new ArrayList();
		this.modificationParser.reset();
	}

	protected void checkContentsLength(int tlvLength) {
		super.checkContentsLength(tlvLength);
		this.octetsRemaining = tlvLength;
	}

	protected void notifyListeners() {
		final Modification[] toReturn = (Modification[]) this.modifications
				.toArray(new Modification[0]);
		this.listener.data(toReturn);
	}

}