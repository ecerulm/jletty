/*
 * Created on 25-sep-2004
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
public class ModifyRDNRequest implements LDAPRequest {

	private String dn;

	private String newrdn;

	private boolean deleteOldRdn;

	private String newSuperior;

	public ModifyRDNRequest(String dn, String newrdn, boolean deleteOldRdn,
			String newSuperior) {
		this.dn = dn;
		this.newrdn = newrdn;
		this.deleteOldRdn = deleteOldRdn;
		this.newSuperior = newSuperior;

	}

	public ModifyRDNRequest(String dn, String newrdn, boolean deleteOldRdn) {
		this(dn, newrdn, deleteOldRdn, null);
	}

	public boolean equals(final Object other) {
		if (!(other instanceof ModifyRDNRequest)) {
			return false;
		}
		ModifyRDNRequest castOther = (ModifyRDNRequest) other;
		return new EqualsBuilder().append(this.dn, castOther.dn).append(this.newrdn,
				castOther.newrdn).append(this.deleteOldRdn, castOther.deleteOldRdn)
				.append(this.newSuperior, castOther.newSuperior).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.dn).append(this.newrdn).append(
				this.deleteOldRdn).append(this.newSuperior).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(
				"dn", this.dn).append("newrdn", this.newrdn).append("deleteOldRdn",
				this.deleteOldRdn).append("newSuperior", this.newSuperior).toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rubenlaguna.ldapstack.ldapops.LDAPOperation#getBytes()
	 */
	public byte[] getBytes() {
		throw new NotImplementedException(ModifyRDNRequest.class);
	}

	public byte getTag() {
		return BerTags.APPLICATION_12;
	}

}