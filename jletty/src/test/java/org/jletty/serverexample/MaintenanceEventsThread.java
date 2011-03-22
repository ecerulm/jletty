/*
 * Created on 23-oct-2004
 *
 */

package org.jletty.serverexample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jletty.framework.MaintenanceEvent;
import org.jletty.framework.Stage;
import org.jletty.ldapparsingstage.EventForLdapListener;
import org.jletty.ldapparsingstage.LdapListener;
import org.jletty.messageprocessorstage.EventForMessageProcessor;
import org.jletty.messageprocessorstage.MessageProcessor;
import org.jletty.tcpserverstage.EventForTcpServer;
import org.jletty.tcpserverstage.TcpServerEventHandler;

/**
 * @author Ruben
 * 
 */
public class MaintenanceEventsThread extends Thread {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(MaintenanceEventsThread.class);

	public final static class Event implements MaintenanceEvent, EventForLdapListener,
			EventForMessageProcessor, EventForTcpServer {

		private int i;

		Event(int i) {
			this.i = i;
		}

		public void visit(LdapListener stage) {
			stage.process(this);
		}

		public void visit(MessageProcessor s) {
			s.process(this);
		}

		public Map getData() {
			return null;
		}

		public void visit(TcpServerEventHandler eh) {
			eh.process(this);

		}

		public String toString() {
			return "Maintenance event id=" + i;
		}

	}

	private List stages;

	private long period;

	public MaintenanceEventsThread(ThreadGroup group, String name,
			Stage[] stages) {
		this(group, name, stages, 1000);
	}

	public MaintenanceEventsThread(ThreadGroup group, String name,
			Stage[] stages, long period) {
		super(group, name);
		this.stages = new ArrayList(Arrays.asList(stages));
		this.period = period;
	}

	public void run() {
		int i = 0;
		while (true) {
			for (Iterator it = stages.iterator(); it.hasNext();) {
				Stage stage = (Stage) it.next();
				Object maintenanceEvent = new Event(i);
				stage.enqueue(maintenanceEvent);
				logger.debug("Sent maintenance event (" + i + ")to stage "
						+ stage);
				i++;
			}
			try {
				logger.debug("Going to sleep for " + period + " ms");
				sleep(period);
			} catch (InterruptedException e) {
			}
		}
	}
}
