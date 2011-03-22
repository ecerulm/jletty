/*
 * Created on 17-feb-2005
 *
 */
package org.jletty.schema;

import java.nio.charset.CharacterCodingException;

import javax.naming.directory.InvalidAttributeValueException;

import org.jletty.ldapstackldapops.LdapInvalidAttributeSyntaxException;
import org.jletty.util.NotImplementedException;
import org.jletty.util.StringUtil;

/**
 * @author Ruben
 * 
 */
public class PrintableString implements Matchable, AttributeValue {

	private String printableString;

	/**
	 * @param octetstring
	 * @throws InvalidAttributeValueException
	 */
	public PrintableString(byte[] octetstring)
			throws LdapInvalidAttributeSyntaxException {
		try {
			for (int i = 0; i < octetstring.length; i++) {
				byte b = octetstring[i];
				if (b < 32 || b > 127) {
					throw new IllegalArgumentException(
							"A printable string doesn allow char (" + b
									+ ") in position " + i);
				}
			}

			this.printableString = StringUtil.byteArrayToString("US-ASCII",
					octetstring);
		} catch (CharacterCodingException e) {
			throw new LdapInvalidAttributeSyntaxException(e);
		}
	}

	public MatchResult eqMatch(MatchRule matchRule, Matchable object) {
		if (!(object instanceof PrintableString)) {
			throw new RuntimeException("Parameter " + object
					+ " hasn't the right type " + this.getClass());
		}
		PrintableString castOther = (PrintableString) object;
		if (MatchRule.CASE_IGNORE_MATCH.equals(matchRule)) {
			return caseIgnoreMatch(castOther);
		}

		throw new NotImplementedException();
	}

	public MatchResult ordMatch(MatchRule matchRule, Matchable object,
			MatchResultClosure closure) {
		if (!(object instanceof PrintableString)) {
			throw new RuntimeException("Parameter " + object
					+ " hasn't the right type " + this.getClass());
		}
		PrintableString castOther = (PrintableString) object;

		if (MatchRule.CASE_IGNORE_ORDERING_MATCH.equals(matchRule)) {

			return closure.match(caseIgnoreOrdering(castOther));
		}

		throw new NotImplementedException("Ordering matching rule " + matchRule
				+ " not implemented");
	}

	/**
	 * @param castOther
	 * @return
	 */
	private int caseIgnoreOrdering(PrintableString castOther) {
		return this.printableString.compareToIgnoreCase(castOther.printableString);
	}

	private MatchResult caseIgnoreMatch(PrintableString castOther) {
		final boolean equalsIgnoreCase = this.printableString
				.equalsIgnoreCase(castOther.printableString);
		return equalsIgnoreCase ? MatchResult.TRUE : MatchResult.FALSE;
	}

	public MatchResult subMatch(MatchRule matchRule, String object) {
		throw new NotImplementedException();
	}

	public MatchResult approxMatch(MatchRule eqMatchRule, Matchable object) {
		return eqMatch(eqMatchRule, object);
	}

	public byte[] getContents() {
		return StringUtil.toUTF8(this.printableString);
	}

	public String toString() {
		return this.printableString;
	}

}
