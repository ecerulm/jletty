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
public interface Database {
	public void addEntry(Entry entry);

	/**
	 * Return the entry with the given dn
	 * 
	 * @param dn
	 * @return the entry or <code>null</code> if the entry doesn't exists
	 */
	public Entry readEntry(String dn);

	public void updateEntry(Entry entry, String oldDn);

	public void updateEntry(Entry entry);

	public boolean deleteEntry(String dn);

	public Set search(String baseDn, Filter f);
}
