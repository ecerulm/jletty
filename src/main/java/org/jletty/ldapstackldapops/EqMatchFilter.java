/*
 * $Id: EqMatchFilter.java,v 1.2 2006/02/27 18:23:01 ecerulm Exp $
 * Created on Jun 29, 2004
 */
package org.jletty.ldapstackldapops;

import java.util.Iterator;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jletty.jndiprovider.LdapAttribute;
import org.jletty.schema.AttributeType;
import org.jletty.schema.MatchResult;
import org.jletty.schema.MatchRule;
import org.jletty.schema.Matchable;
import org.jletty.schema.Schema;
import org.jletty.schema.Syntax;
import org.jletty.schema.Syntaxes;

/**
 * @author $Author: ecerulm $
 * 
 */
public final class EqMatchFilter extends AttributeValueAssertionFilter
		implements Filter {

	private static final int NOT_INIT = -1;

	private int hashCode = NOT_INIT;

	/**
	 * @param string
	 * @param bytes
	 */
	public EqMatchFilter(String attrDesc, byte[] assertionValue) {
		super(attrDesc, assertionValue);
	}

	/**
	 * @param string
	 * @param string2
	 */
	public EqMatchFilter(String attrDesc, String assertionValue) {

		super(attrDesc, assertionValue);

	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		if (this.hashCode == NOT_INIT) {
			this.hashCode = new HashCodeBuilder(7043, 7907).appendSuper(
					super.hashCode()).toHashCode();
		}
		return this.hashCode;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof EqMatchFilter)) {
			return false;
		}
		EqMatchFilter rhs = (EqMatchFilter) object;
		return new EqualsBuilder().appendSuper(super.equals(rhs)).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jletty.ldapstack.ldapops.AttributeValueAssertionFilter#getOperatorString()
	 */
	protected String getOperatorString() {
		return "=";
	}

	public MatchResult match(LdapEntry entry) {
		try {
			Schema schema = Schema.getInstance();
			AttributeType attributeType = schema
					.getAttributeType(getAttrDesc());
			if (null == attributeType) {
				// attribute description not recognized by the server
				return MatchResult.UNDEF;
			}
			String syntaxName = attributeType.getSyntax();
			MatchRule eqMatchRule = MatchRule.getEnum(attributeType
					.getEqMatchRule());
			Syntax syntax = Syntaxes.getSyntax(syntaxName);
			Matchable matchable = syntax.get(getAssertionValue());

			// TODO change getAttributesAsNamingAttributes with getAttributes
			LdapAttribute attribute = entry.getAttributes().get(getAttrDesc());
			if (null == attribute) {
				return MatchResult.FALSE;
			}

			Iterator all = attribute.getAll().iterator();
			while (all.hasNext()) {
				Matchable object = (Matchable) all.next();
				if (MatchResult.TRUE.equals(matchable.eqMatch(eqMatchRule,
						object))) {
					return MatchResult.TRUE;
				}
			}
			return MatchResult.FALSE;
		} catch (LdapInvalidAttributeSyntaxException e) {
			return MatchResult.UNDEF;
		}

	}

}