/*
 * $Header: /usr/local/cvsroot/jletty-new/src/org/jletty/ldapstackldapops/BindRequest.java,v 1.3 2006/02/27 18:24:45 ecerulm Exp $
 * $Log: BindRequest.java,v $
 * Revision 1.3  2006/02/27 18:24:45  ecerulm
 * source clean up remove unnecessay code (casts)
 *
 * Revision 1.2  2006/02/27 18:23:01  ecerulm
 * source clean up
 *
 * Revision 1.1  2006/02/12 19:22:12  ecerulm
 * first version
 *
 * Revision 1.1  2005/07/22 16:53:16  ecerulm
 * first version
 *
 * Revision 1.5  2004/05/29 18:23:19  ecerulm
 * Formatting
 *
 * Revision 1.4  2004/05/21 20:53:42  ecerulm
 * Formatting
 * Added a NotImplementedException for getBytes()
 *
 * Revision 1.3  2004/05/08 11:29:20  ecerulm
 * CVS keywords
 *
 * Created on 24-feb-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.jletty.ldapstackldapops;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jletty.util.NotImplementedException;

/**
 * @author $Author: ecerulm $
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BindRequest implements LDAPRequest {

	private byte[] _passwd;

	private String _ldapdn;

	private int _version;

	public int hashCode() {
		return new HashCodeBuilder().append(this._passwd).append(this._ldapdn).append(
				this._version).toHashCode();
	}

	public boolean equals(final Object other) {
		if (!(other instanceof BindRequest)) {
			return false;
		}
		BindRequest castOther = (BindRequest) other;
		return new EqualsBuilder().append(this._passwd, castOther._passwd).append(
				this._ldapdn, castOther._ldapdn)
				.append(this._version, castOther._version).isEquals();
	}

	/**
	 * @param i
	 * @param string
	 * @param string2
	 */
	public BindRequest(int version, String ldapdn, byte[] passwd) {
		if ((version < 2) || (version > 3)) {
			throw new IllegalArgumentException(
					"Only LDAPv2 and LDAPv3 allowed. version supplied = "
							+ version);
		}
		if (ldapdn == null) {
			throw new IllegalArgumentException("Must provide a DN");
		}
		this._version = version;
		this._ldapdn = ldapdn;
		this._passwd = passwd.clone();
	}

	public BindRequest(int version, String ldapdn, String passwd) {
		this(version, ldapdn, passwd.getBytes());
	}

	/**
	 * 
	 */
	public int getVersion() {
		return this._version;
	}

	public String getLdapDN() {
		return this._ldapdn;
	}

	public byte[] getPasswd() {
		return this._passwd.clone();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public byte getTag() {
		return BerTags.APPLICATION_0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rubenlaguna.ldapstack.ldapops.LDAPOperation#getBytes()
	 */
	public byte[] getBytes() {
		throw new NotImplementedException();
	}
}