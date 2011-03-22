package org.jletty.schema;

import org.jletty.ldapstackldapops.LdapInvalidAttributeSyntaxException;

public class PostalAddress extends DirectoryString {

	public PostalAddress(byte[] octetstring)
			throws LdapInvalidAttributeSyntaxException {
		super(octetstring);
	}

}
