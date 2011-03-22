/*
 * $Id: AttributeTypeAndValuesListener.java,v 1.1 2006/02/12 19:22:12 ecerulm Exp $
 * Created on May 20, 2004
 *
 */
package org.jletty.ldapstackldapops;

/**
 * @author $Author: ecerulm $
 * 
 */
public interface AttributeTypeAndValuesListener {
	public void data(AttributeTypeAndValues value);
}