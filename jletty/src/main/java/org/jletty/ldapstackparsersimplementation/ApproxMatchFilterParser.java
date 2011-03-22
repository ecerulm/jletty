/*
 * $Id: ApproxMatchFilterParser.java,v 1.2 2006/02/27 18:20:14 ecerulm Exp $
 * Created on Jun 29, 2004
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.ApproxMatchFilter;
import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.FilterListener;

/**
 * @author $Author: ecerulm $
 * 
 */
public class ApproxMatchFilterParser extends AttributeValueAssertionParser {
	public static final int TAG = (BerTags.CHOICE_8 | BerTags.CONSTRUCTED);

	private FilterListener listener;

	/**
	 * @param listener2
	 */
	public ApproxMatchFilterParser(FilterListener listener) {
		this.listener = listener;
	}

	protected final void notifyListener(String attrDesc, byte[] assertionValue) {
		this.listener.data(new ApproxMatchFilter(attrDesc, assertionValue));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rubenlaguna.ldapstack.parsers.implementation.TLVParser#getExpectedTag()
	 */
	protected boolean checkExpectedTag(byte tag) {
		return (TAG == tag);

	}

}