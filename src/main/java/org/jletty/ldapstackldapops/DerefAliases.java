/*
 * $Id: DerefAliases.java,v 1.1 2006/02/12 19:22:12 ecerulm Exp $
 * Created on Jun 15, 2004
 */
package org.jletty.ldapstackldapops;

import org.apache.commons.lang.enums.ValuedEnum;

/**
 * @author $Author: ecerulm $
 * 
 */
public class DerefAliases extends ValuedEnum {

	private static final long serialVersionUID = 1L;

	private static final int NEVER_DEREF_ALIASES_VALUE = 100;

	private static final int DEREF_IN_SEARCHING_VALUE = 110;

	private static final int DEREF_FINDING_BASE_VALUE = 120;

	private static final int DEREF_ALWAYS_VALUE = 130;

	public static final DerefAliases NEVER_DEREF_ALIASES = new DerefAliases(
			"Never deref aliases", NEVER_DEREF_ALIASES_VALUE);

	public static final DerefAliases DEREF_IN_SEARCHING = new DerefAliases(
			"Deref in searching", DEREF_IN_SEARCHING_VALUE);

	public static final DerefAliases DEREF_FINDING_BASE = new DerefAliases(
			"Deref in finding base object", DEREF_FINDING_BASE_VALUE);

	public static final DerefAliases DEREF_ALWAYS = new DerefAliases(
			"Deref always", DEREF_ALWAYS_VALUE);

	protected DerefAliases(String name, int value) {
		super(name, value);
	}

}