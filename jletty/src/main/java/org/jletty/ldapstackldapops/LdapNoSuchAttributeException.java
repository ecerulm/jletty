package org.jletty.ldapstackldapops;

public class LdapNoSuchAttributeException extends LdapException {

	private static final long serialVersionUID = 1L;

	public LdapNoSuchAttributeException() {
		this("No such attribute");
	}

	public LdapNoSuchAttributeException(String explanation) {
		super(explanation, LDAPResultCode.NO_SUCH_ATTR);
	}

}
