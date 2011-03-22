/*
 * Created on Jul 16, 2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.Filter;
import org.jletty.ldapstackldapops.FilterListener;
import org.jletty.ldapstackldapops.SetOfFiltersListener;
import org.jletty.util.HexUtils;

/**
 * @author Administrator
 * 
 */
public final class SetOfFiltersParser extends TLVParser {

	private Set filters = new HashSet();

	private final FilterListener componentListener = new FilterListener() {
		public void data(Filter f) {
			SetOfFiltersParser.this.filters.add(f);
		}
	};

	private Parser parser = new FilterParser(this.componentListener);

	private SetOfFiltersListener listener;

	private State RFILTER = new ParserStateBase("Reading a filter") {
		public boolean parse(ByteBuffer buffer) {
			return SetOfFiltersParser.this.parser.parse(buffer);
		}
	};

	private State FINAL = new FinalParserState();

	private State state = this.RFILTER;

	private int octetsLeft;

	/**
	 * @param listener
	 */
	public SetOfFiltersParser(SetOfFiltersListener listener) {
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
			if (this.octetsLeft <= 0) {
				this.state = this.FINAL;
			}
		}
		return true;
	}

	protected boolean checkExpectedTag(byte tag) {
		final byte expectedTag = BerTags.SETOF;
		boolean tagOk = expectedTag == tag;
		if (!tagOk) {
			throw new ParserException("Unexpected tag ("
					+ HexUtils.toHexString(tag) + ") while expecting ("
					+ HexUtils.toHexString(expectedTag) + ")");
		}
		return tagOk;
	}

	protected void resetInternal() {
		this.filters = new HashSet();
		this.state = this.RFILTER;
		this.parser.reset();
	}

	protected void notifyListeners() {
		Filter[] set = (Filter[]) this.filters.toArray(new Filter[0]);
		this.listener.data(set);
	}

	protected void checkContentsLength(int tlvLength) {
		super.checkContentsLength(tlvLength);
		this.octetsLeft = tlvLength;
	}
}