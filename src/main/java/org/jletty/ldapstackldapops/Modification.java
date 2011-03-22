/*
 * Created on 23-sep-2004
 *
 */
package org.jletty.ldapstackldapops;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jletty.util.CloneUtils;

/**
 * @author Ruben
 * 
 */
public final class Modification {

	private ModificationType type;

	private String attrName;

	// private AttributeValues values;
	private byte[][] values;

	/**
	 * @param replace
	 * @param string
	 * @param bytes
	 */
	public Modification(ModificationType type, String attrName, byte[][] values) {

		this.type = type;
		this.attrName = attrName;
		this.values = CloneUtils.deepCloneByteArray(values);
		if (values == null) {
			throw new NullArgumentException("values");
		}
	}

	/**
	 * @return Returns the type.
	 */
	public ModificationType getType() {
		return this.type;
	}

	public boolean equals(final Object other) {
		if (!(other instanceof Modification)) {
			return false;
		}
		Modification castOther = (Modification) other;
		return new EqualsBuilder().append(this.type, castOther.type).append(
				this.attrName, castOther.attrName).append(this.values, castOther.values)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.type).append(this.attrName).append(
				this.values).toHashCode();
	}

	/**
	 * @return Returns the attrName.
	 */
	public String getAttrName() {
		return this.attrName;
	}

	/**
	 * @return Returns the values.
	 */
	public byte[][] getValues() {
		return CloneUtils.deepCloneByteArray(this.values);
	}
}