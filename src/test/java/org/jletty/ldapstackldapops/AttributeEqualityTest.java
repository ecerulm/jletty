/*
 * $Id: AttributeEqualityTest.java,v 1.1 2006/02/12 19:18:49 ecerulm Exp $
 * Created on May 24, 2004
 */
package org.jletty.ldapstackldapops;

import org.jletty.util.EqualsHashCodeTestCase;


/**
 * @author $Author: ecerulm $
 *  
 */
public class AttributeEqualityTest extends EqualsHashCodeTestCase {


	protected Object createInstance() throws Exception {
		AttributeValues values = new AttributeValues();
		values.add("top");
		values.add("organizationalPerson");
		return new AttributeTypeAndValues("objectClass", values.getValuesAsByteArray());
	}

	protected Object createNotEqualInstance() throws Exception {
		AttributeValues values = new AttributeValues();
		values.add("top");
		values.add("organizationalPerson");
		values.add("inetOrgPerson");
		return new AttributeTypeAndValues("objectClass", values.getValuesAsByteArray());
	}
}