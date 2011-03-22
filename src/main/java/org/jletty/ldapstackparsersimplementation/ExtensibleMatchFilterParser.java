package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.jletty.ldapstackldapops.BEROctetStringListener;
import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.ExtensibleMatchFilter;
import org.jletty.ldapstackldapops.Filter;
import org.jletty.ldapstackldapops.FilterListener;
import org.jletty.ldapstackldapops.LDAPBooleanListener;
import org.jletty.ldapstackldapops.LDAPStringListener;
import org.jletty.util.HexUtils;

public class ExtensibleMatchFilterParser extends TLVParser {

	public static final int TAG = (BerTags.CHOICE_9 | BerTags.CONSTRUCTED);

	private FilterListener listener;

	private String matchingRuleId = null;

	private String type = null;

	private byte[] matchValue = null;

	private boolean dnAttributes = false;

	private State readingMatchRuleState = new ParserStateBase(
			"Reading Matching rule") {
		private LDAPStringListener matchRuleListener = new LDAPStringListener() {
			public void data(String value) {
				ExtensibleMatchFilterParser.this.matchingRuleId = value;
			}
		};

		private Parser matchRuleParser = new LDAPStringParser(this.matchRuleListener) {
			protected boolean checkExpectedTag(byte tag) {
				return (BerTags.CONTEXT_SPECIFIC | BerTags.TAG_VALUE_01) == tag;
			}
		};

		public boolean parse(ByteBuffer buffer) {
			final boolean completed = this.matchRuleParser.parse(buffer);
			if (completed) {
				ExtensibleMatchFilterParser.this.state = ExtensibleMatchFilterParser.this.readingTypeState;
			}
			return completed;
		}

		public void resetInternal() {
			this.matchRuleParser.reset();
		}
	};

	private State readingTypeState = new ParserStateBase(
			"Reading type (attribute description)") {

		private LDAPStringListener typeListener = new LDAPStringListener() {
			public void data(String value) {
				ExtensibleMatchFilterParser.this.type = value;
			}
		};

		private Parser typeParser = new LDAPStringParser(this.typeListener) {
			protected boolean checkExpectedTag(byte tag) {
				return (BerTags.CONTEXT_SPECIFIC | BerTags.TAG_VALUE_02) == tag;
			}
		};

		public boolean parse(ByteBuffer buffer) {
			final boolean completed = this.typeParser.parse(buffer);
			if (completed) {
				ExtensibleMatchFilterParser.this.state = ExtensibleMatchFilterParser.this.readingMatchValueState;
			}
			return completed;
		}

		public void resetInternal() {
			this.typeParser.reset();
		}

	};

	private State readingMatchValueState = new ParserStateBase(
			"Reading matchValue (AssertionValue)") {

		private BEROctetStringListener matchValueListener = new BEROctetStringListener() {
			public void data(byte[] value) {
				ExtensibleMatchFilterParser.this.matchValue = value;
			}
		};

		private Parser matchValueParser = new BEROctetStringParser(
				this.matchValueListener) {
			protected boolean checkExpectedTag(byte tag) {
				return (BerTags.CONTEXT_SPECIFIC | BerTags.TAG_VALUE_03) == tag;
			}
		};

		public boolean parse(ByteBuffer buffer) {
			final boolean completed = this.matchValueParser.parse(buffer);
			if (completed) {
				ExtensibleMatchFilterParser.this.state = ExtensibleMatchFilterParser.this.readingRdnAttrsState;
			}
			return completed;
		}

		public void resetInternal() {
			this.matchValueParser.reset();
		}
	};

	private State readingRdnAttrsState = new ParserStateBase(
			"Reading dnAttributes (boolean)") {
		private LDAPBooleanListener rdnAttrsListener = new LDAPBooleanListener() {
			public void data(boolean value) {
				ExtensibleMatchFilterParser.this.dnAttributes = value;
			}
		};

		private Parser rdnAttrsParser = new LDAPBooleanParser(this.rdnAttrsListener) {
			public boolean checkExpectedTag(byte tag) {
				final byte expectedTag = (BerTags.CONTEXT_SPECIFIC | BerTags.TAG_VALUE_04);

				if (expectedTag != tag) {
					throw new ParserException("Unexpected tag ("
							+ HexUtils.toHexString(tag) + ") while expecting ("
							+ HexUtils.toHexString(expectedTag) + ")");
				}
				return true;
			}

		};

		public boolean parse(ByteBuffer buffer) {
			final boolean completed = this.rdnAttrsParser.parse(buffer);
			if (completed) {
				ExtensibleMatchFilterParser.this.state = ExtensibleMatchFilterParser.this.finalState;
			}
			return completed;
		}

		public void resetInternal() {
			this.rdnAttrsParser.reset();
		}

	};

	private State finalState = new FinalParserState();

	private State state = this.readingMatchRuleState;

	public ExtensibleMatchFilterParser(FilterListener listener) {
		this.listener = listener;
	}

	protected boolean parseContents(ByteBuffer buffer) {
		while (!this.state.equals(this.finalState)) {
			boolean completed = this.state.parse(buffer);
			if (!completed) {
				return false;
			// state = state.next();
			}
		}

		return true;
	}

	protected boolean checkExpectedTag(byte tag) throws ParserException {
		return ExtensibleMatchFilterParser.TAG == tag;
	}

	protected void resetInternal() {
		this.matchingRuleId = "";
		this.type = "";
		this.matchValue = new byte[0];
		this.dnAttributes = false;

		this.state.resetInternal();
		this.state = this.readingMatchRuleState;
	}

	protected void notifyListeners() {
		Filter toReturn = new ExtensibleMatchFilter(this.type, this.matchingRuleId,
				this.matchValue, this.dnAttributes);
		this.listener.data(toReturn);
	}

}