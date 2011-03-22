/*
 * $Id: AttributeValues.java,v 1.3 2006/02/27 18:24:45 ecerulm Exp $ Created on
 * May 14, 2004
 *  
 */
package org.jletty.ldapstackldapops;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jletty.util.StringUtil;
import org.jletty.util.Style;

/**
 * @author $Author: ecerulm $
 * 
 * 
 */
public class AttributeValues {

	// TODO replace OctetString class with ByteArray
	static private final class OctetString {

		private byte[] value;

		OctetString(byte[] value) {
			this.value = value;
		}

		public byte[] getBytes() {
			return this.value.clone();
		}

		public boolean equals(Object object) {
			if (!(object instanceof OctetString)) {
				return false;
			}
			OctetString rhs = (OctetString) object;
			return new EqualsBuilder().append(this.value, rhs.value).isEquals();
		}

		public String toString() {
			return new ToStringBuilder(this, Style.getDefaultStyle()).append(
					this.value).toString();
		}

		public int hashCode() {
			return new HashCodeBuilder(17, 37).append(this.value).toHashCode();
		}
	}

	private List values = new ArrayList();

	private List byteValues = new ArrayList();

	public AttributeValues() {
	}

	/**
	 * @param value
	 * @return
	 */
	public AttributeValues add(byte[] value) {
		OctetString wrapper = new OctetString(value);
		boolean alreadyExists = !this.values.add(wrapper);
		if (alreadyExists) {
			throw new IllegalArgumentException("the value " + value
					+ " already in this set");
		}

		this.byteValues.add(value.clone());
		return this;
	}

	public AttributeValues add(String value) {
		add(StringUtil.toUTF8(value));
		return this;

	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this, Style.getDefaultStyle())
				.append(this.values).toString();
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof AttributeValues)) {
			return false;
		}
		AttributeValues rhs = (AttributeValues) object;
		return new EqualsBuilder().append(this.values, rhs.values).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.values).toHashCode();
	}

	/**
	 * 
	 */
	public byte[][] getValuesAsByteArray() {
		byte[][] toReturn = (byte[][]) this.byteValues.toArray(new byte[this.values
				.size()][]);
		return toReturn;
	}
}