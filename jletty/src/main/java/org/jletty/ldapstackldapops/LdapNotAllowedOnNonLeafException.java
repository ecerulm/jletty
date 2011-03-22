package org.jletty.ldapstackldapops;

public class LdapNotAllowedOnNonLeafException extends LdapException {

	private static final long serialVersionUID = 1L;

	public LdapNotAllowedOnNonLeafException(String explanation) {
		super(explanation, LDAPResultCode.NOT_ALLOWED_ON_NON_LEAF);
	}

	public LdapNotAllowedOnNonLeafException() {
		this("");
	}

}
