/*
 * Created on Jul 16, 2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.jletty.ldapstackldapops.Filter;
import org.jletty.ldapstackldapops.FilterListener;
import org.jletty.util.HexUtils;

/**
 * @author Administrator
 * 
 */
public class FilterParser extends TLVParser {

	private Filter filter = null;

	private static final byte[] validTags = new byte[] {
			PresentFilterParser.TAG, EqMatchFilterParser.TAG,
			SubstringFilterParser.TAG, GreaterOrEqualFilterParser.TAG,
			LessOrEqualFilterParser.TAG, ApproxMatchFilterParser.TAG,
			AndFilterParser.TAG, OrFilterParser.TAG, NotFilterParser.TAG,
			ExtensibleMatchFilterParser.TAG };

	private byte tag = 0;

	private final FilterListener componentListener = new FilterListener() {
		public void data(Filter f) {
			FilterParser.this.filter = f;
		}
	};

	private final Parser presentFilterParser = new PresentFilterParser(
			this.componentListener);

	private final Parser eqMatchFilterParser = new EqMatchFilterParser(
			this.componentListener);

	private final Parser substringFilterParser = new SubstringFilterParser(
			this.componentListener);

	private final Parser greaterOrEqualFilterParser = new GreaterOrEqualFilterParser(
			this.componentListener);

	private final Parser lessOrEqualFilterParser = new LessOrEqualFilterParser(
			this.componentListener);

	private final Parser approxMatchFilterParser = new ApproxMatchFilterParser(
			this.componentListener);

	private final Parser extensibleMatchFilterParser = new ExtensibleMatchFilterParser(
			this.componentListener);

	private Parser parser = null;

	private FilterListener listener;

	private State WAITINGNEWFILTER = new ParserStateBase("Waiting New Filter") {

		public boolean parse(ByteBuffer buffer) {
			// tag was read by the superclass and we received it from the
			// checkExpectedTag method
			switch (FilterParser.this.tag) {
			case PresentFilterParser.TAG:
				FilterParser.this.parser = FilterParser.this.presentFilterParser;
				break;
			case EqMatchFilterParser.TAG:
				FilterParser.this.parser = FilterParser.this.eqMatchFilterParser;
				break;
			case SubstringFilterParser.TAG:
				FilterParser.this.parser = FilterParser.this.substringFilterParser;
				break;
			case GreaterOrEqualFilterParser.TAG:
				FilterParser.this.parser = FilterParser.this.greaterOrEqualFilterParser;
				break;
			case LessOrEqualFilterParser.TAG:
				FilterParser.this.parser = FilterParser.this.lessOrEqualFilterParser;
				break;
			case ApproxMatchFilterParser.TAG:
				FilterParser.this.parser = FilterParser.this.approxMatchFilterParser;
				break;
			case AndFilterParser.TAG:
				FilterParser.this.parser = new AndFilterParser(FilterParser.this.componentListener);
				break;
			case OrFilterParser.TAG:
				FilterParser.this.parser = new OrFilterParser(FilterParser.this.componentListener);
				break;
			case NotFilterParser.TAG:
				FilterParser.this.parser = new NotFilterParser(FilterParser.this.componentListener);
				break;
			case ExtensibleMatchFilterParser.TAG:
				FilterParser.this.parser = FilterParser.this.extensibleMatchFilterParser;
				break;
			default:
				throw new ParserException("Unexpected tag "
						+ HexUtils.toHexString(FilterParser.this.tag)
						+ " while expecting a tag denoting a Filter");
			}
			FilterParser.this.parser.implicit(getTlvLength());
			FilterParser.this.state = FilterParser.this.RFILTER;
			return true;
		}

	};

	private State RFILTER = new ParserStateBase("Reading a filter") {

		public boolean parse(ByteBuffer buffer) {
			final boolean completed = FilterParser.this.parser.parse(buffer);
			if (completed) {
				FilterParser.this.state = FilterParser.this.FINAL;
			}
			return completed;
		}

	};

	private State FINAL = new FinalParserState();

	private State state = this.WAITINGNEWFILTER;

	private int octetsLeft;

	/**
	 * @param listener
	 */
	public FilterParser(FilterListener listener) {
		this.listener = listener;
	}

	protected boolean parseContents(ByteBuffer buffer) {

		while (!this.state.equals(this.FINAL)) {
			int tmp = buffer.remaining();
			boolean completed = this.state.parse(buffer);
			this.octetsLeft = this.octetsLeft - (tmp - buffer.remaining());
			if (!completed) {
				return false;
			}
		}
		return true;
	}

	protected boolean checkExpectedTag(byte tag) {
		for (int i = 0; i < validTags.length; i++) {
			if (tag == validTags[i]) { // its a valid tag
				this.tag = tag;
				return true;
			}
		}
		return false;
	}

	protected void resetInternal() {
		this.tag = 0;
		this.filter = null;
		this.state = this.WAITINGNEWFILTER;
		if (this.parser != null) {
			this.parser.reset();
		}
	}

	protected void notifyListeners() {
		this.listener.data(this.filter);
	}

	protected void checkContentsLength(final int tlvLength) {
		super.checkContentsLength(tlvLength);
		this.octetsLeft = tlvLength;
	}
}