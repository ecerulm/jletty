/*
 * $Id: AttributeTypeAndValues.java,v 1.2 2006/02/27 18:23:01 ecerulm Exp $
 */
package org.jletty.ldapstackldapops;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jletty.util.CloneUtils;
import org.jletty.util.Style;

/**
 * SEQUENCE { type AttributeDescription, vals SET OF AttributeValue }
 * 
 * @author $Author: ecerulm $
 * 
 */
public final class AttributeTypeAndValues {

	/**
	 * <code>type</code> AttributeDescription. AttributeDescription ::=
	 * LDAPString
	 * 
	 */
	private String attrDesc;

	private byte[][] vals;

	/**
	 * @param desc
	 *            is the AttributeDescription of this Attribute
	 * @param vals
	 *            is the set of AttributeValue
	 */
	public AttributeTypeAndValues(String desc, byte[][] theValues) {

		this.attrDesc = desc;
		if (theValues == null) {
			this.vals = new byte[0][];
		} else {
			this.vals = CloneUtils.deepCloneByteArray(theValues);
		}
	}

	public String getAttributeDescription() {
		return this.attrDesc;
	}

	public byte[][] getValues() {
		return CloneUtils.deepCloneByteArray(this.vals);
	}

	public String toString() {
		return new ToStringBuilder(this, Style.getDefaultStyle()).append(
				"description", this.getAttributeDescription()).append("values",
				this.getValues()).toString();
	}

	public int hashCode() {
		Collection vals1 = valuesToHashSetOfByteBuffers(this.vals);
		return new HashCodeBuilder(17, 37).append(this.attrDesc).append(vals1)
				.toHashCode();
	}

	public boolean equals(Object object) {
		if (!(object instanceof AttributeTypeAndValues)) {
			return false;
		}
		AttributeTypeAndValues rhs = (AttributeTypeAndValues) object;
		Collection vals1 = valuesToHashSetOfByteBuffers(this.vals);

		Collection vals2 = valuesToHashSetOfByteBuffers(rhs.vals);
		final boolean equals = new EqualsBuilder().append(this.attrDesc,
				rhs.attrDesc).append(vals1, vals2).isEquals();
		return equals;
	}

	private Collection valuesToHashSetOfByteBuffers(byte[][] vals) {
		Collection collection = new HashSet();
		for (int i = 0; i < vals.length; i++) {
			collection.add(ByteBuffer.wrap(vals[i]));
		}
		return collection;
	}
}