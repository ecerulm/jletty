/*
 * Created on 12-nov-2004
 *
 */
package org.jletty.db.experimental;

/**
 * @author Ruben
 * 
 */

// TODO change to checked exception
public class NoParentException extends RuntimeException {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3257854259679539509L;

	/**
	 * 
	 */
	public NoParentException() {
		super();
	}

	/**
	 * @param message
	 */
	public NoParentException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public NoParentException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NoParentException(String message, Throwable cause) {
		super(message, cause);
	}

}
