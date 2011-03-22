/*
 * Created on 16-abr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.jletty.ldapstackldapops;

/**
 * @author rlm
 * 
 */
public class BindResponse extends LDAPResult implements LDAPResponse {
	public BindResponse(LDAPResultCode result) {
		super(result);
	}

	public BindResponse(LDAPResultCode result, String matchedDN, String errMsg) {
		super(result, matchedDN, errMsg);
	}

	protected byte getTag() {
		return BerTags.APPLICATION_1;
	}
}