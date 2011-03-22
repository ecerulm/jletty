package org.jletty.ldapstackldapops;

import javax.naming.Name;

public abstract class LdapException extends RuntimeException {

	private final LDAPResultCode resultCode;

	private Name resolvedName;

	public LdapException(String explanation, LDAPResultCode resultCode) {
		super(explanation);
		this.resultCode = resultCode;
	}

	public final LDAPResultCode getResultCode() {
		return this.resultCode;
	}

	public void setResolvedName(Name name) {
		this.resolvedName = name;
	}

	public Name getResolvedName() {
		return this.resolvedName;
	}

}
