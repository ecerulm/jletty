/*
 * Created on 22-sep-2004
 *
 */
package org.jletty.ldapstackldapops;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author Ruben
 * 
 */
public class DeleteRequest implements LDAPRequest {

	private String dn;

	public DeleteRequest(String dn) {
		this.dn = dn;
	}

	public byte[] getBytes() {
		throw new NotImplementedException(DeleteRequest.class);
	}

	public boolean equals(final Object other) {
		if (!(other instanceof DeleteRequest)) {
			return false;
		}
		DeleteRequest castOther = (DeleteRequest) other;
		return new EqualsBuilder().append(this.dn, castOther.dn).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.dn).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(
				"dn", this.dn).toString();
	}

	/**
	 * @return Returns the dn.
	 */
	public String getDn() {
		return this.dn;
	}

	public byte getTag() {
		return BerTags.APPLICATION_10;
	}
}