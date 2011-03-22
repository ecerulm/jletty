/*
 * Created on 11-oct-2004
 *
 */
package org.jletty.framework;

/**
 * @author Ruben
 * 
 */
public interface Stage {
	void enqueue(Object event);

	void start();

	void stop();
}
