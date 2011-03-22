package org.jletty.db.jndiprovider;

import junitx.extensions.EqualsHashCodeTestCase;

import org.jletty.dn.Rfc2253NameParserImpl;

public class DistinguishedNameEqualityTest extends EqualsHashCodeTestCase {

	public DistinguishedNameEqualityTest(String name) {
		super(name);
	}

	protected Object createInstance() throws Exception {
		// return new DistinguishedName(new String[]{"cn=New Entry","o=org"});
		return new Rfc2253NameParserImpl().parse("cn=NewEntry,o=org");
	}

	protected Object createNotEqualInstance() throws Exception {
		// return new DistinguishedName(new String[]{"cn=New Entry","o=org2"});
		return new Rfc2253NameParserImpl().parse("cn=New Entry,o=org2");
	}
}
