/*
 * Created on 16-feb-2005
 *
 */
package org.jletty.schema;

import javax.naming.directory.InvalidAttributeValueException;

import org.jletty.ldapstackldapops.LdapInvalidAttributeSyntaxException;
import org.jletty.util.NotImplementedException;

/**
 * @author Ruben
 * 
 */
public class Oid extends DirectoryString implements Matchable, OctetStringable {

	/**
	 * @param octetstring
	 * @throws InvalidAttributeValueException
	 */
	public Oid(byte[] octetstring) throws LdapInvalidAttributeSyntaxException {
		super(octetstring);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jletty.schema.Matchable#caseIgnoreMatch(java.lang.Object)
	 */
	public boolean caseIgnoreMatch(Object o) {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jletty.schema.Matchable#caseExactMatch(java.lang.Object)
	 */
	public boolean caseExactMatch(Object o) {
		throw new NotImplementedException();
	}

}
