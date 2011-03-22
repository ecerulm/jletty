/*
 * Created on 03-nov-2004
 *
 */
package org.jletty.messageprocessorstage;

import org.jletty.ldapstackldapops.LDAPResponse;

/**
 * @author Ruben
 * 
 */

// TODO Move this to another package (protocol?)
public interface ResponseHandler {	

	void sendResponse(LDAPResponse response);

}
