/*
 * $Id: BerTags.java,v 1.1 2006/02/12 19:22:12 ecerulm Exp $
 * Created on May 29, 2004
 */
package org.jletty.ldapstackldapops;

/**
 * @author $Author: ecerulm $
 * 
 */
public class BerTags {

	private BerTags() {
	} // Prevents instantiation

	public final static byte INTEGER = 0x02;

	public final static byte OCTETSTRING = 0x04;

	// Type Tag number Tag number
	// (decimal) (hexadecimal)
	// INTEGER 2 02
	// BIT STRING 3 03
	// OCTET STRING 4 04
	// NULL 5 05
	// OBJECT IDENTIFIER 6 06
	// SEQUENCE and SEQUENCE OF 16 10
	// SET and SET OF 17 11
	// PrintableString 19 13
	// T61String 20 14
	// IA5String 22 16
	// UTCTime 23 17
	//
	// Table 1. Some types and their universal-class tags.

	/**
	 * <code>TAG_VALUE_HEX_08</code> is 0x08 used to construct other tags.
	 */
	public static final byte TAG_VALUE_00 = 0x00;

	public static final byte TAG_VALUE_01 = 0x01;

	public static final byte TAG_VALUE_02 = 0x02;

	public static final byte TAG_VALUE_03 = 0x03;

	public static final byte TAG_VALUE_04 = 0x04;

	public static final byte TAG_VALUE_05 = 0x05;

	public static final byte TAG_VALUE_06 = 0x06;

	public static final byte TAG_VALUE_07 = 0x07;

	public static final byte TAG_VALUE_08 = 0x08;

	public static final byte TAG_VALUE_09 = 0x09;

	public static final byte TAG_VALUE_10 = 0x0a;

	public static final byte TAG_VALUE_11 = 0x0b;

	public static final byte TAG_VALUE_12 = 0x0c;

	public static final byte TAG_VALUE_13 = 0x0d;

	public static final byte TAG_VALUE_14 = 0x0e;

	public static final byte TAG_VALUE_15 = 0x0f;

	public static final byte TAG_VALUE_16 = 0x10;

	/**
	 * <code>TAG_VALUE_HEX_11</code> is 0x11 used to construct other tags.
	 */
	public static final byte TAG_VALUE_17 = 0x11;

	/**
	 * In the low-tag-number form of encoding identifier octets the class
	 * (universal,application,context-specific) is encoded using bit 8 & 7. For
	 * <code>APPLICATION</code> the tuple is (01) which leads to the 0x40
	 * mask.
	 */
	public static final byte APPLICATION = 0x40;

	/**
	 * In the low-tag-number form of encoding identifier octets the class
	 * (universal,application,context-specific) is encoded using bit 8 & 7. For
	 * <code>CONTEXT_SPECIFIC</code> the tuple is (10) which leads to the 0x80
	 * mask.
	 */
	public static final byte CONTEXT_SPECIFIC = (byte) 0x80;

	/**
	 * In the low-tag-number form of enconding identifier octets the bit 6 is
	 * used to indicate that the encoding is constructed. For
	 * <code>CONSTRUCTED</code> the bit is set to 1 which leads to the 0x20
	 * mask.
	 */
	public static final byte CONSTRUCTED = 0x20;

	/**
	 * In the low-tag-number form of encoding identifier octets the class
	 * (universal,application,context-specific) is encoded using bit 8 & 7. For
	 * <code>UNIVERSAL</code> the tuple is (00) which leads to the 0x00 mask.
	 */
	public static final byte UNIVERSAL = 0x00;

	/**
	 * <code>SEQUENCEOF</code> is a ASN.1 type with an universal tag value of
	 * 0x10.The types with universal tags are defined in X.208, which also gives
	 * the types' universal tag numbers.
	 */
	public static final byte SEQUENCEOF = (UNIVERSAL | CONSTRUCTED)
			+ TAG_VALUE_16;

	/**
	 * <code>SETOF</code> is a ASN.1 type with an universal tag value of
	 * 0x11.The types with universal tags are defined in X.208, which also gives
	 * the types' universal tag numbers.
	 */
	public static final byte SETOF = (UNIVERSAL | CONSTRUCTED) + TAG_VALUE_17;

	public static final byte ENUMERATED = (UNIVERSAL) + TAG_VALUE_10;

	public static final byte APPLICATION_0 = (APPLICATION | CONSTRUCTED)
			+ TAG_VALUE_00;

	public static final byte APPLICATION_1 = (APPLICATION | CONSTRUCTED)
			+ TAG_VALUE_01;

	public static final byte APPLICATION_2 = (APPLICATION) + TAG_VALUE_02;

	public static final byte APPLICATION_3 = (APPLICATION | CONSTRUCTED)
			+ TAG_VALUE_03;

	public static final byte APPLICATION_4 = (APPLICATION | CONSTRUCTED)
			+ TAG_VALUE_04;

	public static final byte APPLICATION_5 = (APPLICATION | CONSTRUCTED)
			+ TAG_VALUE_05;

	public static final byte APPLICATION_6 = (APPLICATION | CONSTRUCTED)
			+ TAG_VALUE_06;

	public static final byte APPLICATION_7 = (APPLICATION | CONSTRUCTED)
			+ TAG_VALUE_07;

	/**
	 * <code>APPLICATION_8</code> is an application tag specific to LDAP
	 * application. In LDAPv3 it used for AddRequest
	 */
	public static final byte APPLICATION_8 = (APPLICATION | CONSTRUCTED)
			+ TAG_VALUE_08;

	public static final byte APPLICATION_9 = (APPLICATION | CONSTRUCTED)
			+ TAG_VALUE_09;

	public static final byte APPLICATION_10 = (APPLICATION) + TAG_VALUE_10;

	public static final byte APPLICATION_11 = (APPLICATION) + TAG_VALUE_11;

	public static final byte APPLICATION_12 = (APPLICATION | CONSTRUCTED)
			+ TAG_VALUE_12;

	public static final byte APPLICATION_14 = (APPLICATION | CONSTRUCTED)
			+ TAG_VALUE_14;

	public static final byte APPLICATION_16 = (APPLICATION) + TAG_VALUE_16;

	public static final byte CHOICE_0 = (CONTEXT_SPECIFIC | TAG_VALUE_00);

	public static final byte CHOICE_1 = (CONTEXT_SPECIFIC | TAG_VALUE_01);

	public static final byte CHOICE_2 = (CONTEXT_SPECIFIC | TAG_VALUE_02);

	public static final byte CHOICE_3 = (CONTEXT_SPECIFIC | TAG_VALUE_03);

	public static final byte CHOICE_4 = (CONTEXT_SPECIFIC | TAG_VALUE_04);

	public static final byte CHOICE_5 = (CONTEXT_SPECIFIC | TAG_VALUE_05);

	public static final byte CHOICE_6 = (CONTEXT_SPECIFIC | TAG_VALUE_06);

	public static final byte CHOICE_7 = (CONTEXT_SPECIFIC | TAG_VALUE_07);

	public static final byte CHOICE_8 = (CONTEXT_SPECIFIC | TAG_VALUE_08);

	public static final byte CHOICE_9 = (CONTEXT_SPECIFIC | TAG_VALUE_09);

}