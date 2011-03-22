/*
 * Created on 15-sep-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.Filter;
import org.jletty.ldapstackldapops.FilterListener;
import org.jletty.ldapstackldapops.NotFilter;

/**
 * @author Ruben
 * 
 */
public class NotFilterParser extends TLVParser {

	public static final int TAG = (BerTags.CHOICE_2 | BerTags.CONSTRUCTED);

	private FilterListener listener;

	private Filter filter = null;

	private FilterListener l = new FilterListener() {
		public void data(Filter f) {
			NotFilterParser.this.filter = f;
		}
	};

	private Parser parser = new FilterParser(this.l);

	public NotFilterParser(FilterListener listener) {
		this.listener = listener;
	}

	protected boolean parseContents(ByteBuffer buffer) {
		return this.parser.parse(buffer);
	}

	protected boolean checkExpectedTag(byte tag) throws ParserException {
		return NotFilterParser.TAG == tag;
	}

	protected void resetInternal() {
		this.parser.reset();
	}

	protected void notifyListeners() {
		this.listener.data(new NotFilter(this.filter));
	}

}