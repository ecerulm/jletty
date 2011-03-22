package org.jletty.jndiprovider;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jletty.ldapstackldapops.LdapInvalidAttributeSyntaxException;
import org.jletty.schema.AttributeValue;
import org.jletty.schema.Schema;
import org.jletty.schema.Syntax;
import org.jletty.schema.Syntaxes;

//TODO remove dependencies with javax.naming.directory like Attribute
public class LdapAttribute {

	private String attrName;

	private Collection attrValues = CollectionUtils.typedCollection(
			new ArrayList(), AttributeValue.class);

	public LdapAttribute(String attrName) {
		if (attrName.matches("^\\d.*")) {
			// its an oid
			this.attrName = Schema.getInstance()
					.getAttributeTypeByOid(attrName).getName();
		} else {
			this.attrName = attrName;
		}
	}

	public LdapAttribute(Attribute namingAttribute) {
		try {
			this.attrName = namingAttribute.getID();
			NamingEnumeration all = namingAttribute.getAll();
			while (all.hasMoreElements()) {
				AttributeValue value = (AttributeValue) all.nextElement();
				this.attrValues.add(value);
			}
		} catch (NamingException e) {
			// TODO handle exception
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets an unmodificable Collection with all the LdapAttribute values
	 * 
	 * @return an unmodificable Collection with all the LdapAttribute values
	 */
	public Collection getAll() {
		return Collections.unmodifiableCollection(this.attrValues);
	}

	public String getID() {
		return this.attrName;
	}

	public void add(AttributeValue inputVal) {
		// TODO testcase. adding the same value should throw an exception?
		this.attrValues.add(inputVal);
	}

	public boolean remove(AttributeValue val) {
		Iterator iterator = this.attrValues.iterator();
		while (iterator.hasNext()) {
			AttributeValue attrValue = (AttributeValue) iterator.next();
			if (val.equals(attrValue)) {
				iterator.remove();
				return true;
			}
		}
		return false;
	}

	public int size() {
		return this.attrValues.size();
	}

	public Attribute asNamingAttribute() {

		BasicAttribute toReturn = new BasicAttribute(this.attrName);
		Iterator iterator = this.attrValues.iterator();
		while (iterator.hasNext()) {
			AttributeValue attrValue = (AttributeValue) iterator.next();
			toReturn.add(attrValue);
		}

		return toReturn;
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("attrName", this.attrName).append("attrValues", this.attrValues)
				.toString();
	}

	public boolean contains(Object attrValue) {
		// TODO test this method
		try {
			if (attrValue instanceof String) {
				String syntaxName = Schema.getInstance().getAttributeType(
						this.attrName).getSyntax();
				Syntax syntax = Syntaxes.getSyntax(syntaxName);
				attrValue = syntax.get(((String) attrValue).getBytes("UTF-8"));
			}
			boolean toReturn = this.attrValues.contains(attrValue);
			return toReturn;
		} catch (LdapInvalidAttributeSyntaxException e) {
			throw new RuntimeException();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException();
		}
	}

	public String getIDOID() {
		return Schema.getInstance().getAttributeType(this.attrName).getNumericoid();
	}

	public LdapAttribute copy() {
		LdapAttribute toReturn = new LdapAttribute(this.attrName);
		for (Iterator iter = this.attrValues.iterator(); iter.hasNext();) {
			AttributeValue element = (AttributeValue) iter.next();
			toReturn.add(element);
		}
		return toReturn;
	}

}
