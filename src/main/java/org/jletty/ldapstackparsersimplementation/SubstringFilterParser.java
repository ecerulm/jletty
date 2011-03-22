/*
 * $Id: SubstringFilterParser.java,v 1.2 2006/02/27 18:23:00 ecerulm Exp $
 * Created on Jun 30, 2004
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.apache.commons.lang.enums.ValuedEnum;
import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.FilterListener;
import org.jletty.ldapstackldapops.LDAPStringListener;
import org.jletty.ldapstackldapops.SequenceOfSubstringFiltersListener;
import org.jletty.ldapstackldapops.SubstringFilter;
import org.jletty.ldapstackldapops.SubstringValue;

/**
 * @author $Author: ecerulm $
 * 
 */
public class SubstringFilterParser extends TLVParser {

	public static final int TAG = (BerTags.CHOICE_4 | BerTags.CONSTRUCTED);

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

		private static final int READ_AD_VALUE = 100;

		public static final State READ_AD = new State(
				"Reading attribute description", READ_AD_VALUE);

		private static final int READ_SEQ_VALUE = 110;

		public static final State READ_SEQUENCE = new State("reading sequence",
				READ_SEQ_VALUE);

		/**
		 * @param name
		 * @param value
		 */
		protected State(String name, int value) {
			super(name, value);
		}

	}

	private FilterListener listener;

	private State state = State.READ_AD;

	private String attrDesc;

	private Parser attrDescParser = new LDAPStringParser(
			new LDAPStringListener() {

				public void data(String value) {
					SubstringFilterParser.this.attrDesc = value;
				}
			});

	protected SubstringValue comps;

	private Parser sequenceParser = new SequenceOfSubstringFiltersParser(
			new SequenceOfSubstringFiltersListener() {
				public void data(SubstringValue value) {
					SubstringFilterParser.this.comps = value;
				}
			});

	/**
	 * @param listener
	 */
	public SubstringFilterParser(FilterListener listener) {
		super();
		this.listener = listener;
		reset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rubenlaguna.ldapstack.parsers.implementation.TLVParser#parseContents(java.nio.ByteBuffer)
	 */
	protected boolean parseContents(ByteBuffer buffer) {
		while (!this.state.equals(State.FINAL)) {
			switch (this.state.getValue()) {
			case State.READ_AD_VALUE:
				if (!this.attrDescParser.parse(buffer)) {
					return false;
				}
				this.state = State.READ_SEQUENCE;
				break;
			case State.READ_SEQ_VALUE: // SEQUENCE of CHOICE
				if (!this.sequenceParser.parse(buffer)) {
					return false;
				}
				this.state = State.FINAL;
				break;
			default:
				throw new RuntimeException("Unknown state " + this.state);
			}

		}

		return true;
	}

	/**
	 * @see org.jletty.ldapstackparsers.implementation.TLVParser#notifyListeners()
	 */
	protected void notifyListeners() {
		SubstringFilter toReturn = new SubstringFilter(this.attrDesc, this.comps);
		this.listener.data(toReturn);
	}

	/**
	 * @see org.jletty.ldapstackparsers.implementation.TLVParser#getExpectedTag()
	 */
	protected boolean checkExpectedTag(byte tag) {
		return SubstringFilterParser.TAG == tag; // SubstringFilter
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rubenlaguna.ldapstack.parsers.implementation.TLVParser#resetInternal()
	 */
	protected void resetInternal() {
		this.state = State.READ_AD;
		this.attrDesc = null;
		this.comps = null;

		// reset parsers
		this.attrDescParser.reset();
		this.sequenceParser.reset();
	}

}