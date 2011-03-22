/*
 * Created on 12-nov-2004
 *
 */
package org.jletty.db.experimental;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jletty.util.NotImplementedException;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;


/**
 * @author Ruben
 * 
 */
public class DatabaseImpl implements Database {

	Map entries = new ConcurrentHashMap();

	/**
	 * Add an entry to the database
	 * 
	 * @throws EntryAlreadyExistsException
	 *             if an entry with the same (canonicalized) dn already exists
	 * @throws NoParentException
	 *             if the parent entry doesn't exists
	 */
	public void addEntry(Entry entry) {
		String dn = entry.getDn();
		String canonicalDn = dn.toUpperCase();
		if (entries.get(canonicalDn) != null) {
			// the entry already exists
			throw new EntryAlreadyExistsException();
		}
		CharSequence inputStr = canonicalDn;
		String patternStr = "[^\\\\],(.*)"; // Everything after the first non
		// escaped ","

		// Compile and use regular expression
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(inputStr);
		boolean matchFound = matcher.find();

		if (matchFound) {
			String parentDn = matcher.group(1);
			if (entries.get(parentDn) == null) {
				throw new NoParentException();
			}
		}

		// add the entry
		entries.put(canonicalDn, entry);
	}

	public Entry readEntry(final String dn) {
		if (dn == null) {
			throw new IllegalArgumentException("dn cannot be null");
		}

		String canonicalDn = dn.toUpperCase();

		return (Entry) entries.get(canonicalDn);
	}

	public boolean deleteEntry(String dn) {
		if (dn == null) {
			throw new IllegalArgumentException("dn cannot be null");
		}

		String canonicalDn = dn.toUpperCase();

		Object tmp = entries.remove(canonicalDn);
		return (tmp != null) ? true : false;
	}

	public Set search(String baseDn, Filter f) {
		Collection entriesValues = entries.values();
		Set resultSet = new HashSet();
		for (Iterator iter = entriesValues.iterator(); iter.hasNext();) {
			Entry entry = (Entry) iter.next();
			if (f.match(entry) && entry.getDn().endsWith(baseDn)) {
				resultSet.add(entry);
			}
		}
		return resultSet;
	}

	public void updateEntry(Entry entry, String oldDn) {
		throw new NotImplementedException();
	}

	public void updateEntry(Entry entry) {
		String canonicalDn = getCanonicalDn(entry);

		entries.put(canonicalDn, entry);

	}

	private String getCanonicalDn(Entry entry) {
		String dn = entry.getDn();
		String canonicalDn = dn.toUpperCase();
		return canonicalDn;

	}

}
