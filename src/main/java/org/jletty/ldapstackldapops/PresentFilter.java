/*
 * $Id: PresentFilter.java,v 1.2 2006/02/27 18:23:01 ecerulm Exp $
 * Created on Jun 25, 2004
 */
package org.jletty.ldapstackldapops;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jletty.jndiprovider.LdapAttribute;
import org.jletty.schema.MatchResult;

/**
 * @author $Author: ecerulm $
 * 
 */
public class PresentFilter implements Filter {

	private String attrName;

	public PresentFilter(String attrName) {
		this.attrName = attrName;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof PresentFilter)) {
			return false;
		}
		PresentFilter rhs = (PresentFilter) object;
		return new EqualsBuilder().append(this.attrName, rhs.attrName)
				.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(1549, 1559).append(this.attrName)
				.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + this.attrName + "=*)";
	}

	/**
	 * @return
	 */
	public String getAttrDesc() {
		return this.attrName;
	}

	public MatchResult match(LdapEntry entry) {
		LdapAttribute attribute = entry.getAttributes().get(getAttrDesc());
		if (null == attribute) {
			return MatchResult.FALSE;
		}
		return MatchResult.TRUE;
	}
}