/*
 * $Id: FilterListener.java,v 1.1 2006/02/12 19:22:12 ecerulm Exp $
 * Created on Jun 25, 2004
 */
package org.jletty.ldapstackldapops;

/**
 * @author $Author: ecerulm $
 * 
 */
public interface FilterListener {
	public void data(Filter f);
}