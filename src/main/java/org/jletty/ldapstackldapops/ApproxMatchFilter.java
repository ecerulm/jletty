/*
 * $Id: ApproxMatchFilter.java,v 1.1 2006/02/12 19:22:12 ecerulm Exp $
 * Created on Jun 29, 2004
 */
package org.jletty.ldapstackldapops;

import java.util.Iterator;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jletty.jndiprovider.LdapAttribute;
import org.jletty.schema.MatchResult;
import org.jletty.schema.Matchable;

/**
 * @author $Author: ecerulm $
 * 
 */
public class ApproxMatchFilter extends AttributeValueAssertionFilter implements
		Filter {

	/**
	 * @param attrDesc
	 * @param assertionValue
	 */
	public ApproxMatchFilter(String attrDesc, byte[] assertionValue) {
		super(attrDesc, assertionValue);
	}

	public ApproxMatchFilter(String attrDesc2, String assertionValue2) {
		super(attrDesc2, assertionValue2);

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
		if (!(object instanceof ApproxMatchFilter)) {
			return false;
		}
		ApproxMatchFilter rhs = (ApproxMatchFilter) object;
		return new EqualsBuilder().appendSuper(super.equals(rhs)).isEquals();
	}

	protected String getOperatorString() {
		return "~=";
	}

	public MatchResult match(LdapEntry entry) {
		try {
			// TODO change getAttributesAsNamingAttributes with getAttributes
			LdapAttribute attribute = entry.getAttributes().get(getAttrDesc());
			if (null == attribute) {
				return MatchResult.UNDEF;
			}

			Matchable matchable = getMatchable();

			Iterator it = attribute.getAll().iterator();
			while (it.hasNext()) {
				Matchable object = (Matchable) it.next();
				if (MatchResult.TRUE.equals(matchable.approxMatch(
						getEqMatchRule(), object))) {
					return MatchResult.TRUE;
				}
			}
			return MatchResult.FALSE;
		} catch (LdapInvalidAttributeSyntaxException e) {
			return MatchResult.UNDEF;
		}
	}
}