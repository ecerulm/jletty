package org.jletty.ldapstackldapops;

public class LdapNoSuchObjectException extends LdapException {

	private static final long serialVersionUID = 1L;

	public LdapNoSuchObjectException(String explanation) {
		super(explanation, LDAPResultCode.NO_SUCH_OBJECT);
	}

	public LdapNoSuchObjectException() {
		this("parent entry doesn't exist");
	}

	public LdapNoSuchObjectException(Throwable e, String explanation) {
		this(explanation);
		initCause(e);
	}

}
