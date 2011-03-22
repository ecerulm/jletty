/*
 * Created on 12-mar-2005
 *
 */
package org.jletty.schema;

/**
 * @author Ruben
 * 
 */
public class AttributeTypeNameNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public AttributeTypeNameNotFoundException() {
		super();
	}

	/**
	 * @param message
	 */
	public AttributeTypeNameNotFoundException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AttributeTypeNameNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public AttributeTypeNameNotFoundException(Throwable cause) {
		super(cause);
	}
}
