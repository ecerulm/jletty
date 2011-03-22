/*
 * Created on 27-sep-2004
 *
 */
package org.jletty.ldapstackldapops;

import org.jletty.ldapstackldapops.AttributeTypeAndValues;
import org.jletty.ldapstackldapops.AttributeValues;

import junitx.extensions.EqualsHashCodeTestCase;

/**
 * @author Ruben
 *  
 */
public class AttributeTypeAndValuesTest extends EqualsHashCodeTestCase {

	public AttributeTypeAndValuesTest(String name) {
		super(name);
	}

	protected Object createInstance() throws Exception {

		return new AttributeTypeAndValues("objectclass", new AttributeValues()
				.add("top").add("organizationalPerson").getValuesAsByteArray());
	}

	protected Object createNotEqualInstance() throws Exception {

		return new AttributeTypeAndValues("cn", new AttributeValues()
				.add("Jorge Jetson").getValuesAsByteArray());
	}

}