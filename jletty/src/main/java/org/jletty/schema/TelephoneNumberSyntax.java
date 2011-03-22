/*
 * Created on 16-feb-2005
 *
 */
package org.jletty.schema;

import org.jletty.ldapstackldapops.LdapInvalidAttributeSyntaxException;

/**
 * @author Ruben
 * 
 */
public class TelephoneNumberSyntax implements Syntax {

	public AttributeValue get(byte[] octetstring)
			throws LdapInvalidAttributeSyntaxException {
		return new TelephoneNumber(octetstring);
	}

	public String getName() {
		return "1.3.6.1.4.1.1466.115.121.1.50";
	}

}
