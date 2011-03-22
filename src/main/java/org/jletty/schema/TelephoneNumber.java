/*
 * Created on 16-feb-2005
 *
 */
package org.jletty.schema;

import java.nio.charset.CharacterCodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.directory.InvalidAttributeValueException;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jletty.ldapstackldapops.LdapInvalidAttributeSyntaxException;
import org.jletty.util.NotImplementedException;
import org.jletty.util.StringUtil;

/**
 * @author Ruben
 * 
 */
public class TelephoneNumber implements Matchable, AttributeValue {

	private String telephoneNumber;

	/**
	 * @param octetstring
	 * @throws InvalidAttributeValueException
	 */
	public TelephoneNumber(byte[] octetstring)
			throws LdapInvalidAttributeSyntaxException {
		try {
			// TODO add syntax check
			this.telephoneNumber = StringUtil.byteArrayToString("US-ASCII",
					octetstring);
		} catch (CharacterCodingException e) {
			throw new LdapInvalidAttributeSyntaxException(e);
		}
	}

	public MatchResult eqMatch(MatchRule matchRule, Matchable object) {
		if (object instanceof TelephoneNumber) {
			TelephoneNumber telNumber = (TelephoneNumber) object;
			if (MatchRule.TELEPHONE_NUMBER_MATCH.equals(matchRule)) {
				return telephoneNumberMatch(telNumber);
			}
			// unsupported matching rule for this syntax
			return MatchResult.UNDEF;

		} else {
			return MatchResult.FALSE;
		}

	}

	public MatchResult telephoneNumberMatch(TelephoneNumber o) {
		String normalizedTel1 = normalize(this.telephoneNumber);
		String normalizedTel2 = normalize(o.telephoneNumber);
		final boolean isEquals = normalizedTel1.equals(normalizedTel2);
		return isEquals ? MatchResult.TRUE : MatchResult.FALSE;
	}

	/**
	 * @param telephoneNumber2
	 * @return
	 */
	private String normalize(String tel) {
		String toReturn = removeWhitespace(tel);
		toReturn = removeDash(tel);
		return toReturn;
	}

	public static String removeWhitespace(CharSequence inputStr) {
		String patternStr = "\\s+";
		String replaceStr = "";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher.replaceAll(replaceStr);
	}

	public static String removeDash(CharSequence inputStr) {
		String patternStr = "-+";
		String replaceStr = "";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher.replaceAll(replaceStr);
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
		return StringUtil.toUTF8(this.telephoneNumber);
	}

	public boolean equals(final Object other) {
		if (!(other instanceof TelephoneNumber)) {
			return false;
		}
		TelephoneNumber castOther = (TelephoneNumber) other;
		return new EqualsBuilder().append(this.telephoneNumber,
				castOther.telephoneNumber).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.telephoneNumber).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("telephoneNumber",
				this.telephoneNumber).toString();
	}

}
