package org.jletty.ldapstackldapops;

public class LdapAttrOrValuesExistsException extends LdapException {

	private static final long serialVersionUID = 1L;

	public LdapAttrOrValuesExistsException(String explanation) {
		super(explanation, LDAPResultCode.ATTR_OR_VALUE_EXISTS);
	}

	public LdapAttrOrValuesExistsException() {
		this("");
	}

}
