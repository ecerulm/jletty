/*
 * Created on 12-nov-2004
 *
 */
package org.jletty.db.experimental;

/**
 * @author Ruben
 * 
 */
public interface Filter {
	boolean match(Entry e);
}
