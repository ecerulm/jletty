/*
 * Created on Jul 16, 2004
 *
 */
package org.jletty.ldapstackldapops;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jletty.jndiprovider.LdapAttribute;
import org.jletty.schema.MatchResult;
import org.jletty.schema.MatchRule;
import org.jletty.schema.Matchable;
import org.jletty.schema.Syntax;
import org.jletty.schema.Syntaxes;
import org.jletty.util.StringUtil;

/**
 * @author Administrator
 * 
 */
public class SubstringFilter extends BaseFilter implements Filter {

	private String initial;

	private List any;

	private String fin;

	public SubstringFilter(String attrName, SubstringValue value) {
		super(attrName);
		this.initial = value.getInitial();
		this.any = value.getAny();
		this.fin = value.getFinal();

	}

	protected Matchable getMatchable()
			throws LdapInvalidAttributeSyntaxException {
		String syntaxName = this.attributeType.getSyntax();
		Syntax syntax = Syntaxes.getSyntax(syntaxName);
		Matchable matchable = syntax.get(StringUtil.toUTF8(valueString()));
		return matchable;
	}

	public List getAny() {
		return this.any;
	}

	public String getFin() {
		return this.fin;
	}

	public String getInitial() {
		return this.initial;
	}

	public int hashCode() {
		return new HashCodeBuilder(-495434291, 1583613307).appendSuper(
				super.hashCode()).append(this.initial).append(this.any).append(this.fin)
				.toHashCode();
	}

	public boolean equals(final Object other) {
		if (!(other instanceof SubstringFilter)) {
			return false;
		}
		SubstringFilter castOther = (SubstringFilter) other;
		return new EqualsBuilder().appendSuper(super.equals(castOther)).append(
				this.initial, castOther.initial).append(this.any, castOther.any).append(
				this.fin, castOther.fin).isEquals();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("(" + getAttrDesc() + "=");
		sb.append(valueString());
		sb.append(")");
		return sb.toString();

	}

	public String valueString() {
		StringBuffer sb = new StringBuffer();
		if (this.initial != null) {
			sb.append(this.initial);
		}
		for (Iterator iter = this.any.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			sb.append("*" + element);
		}
		sb.append("*");
		if (this.fin != null) {
			sb.append(this.fin);
		}
		return sb.toString();

	}

	public MatchResult match(LdapEntry entry) {
		LdapAttribute attribute = entry.getAttributes().get(getAttrDesc());
		if (null == attribute) {
			return MatchResult.UNDEF;
		}

		MatchRule subMatchRule = getSubMatchRule();
		Iterator all = attribute.getAll().iterator();
		while (all.hasNext()) {
			Object object = all.next();
			if (object instanceof Matchable) {
				Matchable matchable = (Matchable) object;
				if (MatchResult.TRUE.equals(matchable.subMatch(subMatchRule,
						valueString()))) {
					return MatchResult.TRUE;
				}
			} else {
				throw new RuntimeException(
						"The attribute value doesn't implement the Matchable interface");
			}

		}
		return MatchResult.FALSE;
	}
}