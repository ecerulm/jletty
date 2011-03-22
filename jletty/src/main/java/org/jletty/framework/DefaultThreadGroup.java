/*
 * Created on 03-ene-2005
 *
 */
package org.jletty.framework;

import org.apache.log4j.Logger;

class DefaultThreadGroup extends ThreadGroup {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(DefaultThreadGroup.class);

	public DefaultThreadGroup(ThreadGroup parent, String name) {
		super(parent, name);
	}

	/**
	 * @param name
	 */
	public DefaultThreadGroup(String name) {
		super(name);
	}

	public void uncaughtException(Thread t, Throwable e) {
		logger.error(t.getName(), e);
		logger.fatal("Exiting due to ", e);
		System.exit(1);
	}

}