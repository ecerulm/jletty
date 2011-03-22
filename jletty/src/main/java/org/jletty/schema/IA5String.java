/*
 * Created on 16-feb-2005
 *
 */
package org.jletty.schema;

import java.nio.charset.CharacterCodingException;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jletty.ldapstackldapops.LdapInvalidAttributeSyntaxException;
import org.jletty.util.NotImplementedException;
import org.jletty.util.StringUtil;

/**
 * @author Ruben
 * 
 */
public class IA5String implements Matchable, AttributeValue {

	private String string;

	public String toString() {
		return this.string;
	}

	/**
	 * @param octetstring
	 * @throws LdapInvalidAttributeSyntaxException
	 */
	public IA5String(byte[] octetstring)
			throws LdapInvalidAttributeSyntaxException {
		try {
			this.string = StringUtil.byteArrayToString("US-ASCII", octetstring);
		} catch (CharacterCodingException e) {
			throw new LdapInvalidAttributeSyntaxException(e);
		}
	}

	public IA5String(String theString) {
		// TODO check that the string doesn't contain illegal ASCII-US
		// characters. create a test case
		this.string = theString;
	}

	public MatchResult eqMatch(MatchRule matchRule, Matchable object) {
		IA5String castOther = (IA5String) object;
		if (MatchRule.CASE_EXACT_MATCH.equals(matchRule)) {
			return caseExactMatch(castOther);
		} else if (MatchRule.CASE_IGNORE_MATCH.equals(matchRule)
				|| MatchRule.CASE_IGNORE_IA5_MATCH.equals(matchRule)) {
			return caseIgnoreMatch(castOther);
		}

		throw new RuntimeException("Matching rule " + matchRule
				+ " not supported by this attribute syntax");
	}

	public MatchResult caseIgnoreMatch(IA5String o) {
		// TODO When performing the caseIgnoreIA5Match, multiple adjoining
		// whitespace characters are treated the same as an individual space,
		// and leading and trailing whitespace is ignored. create a testcase

		boolean isEquals = this.string.equalsIgnoreCase(o.string);
		return isEquals ? MatchResult.TRUE : MatchResult.FALSE;
	}

	public MatchResult caseExactMatch(Object o) {
		throw new NotImplementedException();
	}

	public MatchResult ordMatch(MatchRule matchRule, Matchable object,
			MatchResultClosure closure) {
		throw new NotImplementedException();
	}

	public MatchResult subMatch(MatchRule matchRule, String object) {
		throw new NotImplementedException();
	}

	public MatchResult approxMatch(MatchRule eqMatchRule, Matchable object) {
		return eqMatch(eqMatchRule, object);
	}

	public byte[] getContents() {
		return StringUtil.toUTF8(this.string);
	}

	public boolean equals(final Object other) {
		if (!(other instanceof IA5String)) {
			return false;
		}
		IA5String castOther = (IA5String) other;
		return new EqualsBuilder().append(this.string, castOther.string).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.string).toHashCode();
	}

}
