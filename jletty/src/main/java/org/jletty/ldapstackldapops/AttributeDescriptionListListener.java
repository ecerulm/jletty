/*
 * $Id: AttributeDescriptionListListener.java,v 1.1 2006/02/12 19:22:12 ecerulm Exp $
 * Created on Jun 16, 2004
 */
package org.jletty.ldapstackldapops;

import java.util.List;

/**
 * @author $Author: ecerulm $
 * 
 */
public interface AttributeDescriptionListListener {
	public void data(List value);
}