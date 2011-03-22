/*
 * $Id: OrFilter.java,v 1.2 2006/02/27 18:23:01 ecerulm Exp $
 * Created on Jun 25, 2004
 */
package org.jletty.ldapstackldapops;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jletty.schema.MatchResult;

/**
 * @author $Author: ecerulm $
 * 
 */
public class OrFilter extends ComplexFilter implements Filter {

	public OrFilter(Filter[] setOfFilters) {
		super(setOfFilters);
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof OrFilter)) {
			return false;
		}
		OrFilter rhs = (OrFilter) object;

		return new EqualsBuilder().append(new HashSet(this.setOfFilters),
				new HashSet(rhs.setOfFilters)).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(1549, 1559).append(this.setOfFilters)
				.toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jletty.ldapstack.ldapops.Filter#match(javax.naming.directory.Attributes)
	 */
	public MatchResult match(LdapEntry entry) {
		// A filter of the "or" choice is FALSE if all
		// of the filters in the SET OF evaluate to FALSE, TRUE if at least
		// one filter is TRUE, and Undefined otherwise.
		final List filters = this.setOfFilters;
		boolean allFalse = true;
		for (Iterator iter = filters.iterator(); iter.hasNext();) {
			Filter f = (Filter) iter.next();
			MatchResult result = f.match(entry);
			if (MatchResult.TRUE.equals(result)) {
				return MatchResult.TRUE;
			}
			if (!MatchResult.FALSE.equals(result)) {
				allFalse = false;
			}
		}
		if (allFalse) {
			return MatchResult.FALSE;
		} else {
			return MatchResult.UNDEF;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jletty.ldapstack.ldapops.ComplexFilter#getOperatorString()
	 */
	protected String getOperatorString() {
		return "|";
	}
}