package org.jletty.db.jndiprovider;

import java.util.Enumeration;

import javax.naming.InvalidNameException;
import javax.naming.Name;

import junit.framework.TestCase;
import junitx.framework.Assert;

import org.jletty.dn.DistinguishedName;
import org.jletty.dn.Rfc2253NameParserImpl;

public class DistinguishedNameTest extends TestCase {

	public void testSize() throws Exception {
		DistinguishedName name = new DistinguishedName();
		assertEquals(0, name.size());

		name.add("cn=new");
		assertEquals(1, name.size());

		name.add("o=organization");
		assertEquals(2, name.size());

	}

	public void testIsEmpty() throws InvalidNameException {
		DistinguishedName name = new DistinguishedName();
		assertTrue(name.isEmpty());

		name.add("cn=new");
		assertFalse(name.isEmpty());

		name.remove(0);
		assertTrue(name.isEmpty());
	}

	/*
	 * Class under test for Object clone()
	 */
	public void testClone() throws Exception {
		DistinguishedName name = new DistinguishedName();
		DistinguishedName clonedName = (DistinguishedName) name.clone();
		assertNotSame(name, clonedName);
		assertEquals(name, clonedName);
		clonedName.add("cn=new");
		Assert.assertNotEquals(name, clonedName);
		assertEquals(0, name.size());
		assertEquals(1, clonedName.size());

		name = new DistinguishedName();
		name.add("cn=a");
		clonedName = (DistinguishedName) name.clone();
		assertEquals(name, clonedName);
		clonedName.add("cn=new");
		Assert.assertNotEquals(name, clonedName);
		assertEquals(1, name.size());
		assertEquals(2, clonedName.size());
	}

	public void testRemove() throws Exception {
		DistinguishedName name = new DistinguishedName();
		name.add("cn=a");
		String removedString = (String) name.remove(0);
		assertEquals("cn=a", removedString);
		assertEquals(0, name.size());
		assertTrue(name.isEmpty());
	}

	public void testCompareTo() throws Exception {
		DistinguishedName name1 = new DistinguishedName();
		DistinguishedName name2 = new DistinguishedName();

		assertEquals(0, name1.compareTo(name2));

		//
		name1.add("c=a");
		name2.add("c=b");
		assertEquals(-1, name1.compareTo(name2));
		assertEquals(1, name2.compareTo(name1));

		//		
		name1 = new DistinguishedName();
		name2 = new DistinguishedName();
		name1.add("c=a");
		name1.add("c=a");
		name2.add("c=a");
		name2.add("c=b");
		assertEquals(-1, name1.compareTo(name2));
		assertEquals(1, name2.compareTo(name1));

		//		
		name1 = new DistinguishedName();
		name2 = new DistinguishedName();
		name1.add("c=a");
		name1.add("c=a");
		name2.add("c=a");
		name2.add("c=a");
		assertEquals(0, name1.compareTo(name2));

		//		
		name1 = new DistinguishedName();
		name2 = new DistinguishedName();
		name1.add("c=a");
		name1.add("c=a");
		name2.add("c=a");
		name2.add("c=a");
		name2.add("c=a");
		assertEquals(-1, name1.compareTo(name2));

		try {
			name1.compareTo("c");
			fail();
		} catch (ClassCastException e) {
			// ignore
		}

		try {
			name1.compareTo(null);
			fail();
		} catch (ClassCastException e) {
			// ignore
		}

	}

	public void testGet() throws Exception {
		DistinguishedName name = new DistinguishedName();
		name.add("c=a");
		name.add("c=b");
		name.add("c=c");
		assertEquals("c=a", name.get(0));
		assertEquals("c=b", name.get(1));
		assertEquals("c=c", name.get(2));

	}

	public void testGetAll() throws Exception {
		DistinguishedName name = new DistinguishedName();
		name.add("c=a");
		name.add("c=b");
		name.add("c=c");
		Enumeration all = name.getAll();
		assertTrue(all.hasMoreElements());
		assertEquals("c=a", all.nextElement());
		assertTrue(all.hasMoreElements());
		assertEquals("c=b", all.nextElement());
		assertTrue(all.hasMoreElements());
		assertEquals("c=c", all.nextElement());
	}

	public void testGetPrefix() throws Exception {
		DistinguishedName name = new DistinguishedName();
		name.add("c=a");
		name.add("c=b");
		name.add("c=c");
		Name name2 = name.getPrefix(2);
		assertEquals(2, name2.size());
		assertEquals("c=a", name2.get(0));
		assertEquals("c=b", name2.get(1));

		DistinguishedName name3 = (DistinguishedName) name.clone();
		DistinguishedName name4 = (DistinguishedName) name2.clone();
		assertEquals(name, name3);
		assertEquals(name2, name4);

		// changes in the original doesn't affect the prefix
		name.remove(0);
		assertEquals(name2, name4);

	}

	public void testGetSuffix() throws Exception {
		DistinguishedName name = new DistinguishedName();
		name.add("c=a");
		name.add("c=b");
		name.add("c=c");
		Name name2 = name.getSuffix(1);
		assertEquals(2, name2.size());
		assertEquals("c=b", name2.get(0));
		assertEquals("c=c", name2.get(1));

		DistinguishedName name3 = (DistinguishedName) name.clone();
		DistinguishedName name4 = (DistinguishedName) name2.clone();
		assertEquals(name, name3);
		assertEquals(name2, name4);

		// changes in the original doesn't affect the prefix
		name.remove(0);
		assertEquals(name2, name4);
	}

	public void testEndsWith() throws Exception {
		DistinguishedName name = new DistinguishedName();
		name.add("c=a");
		name.add("c=b");
		name.add("c=c");
		assertTrue(name.endsWith(new DistinguishedName().add("c=c")));
	}

	public void testStartsWith() throws Exception {
		DistinguishedName name = new DistinguishedName();
		name.add("c=a");
		name.add("c=b");
		name.add("c=c");
		assertTrue(name.startsWith(new DistinguishedName().add("c=a")
				.add("c=b")));
	}

	/*
	 * Class under test for Name add(int, String)
	 */
	public void testAddintString() throws Exception {
		DistinguishedName name = new DistinguishedName();
		Name name2 = name.add(0, "c=a");
		assertSame(name, name2);
		assertEquals(name, name2);
		assertEquals(1, name.size());
		assertEquals("c=a", name.get(0));

		name2 = name.add(0, "c=b");
		assertSame(name, name2);
		assertEquals(name, name2);
		assertEquals(2, name.size());
		assertEquals("c=b", name.get(0));
		assertEquals("c=a", name.get(1));

	}

	/*
	 * Class under test for Name add(String)
	 */
	public void testAddString() throws Exception {
		DistinguishedName name = new DistinguishedName();
		Name name2 = name.add("c=a");
		assertSame(name, name2);
		assertEquals(name, name2);
		assertEquals(1, name.size());
		assertEquals("c=a", name.get(0));
	}

	/*
	 * Class under test for Name addAll(int, Name)
	 */
	public void testAddAllintName() throws Exception {
		DistinguishedName name = new DistinguishedName();
		// DistinguishedName tmp = new DistinguishedName(new String[] { "c=b",
		// "c=a" });
		Name tmp = new Rfc2253NameParserImpl().parse("c=b,c=a");
		Name name2 = name.addAll(0, tmp);
		assertSame(name, name2);
		assertEquals(name, name2);
		assertEquals(2, name.size());
		assertEquals("c=a", name.get(0));
		assertEquals("c=b", name.get(1));

		tmp = new Rfc2253NameParserImpl().parse("c=d,c=c");
		name2 = name.addAll(0, tmp);
		assertSame(name, name2);
		assertEquals(name, name2);
		assertEquals(4, name.size());
		assertEquals("c=c", name.get(0));
		assertEquals("c=d", name.get(1));
		assertEquals("c=a", name.get(2));
		assertEquals("c=b", name.get(3));

	}

	/*
	 * Class under test for Name addAll(Name)
	 */
	public void testAddAllName() throws Exception {
		DistinguishedName name = new DistinguishedName();
		// DistinguishedName tmp = new DistinguishedName(new String[] { "c=b",
		// "c=a" });
		Name tmp = new Rfc2253NameParserImpl().parse("c=b,c=a");
		Name name2 = name.addAll(tmp);
		assertSame(name, name2);
		assertEquals(name, name2);
		assertEquals(2, name.size());
		assertEquals("c=a", name.get(0));
		assertEquals("c=b", name.get(1));

		tmp = new Rfc2253NameParserImpl().parse("c=d,c=c");
		name2 = name.addAll(tmp);
		assertSame(name, name2);
		assertEquals(name, name2);
		assertEquals(4, name.size());
		assertEquals("c=a", name.get(0));
		assertEquals("c=b", name.get(1));
		assertEquals("c=c", name.get(2));
		assertEquals("c=d", name.get(3));
	}
	
	public void testToString() throws Exception {
		DistinguishedName name = new DistinguishedName();
		name.add("o=organization");
		name.add("cn=new");
		assertEquals("cn=new,o=organization",name.toString());

	}

}
