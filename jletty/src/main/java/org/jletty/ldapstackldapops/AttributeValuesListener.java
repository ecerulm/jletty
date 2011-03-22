/*
 * $Header: /usr/local/cvsroot/jletty-new/src/org/jletty/ldapstackldapops/AttributeValuesListener.java,v 1.1 2006/02/12 19:22:12 ecerulm Exp $
 * Created on May 14, 2004
 *
 */
package org.jletty.ldapstackldapops;

/**
 * @author $Author: ecerulm $
 * 
 * 
 */
public interface AttributeValuesListener {
	public void data(byte[][] values);
}