/*
 * $Id: BinaryLogicFilterParser.java,v 1.3 2006/02/27 18:23:00 ecerulm Exp $
 * Created on Jun 29, 2004
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.jletty.ldapstackldapops.Filter;
import org.jletty.ldapstackldapops.FilterListener;
import org.jletty.ldapstackldapops.SetOfFiltersListener;

/**
 * @author $Author: ecerulm $
 * 
 */
abstract class BinaryLogicFilterParser extends TLVParser {
	private FilterListener listener;

	private Filter[] setOfFilters = null;

	private final SetOfFiltersListener setOfListener = new SetOfFiltersListener() {
		public void data(Filter[] set) {
			BinaryLogicFilterParser.this.setOfFilters = set;
		}
	};

	private final Parser setOfParser = new SetOfFiltersParser(this.setOfListener);

	/**
	 * @param listener2
	 */
	public BinaryLogicFilterParser(FilterListener listener) {
		super();
		this.listener = listener;
	}

	protected final boolean parseContents(ByteBuffer buffer) {
		return this.setOfParser.parse(buffer);
	}

	protected final void checkContentsLength(final int tlvLength) {
		super.checkContentsLength(tlvLength);
		this.setOfParser.implicit(tlvLength);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rubenlaguna.ldapstack.parsers.implementation.TLVParser#resetInternal()
	 */
	protected final void resetInternal() {
		this.setOfFilters = null;
		this.setOfParser.reset();
	}

	protected final void notifyListeners() {
		this.listener.data(getFilter(this.setOfFilters));
	}

	protected abstract Filter getFilter(Filter[] f);
}