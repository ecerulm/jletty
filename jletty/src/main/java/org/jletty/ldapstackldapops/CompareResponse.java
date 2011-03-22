/*
 * Created on 11-mar-2005
 *
 */
package org.jletty.ldapstackldapops;

/**
 * @author Ruben
 * 
 */
public class CompareResponse extends LDAPResult implements LDAPResponse {

	/**
	 * @param result
	 */
	public CompareResponse(LDAPResultCode result) {
		super(result);
	}

	/**
	 * @param result
	 * @param matchedDN
	 * @param errMsg
	 */
	public CompareResponse(LDAPResultCode result, String matchedDN,
			String errMsg) {
		super(result, matchedDN, errMsg);
		// if (!LDAPResultCode.COMPARE_TRUE.equals(result)
		// || LDAPResultCode.COMPARE_FALSE.equals(result)) {
		// throw new RuntimeException(
		// "CompareResponse must be compareTrue or compareFalse but was " +
		// result);
		// }
	}
}
