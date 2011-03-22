/*
 * $Id: AttributeTypeAndValuesListParser.java,v 1.2 2006/02/27 18:23:00 ecerulm Exp $
 * 
 * 
 * Parser for AttributeList
 * 
 * AttributeList ::= SEQUENCE OF SEQUENCE { type AttributeDescription, vals SET
 * OF AttributeValue }
 *  
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import org.jletty.ldapstackldapops.AttributeTypeAndValues;
import org.jletty.ldapstackldapops.AttributeTypeAndValuesList;
import org.jletty.ldapstackldapops.AttributeTypeAndValuesListListener;
import org.jletty.ldapstackldapops.AttributeTypeAndValuesListener;
import org.jletty.ldapstackldapops.BerTags;

/**
 * @author $Author: ecerulm $
 */
public final class AttributeTypeAndValuesListParser extends TLVParser {

	/**
	 * <code>octetsLeft</code> is used to check how many bytes are supposed to
	 * still be read to complete this TLV. It works with the declared size in
	 * the TLV
	 */
	private int octetsLeft = 0;

	/**
	 * <code>list</code> is a temporary variable used to store the parsed
	 * attributes in this TLV. when parsing is finished the values in this list
	 * are used to create a new {@link AttributeTypeAndValuesList}
	 */
	private List list = new LinkedList();

	private final AttributeTypeAndValuesParser attrParser = new AttributeTypeAndValuesParser(
			new AttributeTypeAndValuesListener() {
				public void data(AttributeTypeAndValues value) {
					AttributeTypeAndValuesListParser.this.list.add(value);
				}
			});

	/**
	 * <code>listener</code> is the receiver of the parsed values
	 */
	private AttributeTypeAndValuesListListener listener;

	/**
	 * Constructor for AttributeListParser
	 * 
	 * @param l
	 *            the receiver of the parsed values
	 */
	public AttributeTypeAndValuesListParser(AttributeTypeAndValuesListListener l) {
		this.listener = l;
	}

	protected boolean checkExpectedTag(byte tag) {
		return (BerTags.SEQUENCEOF == tag); // SEQUENCE OF
	}

	protected boolean parseContents(ByteBuffer buffer) {
		while (this.octetsLeft > 0) {
			int tmp = buffer.remaining();
			boolean completed = this.attrParser.parse(buffer);
			this.octetsLeft = this.octetsLeft - (tmp - buffer.remaining());
			if (!completed) {
				return false;
			}
		}
		return true;
	}

	protected void notifyListeners() {
		this.listener.data(new AttributeTypeAndValuesList(this.list));
	}

	protected void resetInternal() {
		// Reset variables
		this.list = new LinkedList();

		// Reset parser
		this.attrParser.reset();
	}

	protected void checkContentsLength(int tlvLength) {
		super.checkContentsLength(tlvLength);
		this.octetsLeft = tlvLength;
	}
}