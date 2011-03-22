package org.jletty.db;

import javax.naming.directory.InvalidSearchFilterException;

import org.jletty.filterparser.JlettyFilterParser;
import org.jletty.ldapstackldapops.EqMatchFilter;
import org.jletty.ldapstackldapops.ExtensibleMatchFilter;
import org.jletty.ldapstackldapops.Filter;

import junit.framework.TestCase;

public class JlettyFilterParserTest extends TestCase {
	
	public void testWhiteSpaceBeforeEquals() throws Exception {
		Filter f = JlettyFilterParser.parse("(cn= Bab Jensen)");
		assertTrue(f instanceof EqMatchFilter);
		EqMatchFilter eqFilter = (EqMatchFilter) f;
		assertEquals(" Bab Jensen",eqFilter.getAssertionValueAsString());		
	}
	
	public void testWhiteSpaceBeforeRParen() throws Exception {
		Filter f = JlettyFilterParser.parse("(cn=Bab Jensen )");
		assertTrue(f instanceof EqMatchFilter);
		EqMatchFilter eqFilter = (EqMatchFilter) f;
		assertEquals("Bab Jensen ",eqFilter.getAssertionValueAsString());		
	}
	
	public void testWhiteSpaceBeforeRParenExtensible() throws Exception {
		Filter f = JlettyFilterParser.parse("(cn :=Bab Jensen )");
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter extFilter = (ExtensibleMatchFilter) f;
		assertEquals("Bab Jensen ",extFilter.getAssertionValueAsString());		
	}
	
	
	public void testMissingInitialParen() throws Exception {
		try {
			Filter f = JlettyFilterParser.parse("cn= Bab Jensen)");
			fail("should throw an InvalidSeachFilterException");
		} catch (InvalidSearchFilterException e) {
			// ignore
		}
		
	}
	public void testMissingLastParen() throws Exception {
		try {
			Filter f = JlettyFilterParser.parse("(cn= Bab Jensen");
			fail("should throw an InvalidSeachFilterException");
		} catch (InvalidSearchFilterException e) {
			// ignore
		}

	}

}
