/*
 * Created on 11-oct-2004
 *
 */
package org.jletty.ldapparsingstage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jletty.framework.BaseStage;
import org.jletty.framework.MaintenanceEvent;
import org.jletty.framework.Stage;
import org.jletty.ldapstackldapops.LDAPMessage;
import org.jletty.ldapstackldapops.LDAPMessageListener;
import org.jletty.ldapstackldapops.LDAPResponse;
import org.jletty.ldapstackparsersimplementation.LDAPMessageParser;
import org.jletty.ldapstackparsersimplementation.Parser;
import org.jletty.messageprocessorstage.EventForMessageProcessor;
import org.jletty.messageprocessorstage.LdapMessageEvent;
import org.jletty.messageprocessorstage.MessageProcessor;
import org.jletty.messageprocessorstage.ResponseHandler;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ruben
 * 
 */
public class LdapListenerStage extends BaseStage implements Stage, LdapListener {
	/**
	 * @author Ruben
	 * 
	 */
	public static final class MessageEvent implements EventForMessageProcessor,
			LdapMessageEvent {

		private LDAPMessage req;

		private ResponseHandler responseHandler;

		MessageEvent(LDAPMessage req, ResponseHandler handler) {
			this.req = req;
			this.responseHandler = handler;
		}

		public void visit(MessageProcessor s) {
			s.process(this);
		}

		public LDAPMessage getMessage() {
			return req;
		}

		public ResponseHandler getResponseHandler() {
			return responseHandler;
		}
	}

	private static final class ResponseHandlerImpl implements ResponseHandler {

		private int msgid;

		private SocketChannel sc;

		ResponseHandlerImpl(int msgid, SocketChannel sc) {
			this.msgid = msgid;
			this.sc = sc;
		}

		public void sendResponse(LDAPResponse response) {
			ByteBuffer buffer = ByteBuffer
					.wrap(new LDAPMessage(msgid, response).getBytes());

			try {
				sc.write(buffer);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

	}

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(LdapListenerStage.class);

	private Map parsers = new ConcurrentHashMap();

	private Stage nextStage;

	/**
	 * @param printStage
	 */
	public LdapListenerStage(Stage nextStage) {
		this.nextStage = nextStage;
	}

	public void enqueue(Object ev) {
		logger.debug("Entering enqueue event = " + ev);
		EventForLdapListener event = (EventForLdapListener) ev;
		event.visit(this);
		logger.debug("Exiting enqueue");
	}

	public void process(final TcpReadEvent readEvent) {

		ByteBuffer buffer = readEvent.getBuffer();
		Parser parser;

		final SocketChannel sc = readEvent.getSocketChannel();
		if (sc == null)
			return;
		parser = (Parser) parsers.get(sc);
		if (parser == null) {
			LDAPMessageListener l = new LDAPMessageListener() {
				public void data(final LDAPMessage req) {
					ResponseHandler handler = new ResponseHandlerImpl(req
							.get_msgid(), sc);

					MessageEvent event = new MessageEvent(req, handler);
					nextStage.enqueue(event);
				}
			};

			parser = new LDAPMessageParser(l);
			parsers.put(sc, parser);
			logger.debug("New parser created! (" + parsers.size()
					+ ") current parsers");
		}

		parser.parse(buffer);
	}

	public void process(MaintenanceEvent event) {
		logger.debug("Maintenance event (" + event + ")");
		for (Iterator it = parsers.keySet().iterator(); it.hasNext();) {
			SocketChannel sc = (SocketChannel) it.next();
			if (!sc.isOpen()) {
				logger.debug("Removing parser for connection " + sc);
				parsers.remove(sc);
			}
		}
		logger.info("Active parsers after cleanup: " + parsers.size());

	}
}
