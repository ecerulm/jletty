/*
 * Created on 15-feb-2005
 *
 */
package org.jletty.schema;

import java.nio.charset.CharacterCodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.language.Metaphone;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jletty.ldapstackldapops.LdapInvalidAttributeSyntaxException;
import org.jletty.util.NotImplementedException;
import org.jletty.util.StringUtil;

/**
 * @author Ruben
 * 
 */
public class DirectoryString implements AttributeValue {

	protected String directoryString;

	/**
	 * @param octetstring
	 * @throws LdapInvalidAttributeSyntaxException
	 */
	public DirectoryString(byte[] octetstring)
			throws LdapInvalidAttributeSyntaxException {
		try {
			this.directoryString = StringUtil.byteArrayToString("UTF-8",
					octetstring);
		} catch (CharacterCodingException e) {
			throw new LdapInvalidAttributeSyntaxException(e);
		}

	}

	public DirectoryString(String theString) {
		this.directoryString = theString;
	}

	public MatchResult eqMatch(MatchRule matchRule, Matchable toCompare) {
		if (!(toCompare instanceof DirectoryString)) {
			return MatchResult.UNDEF;
		}
		if (matchRule == null) {
			throw new RuntimeException("matchRule cannot be null");
		}
		DirectoryString castOther = (DirectoryString) toCompare;

		if (MatchRule.CASE_EXACT_MATCH.equals(matchRule)) {
			return caseExactMatch(castOther);
		} else if (MatchRule.CASE_IGNORE_MATCH.equals(matchRule)) {
			return caseIgnoreMatch(castOther);
		}

		return MatchResult.UNDEF;
	}

	protected MatchResult caseIgnoreMatch(DirectoryString o) {
		final boolean isEquals = this.directoryString.toLowerCase().equals(
				o.directoryString.toLowerCase());
		return isEquals ? MatchResult.TRUE : MatchResult.FALSE;
	}

	protected MatchResult caseExactMatch(DirectoryString o) {
		final boolean isEquals = this.directoryString.equals(o.directoryString);

		return isEquals ? MatchResult.TRUE : MatchResult.FALSE;
	}

	public MatchResult ordMatch(MatchRule matchRule, Matchable toCompare,
			MatchResultClosure closure) {
		if (!(toCompare instanceof DirectoryString)) {
			return MatchResult.UNDEF;
		}
		if (matchRule == null) {
			// TODO this check should be done by caller (filter)
			return MatchResult.UNDEF;
		}
		if (MatchRule.CASE_IGNORE_ORDERING_MATCH.equals(matchRule)) {
			final int compareResult = caseIgnoreOrderingMatch(toCompare);
			return closure.match(compareResult);
		}
		throw new NotImplementedException();
	}

	/**
	 * 
	 * @param toCompare
	 * @return a negative integer if toCompare is greater than this object
	 */
	private int caseIgnoreOrderingMatch(Object toCompare) {
		DirectoryString lhs = this;
		DirectoryString rhs = (DirectoryString) toCompare;
		final String lhsString = lhs.directoryString;
		final String rhsString = rhs.directoryString;
		final int toReturn = lhsString.compareToIgnoreCase(rhsString);
		// final int toReturn = rhsString.compareToIgnoreCase(lhsString);
		return toReturn;
	}

	public MatchResult subMatch(MatchRule matchRule, String filterValue) {
		if (MatchRule.CASE_IGNORE_SUBSTRING_MATCH.equals(matchRule)) {
			return caseIgnoreSubstringMatch(filterValue);
		}

		throw new NotImplementedException("Matching rule " + matchRule
				+ " not implemented for this type");
	}

	/**
	 * @param filter
	 * @return
	 */
	private MatchResult caseIgnoreSubstringMatch(String filter) {

		String toCompare = this.directoryString.toLowerCase();
		String theFilter = replace(filter.toLowerCase(), "*", ".*");
		// Compile regular expression
		Pattern pattern = Pattern.compile(theFilter);

		// Determine if there is an exact match
		Matcher matcher = pattern.matcher(toCompare);
		boolean matchFound = matcher.matches(); // false
		return matchFound ? MatchResult.TRUE : MatchResult.FALSE;
	}

	static String replace(String str, String pattern, String replace) {
		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();

		while ((e = str.indexOf(pattern, s)) >= 0) {
			result.append(str.substring(s, e));
			result.append(replace);
			s = e + pattern.length();
		}
		result.append(str.substring(s));
		return result.toString();
	}

	public MatchResult approxMatch(MatchRule eqMatchRule, Matchable object) {
		// TODO return match based on metaphone
		if (object instanceof DirectoryString) {
			DirectoryString other = (DirectoryString) object;
			Metaphone metaphone = new Metaphone();
			final boolean isEquals = metaphone.isMetaphoneEqual(
					this.directoryString, other.directoryString);
			return isEquals ? MatchResult.TRUE : MatchResult.FALSE;
		} else {
			throw new RuntimeException("Wrong type argument " + object
					+ ". It should be DirectoryString");
		}

	}

	public byte[] getContents() {
		return StringUtil.toUTF8(this.directoryString);
	}

	public String toString() {
		return this.directoryString;
	}

	public boolean equals(final Object other) {
		if (!(other instanceof DirectoryString)) {
			return false;
		}
		DirectoryString castOther = (DirectoryString) other;
		return new EqualsBuilder().append(this.directoryString,
				castOther.directoryString).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.directoryString).toHashCode();
	}

}
