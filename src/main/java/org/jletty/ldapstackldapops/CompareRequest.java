package org.jletty.ldapstackldapops;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jletty.util.StringUtil;
import org.jletty.util.Style;

public class CompareRequest implements LDAPRequest {

	private String baseDn;

	private String attrName;

	private byte[] attrValue;

	public CompareRequest(String dn, String attrName, byte[] attrValue) {
		this.baseDn = dn;
		this.attrName = attrName;
		this.attrValue = attrValue.clone();
	}

	public CompareRequest(String dn, String attrName, String attrValue) {
		this(dn, attrName, new byte[0]);
		this.attrValue = StringUtil.toUTF8(attrValue);
	}

	public byte[] getBytes() {
		throw new NotImplementedException(CompareRequest.class);
	}

	/**
	 * @return Returns the dn.
	 */
	public String getBaseDn() {
		return this.baseDn;
	}

	/**
	 * @return Returns the attrName.
	 */
	public String getAttrName() {
		return this.attrName;
	}

	public boolean equals(final Object other) {
		if (!(other instanceof CompareRequest)) {
			return false;
		}
		CompareRequest castOther = (CompareRequest) other;
		return new EqualsBuilder().append(this.baseDn, castOther.baseDn).append(
				this.attrName, castOther.attrName).append(this.attrValue,
				castOther.attrValue).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.baseDn).append(this.attrName).append(
				this.attrValue).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this, Style.getDefaultStyle()).append("dn",
				this.baseDn).append("attrName", this.attrName).append("attrValue",
				this.attrValue).toString();
	}

	/**
	 * @return Returns the attrValue.
	 */
	public byte[] getAttrValue() {
		return this.attrValue.clone();
	}

	public byte getTag() {
		return BerTags.APPLICATION_14;
	}
}