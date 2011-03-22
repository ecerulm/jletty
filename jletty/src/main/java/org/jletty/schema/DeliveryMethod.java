package org.jletty.schema;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jletty.util.NotImplementedException;
import org.jletty.util.StringUtil;

public class DeliveryMethod implements AttributeValue {

	private String deliveryMethod;

	public DeliveryMethod(String theString) {
		this.deliveryMethod = theString;
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
		return StringUtil.stringToByteArray("UTF-8", this.deliveryMethod);
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("deliveryMethod", this.deliveryMethod).toString();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.deliveryMethod).toHashCode();
	}

	public boolean equals(final Object other) {
		if (!(other instanceof DeliveryMethod)) {
			return false;
		}
		DeliveryMethod castOther = (DeliveryMethod) other;
		return new EqualsBuilder().append(this.deliveryMethod,
				castOther.deliveryMethod).isEquals();
	}

}
