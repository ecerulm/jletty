/*
 * $Id: ExtensibleMatchFilter.java,v 1.3 2006/02/27 18:24:45 ecerulm Exp $
 * Created on Jun 29, 2004
 */
package org.jletty.ldapstackldapops;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.EnumerationUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jletty.dn.DistinguishedName;
import org.jletty.dn.DnAtom;
import org.jletty.dn.DnNameComponent;
import org.jletty.jndiprovider.LdapAttribute;
import org.jletty.schema.AttributeType;
import org.jletty.schema.AttributeValue;
import org.jletty.schema.MatchResult;
import org.jletty.schema.MatchRule;
import org.jletty.schema.Matchable;
import org.jletty.schema.Schema;
import org.jletty.schema.Syntax;
import org.jletty.schema.Syntaxes;
import org.jletty.util.StringUtil;
import org.jletty.util.Style;

/**
 * @author $Author: ecerulm $
 * 
 */
public class ExtensibleMatchFilter implements Filter {

	private MatchingRuleAssertion matchingRuleAssertion;

	/**
	 * @param string
	 * @param string2
	 * @param bytes
	 * @param b
	 */
	public ExtensibleMatchFilter(String type, String matchingRuleId,
			byte[] assertionValue, boolean dnAttr) {
		this.matchingRuleAssertion = new MatchingRuleAssertion(matchingRuleId,
				type, assertionValue, dnAttr);
	}

	public ExtensibleMatchFilter(String type, String matchingRuleId,
			String assertionValue, boolean dnAttr) {
		byte[] assertionValueByte;
		assertionValueByte = StringUtil.toUTF8(assertionValue);
		this.matchingRuleAssertion = new MatchingRuleAssertion(matchingRuleId,
				type, assertionValueByte, dnAttr);

	}

	public String toString() {
		return new ToStringBuilder(this, Style.getDefaultStyle()).append(
				"matchingRuleAssertion", this.matchingRuleAssertion).toString();
	}

	public boolean equals(final Object other) {
		if (!(other instanceof ExtensibleMatchFilter)) {
			return false;
		}
		ExtensibleMatchFilter castOther = (ExtensibleMatchFilter) other;
		return new EqualsBuilder().append(this.matchingRuleAssertion,
				castOther.matchingRuleAssertion).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.matchingRuleAssertion)
				.toHashCode();
	}

	public String getAttrDesc() {
		return this.matchingRuleAssertion.getType();
	}

	public Object getAssertionValueAsString() {
		return this.matchingRuleAssertion.getAssertionValueAsString();
	}

	public boolean isDnAttributes() {
		return this.matchingRuleAssertion.isDnAttributes();
	}

	public String getMatchingRuleId() {
		return this.matchingRuleAssertion.getMatchingRuleId();
	}

	private MatchResult checkIfAttrMatches(LdapEntry entry, String attrDesc,
			MatchRule inputMatchRule) {
		try {
			// TODO change getAttributesAsNamingAttributes with getAttributes
			final LdapAttribute inputAttribute = entry.getAttributes().get(
					attrDesc);
			final String attrName = inputAttribute.getID();
			if (inputAttribute == null) {
				return MatchResult.FALSE;
			}
			if (inputMatchRule == null) {
				inputMatchRule = getMatchRuleForAttribute(attrName);
			}

			final AttributeValue inputParsedValue = parseValue(attrName,
					getAssertionValue());

			return checkForMatches(inputMatchRule, inputAttribute.getAll(),
					inputParsedValue);
		} catch (LdapInvalidAttributeSyntaxException e) {
			return MatchResult.UNDEF;
		}
	}

	private MatchResult checkForMatches(MatchRule inputMatchRule,
			Collection inputAttributeValues,
			final AttributeValue inputParsedValue) {

		for (Iterator iter = inputAttributeValues.iterator(); iter.hasNext();) {
			Object value = iter.next();
			MatchResult result = inputParsedValue.eqMatch(inputMatchRule,
					(Matchable) value);
			if (MatchResult.TRUE.equals(result)) {
				return MatchResult.TRUE;
			} else if (MatchResult.UNDEF.equals(result)) {
				return MatchResult.UNDEF;
			}
		}
		return MatchResult.FALSE;
	}

	private AttributeValue parseValue(String inputAttrName,
			MatchingRuleAssertion assertionValue)
			throws LdapInvalidAttributeSyntaxException {
		AttributeType attributeType = Schema.getInstance().getAttributeType(
				inputAttrName);
		String syntaxName = attributeType.getSyntax();
		Syntax syntax = Syntaxes.getSyntax(syntaxName);
		AttributeValue toReturn = syntax
				.get(assertionValue.getAssertionValue());
		return toReturn;
	}

	private MatchingRuleAssertion getAssertionValue() {
		return this.matchingRuleAssertion;
	}

	private MatchRule getMatchRuleForAttribute(String inputAttrName) {
		AttributeType attributeType = Schema.getInstance().getAttributeType(
				inputAttrName);
		String eqMatchRule = attributeType.getEqMatchRule();
		MatchRule toReturn = MatchRule.getEnum(eqMatchRule);

		return toReturn;
	}

	public MatchResult match(LdapEntry entry) {
		final String matchingRuleId = getMatchingRuleId();
		AttributeValue parsedValue = null;
		MatchRule matchRule = null;
		if (null != matchingRuleId && !"".equals(matchingRuleId)) {
			// check that the matchingrule is recognized
			matchRule = MatchRule.getEnum(matchingRuleId);
			if (null == matchRule) {
				// unrecognized matching rule
				return MatchResult.UNDEF;
			}
			final String syntaxOid = matchRule.getSyntaxOid();
			Syntax syntax = Syntaxes.getSyntax(syntaxOid);
			if (syntax == null) {
				throw new RuntimeException("Unrecognized syntax " + syntaxOid);
			}
			// check that the assertionvalue can be parsed
			try {
				parsedValue = syntax.get(this.matchingRuleAssertion
						.getAssertionValue());
			} catch (LdapInvalidAttributeSyntaxException e) {
				return MatchResult.UNDEF;
			}

		}

		MatchResult toReturn = MatchResult.FALSE;
		String attrDesc = getAttrDesc();
		if (attrDesc != null && !"".equals(attrDesc)) {
			toReturn = checkIfAttrMatches(entry, attrDesc, matchRule);
			if (MatchResult.TRUE.equals(toReturn)) {
				return MatchResult.TRUE;
			}

		} else {
			// matchingRule only. try with every attribute
			toReturn = checkAttributesMatchingRuleOnly(entry, matchRule,
					parsedValue);
			if (MatchResult.TRUE.equals(toReturn)) {
				return MatchResult.TRUE;
			}
		}
		if (isDnAttributes()) {
			boolean matches = checkDnAttributes(entry, matchRule);
			if (matches) {
				return MatchResult.TRUE;
			}
		}
		return toReturn;

	}

	private boolean checkDnAttributes(LdapEntry entry, final MatchRule matchRule) {
		DistinguishedName dn = entry.getName();
		List dnComponents = EnumerationUtils
				.toList(dn.getAllDnNameComponents());
		boolean matches = CollectionUtils.exists(dnComponents, new Predicate() {
			public boolean evaluate(Object object) {
				DnNameComponent dnComponent = (DnNameComponent) object;
				return checkForMatchesDnComponent(matchRule, dnComponent);
			}

		});
		return matches;
	}

	private MatchResult checkAttributesMatchingRuleOnly(LdapEntry entry,
			final MatchRule matchRule, final AttributeValue parsedValue) {
		Collection attrsList = entry.getAttributes().asCollection();
		boolean matches = CollectionUtils.exists(attrsList, new Predicate() {
			public boolean evaluate(Object object) {
				final LdapAttribute inputAttribute = (LdapAttribute) object;
				final MatchResult matches = checkForMatches(matchRule,
						inputAttribute.getAll(), parsedValue);

				return MatchResult.TRUE.equals(matches);
			}
		});

		// cannot return undef because we already check for matchRule and
		// parsedValue
		return matches ? MatchResult.TRUE : MatchResult.FALSE;
	}

	private boolean checkForMatchesDnComponent(final MatchRule matchRule,
			DnNameComponent dnComponent) {
		List dnAtoms = dnComponent.getAtoms();

		boolean toReturn = CollectionUtils.exists(dnAtoms, new Predicate() {

			public boolean evaluate(Object object) {
				try {
					DnAtom atom = (DnAtom) object;
					final Attribute attribute = atom.toAttribute();
					final String attrName = attribute.getID();
					final AttributeValue parsedValue;
					parsedValue = parseValue(attrName, getAssertionValue());
					MatchRule tmpMatchRule = matchRule;
					if (tmpMatchRule == null) {
						tmpMatchRule = getMatchRuleForAttribute(attrName);
					}
					return MatchResult.TRUE.equals(checkForMatches(
							tmpMatchRule, EnumerationUtils.toList(attribute
									.getAll()), parsedValue));
				} catch (LdapInvalidAttributeSyntaxException e) {
					return false;
				} catch (NamingException e) {
					throw new RuntimeException(e);
				}
			}

		});
		return toReturn;
	}

}