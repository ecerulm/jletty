/*
 * Created on 12-abr-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.jletty.ldapstackldapops.BERIntegerListener;
import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.LDAPMessage;
import org.jletty.ldapstackldapops.LDAPMessageListener;
import org.jletty.ldapstackldapops.LDAPRequest;
import org.jletty.ldapstackldapops.RequestListener;
import org.jletty.util.HexUtils;
import org.jletty.util.Style;

/**
 * @author rlm
 * 
 */
public class LDAPMessageParser extends TLVParser {

	private int msgid;

	private LDAPMessageListener listener;

	protected LDAPRequest _req;

	private final RequestListener requestListener = new RequestListener() {

		public void data(LDAPRequest req) {
			LDAPMessageParser.this._req = req;
		}
	};

	// private LDAPMessageParserState state =
	// LDAPMessageParserState.R_CONTENTS_MSGID;

	public LDAPMessageParser(LDAPMessageListener l) {
		super();
		this.listener = l;
	}

	private final State finalState = new FinalParserState();

	private final State readingOperation = new ParserStateBase(
			"Reading operation") {
		private Parser currentParser = null;

		private Parser brParser = new BindRequestParser(LDAPMessageParser.this.requestListener);

		private Parser searchRequestParser = new SearchRequestParser(
				LDAPMessageParser.this.requestListener);

		private Parser addReqParser = new AddRequestParser(LDAPMessageParser.this.requestListener);

		private Parser modifyRequestParser = new ModifyRequestParser(
				LDAPMessageParser.this.requestListener);

		private Parser modifyRDNRequestParser = new ModifyRDNRequestParser(
				LDAPMessageParser.this.requestListener);

		private Parser deleteRequestParser = new DeleteRequestParser(
				LDAPMessageParser.this.requestListener);

		private Parser unbindRequestParser = new UnbindRequestParser(
				LDAPMessageParser.this.requestListener);

		private Parser compareRequestParser = new CompareRequestParser(
				LDAPMessageParser.this.requestListener);

		private Parser abandonRequestParser = new AbandonRequestParser(
				LDAPMessageParser.this.requestListener);

		public void resetInternal() {
			if (this.currentParser != null) {
				this.currentParser.reset();
			}
			this.currentParser = null;
		}

		public boolean parse(ByteBuffer buffer) {
			if (this.currentParser == null) {

				byte tag = buffer.get(buffer.position());
				this.currentParser = selectParser(tag);
			}
			final boolean completed = this.currentParser.parse(buffer);
			if (!completed) {
				return false;
			}
			this.currentParser = null;
			if (completed) {
				LDAPMessageParser.this.state = LDAPMessageParser.this.finalState;
			}
			return true;
		}

		private Parser selectParser(byte tag) {
			Parser toReturn;
			switch (tag) {
			case BerTags.APPLICATION_0: // BindRequest
				toReturn = this.brParser;
				break;
			case BerTags.APPLICATION_3: // SearchRequest
				toReturn = this.searchRequestParser;
				break;
			case BerTags.APPLICATION_8: // AddRequest
				toReturn = this.addReqParser;
				break;
			case BerTags.APPLICATION_6: // ModifyRequest
				toReturn = this.modifyRequestParser;
				break;
			case BerTags.APPLICATION_12: // ModifyRDNRequest
				toReturn = this.modifyRDNRequestParser;
				break;
			case BerTags.APPLICATION_10: // Delete Request
				toReturn = this.deleteRequestParser;
				break;
			case BerTags.APPLICATION_2: // Unbind Request
				toReturn = this.unbindRequestParser;
				break;
			case BerTags.APPLICATION_14: // Compare Request
				toReturn = this.compareRequestParser;
				break;
			case BerTags.APPLICATION_16: // Abandon Request
				toReturn = this.abandonRequestParser;
				break;
			default:
				throw new ParserException("Unknown operation tag hex("
						+ HexUtils.toHexString(tag) + ")");
			}
			return toReturn;
		}

	};

	private final State readingMsgidState = new ParserStateBase(
			"Reading msgid (BerInteger)") {
		private BERIntegerParser msgidParser = new BERIntegerParser(
				new BERIntegerListener() {

					public void data(int value) {
						LDAPMessageParser.this.msgid = value;
					}
				});

		public boolean parse(ByteBuffer buffer) {
			final boolean completed = this.msgidParser.parse(buffer);
			if (completed) {
				LDAPMessageParser.this.state = LDAPMessageParser.this.readingOperation;
			}
			return completed;
		}

		public void resetInternal() {
			this.msgidParser.reset();
		}

	};

	// private Parser parser = msgidParser;
	private State state = this.readingMsgidState;

	protected boolean parseContents(ByteBuffer buffer) {

		while (!this.state.equals(this.finalState)) {
			if (!buffer.hasRemaining()) {
				return false;
			}
			boolean completed = this.state.parse(buffer);
			if (!completed) {
				return false;
			// state = state.next();
			}
		}
		return true;
	}

	protected void notifyListeners() {
		this.listener.data(new LDAPMessage(this.msgid, this._req));
	}

	protected boolean checkExpectedTag(byte tag) {
		// 0x30 which stands for 'universal sequence'
		// 00110000
		// The first two bits from the left, when off,
		// imply that the query is universal,
		// i.e. it applies to all fields.
		// The next bit from the left is on and that means
		// that the query is a constructed one.
		// The value of the fourth bit is 16 and that's
		// Sequence in RFC 1777. So it's a
		// Universal Sequence.
		if (BerTags.SEQUENCEOF == tag) {
			return true;
		} else {
			throw new ParserException("Incorrent tag ("
					+ HexUtils.toHexString(tag) + "). Should be SEQUENCE tag ("
					+ HexUtils.toHexString(BerTags.SEQUENCEOF));
		}

	}

	protected void resetInternal() {
		//	  
		// reset variables
		this.msgid = 0;
		this._req = null;
		// reset parser
		this.state.resetInternal();
		this.state = this.readingMsgidState;

	}

	public String toString() {
		return new ToStringBuilder(this, Style.getDefaultStyle()).append(
				"state", this.state).toString();
	}

}