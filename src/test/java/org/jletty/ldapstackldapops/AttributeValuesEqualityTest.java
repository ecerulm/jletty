/*
 * $Id: AttributeValuesEqualityTest.java,v 1.1 2006/02/12 19:18:48 ecerulm Exp $
 * Created on May 24, 2004
 */
package org.jletty.ldapstackldapops;

import org.jletty.util.EqualsHashCodeTestCase;


/**
 * @author $Author: ecerulm $
 *  
 */
public class AttributeValuesEqualityTest extends EqualsHashCodeTestCase {


	/*
	 * (non-Javadoc)
	 * 
	 * @see junitx.extensions.EqualsHashCodeTestCase#createInstance()
	 */
	protected Object createInstance() throws Exception {
		AttributeValues values = new AttributeValues();
		values.add("top");
		values.add("organizationalPerson");
		return values;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junitx.extensions.EqualsHashCodeTestCase#createNotEqualInstance()
	 */
	protected Object createNotEqualInstance() throws Exception {
		AttributeValues values = new AttributeValues();
		values.add("top");
		values.add("organizationalPerson");
		values.add("inetOrgPerson");
		return values;
	}

}