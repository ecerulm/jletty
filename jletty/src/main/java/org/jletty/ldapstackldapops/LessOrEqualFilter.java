/*
 * $Id: LessOrEqualFilter.java,v 1.2 2006/02/27 18:23:01 ecerulm Exp $
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
public class LessOrEqualFilter extends AttributeValueAssertionFilter implements
		Filter {

	private MatchRule ordMatchRule;

	private MatchResultClosure matchClosure = new MatchResultClosure() {
		public MatchResult match(int i) {
			if (i >= 0) {
				return MatchResult.TRUE;
			} else {
				return MatchResult.FALSE;
			}
		}
	};

	/**
	 * @param attrDesc
	 * @param assertionValue
	 */
	public LessOrEqualFilter(String attrDesc, byte[] assertionValue) {

		super(attrDesc, assertionValue);
		this.ordMatchRule = getOrdMatchRule();
	}

	public LessOrEqualFilter(String attrDesc2, String assertionValue2) {
		super(attrDesc2, assertionValue2);
		this.ordMatchRule = getOrdMatchRule();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(1287574491, 1794712789).appendSuper(
				super.hashCode()).toHashCode();
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof LessOrEqualFilter)) {
			return false;
		}
		LessOrEqualFilter rhs = (LessOrEqualFilter) object;
		return new EqualsBuilder().appendSuper(super.equals(rhs)).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jletty.ldapstack.ldapops.AttributeValueAssertionFilter#getOperatorString()
	 */
	protected String getOperatorString() {
		return "<=";
	}

	public MatchResult match(LdapEntry entry) {
		try {
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
		} catch (LdapInvalidAttributeSyntaxException e) {
			// TODO add a testcase. an lessorequalfilter where the assertion
			// value is not parseable
			return MatchResult.UNDEF;
		}
	}
}