/*
 * Created on 23-oct-2004
 *
 */
package org.jletty.messageprocessorstage;

import org.jletty.framework.MaintenanceEvent;

/**
 * @author Ruben
 * 
 */
public interface MessageProcessor {

	void process(LdapMessageEvent event);

	void process(MaintenanceEvent event);
}
