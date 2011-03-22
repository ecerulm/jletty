package org.jletty.serverexample;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jletty.framework.MultiThreadStage;
import org.jletty.framework.Stage;
import org.jletty.ldapparsingstage.LdapListenerStage;
import org.jletty.messageprocessorstage.MessageProcessorStage;
import org.jletty.schema.Schema;
import org.jletty.tcpserverstage.TcpServerPollingThread;
import org.jletty.tcpserverstage.TcpServerStage;

/*
 * Created on 11-oct-2004
 *
 */

/**
 * @author Ruben
 * 
 */
public final class TestServer {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TestServer.class);

	public static void main(String[] args) throws Exception {
		new TestServer().doMain();
	}

	private void doMain() throws Exception {
		Properties props = new Properties();
		props.load(getClass().getResourceAsStream("log4j.properties"));
		// props.put("log4j.threshold","OFF");
		PropertyConfigurator.configure(props);
		Schema.getInstance().loadSchemaFromResource(
				"/org/jletty/server/core.schema");
		Stage processorStage = new MultiThreadStage(new MessageProcessorStage());
		Stage ldapListener = new MultiThreadStage(new LdapListenerStage(
				processorStage));
		final Stage tcpServerStage = new TcpServerStage(389, ldapListener);

		ThreadGroup group = new ThreadGroup("TcpServerThreadGroup") {
			public void uncaughtException(Thread t, Throwable e) {
				logger.error(t.getName(), e);
				logger.fatal("Exiting due to ", e);
				System.exit(1);
			}
		};
		new TcpServerPollingThread(group, "TcpServerThread", tcpServerStage, 0)
				.start();
		new MaintenanceEventsThread(group, "MaintenanceEventsThread",
				new Stage[] { processorStage, ldapListener, tcpServerStage },
				10000).start();
		System.out.println("Press enter to stop");
		logger.debug("Press enter to stop");
		System.in.read();
		System.out.println("Stopped");
		System.exit(0);
	}
}