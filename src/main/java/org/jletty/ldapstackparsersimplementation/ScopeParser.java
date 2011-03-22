/*
 * $Id: ScopeParser.java,v 1.1 2006/02/12 19:22:25 ecerulm Exp $
 * Created on Jun 8, 2004
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.Scope;
import org.jletty.ldapstackldapops.ScopeListener;

/**
 * @author $Author: ecerulm $
 * 
 */
public class ScopeParser extends EnumeratedParser {

	private ScopeListener listener;

	public ScopeParser(ScopeListener listener) {

		this.listener = listener;
	}

	protected void notifyListeners(int value) {
		Scope scope;
		switch (value) {
		case 0:
			scope = Scope.BASE_OBJECT;
			break;
		case 1:
			scope = Scope.ONE_LEVEL;
			break;
		case 2:
			scope = Scope.WHOLE_SUBTREE;
			break;

		default:
			throw new ParserException("Unknown scope (" + value + ")");

		}
		this.listener.data(scope);
	}

}