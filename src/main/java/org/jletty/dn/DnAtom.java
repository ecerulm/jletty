package org.jletty.dn;

import javax.naming.InvalidNameException;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jletty.schema.SchemaUtils;

public class DnAtom {

	private String attributeType;

	private String attributeValue;

	/**
	 * Cached toString(). No need to recalculate toString over and over
	 */
	private String toString = null;

	/**
	 * Cached asUnescapedString(). No need to recalculate asUnescapedString over
	 * and over
	 */
	private String asUnescapedString = null;

	public DnAtom(String attributeType, String attributeValue)
			throws InvalidNameException {
		this.attributeType = SchemaUtils.convertOidToAttrName(attributeType);
		if (attributeValue == null || "".equals(attributeValue)) {
			throw new InvalidNameException();
		}
		this.attributeValue = attributeValue;
	}

	public String toString() {
		if (this.toString != null) {
			return this.toString;
		}
		this.toString = this.attributeType + "="
				+ escapeSpecialCharacters(this.attributeValue);
		return this.toString;
	}

	// TODO remove this dead code
	// private String escape(String attributeValue2) {
	// String toReturn = attributeValue2.replaceAll("\\+", "\\\\+");
	// toReturn = toReturn.replaceAll("\\=", "\\\\=");
	// return toReturn;
	// }

	private String escapeSpecialCharacters(final String element) {
		// TODO move to dnatom
		String toReturn = element;
		// replace all slashes with escaped slashes
		toReturn = toReturn.replaceAll("\\\\([^+><;# =])", "\\\\\\\\$1");
		toReturn = toReturn.replaceAll("\\\\$", "\\\\\\\\");
		// replace all commas with escaped commas
		toReturn = toReturn.replaceAll(",", "\\\\,");
		// replace all quotes with escaped quotes
		toReturn = toReturn.replaceAll("\"", "\\\\\"");
		// replace all greater than '>' with escaped
		toReturn = toReturn.replaceAll(">", "\\\\>");
		// replace all less than '<' with escaped
		toReturn = toReturn.replaceAll("<", "\\\\<");
		// replace all semicolon ';' with escaped
		toReturn = toReturn.replaceAll(";", "\\\\;");
		// replace end space with escaped space
		toReturn = toReturn.replaceAll(" $", "\\\\ ");
		toReturn = escapeSpecialCharacterAlways(toReturn);

		return toReturn;
	}

	/**
	 * Escapes '+' and '=' symbols. These symbols are always escaped in a DnAtom
	 * 
	 * @param inputStr
	 * @return
	 */
	private String escapeSpecialCharacterAlways(String inputStr) {
		// replace '+' with escaped
		inputStr = inputStr.replaceAll("\\+", "\\\\+");
		// replace '=' with escaped
		inputStr = inputStr.replaceAll("\\=", "\\\\=");
		return inputStr;
	}

	public Attribute toAttribute() {
		try {
			return SchemaUtils.convertTaVToAttribute(this.attributeType,
					this.attributeValue).asNamingAttribute();
		} catch (NamingException e) {
			// TODO handle exception
			throw new RuntimeException(e);
		}
	}

	public String getAttributeType() {
		return this.attributeType;
	}

	public String getAttributeValue() {
		return this.attributeValue;
	}

	public boolean equals(final Object other) {
		if (!(other instanceof DnAtom)) {
			return false;
		}
		DnAtom castOther = (DnAtom) other;
		return new EqualsBuilder().append(this.attributeType,
				castOther.attributeType).append(this.attributeValue,
				castOther.attributeValue).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.attributeType).append(
				this.attributeValue).toHashCode();
	}

	public String asUnescapedString() {
		if (this.asUnescapedString != null) {
			return this.asUnescapedString;
		}
		// TODO are we required to escape this. I don't think so it will be
		// easier to use in the caller side if we don't escape it
		this.asUnescapedString = this.attributeType + "="
				+ escapeSpecialCharacterAlways(this.attributeValue);
		return this.asUnescapedString;
	}

}
