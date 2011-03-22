/*
 * $Id: AttributeTypeAndValuesListEqualityTest.java,v 1.1 2006/02/12 19:18:49 ecerulm Exp $
 * Created on May 24, 2004
 */
package org.jletty.ldapstackldapops;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jletty.ldapstackldapops.AttributeTypeAndValues;
import org.jletty.ldapstackldapops.AttributeTypeAndValuesList;
import org.jletty.ldapstackldapops.AttributeValues;

import junitx.extensions.EqualsHashCodeTestCase;

/**
 * @author $Author: ecerulm $
 *  
 */
public class AttributeTypeAndValuesListEqualityTest extends EqualsHashCodeTestCase {

	/**
	 * @param arg0
	 */
	public AttributeTypeAndValuesListEqualityTest(String arg0) {
		super(arg0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junitx.extensions.EqualsHashCodeTestCase#createInstance()
	 */
	protected Object createInstance() throws Exception {

		List l = new ArrayList();
		AttributeValues values;
		values = new AttributeValues();
		values.add("top");
		values.add("organizationalPerson");		
		l.add(new AttributeTypeAndValues("objectClass", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("Jorge Jetson");
		l.add(new AttributeTypeAndValues("cn", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("Jetson");
		l.add(new AttributeTypeAndValues("sn", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("System Administrator");
		l.add(new AttributeTypeAndValues("title", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("{MD5aEaBZG/SVbRccU/UYbzxCg==");
		l.add(new AttributeTypeAndValues("userPassword", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("000-000-1234");
		l.add(new AttributeTypeAndValues("telephoneNumber", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("000-111-1234");
		l.add(new AttributeTypeAndValues("facsimileTelephoneNumber", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("My address");
		l.add(new AttributeTypeAndValues("postalAddress", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("System admin");
		l.add(new AttributeTypeAndValues("description", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("My location");
		l.add(new AttributeTypeAndValues("l", values.getValuesAsByteArray()));

		return new AttributeTypeAndValuesList(l);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junitx.extensions.EqualsHashCodeTestCase#createNotEqualInstance()
	 */
	protected Object createNotEqualInstance() throws Exception {
		List l = new ArrayList();
		AttributeValues values;
		values = new AttributeValues();
		values.add("top");
		values.add("organizationalPerson");
		l.add(new AttributeTypeAndValues("objectClass", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("Jorge Jetson");
		l.add(new AttributeTypeAndValues("cn", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("Jetson");
		l.add(new AttributeTypeAndValues("sn", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("System Administrator");
		l.add(new AttributeTypeAndValues("title", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("{MD5aEaBZG/SVbRccU/UYbzxCg==");
		l.add(new AttributeTypeAndValues("userPassword", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("000-000-1234");
		l.add(new AttributeTypeAndValues("telephoneNumber", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("000-111-1234");
		l.add(new AttributeTypeAndValues("facsimileTelephoneNumber", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("My address");
		l.add(new AttributeTypeAndValues("postalAddress", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("System admin");
		l.add(new AttributeTypeAndValues("description", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("My location1");
		l.add(new AttributeTypeAndValues("l", values.getValuesAsByteArray()));

		return new AttributeTypeAndValuesList(l);
	}
	
	public void testEquals1() {
		List l = new ArrayList();
		AttributeValues values;
		values = new AttributeValues();
		values.add("person");
		l.add(new AttributeTypeAndValues("objectClass", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("Jorge Jetson1");
		values.add("Jorge Jetson2");
		l.add(new AttributeTypeAndValues("cn", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("Jetson");
		l.add(new AttributeTypeAndValues("sn", values.getValuesAsByteArray()));

		AttributeTypeAndValuesList attributeTypeAndValuesList1 = new AttributeTypeAndValuesList(l);
		
		l = new LinkedList();
		values = new AttributeValues();
		values.add("Jorge Jetson2");
		values.add("Jorge Jetson1");
		l.add(new AttributeTypeAndValues("cn", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("person");
		l.add(new AttributeTypeAndValues("objectClass", values.getValuesAsByteArray()));
		values = new AttributeValues();
		values.add("Jetson");
		l.add(new AttributeTypeAndValues("sn", values.getValuesAsByteArray()));
		
		AttributeTypeAndValuesList attributeTypeAndValuesList2 = new AttributeTypeAndValuesList(l);
		
		assertTrue(attributeTypeAndValuesList1.equals(attributeTypeAndValuesList1));
		assertTrue(attributeTypeAndValuesList2.equals(attributeTypeAndValuesList2));
		assertTrue(attributeTypeAndValuesList1.equals(attributeTypeAndValuesList2));
		assertTrue(attributeTypeAndValuesList2.equals(attributeTypeAndValuesList1));
		
		//objectclass cn sn
		
		
	}
}