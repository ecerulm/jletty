/*
 * Created on 11-mar-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jletty.schema;

/**
 * @author Ruben
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class MatchingRuleNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public MatchingRuleNotFoundException() {
		super();
	}

	/**
	 * @param message
	 */
	public MatchingRuleNotFoundException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MatchingRuleNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public MatchingRuleNotFoundException(Throwable cause) {
		super(cause);
	}
}
