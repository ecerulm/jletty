/*
 * Created on 23-sep-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.LDAPStringListener;
import org.jletty.ldapstackldapops.Modification;
import org.jletty.ldapstackldapops.ModifyRequest;
import org.jletty.ldapstackldapops.RequestListener;
import org.jletty.ldapstackldapops.SequenceOfModificationsListener;

/**
 * @author Ruben
 * 
 */
public class ModifyRequestParser extends TLVParser {

	public static final byte TAG = BerTags.APPLICATION_6;

	private String object = "";

	private Modification[] modification;

	private State readingObject = new ParserStateBase(
			"Reading object (LDAPDN, string)") {
		private LDAPStringListener objectListener = new LDAPStringListener() {
			public void data(String value) {
				ModifyRequestParser.this.object = value;
			}
		};

		private LDAPStringParser objectParser = new LDAPStringParser(
				this.objectListener);

		public void resetInternal() {
			this.objectParser.reset();
		}

		public boolean parse(ByteBuffer buffer) {
			final boolean completed = this.objectParser.parse(buffer);
			if (!completed) {
				return false;
			}
			ModifyRequestParser.this.state = ModifyRequestParser.this.readingModification;
			return true;
		}
	};

	private State readingModification = new ParserStateBase(
			"Reading Modification (Sequence of)") {

		private SequenceOfModificationsListener seqModificationListener = new SequenceOfModificationsListener() {

			public void data(Modification[] value) {
				ModifyRequestParser.this.modification = value;
			}
		};

		private SequenceOfModificationsParser sequenceOfModificationParser = new SequenceOfModificationsParser(
				this.seqModificationListener);

		public void resetInternal() {
			this.sequenceOfModificationParser.reset();
		}

		public boolean parse(ByteBuffer buffer) {
			boolean completed = this.sequenceOfModificationParser.parse(buffer);
			if (!completed) {
				return false;
			}
			ModifyRequestParser.this.state = ModifyRequestParser.this.finalState;
			return true;
		}
	};

	private State finalState = new FinalParserState();

	private State state = this.readingObject; // DN

	private RequestListener listener;

	public ModifyRequestParser(RequestListener listener) {

		this.listener = listener;
	}

	protected boolean parseContents(ByteBuffer buffer) {
		while (!this.state.equals(this.finalState)) {
			final boolean completed = this.state.parse(buffer);
			if (!completed) {
				return false;
			}
		}

		return true;
	}

	protected boolean checkExpectedTag(byte tag) throws ParserException {
		return (ModifyRequestParser.TAG == tag);
	}

	protected void resetInternal() {
		this.state.resetInternal();
		this.state = this.readingObject;

	}

	protected void notifyListeners() {
		this.listener.data(new ModifyRequest(this.object, this.modification));

	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
				.appendSuper(null).toString();
	}

}