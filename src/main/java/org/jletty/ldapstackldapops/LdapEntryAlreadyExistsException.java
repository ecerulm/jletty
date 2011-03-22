package org.jletty.ldapstackldapops;

public class LdapEntryAlreadyExistsException extends LdapException {

	private static final long serialVersionUID = 1L;

	public LdapEntryAlreadyExistsException(String explanation) {
		super(explanation, LDAPResultCode.ENTRY_ALREADY_EXISTS);
	}

	public LdapEntryAlreadyExistsException() {
		this("");
	}

}
