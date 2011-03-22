/*
 * $Id: LDAPMessage.java,v 1.2 2006/02/27 18:23:01 ecerulm Exp $
 * Created on 12-abr-2004
 *
 */
package org.jletty.ldapstackldapops;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jletty.encoder.BerSequence;
import org.jletty.util.Style;

/**
 * @author $Author: ecerulm $
 * 
 */
public class LDAPMessage {
	private int msgid;

	private LDAPOperation operation;

	/**
	 * @param _msgid
	 * @param _req
	 */
	public LDAPMessage(int msgid, LDAPOperation req) {
		this.msgid = msgid;
		this.operation = req;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof LDAPMessage)) {
			return false;
		}
		LDAPMessage rhs = (LDAPMessage) object;
		return new EqualsBuilder().append(this.msgid, rhs.msgid).append(
				this.operation, rhs.operation).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.msgid).append(this.operation)
				.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this, Style.getDefaultStyle()).append(
				"msg id", this.msgid).append("Ldap operation", this.operation).toString();
	}

	/**
	 * @return
	 */
	public int get_msgid() {
		return this.msgid;
	}

	public byte[] getBytes() {
		final byte[] toBytes = new BerSequence().append(this.msgid)
				.append(this.operation).getBytes();
		return toBytes;

	}

	/**
	 * 
	 */
	public LDAPOperation getLdapOperation() {
		return this.operation;
	}

}