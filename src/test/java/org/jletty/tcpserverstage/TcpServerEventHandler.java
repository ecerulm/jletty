/*
 * Created on 24-oct-2004
 *
 */
package org.jletty.tcpserverstage;

import org.jletty.framework.MaintenanceEvent;

/**
 * @author Ruben
 * 
 */
public interface TcpServerEventHandler {
	void process(MaintenanceEvent e);

	void process(PollEvent e);
}
