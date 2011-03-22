/*
 * $Id: AttributeTypeAndValuesListListener.java,v 1.1 2006/02/12 19:22:12 ecerulm Exp $ Created on May 22, 2004
 * 
 */
package org.jletty.ldapstackldapops;

/**
 * Interface to intended to be used as Anonymous inner classes passed to parser
 * classes. the parser will send the results of the parser to the listener by
 * the <code>data</code> method
 * 
 * @author $Author: ecerulm $
 */
public interface AttributeTypeAndValuesListListener {

	/**
	 * The parser will call data with the results of the parsing
	 * 
	 * @param value
	 *            the results of the parsing
	 */
	public void data(AttributeTypeAndValuesList value);
}