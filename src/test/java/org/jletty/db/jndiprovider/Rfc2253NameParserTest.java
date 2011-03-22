package org.jletty.db.jndiprovider;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NamingException;

import junit.framework.TestCase;

import org.jletty.dn.DistinguishedName;
import org.jletty.dn.DnAtom;
import org.jletty.dn.DnNameComponent;
import org.jletty.dn.Rfc2253NameParserImpl;
import org.jletty.schema.AttributeType;
import org.jletty.schema.AttributeUsage;
import org.jletty.schema.Schema;

public class Rfc2253NameParserTest extends TestCase {

	Rfc2253NameParserImpl parser;

	protected void setUp() throws Exception {
		super.setUp();
		parser = new Rfc2253NameParserImpl();
	}

	public void testParse() throws Exception {
		Name name = parser.parse("c=a");
		Name expected = new DistinguishedName(new String[] { "c=a" });
		assertEquals(expected, name);
	}

	public void testParseSeveralComponents() throws NamingException {
		Name name = parser.parse("cn=New Entry,o=org,c=US");
		Name expected = new DistinguishedName(new String[] { "cn=New Entry",
				"o=org", "c=US" });
		assertEquals(expected, name);

	}

	public void testParseEndSpace() throws Exception {
		Name name = parser.parse("cn=New Entry\\ ,c=US");
		Name expected = new DistinguishedName(new String[] { "cn=New Entry\\ ",
				"c=US" });
		assertEquals(expected, name);
		assertEquals("cn=New Entry\\ ,c=US", name.toString());

		name = parser.parse("cn=New Entry \\ ,c=US");
		expected = new DistinguishedName(new String[] { "cn=New Entry \\ ",
				"c=US" });
		assertEquals(expected, name);
		assertEquals("cn=New Entry \\ ,c=US", name.toString());
	}

	public void testParseEndSpace2() throws Exception {
		Name name = parser.parse("cn=New Entry\\  ,c=US");
		DistinguishedName expected = new DistinguishedName(new String[] {
				"cn=New Entry\\  ", "c=US" });
		assertEquals(expected, name);
		assertEquals("cn=New Entry\\ ,c=US", name.toString());

		name = parser.parse("cn=New Entry \\  ,c=US");
		expected = new DistinguishedName(new String[] { "cn=New Entry \\  ",
				"c=US" });
		assertEquals(expected, name);
		assertEquals("cn=New Entry \\ ,c=US", name.toString());
	}

	public void testParseEndSpaceNotEscaped() throws Exception {
		Name name = parser.parse("cn=New Entry ,c=US");
		DistinguishedName expected = new DistinguishedName(new String[] {
				"cn=New Entry", "c=US" });
		assertEquals(expected, name);
	}

	public void testParseLeadingSpaces() throws Exception {
		Name name = parser.parse("cn=New Entry, c=US");
		DistinguishedName expected = new DistinguishedName(new String[] {
				"cn=New Entry", "c=US" });
		assertEquals(expected, name);

		name = parser.parse(" cn=New Entry,c=US");
		assertEquals(expected, name);

	}

	public void testOrder() throws Exception {
		Name name = parser.parse("cn=New Entry ,c=US");
		DistinguishedName expected = new DistinguishedName(new String[] {
				"cn=New Entry", "c=US" });
		assertEquals("c=US", name.get(0));
		assertEquals("cn=New Entry", name.get(1));
		assertEquals(expected, name);
	}

	public void testOid() throws Exception {
		Schema schema = Schema.getInstance();
		schema.addAttributeType(new AttributeType("2.5.4.3",
				new String[] { "cn" }, "a test attr", false, null,
				"caseIgnoreMatch", "caseIgnoreOrderingMatch",
				"caseIgnoreSubstringsMatch", "1.3.6.1.4.1.1466.115.121.1.26",
				-1, false, true, false, AttributeUsage.USER_APPLICATIONS));
		Name name = parser.parse("2.5.4.3=New Entry ,c=US");
		DistinguishedName expected = new DistinguishedName(new String[] {
				"cn=New Entry", "c=US" });
		assertEquals("c=US", name.get(0));
		assertEquals("cn=New Entry", name.get(1));
		assertEquals(expected, name);
	}

	public void testOid2() throws Exception {
		Schema schema = Schema.getInstance();
		schema.addAttributeType(new AttributeType("2.5.4.3",
				new String[] { "cn" }, "a test attr", false, null,
				"caseIgnoreMatch", "caseIgnoreOrderingMatch",
				"caseIgnoreSubstringsMatch", "1.3.6.1.4.1.1466.115.121.1.26",
				-1, false, true, false, AttributeUsage.USER_APPLICATIONS));
		schema.addAttributeType(new AttributeType("2.5.4.4",
				new String[] { "sn" }, "a test attr", false, null,
				"caseIgnoreMatch", "caseIgnoreOrderingMatch",
				"caseIgnoreSubstringsMatch", "1.3.6.1.4.1.1466.115.121.1.26",
				-1, false, true, false, AttributeUsage.USER_APPLICATIONS));
		Name name = parser.parse("2.5.4.3=New Entry+2.5.4.4=surname1 ,c=US");
		DistinguishedName expected = new DistinguishedName(new String[] {
				"cn=New Entry+sn=surname1", "c=US" });
		assertEquals("c=US", name.get(0));
		assertEquals("cn=New Entry+sn=surname1", name.get(1));
		assertEquals(expected, name);
	}

	public void testOid3() throws Exception {
		Schema schema = Schema.getInstance();
		schema.addAttributeType(new AttributeType("2.5.4.3",
				new String[] { "cn" }, "a test attr", false, null,
				"caseIgnoreMatch", "caseIgnoreOrderingMatch",
				"caseIgnoreSubstringsMatch", "1.3.6.1.4.1.1466.115.121.1.26",
				-1, false, true, false, AttributeUsage.USER_APPLICATIONS));
		schema.addAttributeType(new AttributeType("2.5.4.4",
				new String[] { "sn" }, "a test attr", false, null,
				"caseIgnoreMatch", "caseIgnoreOrderingMatch",
				"caseIgnoreSubstringsMatch", "1.3.6.1.4.1.1466.115.121.1.26",
				-1, false, true, false, AttributeUsage.USER_APPLICATIONS));
		Name name = parser.parse("2.5.4.3=New Entry\\+a ,c=US");
		DistinguishedName expected = new DistinguishedName(new String[] {
				"cn=New Entry\\+a", "c=US" });
		assertEquals("c=US", name.get(0));
		assertEquals("cn=New Entry\\+a", name.get(1));
		assertEquals(expected, name);
	}

	public void testOid4() throws Exception {
		Schema schema = Schema.getInstance();
		schema.addAttributeType(new AttributeType("2.5.4.3",
				new String[] { "cn" }, "a test attr", false, null,
				"caseIgnoreMatch", "caseIgnoreOrderingMatch",
				"caseIgnoreSubstringsMatch", "1.3.6.1.4.1.1466.115.121.1.26",
				-1, false, true, false, AttributeUsage.USER_APPLICATIONS));
		schema.addAttributeType(new AttributeType("2.5.4.4",
				new String[] { "sn" }, "a test attr", false, null,
				"caseIgnoreMatch", "caseIgnoreOrderingMatch",
				"caseIgnoreSubstringsMatch", "1.3.6.1.4.1.1466.115.121.1.26",
				-1, false, true, false, AttributeUsage.USER_APPLICATIONS));
		Name name = parser.parse("2.5.4.3=New Entry\\+ ,c=US");
		DistinguishedName expected = new DistinguishedName(new String[] {
				"cn=New Entry\\+", "c=US" });
		assertEquals("c=US", name.get(0));
		assertEquals("cn=New Entry\\+", name.get(1));
		assertEquals(expected, name);
	}

	public void testOid5() throws Exception {
		Schema schema = Schema.getInstance();
		schema.addAttributeType(new AttributeType("2.5.4.3",
				new String[] { "cn" }, "a test attr", false, null,
				"caseIgnoreMatch", "caseIgnoreOrderingMatch",
				"caseIgnoreSubstringsMatch", "1.3.6.1.4.1.1466.115.121.1.26",
				-1, false, true, false, AttributeUsage.USER_APPLICATIONS));
		schema.addAttributeType(new AttributeType("2.5.4.4",
				new String[] { "sn" }, "a test attr", false, null,
				"caseIgnoreMatch", "caseIgnoreOrderingMatch",
				"caseIgnoreSubstringsMatch", "1.3.6.1.4.1.1466.115.121.1.26",
				-1, false, true, false, AttributeUsage.USER_APPLICATIONS));
		try {
			Name name = parser.parse("2.5.4.3=New Entry+ ,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}
	}

	public void testAttributeEmptyValue() throws Exception {
		try {
			Name name = parser.parse("cn=,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}
	}

	public void testAttributeWithNoStringRepresentation() throws Exception {
		try {
			Name name = parser.parse("cn=#HHSD,c=US"); // SD is not an hexpair
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}

		Name name = parser.parse("cn=\\#HHSD,c=US");
		DistinguishedName expected = new DistinguishedName(new String[] {
				"cn=\\#HHSD", "c=US" });
		assertEquals("c=US", name.get(0));
		assertEquals("cn=\\#HHSD", name.get(1));
		assertEquals(expected, name);
		assertEquals("cn=\\#HHSD,c=US", name.toString());
	}

	public void testComma() throws Exception {
		try {
			Name name = parser.parse("cn=Corp ,Inc.,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}

		Name name = parser.parse("cn=Corp \\,Inc.,c=US");
		assertEquals("c=US", name.get(0));
		assertEquals("cn=Corp ,Inc.", name.get(1));
		DistinguishedName expected = new DistinguishedName(new String[] {
				"cn=Corp \\,Inc.", "c=US" });
		assertEquals(expected, name);
		assertEquals("cn=Corp \\,Inc.,c=US", name.toString());
	}

	public void testPlus() throws Exception {
		try {
			Name name = parser.parse("cn=Corp+a,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}
		try {
			Name name = parser.parse("cn=Corp+,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}
		try {
			Name name = parser.parse("cn=+corp,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}
		Name name = parser.parse("cn=Corp\\+a,c=US");
		assertEquals("c=US", name.get(0));
		assertEquals("cn=Corp\\+a", name.get(1));
		DistinguishedName expected = new DistinguishedName(new String[] {
				"cn=Corp\\+a", "c=US" });
		assertEquals(expected, name);
		assertEquals("cn=Corp\\+a,c=US", name.toString());

	}

	public void testMultivaluedRdn() throws Exception {
		Name name = parser.parse("cn=cn1+sn=sn1,c=US");
		assertEquals("c=US", name.get(0));
		assertEquals("cn=cn1+sn=sn1", name.get(1));
		assertTrue(name instanceof DistinguishedName);
		DistinguishedName dn = (DistinguishedName) name;
		DnNameComponent c = dn.getDnNameComponent(1);
		assertEquals(2, c.getAtoms().size());
		assertEquals("cn=cn1", c.getAtoms().get(0).toString());
		assertEquals("sn=sn1", c.getAtoms().get(1).toString());
		DistinguishedName expected = new DistinguishedName(new String[] {
				"cn=cn1+sn=sn1", "c=US" });
		assertEquals(expected, name);
		assertEquals("cn=cn1+sn=sn1,c=US", name.toString());
	}

	public void testMultivaluedRdnPlusSign() throws Exception {
		Name name = parser.parse("cn=cn\\+1+sn=sn1,c=US");
		assertEquals("c=US", name.get(0));
		assertEquals("cn=cn\\+1+sn=sn1", name.get(1));
		assertTrue(name instanceof DistinguishedName);
		DistinguishedName dn = (DistinguishedName) name;
		DnNameComponent c = dn.getDnNameComponent(1);
		assertEquals(2, c.getAtoms().size());
		assertEquals("cn+1", ((DnAtom) c.getAtoms().get(0)).getAttributeValue());
		assertEquals("sn1", ((DnAtom) c.getAtoms().get(1)).getAttributeValue());
		DistinguishedName expected = new DistinguishedName(new String[] {
				"cn=cn\\+1+sn=sn1", "c=US" });
		assertEquals(expected, name);
		assertEquals("cn=cn\\+1+sn=sn1,c=US", name.toString());
	}

	public void testMultivaluedRdnEqualsSign() throws Exception {
		Name name = parser.parse("cn=cn\\=1+sn=sn1,c=US");
		assertEquals("c=US", name.get(0));
		assertEquals("cn=cn\\=1+sn=sn1", name.get(1));
		assertTrue(name instanceof DistinguishedName);
		DistinguishedName dn = (DistinguishedName) name;
		DnNameComponent c = dn.getDnNameComponent(1);
		assertEquals(2, c.getAtoms().size());
		assertEquals("cn=1", ((DnAtom) c.getAtoms().get(0)).getAttributeValue());
		assertEquals("sn1", ((DnAtom) c.getAtoms().get(1)).getAttributeValue());
		DistinguishedName expected = new DistinguishedName(new String[] {
				"cn=cn\\=1+sn=sn1", "c=US" });
		assertEquals(expected, name);
		assertEquals("cn=cn\\=1+sn=sn1,c=US", name.toString());
	}

	public void testQuote() throws Exception {
		try {
			Name name = parser.parse("cn=he\"llo,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}
		try {
			Name name = parser.parse("cn=\"hello,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}

		try {
			Name name = parser.parse("cn=he\\\\\"llo,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}

		try {
			Name name = parser.parse("cn=\"Corp\",c=US");
		} catch (InvalidNameException e) {
			// ignore
		}

		Name name = parser.parse("cn=the \\\"great\\\" corp,c=US");
		assertEquals("c=US", name.get(0));
		assertEquals("cn=the \"great\" corp", name.get(1));
		DistinguishedName expected = new DistinguishedName(new String[] {
				"cn=the \\\"great\\\" corp", "c=US" });
		assertEquals(expected, name);
		assertEquals("cn=the \\\"great\\\" corp,c=US", name.toString());
	}

	public void testSlash() throws Exception {
		try {
			Name name = parser.parse("cn=Corp \\Inc.,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}
		try {
			Name name = parser.parse("cn=Corp \\\\,Inc.,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}
		try {
			Name name = parser.parse("cn=Corp \\\\=Inc.,c=US");
			fail("parsed " + name.toString());
		} catch (InvalidNameException e) {
			// ignore
		}
		try {
			Name name = parser.parse("cn=Corp \\\\+Inc.,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}
		try {
			Name name = parser.parse("cn=Corp \\\\<Inc.,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}
		try {
			Name name = parser.parse("cn=Corp \\\\>Inc.,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}
		try {
			Name name = parser.parse("cn=Corp \\\\#Inc.,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}
		try {
			Name name = parser.parse("cn=Corp \\\\;Inc.,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}
		try {
			Name name = parser.parse("cn=Corp \\\\\"Inc.,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}
		try {
			Name name = parser.parse("cn=Corp \\\\\\Inc.,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}

		Name name = parser.parse("cn=Corp \\\\Inc.,c=US");
		assertEquals("c=US", name.get(0));
		assertEquals("cn=Corp \\Inc.", name.get(1));
		DistinguishedName expected = new DistinguishedName(new String[] {
				"cn=Corp \\\\Inc.", "c=US" });
		assertEquals(expected, name);
		assertEquals("cn=Corp \\\\Inc.,c=US", name.toString());

		name = parser.parse("cn=Corp\\\\,c=US");
		assertEquals("c=US", name.get(0));
		assertEquals("cn=Corp\\", name.get(1));
		expected = new DistinguishedName(new String[] { "cn=Corp\\\\", "c=US" });
		assertEquals(expected, name);
		assertEquals("cn=Corp\\\\,c=US", name.toString());
	}

	public void testGreaterThan() throws Exception {
		try {
			Name name = parser.parse("cn=h>a,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}
		try {
			Name name = parser.parse("cn=>ha,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}
		try {
			Name name = parser.parse("cn=ha>,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}

		Name name = parser.parse("cn=h\\>a,c=US");
		checkAssertions(name, "cn=h\\>a,c=US", "cn=h>a", "c=US");
	}

	public void testLessThan() throws Exception {
		try {
			Name name = parser.parse("cn=h<a,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}
		try {
			Name name = parser.parse("cn=<ha,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}
		try {
			Name name = parser.parse("cn=ha<,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}

		Name name = parser.parse("cn=h\\<a,c=US");
		checkAssertions(name, "cn=h\\<a,c=US", "cn=h<a", "c=US");
	}

	public void testSemicolon() throws Exception {
		try {
			Name name = parser.parse("cn=h;a,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}
		try {
			Name name = parser.parse("cn=;ha,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}
		try {
			Name name = parser.parse("cn=ha;,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}

		Name name = parser.parse("cn=h\\;a,c=US");
		checkAssertions(name, "cn=h\\;a,c=US", "cn=h;a", "c=US");
	}

	public void testOctothorpe() throws Exception {
		try {
			Name name = parser.parse("cn=a\\#b,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}
		try {
			Name name = parser.parse("cn=ab\\#,c=US");
			fail();
		} catch (InvalidNameException e) {
			// ignore
		}

		Name name = parser.parse("cn=#1a2e3c,c=US");
		checkAssertions(name, "cn=#1a2e3c", "c=US");
		name = parser.parse("cn=\\#hola,c=US");
		checkAssertions(name, "cn=\\#hola", "c=US");
	}

	public void testLeadSpace() throws Exception {
		Name name = parser.parse("cn=\\ hola,c=US");
		checkAssertions(name, "cn=\\ hola", "c=US");
		name = parser.parse("cn=\\  hola,c=US");
		checkAssertions(name, "cn=\\  hola", "c=US");
	}

	public void testEndSpace() throws Exception {
		Name name = parser.parse("cn=hola\\ ,c=US");
		checkAssertions(name, "cn=hola\\ ,c=US", "cn=hola ", "c=US");
		name = parser.parse("cn=hola \\ ,c=US");
		checkAssertions(name, "cn=hola \\ ,c=US", "cn=hola  ", "c=US");
	}

	public void testEscapedAscii() throws Exception {
		try {
			Name name = parser.parse("cn=h\\AS"); // AS is not allowed hex
			fail();
		} catch (InvalidNameException e) {
			// ignore;
		}
		Name name = parser.parse("cn=h\\41,c=US"); // cn=ha
		checkAssertions(name, "cn=hA", "c=US");
		name = parser.parse("sn=Lu\\C4\\8Ci\\C4\\87,c=US");
		checkAssertions(name, "sn=Lu\u010ci\u0107", "c=US");

	}

	public void testParseEquals() throws Exception {
		try {
			Name name = parser.parse("cn=hola=a");

			fail("parsed " + name);
		} catch (InvalidNameException e) {
			// ignore
		}
	}

	public void testParseTelephoneNumber() throws Exception {
		Name name = parser.parse("telephoneNumber=\\+41 1 268 1540,c=US");
		checkAssertions(name, "telephoneNumber=\\+41 1 268 1540", "c=US");
	}

	public void testTest() {

	}

	private void checkAssertions(Name name, String firstComponent,
			String secondComponent) throws Exception {
		checkAssertions(name, firstComponent + "," + secondComponent,
				firstComponent, secondComponent);
	}

	private void checkAssertions(Name name, String dn, String firstComponent,
			String secondComponent) throws Exception {
		assertEquals(secondComponent, name.get(0));
		assertEquals(firstComponent, name.get(1));
		assertEquals(dn, name.toString());
	}

}
