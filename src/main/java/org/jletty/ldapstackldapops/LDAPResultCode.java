/*
 * Created on 16-abr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.jletty.ldapstackldapops;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;
import org.jletty.encoder.BerEncodeable;
import org.jletty.encoder.BerEncoderBase;

/**
 * @author rlm
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class LDAPResultCode extends Enum implements BerEncodeable {

	private static final long serialVersionUID = 1L;

	public static final LDAPResultCode INVALID_CREDENTIALS = new LDAPResultCode(
			"invalid credentials", 49);

	private int _id;

	public static final LDAPResultCode SUCCESS = new LDAPResultCode("success",
			0);

	public static final LDAPResultCode OPERATIONSERROR = new LDAPResultCode(
			"operationsError", 1);

	public static final LDAPResultCode PROTOCOLERROR = new LDAPResultCode(
			"protocolError", 2);

	public static final LDAPResultCode TIME_LIMIT_EXCEEDED = new LDAPResultCode(
			"timeLimitExceeded", 3);

	public static final LDAPResultCode SIZELIMITEXCEEDED = new LDAPResultCode(
			"sizeLimitExceeded", 4);

	public static final LDAPResultCode COMPARE_FALSE = new LDAPResultCode(
			"compareFalse", 5);

	public static final LDAPResultCode COMPARE_TRUE = new LDAPResultCode(
			"compareTrue", 6);

	public static final LDAPResultCode AUTHMETHODNOTSUPPORTED = new LDAPResultCode(
			"authMethodNotSupported", 7);

	public static final LDAPResultCode STRONGAUTHREQUIRED = new LDAPResultCode(
			"strongAuthRequired", 8);

	// Ldap result code 9 -- reserved

	public static final LDAPResultCode REFERRAL = new LDAPResultCode(
			"referral", 10);

	public static final LDAPResultCode ADMINLIMITEXCEEDED = new LDAPResultCode(
			"adminLimitExceeded", 11);

	public static final LDAPResultCode UNAVAILABLECRITICALEXTENSION = new LDAPResultCode(
			"unavailableCriticalExtension", 12);

	public static final LDAPResultCode CONFIDENTIALITYREQUIRED = new LDAPResultCode(
			"confidentialityRequired", 13);

	public static final LDAPResultCode SASLBINDINPROGRESS = new LDAPResultCode(
			"saslBindInProgress", 14);

	// ldap result code 15 -- not defined

	public static final LDAPResultCode NO_SUCH_ATTR = new LDAPResultCode(
			"noSuchAttribute", 16);

	public static final LDAPResultCode UNDEF_ATTR_TYPE = new LDAPResultCode(
			"undefinedAttributeType", 17);

	public static final LDAPResultCode INAPPROPIATE_MATCHING = new LDAPResultCode(
			"inappropiateMatching", 18);

	public static final LDAPResultCode CONSTRAINT_VIOLATION = new LDAPResultCode(
			"constraintViolation", 19);

	public static final LDAPResultCode ATTR_OR_VALUE_EXISTS = new LDAPResultCode(
			"attributeOrValueExists", 20);

	public static final LDAPResultCode INVALID_ATTR_SYNTAX = new LDAPResultCode(
			"invalidAttributeSyntax", 21);

	// ldap result codes 22 - 31 unused

	public static final LDAPResultCode NO_SUCH_OBJECT = new LDAPResultCode(
			"noSuchObject", 32);

	public static final LDAPResultCode ALIAS_PROBLEM = new LDAPResultCode(
			"aliasProblem", 33);

	public static final LDAPResultCode INVALID_DN_SYNTAX = new LDAPResultCode(
			"invalidDnSyntax", 34);

	// -- 35 reserved for undefined isLeaf --

	public static final LDAPResultCode ALIAS_DEREF_PROBLEM = new LDAPResultCode(
			"aliasDereferencingProblem", 36);

	// -- 37-47 unused --
	public static final LDAPResultCode INAPPROPRIATEAUTHENTICATION = new LDAPResultCode(
			"inappropriateAuthentication", 48);

	public static final LDAPResultCode INVALIDCREDENTIALS = new LDAPResultCode(
			"invalidCredentials", 49);

	public static final LDAPResultCode INSUFFICIENTACCESSRIGHTS = new LDAPResultCode(
			"insufficientAccessRights", 50);

	public static final LDAPResultCode BUSY = new LDAPResultCode("busy", 51);

	public static final LDAPResultCode UNAVAILABLE = new LDAPResultCode(
			"unavailable", 52);

	public static final LDAPResultCode UNWILLINGTOPERFORM = new LDAPResultCode(
			"unwillingToPerform", 53);

	public static final LDAPResultCode LOOPDETECT = new LDAPResultCode(
			"loopDetect", 54);

	// -- 55-63 unused --

	public static final LDAPResultCode NAMINGVIOLATION = new LDAPResultCode(
			"namingViolation", 64);

	public static final LDAPResultCode OBJECTCLASS_VIOLATION = new LDAPResultCode(
			"objectClassViolation", 65);

	public static final LDAPResultCode NOT_ALLOWED_ON_NON_LEAF = new LDAPResultCode(
			"notAllowedOnNonLeaf", 66);

	public static final LDAPResultCode NOT_ALLOWED_ON_RDN = new LDAPResultCode(
			"notAllowedOnRdn", 67);

	public static final LDAPResultCode ENTRY_ALREADY_EXISTS = new LDAPResultCode(
			"entryAlreadyExists", 68);

	public static final LDAPResultCode OBJECTCLASSMODSPROHIBITED = new LDAPResultCode(
			"objectClassModsProhibited", 69);

	// -- 70 reserved for CLDAP --

	public static final LDAPResultCode AFFECTSMULTIPLEDSAS = new LDAPResultCode(
			"affectsMultipleDSAs", 71);

	// -- 72-79 unused --

	public static final LDAPResultCode OTHER = new LDAPResultCode("other", 80);

	// -- 81-90 reserved for APIs --

	private LDAPResultCode(String show, int id) {
		super(show);
		this._id = id;
	}

	public static LDAPResultCode getEnum(String show) {
		return (LDAPResultCode) getEnum(LDAPResultCode.class, show);
	}

	public static Map getEnumMap() {
		return getEnumMap(LDAPResultCode.class);
	}

	public static List getEnumList() {
		return getEnumList(LDAPResultCode.class);
	}

	public static Iterator iterator() {
		return iterator(LDAPResultCode.class);
	}

	/**
	 * @return
	 */
	public byte[] getBytes() {

		byte[] toBytes = new BerEncoderBase().berInteger(BerTags.ENUMERATED,
				this._id);
		return toBytes;
	}

}