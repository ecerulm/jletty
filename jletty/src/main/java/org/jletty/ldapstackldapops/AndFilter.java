/*
 * $Id: AndFilter.java,v 1.2 2006/02/27 18:23:01 ecerulm Exp $
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
public class AndFilter extends ComplexFilter implements Filter {

	public AndFilter(Filter[] setOfFilters) {
		super(setOfFilters);
	}

	public boolean equals(Object object) {
		if (!(object instanceof AndFilter)) {
			return false;
		}
		AndFilter rhs = (AndFilter) object;

		return new EqualsBuilder().append(new HashSet(this.setOfFilters),
				new HashSet(rhs.setOfFilters)).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(1549, 1559).append(this.setOfFilters)
				.toHashCode();
	}

	public MatchResult match(LdapEntry entry) {
		// A filter of the "and" choice is TRUE if all the filters in the SET
		// OF evaluate to TRUE, FALSE if at least one filter is FALSE, and
		// otherwise Undefined.
		final List filters = this.setOfFilters;
		boolean allTrue = true;
		for (Iterator iter = filters.iterator(); iter.hasNext();) {
			Filter f = (Filter) iter.next();
			MatchResult result = f.match(entry);
			if (MatchResult.FALSE.equals(result)) {
				return MatchResult.FALSE;
			}
			if (!MatchResult.TRUE.equals(result)) {
				allTrue = false;
			}
		}
		if (allTrue) {
			return MatchResult.TRUE;
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
		return "&";
	}
}