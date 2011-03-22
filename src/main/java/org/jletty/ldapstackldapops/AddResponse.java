/*
 * Created on 06-nov-2004
 *
 */
package org.jletty.ldapstackldapops;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Ruben
 * 
 */
public class AddResponse extends LDAPResult implements LDAPResponse {

	/**
	 * @param result2
	 */
	public AddResponse(LDAPResultCode result2) {
		super(result2);

	}

	/**
	 * @param result
	 * @param matchedDN
	 * @param errMsg
	 */
	public AddResponse(LDAPResultCode result, String matchedDN, String errMsg) {
		super(result, matchedDN, errMsg);
	}

	protected byte getTag() {
		return BerTags.APPLICATION_9;
	}

	public boolean equals(final Object other) {
		if (!(other instanceof AddResponse)) {
			return false;
		}
		AddResponse castOther = (AddResponse) other;
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

}
