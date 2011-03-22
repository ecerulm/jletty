/*
 * Created on 21-feb-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

/**
 * @author $Author $
 * 
 */
public class ParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param e
	 */
	public ParserException(Throwable e) {
		super(e);
	}

	/**
	 * @param string
	 */
	public ParserException(String string) {
		super(string);
	}

	/**
	 * @param string
	 * @param e
	 */
	public ParserException(String string, ParserException e) {
		super(string, e);
	}

}