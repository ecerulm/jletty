/*
 * $Header: /usr/local/cvsroot/jletty-new/src/org/jletty/ldapstackldapops/AddRequest.java,v 1.2 2006/02/27 18:23:01 ecerulm Exp $
 * Created on 08-may-2004
 *
 */
package org.jletty.ldapstackldapops;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jletty.util.StringUtil;
import org.jletty.util.Style;

/**
 * @author $Author: ecerulm $
 */
public final class AddRequest implements LDAPRequest {

	private String dn = null;

	private AttributeTypeAndValuesList attrList = null;

	/**
	 * @param dn
	 * @param attrList
	 */
	public AddRequest(String dn, AttributeTypeAndValuesList attrList) {
		this.dn = StringUtil.unescapeLDAPDN(dn);
		this.attrList = attrList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.jletty.ldapstackldapops.LDAPOperation#getBytes()
	 */
	public byte[] getBytes() {

		throw new RuntimeException("Method getBytes not implemented yet");

	}

	/**
	 * @return the entryDn
	 */
	public String getEntryDn() {
		return this.dn;
	}

	/**
	 * (add JAVADOC)
	 * 
	 * @return
	 */
	public AttributeTypeAndValuesList getAttrList() {

		return this.attrList;

	}

	public String toString() {
		return new ToStringBuilder(this, Style.getDefaultStyle()).append("dn",
				this.dn).append("attrList", this.attrList).toString();
	}

	public boolean equals(final Object other) {
		if (!(other instanceof AddRequest)) {
			return false;
		}
		AddRequest castOther = (AddRequest) other;
		final boolean equals = new EqualsBuilder().append(this.dn, castOther.dn)
				.append(this.attrList, castOther.attrList).isEquals();
		return equals;
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.dn).append(this.attrList).toHashCode();
	}

	public byte getTag() {
		return BerTags.APPLICATION_8;
	}

}