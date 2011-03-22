/*
 * Created on 24-sep-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.jletty.ldapstackldapops.AttributeTypeAndValues;
import org.jletty.ldapstackldapops.AttributeTypeAndValuesListener;
import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.Modification;
import org.jletty.ldapstackldapops.ModificationListener;
import org.jletty.ldapstackldapops.ModificationType;
import org.jletty.ldapstackldapops.ModificationTypeListener;

/**
 * @author Ruben
 * 
 */
public class ModificationParser extends TLVParser {

	private ModificationListener listener;

	private ModificationType operation;

	private AttributeTypeAndValues modification;

	private State readingOperationState = new ParserStateBase(
			"Reading operation (ENUMERATED)") {

		private ModificationTypeListener modificationTypeListener = new ModificationTypeListener() {
			public void data(ModificationType value) {
				ModificationParser.this.operation = value;
			}
		};

		private Parser modificationTypeParser = new ModificationTypeParser(
				this.modificationTypeListener);

		public void resetInternal() {
			this.modificationTypeParser.reset();

		}

		public boolean parse(ByteBuffer buffer) {

			boolean completed = this.modificationTypeParser.parse(buffer);
			if (!completed) {
				return false;
			}
			ModificationParser.this.state = ModificationParser.this.readingAttrTypeAndValuesState;
			return true;
		}
	};

	private State readingAttrTypeAndValuesState = new ParserStateBase(
			"Reading modification (AttributeTypeAndValues)") {

		private AttributeTypeAndValuesListener attributeTypeAndValuesListener = new AttributeTypeAndValuesListener() {
			public void data(AttributeTypeAndValues value) {
				ModificationParser.this.modification = value;

			}
		};

		private Parser attributeTypeAndValuesParser = new AttributeTypeAndValuesParser(
				this.attributeTypeAndValuesListener);

		public void resetInternal() {
			this.attributeTypeAndValuesParser.reset();
		}

		public boolean parse(ByteBuffer buffer) {
			boolean completed = this.attributeTypeAndValuesParser.parse(buffer);
			if (!completed) {
				return false;
			}
			ModificationParser.this.state = ModificationParser.this.finalState;
			return true;
		}
	};

	private final State finalState = new FinalParserState();

	private State state = this.readingOperationState;

	public ModificationParser(ModificationListener listener) {
		this.listener = listener;
	}

	protected boolean parseContents(ByteBuffer buffer) {
		while (!this.state.equals(this.finalState)) {
			boolean completed = this.state.parse(buffer);
			if (!completed) {
				return false;
			}
		}
		return true;
	}

	protected boolean checkExpectedTag(byte tag) throws ParserException {

		return (BerTags.SEQUENCEOF == tag);
	}

	protected void resetInternal() {
		this.state.resetInternal();
		this.state = this.readingOperationState;

	}

	protected void notifyListeners() {
		this.listener.data(new Modification(this.operation, this.modification
				.getAttributeDescription(), this.modification.getValues()));

	}

}