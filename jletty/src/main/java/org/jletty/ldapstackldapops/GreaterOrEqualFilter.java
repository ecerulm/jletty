/*
 * $Id: GreaterOrEqualFilter.java,v 1.2 2006/02/27 18:23:01 ecerulm Exp $
 * Created on Jun 29, 2004
 */
package org.jletty.ldapstackldapops;

import java.util.Iterator;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jletty.jndiprovider.LdapAttribute;
import org.jletty.schema.MatchResult;
import org.jletty.schema.MatchResultClosure;
import org.jletty.schema.MatchRule;
import org.jletty.schema.Matchable;

/**
 * @author $Author: ecerulm $
 * 
 */
public class GreaterOrEqualFilter extends AttributeValueAssertionFilter
		implements Filter {

	private MatchRule ordMatchRule;

	private MatchResultClosure matchClosure = new MatchResultClosure() {
		public MatchResult match(int i) {
			if (i > 0) {
				return MatchResult.FALSE;
			} else {
				return MatchResult.TRUE;
			}
		}

	};

	public GreaterOrEqualFilter(String attrDesc, byte[] assertionValue) {
		super(attrDesc, assertionValue);
		this.ordMatchRule = getOrdMatchRule();
	}

	public GreaterOrEqualFilter(String attrDesc, String assertionValue) {
		super(attrDesc, assertionValue);
		this.ordMatchRule = getOrdMatchRule();
	}

	protected String getOperatorString() {
		return ">=";
	}

	public MatchResult match(LdapEntry entry) {
		// TODO change call to getAttributesAsNamingAttributes to
		// getAttributes
		LdapAttribute attribute = entry.getAttributes().get(getAttrDesc());
		if (null == attribute) {
			return MatchResult.UNDEF;
		}
		if (null == this.ordMatchRule) {
			return MatchResult.UNDEF;
		}

		Matchable matchable = getMatchable();
		Iterator all = attribute.getAll().iterator();
		while (all.hasNext()) {
			Matchable object = (Matchable) all.next();
			if (MatchResult.TRUE.equals(matchable.ordMatch(this.ordMatchRule,
					object, this.matchClosure))) {
				return MatchResult.TRUE;
			}
		}

		return MatchResult.FALSE;

	}

	public int hashCode() {
		return new HashCodeBuilder(1287574491, 1794712789).appendSuper(
				super.hashCode()).toHashCode();
	}

	public boolean equals(Object object) {
		if (!(object instanceof GreaterOrEqualFilter)) {
			return false;
		}
		GreaterOrEqualFilter rhs = (GreaterOrEqualFilter) object;
		return new EqualsBuilder().appendSuper(super.equals(rhs)).isEquals();
	}
}