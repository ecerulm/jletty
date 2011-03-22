/*
 * $Id: DerefAliasesParser.java,v 1.1 2006/02/12 19:22:25 ecerulm Exp $
 * Created on Jun 15, 2004
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.DerefAliases;
import org.jletty.ldapstackldapops.DerefAliasesListener;

/**
 * @author $Author: ecerulm $
 * 
 */
public class DerefAliasesParser extends EnumeratedParser {

	private DerefAliasesListener listener;

	// private DerefAliases derefAliases;

	/**
	 * @param listener
	 */
	public DerefAliasesParser(DerefAliasesListener listener) {

		this.listener = listener;
	}

	protected void notifyListeners(int valueToNotify) {
		DerefAliases toReturn = enumValue(valueToNotify);
		this.listener.data(toReturn);
	}

	protected DerefAliases enumValue(int id) {
		DerefAliases derefAliases;
		switch (id) {
		case 0:
			derefAliases = DerefAliases.NEVER_DEREF_ALIASES;
			break;
		case 1:
			derefAliases = DerefAliases.DEREF_IN_SEARCHING;
			break;
		case 2:
			derefAliases = DerefAliases.DEREF_FINDING_BASE;
			break;
		case 3:
			derefAliases = DerefAliases.DEREF_ALWAYS;
			break;
		default:
			throw new ParserException("Unknown derefAliases (" + id + ")");
		}
		;
		return derefAliases;
	}
}