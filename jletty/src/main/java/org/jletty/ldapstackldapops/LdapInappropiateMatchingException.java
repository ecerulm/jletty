package org.jletty.ldapstackldapops;

import org.jletty.dn.DistinguishedName;

public class LdapInappropiateMatchingException extends LdapException {

	private static final long serialVersionUID = 1L;

	public LdapInappropiateMatchingException() {
		this("");
	}

	public LdapInappropiateMatchingException(String explanation) {
		super(explanation, LDAPResultCode.INAPPROPIATE_MATCHING);
		setResolvedName(new DistinguishedName());
	}

}
