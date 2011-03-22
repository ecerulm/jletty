package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.CompareRequest;
import org.jletty.ldapstackldapops.LDAPStringListener;
import org.jletty.ldapstackldapops.RequestListener;
import org.jletty.util.Style;

public class CompareRequestParser extends TLVParser implements Parser {

	private RequestListener listener;

	private String entry;

	private String attrDesc;

	private byte[] attrValue;

	public CompareRequestParser(RequestListener listener) {
		this.listener = listener;
	}

	private Parser entryParser = new LDAPStringParser(new LDAPStringListener() {
		public void data(String value) {
			CompareRequestParser.this.entry = value;
		}
	});

	private Parser avaParser = new AttributeValueAssertionParser() {

		protected boolean checkExpectedTag(byte tag) {
			return (BerTags.SEQUENCEOF == tag);
		}

		protected void notifyListener(String localAttrDesc,
				byte[] assertionValue) {
			CompareRequestParser.this.attrDesc = localAttrDesc;
			CompareRequestParser.this.attrValue = assertionValue;
		}
	};

	private Parser parser = this.entryParser;

	protected boolean parseContents(ByteBuffer buffer) {
		while (true) {
			boolean completed = this.parser.parse(buffer);
			if (!completed) {
				return false;
			}
			if (this.parser.equals(this.avaParser)) {
				return true;
			}
			this.parser = this.avaParser;
		}
	}

	protected boolean checkExpectedTag(byte tag) throws ParserException {
		return (BerTags.APPLICATION_14 == tag);
	}

	protected void resetInternal() {
		this.entry = "";
		this.attrDesc = "";
		this.attrValue = null;
		// reset parser
		this.parser.reset();
		this.parser = this.entryParser;
	}

	protected void notifyListeners() {
		this.listener.data(new CompareRequest(this.entry, this.attrDesc, this.attrValue));
	}

	public String toString() {
		return new ToStringBuilder(this, Style.getDefaultStyle()).append(
				"entry", this.entry).append("attrDesc", this.attrDesc).append(
				"attrValue", this.attrValue).append("state:", this.parser).toString();
	}

}