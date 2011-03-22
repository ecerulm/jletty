package org.jletty.schema;

import java.util.Iterator;

import javax.naming.directory.InvalidAttributeValueException;

import org.jletty.jndiprovider.LdapAttribute;
import org.jletty.jndiprovider.LdapAttributes;
import org.jletty.ldapstackldapops.AttributeTypeAndValues;
import org.jletty.ldapstackldapops.AttributeTypeAndValuesList;
import org.jletty.ldapstackldapops.LdapInvalidAttributeSyntaxException;
import org.jletty.ldapstackldapops.LdapUndefinedAttributeType;
import org.jletty.util.StringUtil;

public final class SchemaUtils {

	private SchemaUtils() {

	}

	public static LdapAttributes convertAttrTypeAndValsToAttributes(
			AttributeTypeAndValuesList attrList)
			throws LdapUndefinedAttributeType,
			LdapInvalidAttributeSyntaxException {
		LdapAttributes attrs = new LdapAttributes();
		Iterator it = attrList.iterator();
		while (it.hasNext()) {
			AttributeTypeAndValues attrTaV = (AttributeTypeAndValues) it.next();
			LdapAttribute attr = convertTaVToAttribute(attrTaV);
			attrs.put(attr);
		}
		return attrs;
	}

	public static LdapAttribute convertTaVToAttribute(
			AttributeTypeAndValues attrTaV) throws LdapUndefinedAttributeType,
			LdapInvalidAttributeSyntaxException {
		final String attrDesc = attrTaV.getAttributeDescription();
		byte[][] values = attrTaV.getValues();

		return convertTaVToAttribute(attrDesc, values);
	}

	// TODO change return type to ldapattribute
	public static LdapAttribute convertTaVToAttribute(String attributeType,
			String attributeValue) throws LdapInvalidAttributeSyntaxException,
			InvalidAttributeValueException, LdapUndefinedAttributeType {
		byte[] bs = StringUtil.toUTF8(attributeValue);
		return convertTaVToAttribute(attributeType, new byte[][] { bs });
	}

	private static LdapAttribute convertTaVToAttribute(final String attrDesc,
			byte[][] values) throws LdapUndefinedAttributeType,
			LdapInvalidAttributeSyntaxException {
		LdapAttribute attr = new LdapAttribute(attrDesc);
		final AttributeType attributeType = Schema.getInstance()
				.getAttributeType(attrDesc);
		if (attributeType == null) {
			throw new LdapUndefinedAttributeType(attrDesc);
		}
		Syntax syntax = Syntaxes.getSyntax(attributeType.getSyntax());

		if (values.length < 1) {
			throw new LdapInvalidAttributeSyntaxException("attribute "
					+ attrDesc + " cannot be empty");
		}

		for (int i = 0; i < values.length; i++) {
			try {
				byte[] value = values[i];
				AttributeValue matchable = syntax.get(value);
				attr.add(matchable);
			} catch (LdapInvalidAttributeSyntaxException e) {
				throw new LdapInvalidAttributeSyntaxException("value #" + i
						+ " invalid per syntax");
			}
		}
		return attr;
	}

	public static String convertOidToAttrName(String attributeName) {
		AttributeType attributeType = Schema.getInstance().getAttributeType(
				attributeName);
		if (attributeType != null) { // change oid to real attribute name
			attributeName = attributeType.getName();
		}
		return attributeName;
	}

}
