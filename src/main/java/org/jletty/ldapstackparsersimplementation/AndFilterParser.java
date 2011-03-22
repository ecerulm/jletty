/*
 * $Id: AndFilterParser.java,v 1.2 2006/02/27 18:20:14 ecerulm Exp $
 * Created on Jun 29, 2004
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.AndFilter;
import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.Filter;
import org.jletty.ldapstackldapops.FilterListener;

/**
 * @author $Author: ecerulm $
 * 
 */
public class AndFilterParser extends BinaryLogicFilterParser {

	public static final int TAG = (BerTags.CHOICE_0 | BerTags.CONSTRUCTED);

	/**
	 * @param listener2
	 */
	public AndFilterParser(FilterListener listener) {
		super(listener);
	}

	protected boolean checkExpectedTag(byte tag) {
		return AndFilterParser.TAG == tag;
	}

	protected Filter getFilter(Filter[] f) {
		return new AndFilter(f);
	}

}