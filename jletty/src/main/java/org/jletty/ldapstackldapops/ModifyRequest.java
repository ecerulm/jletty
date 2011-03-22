/*
 * Created on 23-sep-2004
 *
 */
package org.jletty.ldapstackldapops;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Ruben
 * 
 */
public class ModifyRequest implements LDAPRequest {

	/**
	 * @return Returns the dn.
	 */
	public String getDn() {
		return this.dn;
	}

	private String dn;

	private Modification[] modificationList;

	/**
	 * @param string
	 * @param modificationList
	 */
	public ModifyRequest(String dn, Modification[] modificationList) {
		this.dn = dn;
		this.modificationList = modificationList.clone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rubenlaguna.ldapstack.ldapops.LDAPOperation#getBytes()
	 */
	public byte[] getBytes() {
		throw new NotImplementedException(ModifyRequest.class);
	}

	public boolean equals(final Object other) {
		if (!(other instanceof ModifyRequest)) {
			return false;
		}
		ModifyRequest castOther = (ModifyRequest) other;
		return new EqualsBuilder().append(this.dn, castOther.dn).append(
				this.modificationList, castOther.modificationList).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.dn).append(this.modificationList)
				.toHashCode();
	}

	/**
	 * @return Returns the modificationList.
	 */
	public Modification[] getModificationList() {
		return this.modificationList.clone();
	}

	public byte getTag() {
		return BerTags.APPLICATION_6;
	}
}