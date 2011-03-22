package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.LDAPBooleanListener;
import org.jletty.ldapstackldapops.LDAPStringListener;
import org.jletty.ldapstackldapops.ModifyRDNRequest;
import org.jletty.ldapstackldapops.RequestListener;

//         The Modify DN Operation allows a client to change the leftmost (least
//		   significant) component of the name of an entry in the directory, or
//		   to move a subtree of entries to a new location in the directory.  The
//		   Modify DN Request is defined as follows:
//
//		        ModifyDNRequest ::= [APPLICATION 12] SEQUENCE {
//		                entry           LDAPDN,
//		                newrdn          RelativeLDAPDN,
//		                deleteoldrdn    BOOLEAN,
//		                newSuperior     [0] LDAPDN OPTIONAL }
//
//		   Parameters of the Modify DN Request are:
//
//		   - entry: the Distinguished Name of the entry to be changed.  This
//		     entry may or may not have subordinate entries.
//
//		   - newrdn: the RDN that will form the leftmost component of the new
//		     name of the entry.
//
//		   - deleteoldrdn: a boolean parameter that controls whether the old RDN
//		     attribute values are to be retained as attributes of the entry, or
//		     deleted from the entry.
//
//		   - newSuperior: if present, this is the Distinguished Name of the entry
//		     which becomes the immediate superior of the existing entry.

public class ModifyRDNRequestParser extends TLVParser {

	private RequestListener listener;

	private String entry;

	private String newRdn;

	private boolean deleteOldRdn;

	private String newSuperior;

	public ModifyRDNRequestParser(RequestListener listener) {
		this.listener = listener;
	}

	private State readingEntryState = new ParserStateBase(
			"Reading Entry (LDAPDN)") {

		private LDAPStringListener entryListener = new LDAPStringListener() {
			public void data(String value) {
				ModifyRDNRequestParser.this.entry = value;
			}
		};

		private LDAPStringParser entryParser = new LDAPStringParser(
				this.entryListener);

		public boolean parse(ByteBuffer buffer) {
			final boolean completed = this.entryParser.parse(buffer);
			if (completed) {
				ModifyRDNRequestParser.this.state = ModifyRDNRequestParser.this.readingNewRdnState;
			}
			return completed;
		}

		public void resetInternal() {
			this.entryParser.reset();
		}

	};

	private State readingNewRdnState = new ParserStateBase(
			"Reading New Rdn (RelativeLDAPDN)") {

		private LDAPStringListener newRdnListener = new LDAPStringListener() {
			public void data(String value) {
				ModifyRDNRequestParser.this.newRdn = value;
			}
		};

		private Parser newRdnParser = new LDAPStringParser(this.newRdnListener);

		public boolean parse(ByteBuffer buffer) {
			final boolean completed = this.newRdnParser.parse(buffer);
			if (completed) {
				ModifyRDNRequestParser.this.state = ModifyRDNRequestParser.this.readingDeleteOldRdnState;
			}
			return completed;
		}

		public void resetInternal() {
			this.newRdnParser.reset();
		}

	};

	private State readingDeleteOldRdnState = new ParserStateBase(
			"Reading deleteoldrdn (BOOLEAN)") {

		private LDAPBooleanListener deleteOldRdnListener = new LDAPBooleanListener() {
			public void data(boolean value) {
				ModifyRDNRequestParser.this.deleteOldRdn = value;
			}
		};

		private Parser deleteOldRdnParser = new LDAPBooleanParser(
				this.deleteOldRdnListener);

		public boolean parse(ByteBuffer buffer) {

			final boolean completed = this.deleteOldRdnParser.parse(buffer);
			if (completed) {
				ModifyRDNRequestParser.this.state = ModifyRDNRequestParser.this.readingNewSuperiorState;
			}
			return completed;

		}

		public void resetInternal() {
			this.deleteOldRdnParser.reset();
		}

	};

	private final State readingNewSuperiorState = new ParserStateBase(
			"Reading new superior (LDAPDN)") {

		private LDAPStringListener newSuperiorListener = new LDAPStringListener() {
			public void data(String value) {
				ModifyRDNRequestParser.this.newSuperior = value;
			}
		};

		private Parser newSuperiorParser = new LDAPStringParser(
				this.newSuperiorListener) {
			protected boolean checkExpectedTag(byte tag) {

				return BerTags.CHOICE_0 == tag;
			}
		};

		public boolean parse(ByteBuffer buffer) {
			if (ModifyRDNRequestParser.this.octetsRemaining <= 0) {
				ModifyRDNRequestParser.this.state = ModifyRDNRequestParser.this.finalState;
				return true;
			}

			final boolean completed = this.newSuperiorParser.parse(buffer);
			if (completed) {
				ModifyRDNRequestParser.this.state = ModifyRDNRequestParser.this.finalState;
			}
			return completed;
		}

		public void resetInternal() {
			this.newSuperiorParser.reset();
		}

	};

	private final State finalState = new FinalParserState();

	private State state = this.readingEntryState;

	private int octetsRemaining;

	protected boolean parseContents(ByteBuffer buffer) {
		while (!this.state.equals(this.finalState)) {
			int tmp = buffer.remaining();
			boolean completed = this.state.parse(buffer);
			this.octetsRemaining = this.octetsRemaining - (tmp - buffer.remaining());
			if (!completed) {
				return false;
			// state = state.next();
			}
		}
		return true;
	}

	protected boolean checkExpectedTag(byte tag) throws ParserException {
		return BerTags.APPLICATION_12 == tag;
	}

	protected void resetInternal() {
		// reset variables
		this.entry = "";
		this.newRdn = "";
		this.deleteOldRdn = false;
		this.newSuperior = null;

		// reset state
		this.state.resetInternal();
		this.state = this.readingEntryState;
	}

	protected void notifyListeners() {
		this.listener.data(new ModifyRDNRequest(this.entry, this.newRdn, this.deleteOldRdn,
				this.newSuperior));
	}

	protected void checkContentsLength(int tlvLength) {
		super.checkContentsLength(tlvLength);
		this.octetsRemaining = tlvLength;
	}
}