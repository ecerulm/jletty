/*
 * Created on 17-oct-2004
 *
 */
package org.jletty.ldapstackldapops;

/**
 * @author Ruben
 * 
 */
public class SearchResultDone extends LDAPResult implements LDAPResponse {

	public SearchResultDone(LDAPResultCode result, String matchedDN,
			String errMsg) {
		super(result, matchedDN, errMsg);
	}

	/**
	 * @param success
	 */
	public SearchResultDone(LDAPResultCode result) {
		super(result);
	}

	protected byte getTag() {
		return BerTags.APPLICATION_5;
	}
}
