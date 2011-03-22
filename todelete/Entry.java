/*
 * Created on 12-nov-2004
 *
 */
package org.jletty.db.experimental;

import java.util.Set;

/**
 * @author Ruben
 * 
 */
public interface Entry {
	String getDn();

	Set getAttributeNames();
}
