/*
 * $Id: NotFilter.java,v 1.2 2006/02/27 18:23:01 ecerulm Exp $
 * Created on Jun 25, 2004
 */
package org.jletty.ldapstackldapops;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jletty.schema.MatchResult;

/**
 * @author $Author: ecerulm $
 * 
 */
public class NotFilter implements Filter {

	private Filter filter;

	public NotFilter(Filter filter) {
		this.filter = filter;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof NotFilter)) {
			return false;
		}
		NotFilter rhs = (NotFilter) object;

		return new EqualsBuilder().append(this.filter, rhs.filter).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(1549, 1559).append(this.filter).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(!" + this.filter.toString() + ")";
	}

	/**
	 * @return
	 */
	public Filter getChild() {
		return this.filter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jletty.ldapstack.ldapops.Filter#match(javax.naming.directory.Attributes)
	 */
	public MatchResult match(LdapEntry entry) {
		if (MatchResult.TRUE.equals(this.filter.match(entry))) {
			return MatchResult.FALSE;
		} else if (MatchResult.FALSE.equals(this.filter.match(entry))) {
			return MatchResult.TRUE;
		}
		return MatchResult.UNDEF;
	}
}