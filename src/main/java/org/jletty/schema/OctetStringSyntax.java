package org.jletty.schema;

import org.jletty.ldapstackldapops.LdapInvalidAttributeSyntaxException;

public class OctetStringSyntax implements Syntax {

	public AttributeValue get(byte[] octetstring)
			throws LdapInvalidAttributeSyntaxException {
		return new OctetString(octetstring);
	}

	public String getName() {
		return "1.3.6.1.4.1.1466.115.121.1.40";
	}

}
