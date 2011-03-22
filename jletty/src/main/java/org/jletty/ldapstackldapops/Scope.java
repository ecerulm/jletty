/*
 * $Id: Scope.java,v 1.1 2006/02/12 19:22:13 ecerulm Exp $
 * Created on Jun 8, 2004
 */
package org.jletty.ldapstackldapops;

import org.apache.commons.lang.enums.ValuedEnum;

/**
 * @author $Author: ecerulm $
 * 
 */
public class Scope extends ValuedEnum {
	private static final long serialVersionUID = 1L;

	// Values
	public static final int ONE_LEVEL_VALUE = 100;

	public static final int BASE_OBJECT_VALUE = 110;

	public static final int WHOLE_SUBTREE_VALUE = 120;

	// Scope objects
	public static final Scope ONE_LEVEL = new Scope("oneLevel", ONE_LEVEL_VALUE);

	public static final Scope BASE_OBJECT = new Scope("base object",
			BASE_OBJECT_VALUE);

	public static final Scope WHOLE_SUBTREE = new Scope("whole subtree",
			WHOLE_SUBTREE_VALUE);

	/**
	 * @param name
	 * @param value
	 */
	protected Scope(String name, int value) {
		super(name + " scope", value);
	}

}