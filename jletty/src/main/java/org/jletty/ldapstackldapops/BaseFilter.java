/*
 * Created on 18-feb-2005
 *
 */
package org.jletty.ldapstackldapops;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jletty.schema.AttributeType;
import org.jletty.schema.MatchRule;
import org.jletty.schema.MatchingRuleNotFoundException;
import org.jletty.schema.Schema;

/**
 * @author Ruben
 * 
 */
abstract class BaseFilter {

	protected AttributeType attributeType;

	private String attrDesc;

	/**
	 * @param attrDesc2
	 */
	public BaseFilter(String attrDesc) {
		if (attrDesc == null || "".equals(attrDesc)) {
			throw new RuntimeException("attrDesc cannnot be null or empty");
		}
		this.attrDesc = attrDesc;
		this.attributeType = getAttributeType();

	}

	public String getAttrDesc() {
		return this.attrDesc;
	}

	protected AttributeType getAttributeType() {
		Schema schema = Schema.getInstance();
		AttributeType attributeType = schema.getAttributeType(getAttrDesc());
		return attributeType;
	}

	protected final MatchRule getOrdMatchRule() {
		try {
			if (this.attributeType == null) {
				return null;
			}
			final String ordMatchRuleName = this.attributeType.getOrdMatchRule();
			MatchRule ordMatchRule = MatchRule.getEnum(ordMatchRuleName);
			return ordMatchRule;
		} catch (MatchingRuleNotFoundException e) {
			throw new MatchingRuleNotFoundException(
					"no ord matching rule found for attribute " + this.attributeType,
					e);
		}
	}

	protected final MatchRule getEqMatchRule() {
		MatchRule eqMatchRule = MatchRule.getEnum(this.attributeType
				.getEqMatchRule());
		return eqMatchRule;
	}

	protected final MatchRule getSubMatchRule() {
		MatchRule subMatchRule = MatchRule.getEnum(this.attributeType
				.getSubMatchRule());
		return subMatchRule;
	}

	public boolean equals(final Object other) {
		if (!(other instanceof BaseFilter)) {
			return false;
		}
		BaseFilter castOther = (BaseFilter) other;
		return new EqualsBuilder().append(this.attributeType,
				castOther.attributeType).append(this.attrDesc, castOther.attrDesc)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.attributeType).append(this.attrDesc)
				.toHashCode();
	}

}
