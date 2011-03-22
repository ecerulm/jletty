/*
 * Created on 27-sep-2004
 *
 */
package org.jletty.ldapstackldapops;

import java.util.LinkedList;
import java.util.List;

import org.jletty.util.EqualsHashCodeTestCase;


/**
 * @author Ruben
 *  
 */
public class AddRequestTest extends EqualsHashCodeTestCase {


	protected Object createInstance() throws Exception {
		List attrs = new LinkedList();
		attrs.add(new AttributeTypeAndValues("objectclass",
				new AttributeValues().add("top").add("organizationalPerson").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("cn", new AttributeValues()
				.add("Jorge Jetson").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("sn", new AttributeValues()
				.add("Jetson").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("title", new AttributeValues()
				.add("System Administrator").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("userPassword",
				new AttributeValues().add("{MD5aEaBZG/SVbRccU/UYbzxCg==").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("telephoneNumber",
				new AttributeValues().add("000-000-1234").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("facsimileTelephoneNumber",
				new AttributeValues().add("000-111-1234").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("postalAddress",
				new AttributeValues().add("My address").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("description",
				new AttributeValues().add("System Admin").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("l", new AttributeValues()
				.add("My location").getValuesAsByteArray()));
		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);

		AddRequest addRequest = new AddRequest(
				"cn=Jorge Jetson, ou=People, o=linuxpowered, c=us", attrList);
		return addRequest;
	}

	protected Object createNotEqualInstance() throws Exception {

		List attrs = new LinkedList();
		attrs.add(new AttributeTypeAndValues("objectClass",
				new AttributeValues().add("top").add("organizationalPerson").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("cn", new AttributeValues()
				.add("Jorge Jetson").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("sn", new AttributeValues()
				.add("Jetson").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("userPassword",
				new AttributeValues().add("{MD5aEaBZG/SVbRccU/UYbzxCg==").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("telephoneNumber",
				new AttributeValues().add("000-000-1234").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("facsimileTelephoneNumber",
				new AttributeValues().add("000-111-1234").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("postalAddress",
				new AttributeValues().add("My address").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("description",
				new AttributeValues().add("System Admin").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("l", new AttributeValues()
				.add("My location").getValuesAsByteArray()));
		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);

		AddRequest addRequest = new AddRequest(
				"cn=Jorge Jetson, ou=People, o=linuxpowered, c=us", attrList);
		return addRequest;
	}

}