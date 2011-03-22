package org.jletty.ldapstackldapops;

public class LdapConstraintViolationException extends LdapException {

	private static final long serialVersionUID = 1L;

	public LdapConstraintViolationException(String message) {
		super(message, LDAPResultCode.CONSTRAINT_VIOLATION);
	}

}
