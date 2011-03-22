package org.jletty.aci;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NamingException;

import org.jletty.dn.DistinguishedName;
import org.jletty.dn.Rfc2253NameParserImpl;

public class AciContext {

	private DistinguishedName entry;

	public DistinguishedName getEntry() {
		return entry;
	}

	public void setEntry(DistinguishedName theEntrydn) {
		this.entry=theEntrydn;				
	}
	
	public void setEntry(String theEntrydn) throws NamingException {
		this.entry= (DistinguishedName) new Rfc2253NameParserImpl().parse(theEntrydn);
	}

}
