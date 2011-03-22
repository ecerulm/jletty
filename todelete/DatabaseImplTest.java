/*
 * Created on 12-nov-2004
 *
 */
package org.jletty.db.experimental;

import java.util.HashSet;
import java.util.Set;

import org.jletty.db.experimental.Attribute;
import org.jletty.db.experimental.Database;
import org.jletty.db.experimental.DatabaseImpl;
import org.jletty.db.experimental.Entry;
import org.jletty.db.experimental.EntryAlreadyExistsException;
import org.jletty.db.experimental.Filter;
import org.jletty.db.experimental.NoParentException;

import junit.framework.TestCase;

/**
 * @author Ruben
 * 
 */
public class DatabaseImplTest extends TestCase {

	/**
	 * @author Ruben
	 * 
	 */
	public class FilterPresent implements Filter {

		private String type;

		public FilterPresent(String type) {
			this.type = type;

		}

		public boolean match(Entry e) {
			if (e.getAttributeNames().contains(type)) {
				return true;
			}
			return false;
		}

	}

	private class AttributeImpl implements Attribute {
		private String type;

		private byte[][] vals = null;

		AttributeImpl(String type, String value) {
			this.type = type;
			vals = new byte[][] { value.getBytes() };
		}

		public String getName() {
			return type;
		}

	}

	private class EntryImpl implements Entry {
		private String dn;

		private Attribute[] attrs;

		EntryImpl(String dn) {
			this(dn, null);
		}

		EntryImpl(String dn, Attribute[] attrs) {
			this.dn = dn;
			this.attrs = attrs;
		}

		public String getDn() {
			return dn;
		}

		public Set getAttributeNames() {
			Set attrNames = new HashSet();
			for (int i = 0; i < attrs.length; i++) {
				Attribute attr = attrs[i];
				attrNames.add(attr.getName());
			}
			return attrNames;
		}

		public String toString() {
			return dn;
		}
	}

	private Database db;

	protected void setUp() throws Exception {
		db = new DatabaseImpl();
	}

	public void testAddEntry() {
		Entry entry = new EntryImpl("cn=myEntry");
		db.addEntry(entry);
		Entry result = db.readEntry("cn=myEntry");
		assertNotNull("The entry should be in the database", result);
		assertEquals(entry, result);
	}

	public void testAddSameEntryTwice() {
		Entry entry = new EntryImpl("cn=myEntry");
		db.addEntry(entry);
		Entry sameEntry = new EntryImpl("cn=myEntry");
		try {
			db.addEntry(sameEntry);
			fail("It should fail the entry already in the database");
		} catch (EntryAlreadyExistsException e) {
			// ignore
		}
	}

	public void testAddParentDoesntExists() {

		Entry entry = new EntryImpl("cn=myEntry,c=ES");
		try {
			db.addEntry(entry);
			fail("It should fail bacause parent doesn't exists yet");
		} catch (NoParentException e) {
			assertNull(db.readEntry("cn=myEntry,c=ES"));// ignore
		}
	}

	public void testAddWithParentEntry() {
		Entry parentEntry = new EntryImpl("c=ES");
		Entry entry = new EntryImpl("cn=myEntry,c=ES");
		db.addEntry(parentEntry);
		db.addEntry(entry);

		Entry resultParentEntry = db.readEntry("c=ES");
		assertNotNull(resultParentEntry);
		assertEquals(parentEntry, resultParentEntry);

		Entry resultEntry = db.readEntry("cn=myEntry,c=ES");
		assertNotNull(resultEntry);
		assertEquals(entry, resultEntry);
	}

	public void testSearch() {
		Attribute[] attrs = new Attribute[] {
				new AttributeImpl("cn", "My Entry"),
				new AttributeImpl("objectclass", "top") };
		EntryImpl entry = new EntryImpl("cn=My Entry", attrs);
		db.addEntry(entry);
		Filter filter = new FilterPresent("objectclass");
		Set entries = db.search("", filter);

		assertTrue("The result set must contain the entry", entries
				.contains(entry));
	}

	public void testSearchNoMatch() {
		EntryImpl entry = new EntryImpl("cn=myEntry");
		db.addEntry(entry);
		Filter filter = new Filter() {
			public boolean match(Entry e) {
				return false; // never match
			}
		};
		Set matchingEntries = db.search("", filter);
		assertTrue("The result set must be empty", matchingEntries.isEmpty());
	}

	public void testSearchMultipleMatch() {
		Entry entry1 = new EntryImpl("cn=MyEntry1");
		Entry entry2 = new EntryImpl("cn=MyEntry2");
		db.addEntry(entry1);
		db.addEntry(entry2);
		Filter f = new Filter() {
			public boolean match(Entry e) {
				return true; // always match
			}
		};
		Set matchingEntries = db.search("", f);

		assertTrue(matchingEntries.contains(entry1));
		assertTrue(matchingEntries.contains(entry2));
	}

	public void testSearchBaseDn() {
		db.addEntry(new EntryImpl("c=US"));
		db.addEntry(new EntryImpl("o=org1,c=US"));
		db.addEntry(new EntryImpl("o=org2,c=US"));
		EntryImpl entry1 = new EntryImpl("cn=MyEntry1,o=org1,c=US");
		db.addEntry(entry1);
		EntryImpl entry2 = new EntryImpl("cn=MyEntry2,o=org2,c=US");
		db.addEntry(entry2);

		Filter f = new Filter() {
			public boolean match(Entry e) {
				return true; // always match
			}
		};
		assertEquals("Search result must contain all entries", 5, db.search("",
				f).size());
		assertEquals(
				"Search result must contains all entries in branch o=org1,c=US",
				2, db.search("o=org1,c=US", f).size());
		assertEquals(
				"Search result must contains all entries in branch o=org2,c=US",
				2, db.search("o=org2,c=US", f).size());
		assertTrue("Must match entry1", db.search("cn=MyEntry1,o=org1,c=US", f)
				.contains(entry1));
		assertTrue("Must match entry2", db.search("cn=MyEntry2,o=org2,c=US", f)
				.contains(entry2));

	}

	public void testSearchBaseDnNoMatch() {
		db.addEntry(new EntryImpl("c=US"));
		db.addEntry(new EntryImpl("o=org1,c=US"));
		db.addEntry(new EntryImpl("o=org2,c=US"));
		EntryImpl entry1 = new EntryImpl("cn=MyEntry1,o=org1,c=US");
		db.addEntry(entry1);
		EntryImpl entry2 = new EntryImpl("cn=MyEntry2,o=org2,c=US");
		db.addEntry(entry2);

		Filter f = new Filter() {
			public boolean match(Entry e) {
				return true; // always match
			}
		};
		assertTrue(
				"Search result must be empty because there is no entry below c=ES",
				db.search("c=ES", f).isEmpty());
	}

	public void testDeleteExistingEntry() {
		testAddEntry();
		assertTrue("db.delete should return true", db.deleteEntry("cn=myEntry"));
		Entry result = db.readEntry("cn=myEntry");
		assertNull("The entry should be removed from database", result);

	}

	public void testDeleteNotExistingEntry() {
		assertFalse("db.delete should return false", db
				.deleteEntry("cn=myEntry"));
	}

//	public void testDeleteNonLeaf() {
//		fail("not impl");
//	}
//
//	public void testUpdateEntry() {
//
//		fail("not impl");
//	}
//
//	public void testModifyParent() {
//		fail("not impl");
//	}

//	public void testModifyNonExistingEntry() {
//		fail("Not impl");
//	}

}
