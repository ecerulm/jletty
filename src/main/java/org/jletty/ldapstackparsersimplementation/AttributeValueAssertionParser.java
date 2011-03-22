package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.jletty.ldapstackldapops.BEROctetStringListener;
import org.jletty.ldapstackldapops.LDAPStringListener;
import org.jletty.util.Style;

/**
 * @author Administrator
 * 
 */
abstract class AttributeValueAssertionParser extends TLVParser {

	protected String attrDesc = null;

	protected byte[] assertionValue = null;

	protected Parser attrDescParser = null;

	protected Parser assertionValueParser = null;

	protected AttributeValueAssertionParser() {
		this.attrDescParser = createAttrDescParser();
		this.assertionValueParser = new BEROctetStringParser(
				new BEROctetStringListener() {
					public void data(byte[] value) {
						AttributeValueAssertionParser.this.assertionValue = value;
					}
				});
		this.parser = this.attrDescParser;
	}

	private Parser parser = null;

	/**
	 * @return a parser
	 */
	private final Parser createAttrDescParser() {
		return new LDAPStringParser(new LDAPStringListener() {

			public void data(String value) {
				AttributeValueAssertionParser.this.attrDesc = value;
			}
		});
	}

	/**
	 * Changes the default attribute description parser (for unit testing only).
	 * 
	 * @param p
	 *            the new parser
	 */
	protected void setAttrDescParser(Parser p) {
		this.attrDescParser = p;
	}

	/**
	 * Changes the default assertion value parser (for unit testing only).
	 * 
	 * @param p
	 *            the new parser
	 */
	protected void setAssertionValueParser(Parser p) {
		this.assertionValueParser = p;
	}

	protected final boolean parseContents(ByteBuffer buffer) {
		while (true) {
			boolean completed = this.parser.parse(buffer);
			if (!completed) {
				return false;
			}
			if (this.parser == this.attrDescParser) {
				this.parser = this.assertionValueParser;
			} else {
				return true;
			}
		}

	}

	protected final void resetInternal() {
		// reset valriables
		this.attrDesc = null;
		this.assertionValue = null;

		// reset parsers
		this.parser.reset();
		this.parser = this.attrDescParser;
	}

	protected final void notifyListeners() {
		notifyListener(this.attrDesc, this.assertionValue);
	}

	protected abstract void notifyListener(String attrDesc,
			byte[] assertionValue);

	public String toString() {
		return new ToStringBuilder(this, Style.getDefaultStyle()).append(
				"Currently parsing", this.parser).append("AttributeDescription",
				this.attrDesc).append("AssertionValue", this.assertionValue).toString();
	}
}