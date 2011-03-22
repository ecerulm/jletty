/*
 * Created on 25-feb-2005
 *
 */
package org.jletty.ldapstackldapops;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Ruben
 * 
 */
public class ModifyResponse extends LDAPResult implements LDAPResponse {

	private transient int hashCode;

	/**
	 * @param result
	 */
	public ModifyResponse(LDAPResultCode result) {
		super(result);
	}

	/**
	 * @param result
	 * @param matchedDN
	 * @param errMsg
	 */
	public ModifyResponse(LDAPResultCode result, String matchedDN, String errMsg) {
		super(result, matchedDN, errMsg);
	}

	protected byte getTag() {
		return BerTags.APPLICATION_7;
	}

	public boolean equals(final Object other) {
		if (!(other instanceof ModifyResponse)) {
			return false;
		}
		return new EqualsBuilder().appendSuper(super.equals(other)).isEquals();
	}

	public int hashCode() {
		if (this.hashCode == 0) {
			this.hashCode = new HashCodeBuilder().appendSuper(super.hashCode())
					.toHashCode();
		}
		return this.hashCode;
	}
}
