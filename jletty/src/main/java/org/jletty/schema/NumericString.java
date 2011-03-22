package org.jletty.schema;

import org.jletty.ldapstackldapops.LdapInvalidAttributeSyntaxException;

public class NumericString extends DirectoryString {

	public NumericString(byte[] octetstring)
			throws LdapInvalidAttributeSyntaxException {
		super(octetstring);
	}

	public NumericString(String theString) {
		super(theString);
	}

	public MatchResult eqMatch(MatchRule matchRule, Matchable toCompare) {
		NumericString castOther = (NumericString) toCompare;
		if (MatchRule.NUMERIC_STRING_MATCH.equals(matchRule)) {
			return numericStringMatch(castOther);
		}
		return super.eqMatch(matchRule, toCompare);
	}

	private MatchResult numericStringMatch(NumericString other) {
		boolean matches = this.directoryString.equals(other.directoryString);
		return matches ? MatchResult.TRUE : MatchResult.FALSE;
	}

}
