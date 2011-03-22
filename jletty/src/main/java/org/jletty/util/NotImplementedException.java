/*
 * $Id: NotImplementedException.java,v 1.1 2006/02/12 19:22:21 ecerulm Exp $
 * Created on May 14, 2004
 *
 */
package org.jletty.util;

/**
 * @author $Author: ecerulm $
 * 
 * 
 */
public class NotImplementedException extends
		org.apache.commons.lang.NotImplementedException {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	public NotImplementedException() {
		super("Not implemented yet");
	}

	/**
	 * @param arg0
	 */
	public NotImplementedException(String arg0) {
		super(arg0);
	}
}