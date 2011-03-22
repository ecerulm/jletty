/*
 * $Id: PresentFilterParser.java,v 1.3 2006/02/27 18:23:00 ecerulm Exp $
 * Created on Jun 25, 2004
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.FilterListener;
import org.jletty.ldapstackldapops.PresentFilter;

/**
 * @author $Author: ecerulm $
 * 
 */
public class PresentFilterParser extends LDAPStringParser {

	private FilterListener listener;

	public static final byte TAG = BerTags.CHOICE_7;

	/**
	 * @param listener
	 */
	public PresentFilterParser(FilterListener listener) {
		super(null);
		this.listener = listener;
	}

	/**
	 * @see org.jletty.ldapstackparsers.implementation.TLVParser#notifyListeners()
	 */
	protected void notifyListeners() {
		this.listener.data(new PresentFilter(this.theString));
	}

	/**
	 * @see org.jletty.ldapstackparsers.implementation.TLVParser#getExpectedTag()
	 */
	protected boolean checkExpectedTag(byte tag) {
		return TAG == tag;
	}

}