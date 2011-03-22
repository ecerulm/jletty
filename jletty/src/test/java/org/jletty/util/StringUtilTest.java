package org.jletty.util;

import org.jletty.util.StringUtil;

import junit.framework.TestCase;

public class StringUtilTest extends TestCase {

	public void testUnescapeLDAPDN() {

		// a space or "#" character occurring at the beginning of the
		// string

		assertEquals("cn=\\ New Entry", StringUtil
				.unescapeLDAPDN("cn=\\\\20New Entry"));
		assertEquals("cn=\\#New Entry", StringUtil
				.unescapeLDAPDN("cn=\\\\23New Entry"));

		// a space character occurring at the end of the string
		assertEquals("cn=New Entry\\ ", StringUtil
				.unescapeLDAPDN("cn=New Entry\\\\20"));

		// one of the characters ",", "+", """, "\", "<", ">" or ";"
		assertEquals("cn=New\\,Entry,c=us", StringUtil
				.unescapeLDAPDN("cn=New\\2cEntry,c=us"));
		assertEquals("cn=New\\+Entry,c=us", StringUtil
				.unescapeLDAPDN("cn=New\\2bEntry,c=us"));
		assertEquals("cn=New\\\"Entry,c=us", StringUtil
				.unescapeLDAPDN("cn=New\\22Entry,c=us"));
		assertEquals("cn=New\\\\Entry,c=us", StringUtil
				.unescapeLDAPDN("cn=New\\5cEntry,c=us"));
		assertEquals("cn=New\\<Entry,c=us", StringUtil
				.unescapeLDAPDN("cn=New\\3cEntry,c=us"));
		assertEquals("cn=New\\>Entry,c=us", StringUtil
				.unescapeLDAPDN("cn=New\\3eEntry,c=us"));
		assertEquals("cn=New\\;Entry,c=us", StringUtil
				.unescapeLDAPDN("cn=New\\3bEntry,c=us"));

	}
	
	public void testUnescapeAnyChar() {
		assertEquals("cn=New Entry,c=us", StringUtil
				.unescapeLDAPDN("cn=New E\\6etry,c=us"));
		
	}

	public void testUnescapeSpaceInTheMiddle1() {
		// a space or # in the middle of the string
		assertEquals("cn=New Entry", StringUtil
				.unescapeLDAPDN("cn=New\\20Entry"));

	}
	
	public void testUnescpeSpaceInTheMiddle2() {
		assertEquals("cn=New  Entry", StringUtil
				.unescapeLDAPDN("cn=New\\20\\20Entry"));
		assertEquals("cn=New=  Entry", StringUtil
				.unescapeLDAPDN("cn=New=\\20\\20Entry"));
		
	}

	public void testUnscapeDashInTheMiddle() {
		assertEquals("cn=New#Entry", StringUtil
				.unescapeLDAPDN("cn=New\\23Entry"));
		assertEquals("cn=New Entry#", StringUtil
				.unescapeLDAPDN("cn=New Entry\\23"));
		assertEquals("cn=New Entry#\\ ", StringUtil
				.unescapeLDAPDN("cn=New Entry\\23\\ "));
	}

}
