package org.jletty.ldapstackldapops;

public class LdapNotAllowedOnRdnException extends LdapException {

	private static final long serialVersionUID = 1L;

	public LdapNotAllowedOnRdnException(String explanation) {
		super(explanation, LDAPResultCode.NOT_ALLOWED_ON_RDN);
	}

	public LdapNotAllowedOnRdnException() {
		this("not allowed on rdn");
	}

}
