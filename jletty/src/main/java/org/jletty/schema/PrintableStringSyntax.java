/*
 * Created on 17-feb-2005
 *
 */
package org.jletty.schema;

import org.jletty.ldapstackldapops.LdapInvalidAttributeSyntaxException;

/**
 * @author Ruben
 * 
 */
public class PrintableStringSyntax implements Syntax {

	public AttributeValue get(byte[] octetstring)
			throws LdapInvalidAttributeSyntaxException {
		return new PrintableString(octetstring);
	}

	public String getName() {
		return "1.3.6.1.4.1.1466.115.121.1.44";
	}

}
