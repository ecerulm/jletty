package org.jletty.ldapstackldapops;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jletty.util.Style;

public final class AbandonRequest implements LDAPRequest {

	private int msgid;

	public AbandonRequest(int msgid) {
		this.msgid = msgid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rubenlaguna.ldapstack.ldapops.LDAPOperation#getBytes()
	 */
	public byte[] getBytes() {
		throw new NotImplementedException(AbandonRequest.class);
	}

	public boolean equals(final Object other) {
		if (!(other instanceof AbandonRequest)) {
			return false;
		}
		AbandonRequest castOther = (AbandonRequest) other;
		return new EqualsBuilder().append(this.msgid, castOther.msgid).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.msgid).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this, Style.getDefaultStyle()).append(
				"msgid", this.msgid).toString();
	}

	public byte getTag() {
		return BerTags.APPLICATION_16;
	}

}