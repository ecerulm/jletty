/*
 * $Id: AttributeTypeAndValuesList.java,v 1.3 2006/02/27 18:24:45 ecerulm Exp $ Created on May 22, 2004
 */
package org.jletty.ldapstackldapops;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jletty.schema.OctetStringable;
import org.jletty.util.Style;

/**
 * @author $Author: ecerulm $
 * 
 */
public class AttributeTypeAndValuesList {

	/**
	 * <code>list</code> contains the {@link AttributeTypeAndValues}s
	 */
	private Collection list;

	public AttributeTypeAndValuesList(Collection attrs) {
		this.list = new HashSet(attrs);
	}

	public AttributeTypeAndValuesList() {
		this.list = new HashSet();
	}

	public Iterator iterator() {
		return this.list.iterator();
	}

	public boolean add(AttributeTypeAndValues o) {
		if (!(o instanceof AttributeTypeAndValues)) {
			throw new IllegalArgumentException(
					"Only AttributeTypeAndValues can be added. offending paramenter "
							+ o);
		}
		return this.list.add(o);
	}

	public boolean addAll(Attributes attrs) {
		try {
			List tmpList = new ArrayList();
			NamingEnumeration all = attrs.getAll();
			while (all.hasMoreElements()) {
				Attribute attr = (Attribute) all.nextElement();
				List values = new ArrayList();
				NamingEnumeration allValues = attr.getAll();
				while (allValues.hasMoreElements()) {
					Object element = allValues.nextElement();
					if (element instanceof OctetStringable) {
						values.add(((OctetStringable) element).getContents());
					} else {
						throw new IllegalArgumentException(
								"attrs must contain Attribute(s) with OctetStringable values");
					}
				}
				byte[][] byteValues = (byte[][]) values.toArray(new byte[values
						.size()][]);
				tmpList
						.add(new AttributeTypeAndValues(attr.getID(),
								byteValues));
			}
			return this.list.addAll(tmpList);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}

	}

	public AttributeTypeAndValues get(String attrName) {
		for (Iterator i = this.list.iterator(); i.hasNext();) {
			AttributeTypeAndValues attr = (AttributeTypeAndValues) i.next();
			if (attr.getAttributeDescription().equalsIgnoreCase(attrName)) {
				return attr;
			}
		}
		return null;
	}

	public String toString() {
		return new ToStringBuilder(this, Style.getDefaultStyle()).append(
				"List", this.list).toString();
	}

	public boolean equals(Object object) {
		if (!(object instanceof AttributeTypeAndValuesList)) {
			return false;
		}
		AttributeTypeAndValuesList rhs = (AttributeTypeAndValuesList) object;
		final boolean toReturn = new EqualsBuilder()
				.append(this.list, rhs.list).isEquals();
		return toReturn;
	}

	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.list).toHashCode();
	}
}