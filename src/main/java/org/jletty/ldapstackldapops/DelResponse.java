/*
 * Created on 25-feb-2005
 *
 */
package org.jletty.ldapstackldapops;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.StandardToStringStyle;

/**
 * @author Ruben
 * 
 */
public class DelResponse extends LDAPResult implements LDAPResponse {

	/**
	 * @param result
	 */
	public DelResponse(LDAPResultCode result) {
		super(result);
	}

	/**
	 * @param result
	 * @param matchedDN
	 * @param errMsg
	 */
	public DelResponse(LDAPResultCode result, String matchedDN, String errMsg) {
		super(result, matchedDN, errMsg);
	}

	protected byte getTag() {
		return BerTags.APPLICATION_11;
	}

	public boolean equals(final Object other) {
		if (!(other instanceof DelResponse)) {
			return false;
		}
		DelResponse castOther = (DelResponse) other;
		return new EqualsBuilder().appendSuper(super.equals(castOther))
				.isEquals();
	}

	private transient int hashCode;

	public int hashCode() {
		if (this.hashCode == 0) {
			this.hashCode = new HashCodeBuilder().appendSuper(super.hashCode())
					.toHashCode();
		}
		return this.hashCode;
	}

	private static final StandardToStringStyle toStringStyle = new StandardToStringStyle();
	{
		// This is needed because the way easymock uses toString to store
		// arguments in an ordered treemap. if the identity hashcode is
		// different then ...
		toStringStyle.setUseIdentityHashCode(false);
	}

	// TODO remove dead code
	// public String toString() {
	// if (toString == null) {
	//
	// toString = new ToStringBuilder(this, toStringStyle).append(
	// "resultcode", getResult()).toString();
	// }
	// return toString;
	// }

}
