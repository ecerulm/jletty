package org.jletty.jndiprovider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

//TODO remove this class, i think it's not used 
public class LdapAttributes {

	// TODO change to Map
	private Collection attributes = CollectionUtils.typedCollection(
			new HashSet(), LdapAttribute.class);;

	// TODO remove this constructor
	public LdapAttributes(Attributes namingAttributes) {
		NamingEnumeration all = namingAttributes.getAll();
		while (all.hasMoreElements()) {
			Attribute element = (Attribute) all.nextElement();
			LdapAttribute attribute = new LdapAttribute(element);
			this.attributes.add(attribute);
		}
	}

	public LdapAttributes() {
	}

	public LdapAttributes(Collection set) {
		this.attributes = CollectionUtils.typedCollection(set, LdapAttribute.class);
	}

	public Collection asCollection() {
		return Collections.unmodifiableCollection(this.attributes);
	}

	public LdapAttribute get(String attrName) {
		if (null == attrName) {
			return null;
		}
		Iterator iterator = this.attributes.iterator();
		while (iterator.hasNext()) {
			LdapAttribute attr = (LdapAttribute) iterator.next();
			if (attrName.equalsIgnoreCase(attr.getID())) {
				return attr;
			}
		}
		return null;
	}

	public void put(LdapAttribute currentAttr) {
		Iterator iterator = this.attributes.iterator();
		String id = currentAttr.getID();
		while (iterator.hasNext()) {
			LdapAttribute attr = (LdapAttribute) iterator.next();
			if (id.equals(attr.getID())) {
				iterator.remove();
				break;
			}
		}
		this.attributes.add(currentAttr);
	}

	public boolean remove(String attrName) {
		if (null == attrName) {
			throw new IllegalArgumentException(
					"The attribute name cannot be null");
		}
		Iterator iterator = this.attributes.iterator();
		while (iterator.hasNext()) {
			LdapAttribute attr = (LdapAttribute) iterator.next();
			if (attrName.equals(attr.getID())) {
				iterator.remove();
				return true;
			}
		}
		return false;
	}

	public Attributes getAllAsNamingAttributes() {
		BasicAttributes toReturn = new BasicAttributes(true);
		Iterator iterator = this.attributes.iterator();
		while (iterator.hasNext()) {
			LdapAttribute attr = (LdapAttribute) iterator.next();
			toReturn.put(attr.asNamingAttribute());
		}
		return toReturn;
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("attributes", this.attributes).toString();
	}

	public Collection getIDs() {
		Collection toReturn = new ArrayList();
		for (Iterator iter = this.attributes.iterator(); iter.hasNext();) {
			LdapAttribute attr = (LdapAttribute) iter.next();
			toReturn.add(attr.getID());
		}
		return toReturn;
	}

	public LdapAttributes copy() {
		final Collection outputCollection = new HashSet();
		final Transformer transformer = new Transformer() {

			public Object transform(Object input) {
				return ((LdapAttribute) input).copy();
			}

		};
		CollectionUtils.collect(this.attributes, transformer, outputCollection);
		return new LdapAttributes(outputCollection);
	}
}
