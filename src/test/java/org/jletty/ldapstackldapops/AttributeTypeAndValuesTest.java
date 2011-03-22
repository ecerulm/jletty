/*
 * Created on 27-sep-2004
 *
 */
package org.jletty.ldapstackldapops;

import org.jletty.util.EqualsHashCodeTestCase;


/**
 * @author Ruben
 *  
 */
public class AttributeTypeAndValuesTest extends EqualsHashCodeTestCase {


	protected Object createInstance() throws Exception {

		return new AttributeTypeAndValues("objectclass", new AttributeValues()
				.add("top").add("organizationalPerson").getValuesAsByteArray());
	}

	protected Object createNotEqualInstance() throws Exception {

		return new AttributeTypeAndValues("cn", new AttributeValues()
				.add("Jorge Jetson").getValuesAsByteArray());
	}

}