package org.jletty.ldapstackldapops;

public class LdapInvalidAttributeSyntaxException extends LdapException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int valueNumber;

	public LdapInvalidAttributeSyntaxException(String message) {
		super(message, LDAPResultCode.INVALID_ATTR_SYNTAX);
	}

	public LdapInvalidAttributeSyntaxException() {
		this("");
	}

	public LdapInvalidAttributeSyntaxException(String message, Throwable e) {
		this(message);
		this.initCause(e);
	}

	public LdapInvalidAttributeSyntaxException(Throwable e) {
		this("");
		this.initCause(e);
	}

	public void setValueNumber(int i) {
		this.valueNumber = i;

	}

	public int getValueNumber() {
		return this.valueNumber;
	}

}
