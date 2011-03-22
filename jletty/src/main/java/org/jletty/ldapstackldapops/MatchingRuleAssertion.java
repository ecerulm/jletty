package org.jletty.ldapstackldapops;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jletty.util.Style;

/**
 * @author Ruben
 * 
 */
public class MatchingRuleAssertion {

	private String matchinRuleId;

	private String type;

	private byte[] assertionValue;

	private boolean dnAttr;

	/**
	 * @param matchingRuleId
	 * @param type
	 * @param assertionValue
	 * @param dnAttr
	 */
	public MatchingRuleAssertion(String matchingRuleId, String type,
			byte[] assertionValue, boolean dnAttr) {
		this.matchinRuleId = matchingRuleId;
		this.type = type;
		this.assertionValue = assertionValue.clone();
		this.dnAttr = dnAttr;
	}

	public String toString() {
		return new ToStringBuilder(this, Style.getDefaultStyle()).append(
				"matchinRuleId", this.matchinRuleId).append("type", this.type).append(
				"matchValue", this.assertionValue).append("dnAttributes", this.dnAttr)
				.toString();
	}

	public boolean equals(final Object other) {
		if (!(other instanceof MatchingRuleAssertion)) {
			return false;
		}
		MatchingRuleAssertion castOther = (MatchingRuleAssertion) other;
		return new EqualsBuilder().append(this.matchinRuleId,
				castOther.matchinRuleId).append(this.type, castOther.type).append(
				this.assertionValue, castOther.assertionValue).append(this.dnAttr,
				castOther.dnAttr).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.matchinRuleId).append(this.type)
				.append(this.assertionValue).append(this.dnAttr).toHashCode();
	}

	/**
	 * @return
	 */
	public String getType() {
		return this.type;
	}

	public byte[] getAssertionValue() {
		return ArrayUtils.clone(this.assertionValue);
	}

	public String getAssertionValueAsString() {
		// Create the encoder and decoder for ISO-8859-1
		Charset charset = Charset.forName("UTF-8");
		CharsetDecoder decoder = charset.newDecoder();
		// CharsetEncoder encoder = charset.newEncoder();

		try {
			// Convert ISO-LATIN-1 bytes in a ByteBuffer to a character
			// ByteBuffer and then to a string.
			// The new ByteBuffer is ready to be read.
			CharBuffer cbuf = decoder.decode(ByteBuffer.wrap(this.assertionValue));
			String s = cbuf.toString();
			return s;
		} catch (CharacterCodingException e) {
			// almost impossible
			// TODO write a junit test for this case
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return
	 */
	public boolean isDnAttributes() {
		return this.dnAttr;
	}

	/**
	 * @return
	 */
	public String getMatchingRuleId() {
		return this.matchinRuleId;
	}
}