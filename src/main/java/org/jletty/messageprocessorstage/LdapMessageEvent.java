/*
 * Created on 23-oct-2004
 *
 */
package org.jletty.messageprocessorstage;

import org.jletty.framework.Event;
import org.jletty.ldapstackldapops.LDAPMessage;


/**
 * @author Ruben
 * 
 */
// TODO eliminate this interface
public interface LdapMessageEvent extends Event {
	LDAPMessage getMessage();

	ResponseHandler getResponseHandler();
}
