/*
 * Created on 23-oct-2004
 *
 */
package org.jletty.ldapparsingstage;

import org.jletty.framework.MaintenanceEvent;

/**
 * @author Ruben
 * 
 */
public interface LdapListener {
	void process(TcpReadEvent readEvent);

	void process(MaintenanceEvent event);
}
