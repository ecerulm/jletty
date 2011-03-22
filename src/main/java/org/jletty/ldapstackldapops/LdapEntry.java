package org.jletty.ldapstackldapops;

import javax.naming.directory.ModificationItem;

import org.jletty.dn.DistinguishedName;
import org.jletty.jndiprovider.LdapAttributes;

public interface LdapEntry {

	LdapAttributes getAttributes();

	DistinguishedName getName();

	void modify(ModificationItem[] mods) throws LdapException;

	LdapEntry copy();

	// Attributes getAttributesAsNamingAttributes();

}
