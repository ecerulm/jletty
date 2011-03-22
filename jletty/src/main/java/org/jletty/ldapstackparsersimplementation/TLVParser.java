/*
 * $Id: TLVParser.java,v 1.2 2006/02/27 18:23:00 ecerulm Exp $
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.enums.ValuedEnum;
import org.jletty.util.HexUtils;
import org.jletty.util.Style;

/**
 * An abstract base class for parsers. TLVParser is intented to provide base
 * funcionality for parsing tag-length-value structures like BER encoded
 * Integer, etc. TLVParse provides a main template method parse(ByteBuffer) that
 * is responsible for all parsing. To create a concrete parser the programmer
 * must provide an implementation for the following methods
 * <ul>
 * <li>parseContents(ByteBuffer)
 * <li>getExpectedTag()
 * <li>and resetInternal()
 * </ul>
 * 
 * @author $Author: ecerulm $
 * @version $Revision: 1.2 $
 */
public abstract class TLVParser implements Parser {

	final static class StateEnum extends ValuedEnum {
		private static final long serialVersionUID = 1L;

		// standard enums for version of JVM
		public static final int R_TAG_STATE_VALUE = 100;

		public static final int R_LEN_STATE_VALUE = 110;

		public static final int R_CONTENTS_STATE_VALUE = 120;

		public static final int FINAL_STATE_VALUE = 120;

		public static final StateEnum R_TAG_STATE = new StateEnum(
				"Reading tag", R_TAG_STATE_VALUE);

		public static final StateEnum R_LENGTH_STATE = new StateEnum(
				"Reading length", R_LEN_STATE_VALUE);

		public static final StateEnum R_CONTENTS_STATE = new StateEnum(
				"Reading contents", R_CONTENTS_STATE_VALUE);

		public static final StateEnum FINAL_STATE = new StateEnum("Final",
				FINAL_STATE_VALUE);

		private StateEnum(String name, int value) {
			super(name, value);
		}

	}

	protected interface State {
		boolean parse(ByteBuffer buffer);

		void resetInternal();

	}

	/**
	 * @author Ruben
	 * 
	 * Designed to override getParser and next()
	 */
	abstract class ParserStateBase implements State {
		private String name;

		ParserStateBase(String stateName) {
			this.name = stateName;
		}

		public String toString() {
			return "State: " + this.name;
		}

		public void resetInternal() {
			// do nothing
		}
	}

	final class FinalParserState extends ParserStateBase {
		public FinalParserState() {
			super("Final State");
		}

		public boolean parse(ByteBuffer buffer) {
			throw new RuntimeException("Called parse in finalState");
		}

	}

	private StateEnum stateEnum = StateEnum.R_TAG_STATE;

	/**
	 * <code>BIT_8_MASK</code> 0x80
	 */
	private static final int BIT_8_MASK = 0x80;

	private int octetsLeft;

	private boolean lengthFirstOctet = true;

	private int tlvLength;

	private int tmpLength;

	private int num_length_octets;

	private int length_octets_read;

	/**
	 * Wrapper around {@link #parse(ByteBuffer)}.
	 * 
	 * @see #parse(ByteBuffer)
	 */
	public final boolean parse(byte[] in) {
		return parse(ByteBuffer.wrap(in));
	}

	/**
	 * Parses a ByteBuffer. This implementation follows the Template Method
	 * pattern (see <a
	 * href="http://c2.com/cgi/wiki?TemplateMethodPattern">TemplateMethodPattern
	 * at c2.com </a>) It depends on specific (concrete) implementation by the
	 * subclasses of these methods
	 * <ul>
	 * <li>getExpectedTag() to obtain the value of the tag of the expected TLV
	 * <li>parseContents() to parse the V part in the TLV.
	 * </ul>
	 * The actual LDAPRequest produced is recieved by concrete LDAPListener.
	 */
	public final boolean parse(ByteBuffer buffer) {
		try {
			boolean completed;
			while (!this.stateEnum.equals(StateEnum.FINAL_STATE)) {

				if (!buffer.hasRemaining()) {
					return false; // nothing to parse now
				}
				switch (this.stateEnum.getValue()) {
				case StateEnum.R_TAG_STATE_VALUE:
					completed = readTag(buffer);
					// it always return true
					this.stateEnum = StateEnum.R_LENGTH_STATE;
					break;
				case StateEnum.R_LEN_STATE_VALUE:
					completed = readLengthOctets(buffer);
					if (!completed) {
						return false;
					}
					setTlvLength(this.tmpLength);

					if (this.tlvLength == 0) {
						this.stateEnum = StateEnum.FINAL_STATE;
					} else {
						this.stateEnum = StateEnum.R_CONTENTS_STATE;
					}
					break;
				case StateEnum.R_CONTENTS_STATE_VALUE:
					completed = readContents(buffer);
					if (!completed) {
						return false;
					}
					this.stateEnum = StateEnum.FINAL_STATE;
					break;
				default:
					throw new RuntimeException("Unknown state" + this.stateEnum);
				}

			}
			notifyListeners();
			reset();
			return true;
		} catch (ParserException e) {
			ParserException exception = new ParserException("Parser: "
					+ this.toString(), e);
			reset();
			throw exception;
		}
	}

	public final void implicit(int length) {
		if (this.stateEnum.equals(StateEnum.R_TAG_STATE)) {
			if (length < 0) {
				this.stateEnum = StateEnum.R_LENGTH_STATE;
			} else {
				setTlvLength(length);
				this.stateEnum = StateEnum.R_CONTENTS_STATE;
			}

		}

		else {
			final ParserException parserException = new ParserException(
					"implicit called when state = " + this.stateEnum);
			reset();
			throw parserException;
		}
	}

	public final void reset() {
		this.tlvLength = 0;
		this.lengthFirstOctet = true;

		this.stateEnum = StateEnum.R_TAG_STATE;
		resetInternal();
	}

	/**
	 * @return Returns the tlvLength.
	 */
	protected final int getTlvLength() {
		return this.tlvLength;
	}

	private boolean readTag(ByteBuffer buffer) {
		byte tag = buffer.get();
		if (!checkExpectedTag(tag)) {
			throw new ParserException("Unexpected tag (0x"
					+ HexUtils.toHexString(tag) + ") in parser " + this);
		}
		return true;
	}

	private boolean readContents(ByteBuffer buffer) {
		if (buffer.remaining() > this.octetsLeft) {
			// set the buffer limit to match octetsLeft and put original
			// buffer position after that position
			ByteBuffer orig = buffer;
			buffer = buffer.duplicate();
			buffer.limit(buffer.position() + this.octetsLeft);
			orig.position(orig.position() + this.octetsLeft);
		}
		int remainingInAcc = buffer.remaining();
		boolean completed = parseContents(buffer);

		int bytesNotConsumed = buffer.remaining();
		if (bytesNotConsumed > 0) {
			// the parser should consume all the bytes in the buffer because
			// this buffer is guaranteed to have <=octetsLeft
			throw new ParserException("The parsing of the TLV " + getClass()
					+ " finished (" + completed
					+ ") without consuming all the bytes. bytes left"
					+ bytesNotConsumed);
		}

		int bytesConsumed = remainingInAcc - bytesNotConsumed;
		this.octetsLeft = this.octetsLeft - bytesConsumed;
		checkForExceptions(completed);

		return completed;
	}

	/**
	 * Reads and decodes a length byte and then that many octets from the input
	 * stream. The length of contents or -1 if indefinite length is stored on
	 * <code>_content_length</code>
	 * 
	 * @param bbuf
	 *            input stream from which to read
	 */
	private boolean readLengthOctets(final ByteBuffer bbuf) {
		byte octet = 0;
		if (this.lengthFirstOctet) {
			octet = bbuf.get();
			this.lengthFirstOctet = false;
			if (octet == (byte) BIT_8_MASK) {
				/* Indefinite length */
				this.tmpLength = -1;
				return true;
			}
			if ((octet & BIT_8_MASK) > 0) {
				/*
				 * Definite (long form) - num octets encoded in 7 rightmost bits
				 */
				this.num_length_octets = (octet & 0x7F);
				if (this.num_length_octets <= 0 || this.num_length_octets > 4) {
					throw new ParserException(
							"Definite long form of length but number of length octets is incorrect: "
									+ this.num_length_octets);
				}
				this.length_octets_read = 0;
				this.tmpLength = 0;
			} else {
				/*
				 * Definite (short form) - one length octet. Value encoded in 7
				 * rightmost bits.
				 */
				this.tmpLength = octet;
				return true;
			}
		}

		while (this.length_octets_read < this.num_length_octets) {
			if (!bbuf.hasRemaining()) {
				return false;
			}
			octet = bbuf.get();
			this.length_octets_read++;
			this.tmpLength = (this.tmpLength << 8) | (octet & 0xFF);
		}
		return true;
	}

	private void checkForExceptions(boolean completed) {
		if (!completed) {
			if (this.octetsLeft <= 0) { // not completed but should be
				throw new ParserException(
						"The parsing of the TLV didn't complete although the parser was feed with  "
								+ this.tlvLength
								+ " bytes and the length of the TLV was "
								+ this.tlvLength);
			}

		} else {
			if (this.octetsLeft > 0) { // completed but it shouldn't
				throw new ParserException(
						"The parsing of the TLV was completed but not all the bytes were consumed. There is still "
								+ this.octetsLeft
								+ " byte(s) to be read to match the TLV length "
								+ this.tlvLength);
			}
		}
	}

	protected void checkContentsLength(int tlvLength) {
		if (tlvLength <= 0) {
			// -1 or 0
			throw new ParserException("Incorrect  length for this type "
					+ tlvLength);
		}
	}

	/**
	 * Used by {@link #parse(ByteBuffer) parse}to parse the value of TLV
	 * 
	 * @return true if parsing complete. false otherwise.
	 */
	protected abstract boolean parseContents(ByteBuffer buffer);

	/**
	 * Used by {@link #parse(ByteBuffer) parse}to figure out the expected tag
	 * for this TLV
	 * 
	 * @return the tag (the T in TLV)
	 */
	protected abstract boolean checkExpectedTag(byte tag)
			throws ParserException;

	/**
	 * Reset the internal concrete state of the parser. All the base class state
	 * is managed by the {@link reset}method and the subclass is not supposed
	 * to try to reset this state nor call reset() from resetInternal. After
	 * this method returns the state of the object should allow a new byte
	 * stream to be parsed. The contents parsed so far are lost. You must
	 * <ul>
	 * <li>reset all variables used to store intermediate values
	 * <li>reset all parsers used by your parser
	 * <ul>
	 */
	protected abstract void resetInternal();

	protected abstract void notifyListeners();

	/**
	 * (add JAVADOC)
	 * 
	 * @param length
	 */
	private void setTlvLength(int length) {
		this.tlvLength = this.octetsLeft = length;
		checkContentsLength(this.tlvLength);
	}

	public String toString() {
		return new ToStringBuilder(this, Style.getDefaultStyle()).append(
				"TLVParser state", this.stateEnum).toString();
	}

}