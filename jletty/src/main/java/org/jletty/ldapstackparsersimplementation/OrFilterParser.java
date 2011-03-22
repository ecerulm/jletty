/*
 * $Id: OrFilterParser.java,v 1.2 2006/02/27 18:20:14 ecerulm Exp $
 * Created on Jun 29, 2004
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.Filter;
import org.jletty.ldapstackldapops.FilterListener;
import org.jletty.ldapstackldapops.OrFilter;

/**
 * @author $Author: ecerulm $
 * 
 */
public class OrFilterParser extends BinaryLogicFilterParser {
	public static final int TAG = (BerTags.CHOICE_1 | BerTags.CONSTRUCTED);

	public OrFilterParser(FilterListener listener) {
		super(listener);
	}

	protected boolean checkExpectedTag(byte tag) {
		return TAG == tag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rubenlaguna.ldapstack.parsers.implementation.BinaryLogicFilterParser#getFilter(com.rubenlaguna.ldapstack.ldapops.Filter[])
	 */
	protected Filter getFilter(Filter[] f) {
		return new OrFilter(f);
	}
}