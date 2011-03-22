package org.jletty.ldapstackldapops;

import org.jletty.dn.DistinguishedName;

public class LdapInvalidDnSyntaxException extends LdapException {

	private static final long serialVersionUID = 1L;

	public LdapInvalidDnSyntaxException(String explanation) {
		super(explanation, LDAPResultCode.INVALID_DN_SYNTAX);
		setResolvedName(new DistinguishedName());
	}

	public LdapInvalidDnSyntaxException() {
		this("Invalid DN");
	}

}
