/*
 * Created on 12-nov-2004
 *
 */
package org.jletty.db.experimental;

/**
 * @author Ruben
 * 
 */
public class EntryAlreadyExistsException extends RuntimeException {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3545517292075890480L;

	/**
	 * 
	 */
	public EntryAlreadyExistsException() {
		super();

	}

	/**
	 * @param message
	 */
	public EntryAlreadyExistsException(String message) {
		super(message);

	}

	/**
	 * @param cause
	 */
	public EntryAlreadyExistsException(Throwable cause) {
		super(cause);

	}

	/**
	 * @param message
	 * @param cause
	 */
	public EntryAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);

	}

}
