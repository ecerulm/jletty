/*
 * Created on 23-oct-2004
 *
 */
package org.jletty.ldapparsingstage;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.jletty.framework.Event;


/**
 * @author Ruben
 * 
 */
public interface TcpReadEvent extends Event {
	ByteBuffer getBuffer();

	public SocketChannel getSocketChannel();
}
