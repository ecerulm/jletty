/*
 * Created on Jul 16, 2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.enums.ValuedEnum;
import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.LDAPStringListener;
import org.jletty.ldapstackldapops.SequenceOfSubstringFiltersListener;
import org.jletty.ldapstackldapops.SubstringValue;

/**
 * @author Administrator
 * 
 */
public final class SequenceOfSubstringFiltersParser extends TLVParser {

	private SequenceOfSubstringFiltersListener listener;

	/**
	 * @author $Author: ecerulm $
	 * 
	 */
	public static final class State extends ValuedEnum {

		/**
		 * Comment for <code>serialVersionUID</code>
		 */
		private static final long serialVersionUID = 1L;

		private static final int FINAL_VALUE = 999;

		public static final State FINAL = new State("FINAL", FINAL_VALUE);

		private static final int READ_SSC_CHOICE_VALUE = 100;

		public static final State READ_SSC_CHOICE = new State(
				"Reading choice (initial, final, any)", READ_SSC_CHOICE_VALUE);

		private static final int READ_SSC_STRING_VALUE = 110;

		public static final State READ_SSC_STRING = new State("Type initial",
				READ_SSC_STRING_VALUE);

		/**
		 * @param name
		 * @param value
		 */
		protected State(String name, int value) {
			super(name, value);
		}

	}

	private State state = State.READ_SSC_CHOICE;

	private Parser cSubFilterParser;

	private final Parser cSubInitialFilterParser = createParserInitial();

	private final Parser cSubAnyFilterParser = createParserAny();

	private final Parser cSubFinalFilterParser = createParserFinal();

	// private LinkedList components = null;
	private String initial = null;

	private List any = new ArrayList();

	private String fin = null;

	private int octetsLeft;

	private final Parser createParserInitial() {
		return new LDAPStringParser(new LDAPStringListener() {
			public void data(String value) {
				SequenceOfSubstringFiltersParser.this.initial = value;
			}
		});
	}

	private final Parser createParserAny() {
		return new LDAPStringParser(new LDAPStringListener() {
			public void data(String value) {
				SequenceOfSubstringFiltersParser.this.any.add(value);
			}
		});
	}

	private final Parser createParserFinal() {
		return new LDAPStringParser(new LDAPStringListener() {
			public void data(String value) {
				SequenceOfSubstringFiltersParser.this.fin = value;
			}
		});
	}

	public SequenceOfSubstringFiltersParser(
			SequenceOfSubstringFiltersListener listener) {
		super();
		this.listener = listener;
		reset();
	}

	protected boolean parseContents(ByteBuffer buffer) {
		while (!this.state.equals(State.FINAL)) {

			boolean completed = true;
			switch (this.state.getValue()) {
			case State.READ_SSC_CHOICE_VALUE:
				if (!buffer.hasRemaining()) {
					return false;
				}
				choice(buffer);
				continue;
			case State.READ_SSC_STRING_VALUE:
				int tmp = buffer.remaining();
				completed = this.cSubFilterParser.parse(buffer);
				this.octetsLeft -= tmp - buffer.remaining();
				if (!completed) {
					return false;
				}
				if (this.octetsLeft > 0) {
					this.state = State.READ_SSC_CHOICE;
				} else {
					this.state = State.FINAL;
				}
				break;
			default:
				throw new RuntimeException("Unknown state " + this.state);
			}

		}

		return true;
	}

	/**
	 * @param buffer
	 */
	private void choice(ByteBuffer buffer) {
		byte octet = buffer.get();
		this.octetsLeft--;
		if (this.fin != null) {
			throw new ParserException("Only one final filter is allowd");
		}
		switch (octet) {

		case BerTags.CHOICE_0:
			if (this.initial != null || this.any.size() > 0) {
				throw new ParserException(
						"Substring Initial is only allowed at the beginning of the substring filter");
			}
			this.cSubFilterParser = this.cSubInitialFilterParser;
			break;
		case BerTags.CHOICE_1:
			this.cSubFilterParser = this.cSubAnyFilterParser;
			break;
		case BerTags.CHOICE_2:
			this.cSubFilterParser = this.cSubFinalFilterParser;
			break;
		default:
			throw new ParserException(
					"Unknown choice (not initial, nor any nor final)" + octet);
		}
		this.cSubFilterParser.reset();
		this.cSubFilterParser.implicit(-1);
		this.state = State.READ_SSC_STRING;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rubenlaguna.ldapstack.parsers.implementation.TLVParser#checkContentsLength()
	 */
	protected void checkContentsLength(int tlvLength) {
		this.octetsLeft = getTlvLength();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rubenlaguna.ldapstack.parsers.implementation.TLVParser#getExpectedTag()
	 */
	protected boolean checkExpectedTag(byte tag) {
		return BerTags.SEQUENCEOF == tag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rubenlaguna.ldapstack.parsers.implementation.TLVParser#resetInternal()
	 */
	protected void resetInternal() {
		// octetsLeft = getTlvLength();
		this.cSubInitialFilterParser.reset();
		this.initial = null;
		this.fin = null;
		this.any = new ArrayList();
		this.state = State.READ_SSC_CHOICE;
	}

	/**
	 * @see org.jletty.ldapstackparsers.implementation.TLVParser#notifyListeners()
	 */
	protected void notifyListeners() {
		SubstringValue toReturn = new SubstringValue(this.initial, this.any, this.fin);

		this.listener.data(toReturn);
	}

}