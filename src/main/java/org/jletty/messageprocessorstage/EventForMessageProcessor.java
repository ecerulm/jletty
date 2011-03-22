/*
 * Created on 23-oct-2004
 *
 */
package org.jletty.messageprocessorstage;

import org.jletty.framework.Event;

/**
 * @author Ruben
 * 
 */
public interface EventForMessageProcessor extends Event {
	void visit(MessageProcessor s);
}
