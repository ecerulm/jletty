/*
 * Created on 15-feb-2005
 *
 */
package org.jletty.schema;

import org.jletty.ldapstackldapops.LdapInvalidAttributeSyntaxException;

/**
 * @author Ruben
 * 
 */
public interface Syntax {
	AttributeValue get(byte[] octetstring)
			throws LdapInvalidAttributeSyntaxException;

	String getName();
}
