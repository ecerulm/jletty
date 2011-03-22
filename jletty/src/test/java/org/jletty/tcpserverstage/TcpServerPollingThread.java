package org.jletty.tcpserverstage;

import org.jletty.framework.Stage;

/*
 * Created on 23-oct-2004
 *
 */

public final class TcpServerPollingThread extends Thread {
	private final Stage tcpServerStage;

	private int sleepingPeriod;

	private class Event implements PollEvent, EventForTcpServer {
		public void visit(TcpServerEventHandler eh) {
			eh.process(this);
		}
	}

	public TcpServerPollingThread(ThreadGroup group, String name,
			Stage tcpServerStage) {
		this(group, name, tcpServerStage, 0);
	}

	public TcpServerPollingThread(ThreadGroup group, String name,
			Stage tcpServerStage, int sleepingPeriod) {
		super(group, name);
		this.tcpServerStage = tcpServerStage;
		this.sleepingPeriod = sleepingPeriod;
	}

	public void run() {
		Object emptyObject = new Event() {
		};
		while (true) {
			tcpServerStage.enqueue(emptyObject);
			 try {
				sleep(this.sleepingPeriod);
			} catch (InterruptedException e) {
				// ignore
			}
		}
	}
}