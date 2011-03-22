/*
 * Created on 11-oct-2004
 *
 */
package org.jletty.tcpserverstage;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jletty.framework.BaseStage;
import org.jletty.framework.Event;
import org.jletty.framework.MaintenanceEvent;
import org.jletty.framework.Stage;
import org.jletty.ldapparsingstage.EventForLdapListener;
import org.jletty.ldapparsingstage.LdapListener;
import org.jletty.ldapparsingstage.TcpReadEvent;

/**
 * @author Ruben
 * 
 */
public class TcpServerStage extends BaseStage implements Stage,
		TcpServerEventHandler {
	/**
	 * @author Ruben
	 * 
	 */
	public static class LocalEvent implements Event, EventForLdapListener,
			TcpReadEvent {

		private Map data = new HashMap();

		private ByteBuffer buffer;

		public LocalEvent(ByteBuffer buffer, SocketChannel conn) {
			data.put("connection", new WeakReference(conn));
			this.buffer = buffer;

		}

		public Map getData() {
			return data;
		}

		public void visit(LdapListener stage) {
			stage.process(this);
		}

		public ByteBuffer getBuffer() {
			return buffer;
		}

		public SocketChannel getSocketChannel() {
			return (SocketChannel) ((WeakReference) (data.get("connection")))
					.get();
		}

	}

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger("TCPSERVER");

	// TODO implement proper close of this channel when closing this stage
	private ServerSocketChannel ssc;

	private Stage nextStage;

	private Selector selector;

	public TcpServerStage(int port, Stage nextStage) {
		this.nextStage = nextStage;
		try {
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
			ServerSocket ss = ssc.socket();
			InetSocketAddress address = new InetSocketAddress(port);
			ss.bind(address);
			this.ssc = ssc;
			logger.debug("Starting mainLoop()");
			selector = Selector.open();
			ssc.register(selector, SelectionKey.OP_ACCEPT);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public void enqueue(Object event) {
		// Check connections one time
		logger.debug("Entering enqueue");
		EventForTcpServer e = (EventForTcpServer) event;
		e.visit(this);
		logger.debug("Exiting enqueue");
	}

	public void process(MaintenanceEvent e) {
		logger.debug("Maintenance process");

	}

	public void stop() {
		super.stop();
		try {
			ssc.close();
		} catch (IOException e) {
			logger.error("Could not close the ServerSocketChannel properly", e);
		}
	}

	public void process(PollEvent e) {
		// TODO get rid of PollEvent let mainLoop run forever until a stop event
		// is received. We can create a new kind of stage that is always runnin
		mainLoop();
	}

	protected void mainLoop() {
		logger.debug("Enter mainLoop");
		try {
			int num = selector.select();
			if (num < 1) {
				return;
			}
			Set selectedKeys = selector.selectedKeys();
			Iterator it = selectedKeys.iterator();
			while (it.hasNext()) {
				SelectionKey key = (SelectionKey) it.next();
				logger.debug("Iteration over SelectionKey " + key);
				// ... deal with I/O event ...

				try {
					if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
						// Accept the new connection

						ServerSocketChannel ssc = (ServerSocketChannel) key
								.channel();
						SocketChannel sc = ssc.accept();
						sc.configureBlocking(false);
						sc.register(selector, SelectionKey.OP_READ);

						if (logger.isDebugEnabled()) {
							logger
									.debug("mainLoop() - Got a new connection : sc = "
											+ sc);
						}
					} else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
						// Read the data
						logger.debug("Read event " + key);
						SocketChannel sc = (SocketChannel) key.channel();

						// TODO direct buffers
						ByteBuffer echoBuffer = ByteBuffer.allocate(512);
						echoBuffer.clear();

						try {
							int r = sc.read(echoBuffer);
							if (r == -1) {
								sc.close();
							} else {
								echoBuffer.flip();
								nextStage
										.enqueue(new LocalEvent(echoBuffer, sc));
							}
						} catch (IOException e) {
							sc.close();
						}
					}

				} catch (CancelledKeyException e) {
					// ignore
				}

				it.remove();

			}
		} catch (IOException e) {
			// FIXME handle appropiatelly
			throw new RuntimeException(e);
		}
		logger.debug("Exit mainLoop()");

	}
}
