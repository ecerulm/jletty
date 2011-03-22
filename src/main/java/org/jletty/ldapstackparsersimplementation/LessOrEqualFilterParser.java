/*
 * $Id: LessOrEqualFilterParser.java,v 1.2 2006/02/27 18:20:14 ecerulm Exp $
 * Created on Jun 29, 2004
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.FilterListener;
import org.jletty.ldapstackldapops.LessOrEqualFilter;

/**
 * @author $Author: ecerulm $
 * 
 */
public class LessOrEqualFilterParser extends AttributeValueAssertionParser {
	public static final int TAG = (BerTags.CHOICE_6 | BerTags.CONSTRUCTED);

	private FilterListener listener;

	/**
	 * @param listener2
	 */
	public LessOrEqualFilterParser(FilterListener listener) {
		super();
		this.listener = listener;
	}

	protected final void notifyListener(String attrDesc, byte[] assertionValue) {
		this.listener.data(new LessOrEqualFilter(attrDesc, assertionValue));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rubenlaguna.ldapstack.parsers.implementation.TLVParser#getExpectedTag()
	 */
	protected boolean checkExpectedTag(byte tag) {
		return LessOrEqualFilterParser.TAG == tag;

	}

}