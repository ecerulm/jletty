/*
 * Created on 17-oct-2004
 *
 */
package org.jletty.ldapstackldapops;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jletty.encoder.BERString;
import org.jletty.encoder.BerSequence;
import org.jletty.util.ByteArrayBuffer;
import org.jletty.util.HexUtils;

/**
 * @author Ruben
 * 
 */
public class LDAPResult {

	public LDAPResult(LDAPResultCode result, String matchedDN, String errMsg) {
		this.result = result;
		if (null == matchedDN) {
			throw new NullArgumentException("matchedDN");
		}
		if (!result.equals(LDAPResultCode.NO_SUCH_OBJECT)
				&& !result.equals(LDAPResultCode.ALIAS_PROBLEM)
				&& !result.equals(LDAPResultCode.INVALID_DN_SYNTAX)
				&& !result.equals(LDAPResultCode.ALIAS_DEREF_PROBLEM)) {
			if (!"".equals(matchedDN)) {
				throw new IllegalArgumentException(
						"matchedDn is only allowed in noSuchObject, aliasProblem, invalidDnSyntax and aliasDereferencingProblem result codes");
			}
		}
		this.matchedDN = matchedDN;
		this.errMsg = errMsg;
	}

	/**
	 * @param result
	 */
	public LDAPResult(LDAPResultCode result) {
		this(result, "", "");
	}

	public final String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("result code", this.result).append("matchedDN", this.matchedDN)
				.append("errMsg", this.errMsg).append("bytes",
						HexUtils.toHexString(this.getBytes())).toString();
	}

	public final byte[] getBytes() {

		ByteArrayBuffer _contents = new ByteArrayBuffer();
		_contents.append(this.result.getBytes()).append(
				new BERString(this.matchedDN).getBytes()).append(
				new BERString(this.errMsg).getBytes());
		byte[] toBytes2 = new BerSequence(getTag()).append(this.result.getBytes())
				.append(this.matchedDN).append(this.errMsg).getBytes();
		return toBytes2;
	}

	protected LDAPResultCode result;

	protected String matchedDN;

	protected String errMsg;

	protected byte getTag() {
		return BerTags.SEQUENCEOF;
	}

	public String getErrMsg() {
		return this.errMsg;
	}

	public String getMatchedDN() {
		return this.matchedDN;
	}

	public LDAPResultCode getResult() {
		return this.result;
	}

	public boolean equals(final Object other) {
		if (!(other instanceof LDAPResult)) {
			return false;
		}
		LDAPResult castOther = (LDAPResult) other;
		return new EqualsBuilder().append(this.result, castOther.result).append(
				this.matchedDN, castOther.matchedDN)
				.append(this.errMsg, castOther.errMsg).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.result).append(this.matchedDN).append(
				this.errMsg).toHashCode();
	}
}
