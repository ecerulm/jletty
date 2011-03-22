package org.jletty.db.jndiprovider;


import org.jletty.dn.Rfc2253NameParserImpl;
import org.jletty.util.EqualsHashCodeTestCase;

public class DistinguishedNameEqualityTest extends EqualsHashCodeTestCase {


        @Override
	protected Object createInstance() throws Exception {
		// return new DistinguishedName(new String[]{"cn=New Entry","o=org"});
		return new Rfc2253NameParserImpl().parse("cn=NewEntry,o=org");
	}

        @Override
	protected Object createNotEqualInstance() throws Exception {
		// return new DistinguishedName(new String[]{"cn=New Entry","o=org2"});
		return new Rfc2253NameParserImpl().parse("cn=New Entry,o=org2");
	}
        
        
}
