package org.jletty.ldapstackldapops;

public class LdapObjectClassViolationException extends LdapException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LdapObjectClassViolationException(String message) {
		super(message, LDAPResultCode.OBJECTCLASS_VIOLATION);
	}

}
