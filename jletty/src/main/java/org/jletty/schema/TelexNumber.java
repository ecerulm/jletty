package org.jletty.schema;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jletty.util.NotImplementedException;
import org.jletty.util.StringUtil;

public class TelexNumber implements AttributeValue {

	private String telexNumber;

	public TelexNumber(String value) {
		this.telexNumber = value;
	}

	public MatchResult eqMatch(MatchRule matchRule, Matchable object) {
		throw new NotImplementedException();
	}

	public MatchResult ordMatch(MatchRule matchRule, Matchable object,
			MatchResultClosure closure) {
		throw new NotImplementedException();
	}

	public MatchResult subMatch(MatchRule matchRule, String filterValue) {
		throw new NotImplementedException();
	}

	public MatchResult approxMatch(MatchRule eqMatchRule, Matchable object) {
		throw new NotImplementedException();
	}

	public byte[] getContents() {
		return StringUtil.stringToByteArray("UTF-8", this.telexNumber);
	}

	public boolean equals(final Object other) {
		if (!(other instanceof TelexNumber)) {
			return false;
		}
		TelexNumber castOther = (TelexNumber) other;
		return new EqualsBuilder().append(this.telexNumber, castOther.telexNumber)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.telexNumber).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("telexNumber", this.telexNumber)
				.toString();
	}

}
