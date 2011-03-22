package org.jletty.schema;

import junit.framework.TestCase;

import org.jletty.ldapstackldapops.LdapInvalidAttributeSyntaxException;

public class GuideSyntaxTest extends TestCase {

	private GuideSyntax syntax;

	protected void setUp() throws Exception {
		super.setUp();
		syntax = new GuideSyntax();
	}

	public void testGetOid() throws Exception {
		AttributeValue value = syntax.get("2.5.6.2#(c$EQ)".getBytes());
		assertEquals(new GuideValue("2.5.6.2#(c$EQ)"), value);
	}

	public void testGetObjectclass() throws Exception {
		AttributeValue value = syntax.get("country#(c$EQ)".getBytes());
		assertEquals(new GuideValue("country#(c$EQ)"), value);
	}

	public void testGetNoOid() throws Exception {
		AttributeValue value = syntax.get("(c$EQ)".getBytes());
		assertEquals(new GuideValue("(c$EQ)"), value);
	}

	public void testGetCriteriaSetAnd() throws Exception {
		final String searchGuideString = "((c$EQ)&(attr2$GE))";
		AttributeValue value = syntax.get(searchGuideString.getBytes());
		assertEquals(new GuideValue(searchGuideString), value);
	}

	public void testGetCriteriaSetOr() throws Exception {
		final String searchGuideString = "((c$EQ)|(attr2$GE))";
		AttributeValue value = syntax.get(searchGuideString.getBytes());
		assertEquals(new GuideValue(searchGuideString), value);
	}

	public void testGetCriteriaNot() throws Exception {
		final String searchGuideString = "!(c$EQ)";
		AttributeValue value = syntax.get(searchGuideString.getBytes());
		assertEquals(new GuideValue(searchGuideString), value);
	}

	public void testGetExceptionIncorrectOid() throws Exception {
		try {
			AttributeValue value = syntax.get("2.a.6.2#(c$EQ)".getBytes());
			fail();
		} catch (LdapInvalidAttributeSyntaxException e) {
			// ignore
		}

	}

	public void testGetExceptionMissingDash() throws Exception {
		try {
			AttributeValue value = syntax.get("2.4.6.2(c$EQ)".getBytes());
			fail();
		} catch (LdapInvalidAttributeSyntaxException e) {
			// ignore
		}
	}

	public void testGetExceptionNoMatchType() throws Exception {
		try {
			AttributeValue value = syntax.get("c".getBytes());
			fail();
		} catch (LdapInvalidAttributeSyntaxException e) {
			// ignore
		}
	}

	public void testGetExceptionIncorrectMatchType() throws Exception {
		try {
			AttributeValue value = syntax.get("c$NONEXISTENT".getBytes());
			fail();
		} catch (LdapInvalidAttributeSyntaxException e) {
			// ignore
		}
	}

	public void testGetExceptionWrongSetOperator() throws Exception {
		try {
			AttributeValue value = syntax.get("(c$EQ)%(c$GE)".getBytes());
			fail();
		} catch (LdapInvalidAttributeSyntaxException e) {
			// ignore
		}
	}

	public void testGetName() {
		assertEquals("1.3.6.1.4.1.1466.115.121.1.25", syntax.getName());

	}

}
