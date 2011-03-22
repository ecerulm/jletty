/*
 * Created on 09-ene-2005
 *
 */
package org.jletty.db.schema.individualObjectclasses;

import java.util.Arrays;
import java.util.List;

import org.jletty.db.schema.SchemaTestCase;
import org.jletty.schema.ObjectClass;
import org.jletty.schema.ObjectClassType;
import org.jletty.util.Log4jTestsConfigurator;

/**
 * @author Ruben
 * 
 */
public class ObjectclassesSchemaParserTest extends SchemaTestCase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		Log4jTestsConfigurator.configure();
	}

	// ObjectClassDescription = "(" whsp
	// numericoid whsp ; ObjectClass identifier
	// [ "NAME" qdescrs ]
	// [ "DESC" qdstring ]
	// [ "OBSOLETE" whsp ]
	// [ "SUP" oids ] ; Superior ObjectClasses
	// [ ( "ABSTRACT" / "STRUCTURAL" / "AUXILIARY" ) whsp ]
	// ; default structural
	// [ "MUST" oids ] ; AttributeTypes
	// [ "MAY" oids ] ; AttributeTypes
	// whsp ")"

	public void testAlias() throws Exception {
		// objectclass ( 2.5.6.1 NAME 'alias'
		// DESC 'RFC2256: an alias'
		// SUP top STRUCTURAL
		// MUST aliasedObjectName )
		ObjectClass ob = parseObjectClass("aliasObjectclass.txt");

		assertNotNull(ob);
		assertEquals("2.5.6.1", ob.getNumericoid());
		final List names = Arrays.asList(ob.getNames());
		assertEquals(1, names.size());
		assertTrue(names.contains("alias"));
		assertEquals("RFC2256: an alias", ob.getDesc());
		assertFalse(ob.isObsolete());
		final List superClasses = Arrays.asList(ob.getSuperClasses());
		assertEquals(1, superClasses.size());
		assertTrue(superClasses.contains("top"));
		assertEquals(ObjectClassType.STRUCTURAL, ob.getType());
		final List mustAttrs = Arrays.asList(ob.getMustAttrs());
		assertEquals(1, mustAttrs.size());
		assertTrue(mustAttrs.contains("aliasedObjectName"));
		checkObjectclassInSchema(ob, names);

	}

	public void testObjectclassWithSeveralSups() throws Exception {
		// objectclass ( 2.5.6.1 NAME 'severalSups'
		// DESC 'An objectclass with several parents'
		// SUP (top $ other $ another) STRUCTURAL
		// MUST aliasedObjectName )

		ObjectClass ob = parseObjectClass("severalSups.txt");

		assertNotNull(ob);
		assertEquals("2.5.6.1", ob.getNumericoid());
		final List names = Arrays.asList(ob.getNames());
		assertEquals(1, names.size());
		assertTrue(names.contains("severalSups"));
		assertEquals("An objectclass with several parents", ob.getDesc());
		assertFalse(ob.isObsolete());
		final List superClasses = Arrays.asList(ob.getSuperClasses());
		assertEquals(3, superClasses.size());
		assertTrue(superClasses.contains("top"));
		assertTrue(superClasses.contains("other"));
		assertTrue(superClasses.contains("another"));

		assertEquals(ObjectClassType.STRUCTURAL, ob.getType());
		final List mustAttrs = Arrays.asList(ob.getMustAttrs());
		assertEquals(1, mustAttrs.size());
		assertTrue(mustAttrs.contains("aliasedObjectName"));
		checkObjectclassInSchema(ob, names);

	}

	public void testObjectclassDefaultAbstract() throws Exception {
		// objectclass ( 2.5.6.1 NAME 'severalSups'
		// DESC 'An objectclass with several parents'
		// SUP (top $ other $ another)
		// MUST aliasedObjectName )
		ObjectClass ob = parseObjectClass("defaultAbstract.txt");

		assertNotNull(ob);
		assertEquals("2.5.6.1", ob.getNumericoid());
		final List names = Arrays.asList(ob.getNames());
		assertEquals(1, names.size());
		assertTrue(names.contains("severalSups"));
		assertEquals("An objectclass with several parents", ob.getDesc());
		assertFalse(ob.isObsolete());
		final List superClasses = Arrays.asList(ob.getSuperClasses());
		assertEquals(3, superClasses.size());
		assertTrue(superClasses.contains("top"));
		assertTrue(superClasses.contains("other"));
		assertTrue(superClasses.contains("another"));

		assertEquals(ObjectClassType.ABSTRACT, ob.getType());
		final List mustAttrs = Arrays.asList(ob.getMustAttrs());
		assertEquals(1, mustAttrs.size());
		assertTrue(mustAttrs.contains("aliasedObjectName"));
		checkObjectclassInSchema(ob, names);
	}

	public void testObjectclassAbstract() throws Exception {
		// objectclass ( 2.5.6.1 NAME 'severalSups'
		// DESC 'An objectclass with several parents'
		// SUP (top $ other $ another) ABSTRACT
		// MUST aliasedObjectName )
		ObjectClass ob = parseObjectClass("abstract.txt");

		assertNotNull(ob);
		assertEquals("2.5.6.1", ob.getNumericoid());
		final List names = Arrays.asList(ob.getNames());
		assertEquals(1, names.size());
		assertTrue(names.contains("severalSups"));
		assertEquals("An objectclass with several parents", ob.getDesc());
		assertFalse(ob.isObsolete());
		final List superClasses = Arrays.asList(ob.getSuperClasses());
		assertEquals(3, superClasses.size());
		assertTrue(superClasses.contains("top"));
		assertTrue(superClasses.contains("other"));
		assertTrue(superClasses.contains("another"));

		assertEquals(ObjectClassType.ABSTRACT, ob.getType());
		final List mustAttrs = Arrays.asList(ob.getMustAttrs());
		assertEquals(1, mustAttrs.size());
		assertTrue(mustAttrs.contains("aliasedObjectName"));
		checkObjectclassInSchema(ob, names);

	}

	public void testObjectclassAuxiliary() throws Exception {
		// objectclass ( 2.5.6.1 NAME 'severalSups'
		// DESC 'An objectclass with several parents'
		// SUP (top $ other $ another) AUXILIARY
		// MUST aliasedObjectName )
		ObjectClass ob = parseObjectClass("auxiliary.txt");

		assertNotNull(ob);
		assertEquals("2.5.6.1", ob.getNumericoid());
		final List names = Arrays.asList(ob.getNames());
		assertEquals(1, names.size());
		assertTrue(names.contains("severalSups"));
		assertEquals("An objectclass with several parents", ob.getDesc());
		assertFalse(ob.isObsolete());
		final List superClasses = Arrays.asList(ob.getSuperClasses());
		assertEquals(3, superClasses.size());
		assertTrue(superClasses.contains("top"));
		assertTrue(superClasses.contains("other"));
		assertTrue(superClasses.contains("another"));

		assertEquals(ObjectClassType.AUXILIARY, ob.getType());
		final List mustAttrs = Arrays.asList(ob.getMustAttrs());
		assertEquals(1, mustAttrs.size());
		assertTrue(mustAttrs.contains("aliasedObjectName"));
		checkObjectclassInSchema(ob, names);
	}

	public void testObjectclassWithMay() throws Exception {
		// objectclass ( 2.5.6.1 NAME 'severalSups'
		// DESC 'An objectclass with several parents'
		// SUP (top $ other $ another) AUXILIARY
		// MAY aliasedObjectName )

		ObjectClass ob = parseObjectClass("may1.txt");

		assertNotNull(ob);
		assertEquals("2.5.6.1", ob.getNumericoid());
		final List names = Arrays.asList(ob.getNames());
		assertEquals(1, names.size());
		assertTrue(names.contains("severalSups"));
		assertEquals("An objectclass with several parents", ob.getDesc());
		assertFalse(ob.isObsolete());
		final List superClasses = Arrays.asList(ob.getSuperClasses());
		assertEquals(3, superClasses.size());
		assertTrue(superClasses.contains("top"));
		assertTrue(superClasses.contains("other"));
		assertTrue(superClasses.contains("another"));

		assertEquals(ObjectClassType.AUXILIARY, ob.getType());
		final List mayAttrs = Arrays.asList(ob.getMayAttrs());
		assertEquals(1, mayAttrs.size());
		assertTrue(mayAttrs.contains("aliasedObjectName"));
		checkObjectclassInSchema(ob, names);
	}

	public void testObjectclassWithMay2() throws Exception {
		// objectclass ( 2.5.6.1 NAME 'severalSups'
		// DESC 'An objectclass with several parents'
		// SUP (top $ other $ another) AUXILIARY
		// MAY ( aliasedObjectName $ aliasedObjectName2 $ aliasedObjectName3 ) )
		ObjectClass ob = parseObjectClass("may2.txt");

		assertNotNull(ob);
		assertEquals("2.5.6.1", ob.getNumericoid());
		final List names = Arrays.asList(ob.getNames());
		assertEquals(1, names.size());
		assertTrue(names.contains("severalSups"));
		assertEquals("An objectclass with several parents", ob.getDesc());
		assertFalse(ob.isObsolete());
		final List superClasses = Arrays.asList(ob.getSuperClasses());
		assertEquals(3, superClasses.size());
		assertTrue(superClasses.contains("top"));
		assertTrue(superClasses.contains("other"));
		assertTrue(superClasses.contains("another"));

		assertEquals(ObjectClassType.AUXILIARY, ob.getType());
		final List mayAttrs = Arrays.asList(ob.getMayAttrs());
		assertEquals(3, mayAttrs.size());
		assertTrue(mayAttrs.contains("aliasedObjectName"));
		assertTrue(mayAttrs.contains("aliasedObjectName2"));
		assertTrue(mayAttrs.contains("aliasedObjectName3"));
		checkObjectclassInSchema(ob, names);
	}

	public void testObjectclassWithMust() throws Exception {
		// objectclass ( 2.5.6.1 NAME 'severalSups'
		// DESC 'An objectclass with several parents'
		// SUP (top $ other $ another) AUXILIARY
		// MUST ( aliasedObjectName $ aliasedObjectName2 $ aliasedObjectName3 )
		// )
		ObjectClass ob = parseObjectClass("must.txt");

		assertNotNull(ob);
		assertEquals("2.5.6.1", ob.getNumericoid());
		final List names = Arrays.asList(ob.getNames());
		assertEquals(1, names.size());
		assertTrue(names.contains("severalSups"));
		assertEquals("An objectclass with several parents", ob.getDesc());
		assertFalse(ob.isObsolete());
		final List superClasses = Arrays.asList(ob.getSuperClasses());
		assertEquals(3, superClasses.size());
		assertTrue(superClasses.contains("top"));
		assertTrue(superClasses.contains("other"));
		assertTrue(superClasses.contains("another"));

		assertEquals(ObjectClassType.AUXILIARY, ob.getType());
		final List mustAttrs = Arrays.asList(ob.getMustAttrs());
		assertEquals(3, mustAttrs.size());
		assertTrue(mustAttrs.contains("aliasedObjectName"));
		assertTrue(mustAttrs.contains("aliasedObjectName2"));
		assertTrue(mustAttrs.contains("aliasedObjectName3"));
		checkObjectclassInSchema(ob, names);
	}
}
