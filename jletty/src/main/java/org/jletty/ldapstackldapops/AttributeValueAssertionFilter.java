/*
 * Created on 14-ago-2004
 *
 */
package org.jletty.ldapstackldapops;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jletty.schema.Matchable;
import org.jletty.schema.Syntax;
import org.jletty.schema.Syntaxes;
import org.jletty.util.StringUtil;

/**
 * @author Administrator
 * 
 */
abstract class AttributeValueAssertionFilter extends BaseFilter {

	private static final int NOT_INIT = -1;

	private int hashCode = NOT_INIT;

	private byte[] assertionValue;

	/**
	 * @param attrDesc
	 * @param assertionValue
	 */
	public AttributeValueAssertionFilter(String attrDesc, byte[] assertionValue) {
		super(attrDesc);
		this.assertionValue = assertionValue;
	}

	/**
	 * @param attrDesc
	 * @param assertionValue2
	 */
	public AttributeValueAssertionFilter(String attrDesc, String assertionValue2) {
		super(attrDesc);
		this.assertionValue = StringUtil.toUTF8(assertionValue2);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		if (this.hashCode == NOT_INIT) {
			this.hashCode = new HashCodeBuilder(1287574491, 1794712789).appendSuper(
					super.hashCode()).append(this.assertionValue).toHashCode();
		}
		return this.hashCode;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof AttributeValueAssertionFilter)) {
			return false;
		}
		AttributeValueAssertionFilter rhs = (AttributeValueAssertionFilter) object;
		return new EqualsBuilder().appendSuper(super.equals(rhs)).append(
				this.assertionValue, rhs.assertionValue).isEquals();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		// return new ToStringBuilder(this, Style.getDefaultStyle()).append(
		// "AttributeDescription", attrDesc).append("AssertionValue",
		// assertionValue).toString();

		return "(" + getAttrDesc() + getOperatorString()
				+ getAssertionValueAsString() + ")";
	}

	public byte[] getAssertionValue() {
		return this.assertionValue;
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

	protected abstract String getOperatorString();

	/**
	 * @param attributeType
	 * @return
	 * @throws LdapInvalidAttributeSyntaxException
	 */
	protected Matchable getMatchable()
			throws LdapInvalidAttributeSyntaxException {
		String syntaxName = this.attributeType.getSyntax();
		Syntax syntax = Syntaxes.getSyntax(syntaxName);
		Matchable matchable = syntax.get(getAssertionValue());
		return matchable;
	}
}