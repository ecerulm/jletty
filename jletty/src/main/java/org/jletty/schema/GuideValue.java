package org.jletty.schema;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jletty.util.NotImplementedException;
import org.jletty.util.StringUtil;

public class GuideValue implements AttributeValue {

	private String value;

	public GuideValue(String string) {
		this.value = string;
	}

	public MatchResult eqMatch(MatchRule matchRule, Matchable object) {
		// FIXME Not implemented
		throw new NotImplementedException();
	}

	public MatchResult ordMatch(MatchRule matchRule, Matchable object,
			MatchResultClosure closure) {
		// FIXME Not implemented
		throw new NotImplementedException();
	}

	public MatchResult subMatch(MatchRule matchRule, String filterValue) {
		// FIXME Not implemented
		throw new NotImplementedException();
	}

	public MatchResult approxMatch(MatchRule eqMatchRule, Matchable object) {
		// FIXME Not implemented
		throw new NotImplementedException();
	}

	public byte[] getContents() {
		return StringUtil.toUTF8(this.value);
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.value).toHashCode();
	}

	public boolean equals(final Object other) {
		if (!(other instanceof GuideValue)) {
			return false;
		}
		GuideValue castOther = (GuideValue) other;
		return new EqualsBuilder().append(this.value, castOther.value).isEquals();
	}

	public String toString() {
		return this.value;
	}

}
