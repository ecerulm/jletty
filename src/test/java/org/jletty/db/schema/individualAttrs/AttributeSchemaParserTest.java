/*
 * Created on 09-ene-2005
 *
 */
package org.jletty.db.schema.individualAttrs;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;


import org.jletty.db.schema.SchemaTestCase;
import org.jletty.schema.AttributeType;
import org.jletty.schema.AttributeUsage;
import org.jletty.schemaparser.SchemaLexer;
import org.jletty.schemaparser.SchemaParser;

import antlr.Token;

/**
 * @author Ruben
 * 
 */
public class AttributeSchemaParserTest extends SchemaTestCase {

	public void testLexer() throws Exception {
		InputStream is = getClass().getResourceAsStream("postOfficeBox.txt");

		// create filter lexer
		SchemaLexer mainLexer = new SchemaLexer(is);
		while (true) {
			Token token = mainLexer.nextToken();
//			System.out.println("Token:" + token.getType() + " "
//					+ token.getText());
			if (token.getText() == null)
				break;

		}
	}

	public void testObjectClass() throws Exception {
		// # system schema
		// attributetype ( 2.5.4.0 NAME 'objectClass'
		// DESC 'RFC2256: object classes of the entity'
		// EQUALITY objectIdentifierMatch
		// SYNTAX 1.3.6.1.4.1.1466.115.121.1.38 )
		AttributeType at = parseAttribute("objectClass.txt");

		assertNotNull(at);
		assertEquals("2.5.4.0", at.getNumericoid());
		final List names = Arrays.asList(at.getNames());
		assertEquals(1, names.size());
		assertTrue(names.contains("objectClass"));
		assertEquals("RFC2256: object classes of the entity", at.getDesc());
		assertFalse(at.isObsolete());
		assertEquals("", at.getSuperAttr());
		assertEquals("objectIdentifierMatch", at.getEqMatchRule());
		assertEquals("", at.getSubMatchRule());
		assertEquals("", at.getOrdMatchRule());
		assertEquals("1.3.6.1.4.1.1466.115.121.1.38", at.getSyntax());
		assertFalse(at.isSingleValue());
		assertFalse(at.isCollective());
		assertFalse(at.isNonUserModificable());
		assertEquals(AttributeUsage.USER_APPLICATIONS, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);

	}

	public void testAliasedObjectName() throws Exception {
		// # system schema
		// #attributetype ( 2.5.4.1 NAME ( 'aliasedObjectName'
		// 'aliasedEntryName' )
		// # DESC 'RFC2256: name of aliased object'
		// # EQUALITY distinguishedNameMatch
		// # SYNTAX 1.3.6.1.4.1.1466.115.121.1.12 SINGLE-VALUE )

		AttributeType at = parseAttribute("aliasedObjectName.txt");

		assertNotNull(at);
		assertEquals("2.5.4.1", at.getNumericoid());
		final List names = at.getNamesAsList();
		assertEquals(2, names.size());
		assertTrue(names.contains("aliasedObjectName"));
		assertTrue(names.contains("aliasedEntryName"));
		assertEquals("RFC2256: name of aliased object", at.getDesc());
		assertFalse(at.isObsolete());
		assertEquals("", at.getSuperAttr());
		assertEquals("distinguishedNameMatch", at.getEqMatchRule());
		assertEquals("", at.getSubMatchRule());
		assertEquals("", at.getOrdMatchRule());
		assertEquals("1.3.6.1.4.1.1466.115.121.1.12", at.getSyntax());
		assertTrue(at.isSingleValue());
		assertFalse(at.isCollective());
		assertFalse(at.isNonUserModificable());
		assertEquals(AttributeUsage.USER_APPLICATIONS, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);
	}

	public void testKnowledgeInformation() throws Exception {
		// attributetype ( 2.5.4.2 NAME 'knowledgeInformation'
		// DESC 'RFC2256: knowledge information'
		// EQUALITY caseIgnoreMatch
		// SYNTAX 1.3.6.1.4.1.1466.115.121.1.15{32768} )
		AttributeType at = parseAttribute("knowledgeInformation.txt");

		assertNotNull(at);
		assertEquals("2.5.4.2", at.getNumericoid());
		final List names = at.getNamesAsList();
		assertEquals(1, names.size());
		assertTrue(names.contains("knowledgeInformation"));
		assertEquals("RFC2256: knowledge information", at.getDesc());
		assertFalse(at.isObsolete());
		assertEquals("", at.getSuperAttr());
		assertEquals("caseIgnoreMatch", at.getEqMatchRule());
		assertEquals("", at.getSubMatchRule());
		assertEquals("", at.getOrdMatchRule());
		assertEquals("1.3.6.1.4.1.1466.115.121.1.15", at.getSyntax());
		assertEquals(32768,at.getLength());
		assertFalse(at.isSingleValue());
		assertFalse(at.isCollective());
		assertFalse(at.isNonUserModificable());
		assertEquals(AttributeUsage.USER_APPLICATIONS, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);
	}

	public void testCommonName() throws Exception {
		// # system schema
		// #attributetype ( 2.5.4.3 NAME ( 'cn' 'commonName' )
		// # DESC 'RFC2256: common name(s) for which the entity is known by'
		// # SUP name )

		AttributeType at = parseAttribute("commonName.txt");

		assertNotNull("It cannot parse CommonName attributetype", at);

		assertEquals("2.5.4.3", at.getNumericoid());
		final List names = at.getNamesAsList();
		assertEquals(2, names.size());
		assertTrue(names.contains("cn"));
		assertTrue(names.contains("commonName"));
		assertFalse(at.isObsolete());
		assertEquals(
				"RFC2256: common name(s) for which the entity is known by", at
						.getDesc());
		assertEquals("name", at.getSuperAttr());
		assertEquals("", at.getEqMatchRule());
		assertEquals("", at.getSubMatchRule());
		assertEquals("", at.getOrdMatchRule());
		assertEquals("", at.getSyntax());
		assertFalse(at.isSingleValue());
		assertFalse(at.isCollective());
		assertFalse(at.isNonUserModificable());
		assertEquals(AttributeUsage.USER_APPLICATIONS, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);

	}

	public void testSurname() throws Exception {
		// attributetype ( 2.5.4.4 NAME ( 'sn' 'surname' )
		// DESC 'RFC2256: last (family) name(s) for which the entity is known
		// by'
		// SUP name )
		AttributeType at = parseAttribute("surname.txt");

		assertNotNull("It cannot parse attributetype", at);
		schema.resolveDependencies();
		assertEquals("2.5.4.4", at.getNumericoid());
		final List names = at.getNamesAsList();
		assertEquals(2, names.size());
		assertTrue(names.contains("sn"));
		assertTrue(names.contains("surname"));
		assertFalse(at.isObsolete());
		assertEquals(
				"RFC2256: last (family) name(s) for which the entity is known by",
				at.getDesc());
		assertEquals("name", at.getSuperAttr());
		assertEquals("", at.getEqMatchRule());
		assertEquals("caseIgnoreSubstringsMatch", at.getSubMatchRule());
		assertEquals("", at.getOrdMatchRule());
		assertEquals("1.3.6.1.4.1.1466.115.121.1.15", at.getSyntax());
		assertEquals(32768, at.getLength());
		assertFalse(at.isSingleValue());
		assertFalse(at.isCollective());
		assertFalse(at.isNonUserModificable());
		assertEquals(AttributeUsage.USER_APPLICATIONS, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);
	}

	public void testSerialNumber() throws Exception {
		// attributetype ( 2.5.4.5 NAME 'serialNumber'
		// DESC 'RFC2256: serial number of the entity'
		// EQUALITY caseIgnoreMatch
		// SUBSTR caseIgnoreSubstringsMatch
		// SYNTAX 1.3.6.1.4.1.1466.115.121.1.44{64} )
		AttributeType at = parseAttribute("serialNumber.txt");

		assertNotNull("It cannot parse attributetype", at);
		assertEquals("2.5.4.5", at.getNumericoid());
		final List names = at.getNamesAsList();
		assertEquals(1, names.size());
		assertTrue(names.contains("serialNumber"));
		assertFalse(at.isObsolete());
		assertEquals("RFC2256: serial number of the entity", at.getDesc());
		assertEquals("", at.getSuperAttr());
		assertEquals("caseIgnoreMatch", at.getEqMatchRule());
		assertEquals("caseIgnoreSubstringsMatch", at.getSubMatchRule());
		assertEquals("", at.getOrdMatchRule());
		assertEquals("1.3.6.1.4.1.1466.115.121.1.44", at.getSyntax());
		assertEquals(64, at.getLength());
		assertFalse(at.isSingleValue());
		assertFalse(at.isCollective());
		assertFalse(at.isNonUserModificable());
		assertEquals(AttributeUsage.USER_APPLICATIONS, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);
	}

	public void testCountryName() throws Exception {
		// attributetype ( 2.5.4.6 NAME ( 'c' 'countryName' )
		// DESC 'RFC2256: ISO-3166 country 2-letter code'
		// SUP name SINGLE-VALUE )
		AttributeType at = parseAttribute("countryName.txt");

		assertNotNull("It cannot parse attributetype", at);
		assertEquals("2.5.4.6", at.getNumericoid());
		final List names = at.getNamesAsList();
		assertEquals(2, names.size());
		assertTrue(names.contains("c"));
		assertTrue(names.contains("countryName"));
		assertFalse(at.isObsolete());
		assertEquals("RFC2256: ISO-3166 country 2-letter code", at.getDesc());
		assertEquals("name", at.getSuperAttr());
		assertEquals("", at.getEqMatchRule());
		assertEquals("", at.getSubMatchRule());
		assertEquals("", at.getOrdMatchRule());
		assertEquals("", at.getSyntax());
		assertTrue(at.isSingleValue());
		assertFalse(at.isCollective());
		assertFalse(at.isNonUserModificable());
		assertEquals(AttributeUsage.USER_APPLICATIONS, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);
	}

	public void testLocalityName() throws Exception {
		// attributetype ( 2.5.4.7 NAME ( 'l' 'localityName' )
		// DESC 'RFC2256: locality which this object resides in'
		// SUP name )
		AttributeType at = parseAttribute("localityName.txt");

		assertNotNull("It cannot parse attributetype", at);
		assertEquals("2.5.4.7", at.getNumericoid());
		final List names = at.getNamesAsList();
		assertEquals(2, names.size());
		assertTrue(names.contains("l"));
		assertTrue(names.contains("localityName"));
		assertFalse(at.isObsolete());
		assertEquals("RFC2256: locality which this object resides in", at
				.getDesc());
		assertEquals("name", at.getSuperAttr());
		assertEquals("", at.getEqMatchRule());
		assertEquals("", at.getSubMatchRule());
		assertEquals("", at.getOrdMatchRule());
		assertEquals("", at.getSyntax());
		assertFalse(at.isSingleValue());
		assertFalse(at.isCollective());
		assertFalse(at.isNonUserModificable());
		assertEquals(AttributeUsage.USER_APPLICATIONS, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);

	}

	public void testStateOrProvinceName() throws Exception {
		// attributetype ( 2.5.4.8 NAME ( 'st' 'stateOrProvinceName' )
		// DESC 'RFC2256: state or province which this object resides in'
		// SUP name )
		AttributeType at = parseAttribute("stateOrProvinceName.txt");

		assertNotNull("It cannot parse attributetype", at);
		assertEquals("2.5.4.8", at.getNumericoid());
		final List names = at.getNamesAsList();
		assertEquals(2, names.size());
		assertTrue(names.contains("st"));
		assertTrue(names.contains("stateOrProvinceName"));
		assertFalse(at.isObsolete());
		assertEquals("RFC2256: state or province which this object resides in",
				at.getDesc());
		assertEquals("name", at.getSuperAttr());
		assertEquals("", at.getEqMatchRule());
		assertEquals("", at.getSubMatchRule());
		assertEquals("", at.getOrdMatchRule());
		assertEquals("", at.getSyntax());
		assertFalse(at.isSingleValue());
		assertFalse(at.isCollective());
		assertFalse(at.isNonUserModificable());
		assertEquals(AttributeUsage.USER_APPLICATIONS, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);

	}

	public void testStreetAddress() throws Exception {
		// attributetype ( 2.5.4.9 NAME ( 'street' 'streetAddress' )
		// DESC 'RFC2256: street address of this object'
		// EQUALITY caseIgnoreMatch
		// SUBSTR caseIgnoreSubstringsMatch
		// SYNTAX 1.3.6.1.4.1.1466.115.121.1.15{128} )
		AttributeType at = parseAttribute("streetAddress.txt");

		assertNotNull("It cannot parse attributetype", at);
		assertEquals("2.5.4.9", at.getNumericoid());
		final List names = at.getNamesAsList();
		assertEquals(2, names.size());
		assertTrue(names.contains("street"));
		assertTrue(names.contains("streetAddress"));
		assertFalse(at.isObsolete());
		assertEquals("RFC2256: street address of this object", at.getDesc());
		assertEquals("", at.getSuperAttr());
		assertEquals("caseIgnoreMatch", at.getEqMatchRule());
		assertEquals("caseIgnoreSubstringsMatch", at.getSubMatchRule());
		assertEquals("", at.getOrdMatchRule());
		assertEquals("1.3.6.1.4.1.1466.115.121.1.15", at.getSyntax());
		assertEquals(128, at.getLength());
		assertFalse(at.isSingleValue());
		assertFalse(at.isCollective());
		assertFalse(at.isNonUserModificable());
		assertEquals(AttributeUsage.USER_APPLICATIONS, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);
	}

	public void testOrganizationName() throws Exception {
		// attributetype ( 2.5.4.10 NAME ( 'o' 'organizationName' )
		// DESC 'RFC2256: organization this object belongs to'
		// SUP name )
		AttributeType at = parseAttribute("organizationName.txt");

		assertNotNull("It cannot parse attributetype", at);
		assertEquals("2.5.4.10", at.getNumericoid());
		final List names = at.getNamesAsList();
		assertEquals(2, names.size());
		assertTrue(names.contains("o"));
		assertTrue(names.contains("organizationName"));
		assertFalse(at.isObsolete());
		assertEquals("RFC2256: organization this object belongs to", at
				.getDesc());
		assertEquals("name", at.getSuperAttr());
		assertEquals("", at.getEqMatchRule());
		assertEquals("", at.getSubMatchRule());
		assertEquals("", at.getOrdMatchRule());
		assertEquals("", at.getSyntax());
		assertFalse(at.isSingleValue());
		assertFalse(at.isCollective());
		assertFalse(at.isNonUserModificable());
		assertEquals(AttributeUsage.USER_APPLICATIONS, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);

	}

	public void testOrganizationalUnitName() throws Exception {
		// attributetype ( 2.5.4.11 NAME ( 'ou' 'organizationalUnitName' )
		// DESC 'RFC2256: organizational unit this object belongs to'
		// SUP name )
		AttributeType at = parseAttribute("organizationalUnitName.txt");

		assertNotNull("It cannot parse attributetype", at);
		assertEquals("2.5.4.11", at.getNumericoid());
		final List names = at.getNamesAsList();
		assertEquals(2, names.size());
		assertTrue(names.contains("ou"));
		assertTrue(names.contains("organizationalUnitName"));
		assertFalse(at.isObsolete());
		assertEquals("RFC2256: organizational unit this object belongs to", at
				.getDesc());
		assertEquals("name", at.getSuperAttr());
		assertEquals("", at.getEqMatchRule());
		assertEquals("", at.getSubMatchRule());
		assertEquals("", at.getOrdMatchRule());
		assertEquals("", at.getSyntax());
		assertFalse(at.isSingleValue());
		assertFalse(at.isCollective());
		assertFalse(at.isNonUserModificable());
		assertEquals(AttributeUsage.USER_APPLICATIONS, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);

	}

	public void testTitle() throws Exception {
		// attributetype ( 2.5.4.12 NAME 'title'
		// DESC 'RFC2256: title associated with the entity'
		// SUP name )

		AttributeType at = parseAttribute("title.txt");

		assertNotNull("It cannot parse attributetype", at);
		assertEquals("2.5.4.12", at.getNumericoid());
		final List names = at.getNamesAsList();
		assertEquals(1, names.size());
		assertTrue(names.contains("title"));
		assertFalse(at.isObsolete());
		assertEquals("RFC2256: title associated with the entity", at.getDesc());
		assertEquals("name", at.getSuperAttr());
		assertEquals("", at.getEqMatchRule());
		assertEquals("", at.getSubMatchRule());
		assertEquals("", at.getOrdMatchRule());
		assertEquals("", at.getSyntax());
		assertFalse(at.isSingleValue());
		assertFalse(at.isCollective());
		assertFalse(at.isNonUserModificable());
		assertEquals(AttributeUsage.USER_APPLICATIONS, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);
	}

	public void testDescription() throws Exception {
		// attributetype ( 2.5.4.13 NAME 'description'
		// DESC 'RFC2256: descriptive information'
		// EQUALITY caseIgnoreMatch
		// SUBSTR caseIgnoreSubstringsMatch
		// SYNTAX 1.3.6.1.4.1.1466.115.121.1.15{1024} )

		AttributeType at = parseAttribute("description.txt");

		assertNotNull("It cannot parse attributetype", at);
		assertEquals("2.5.4.13", at.getNumericoid());
		final List names = at.getNamesAsList();
		assertEquals(1, names.size());
		assertTrue(names.contains("description"));
		assertFalse(at.isObsolete());
		assertEquals("RFC2256: descriptive information", at.getDesc());
		assertEquals("", at.getSuperAttr());
		assertEquals("caseIgnoreMatch", at.getEqMatchRule());
		assertEquals("caseIgnoreSubstringsMatch", at.getSubMatchRule());
		assertEquals("", at.getOrdMatchRule());
		assertEquals("1.3.6.1.4.1.1466.115.121.1.15", at.getSyntax());
		assertEquals(1024, at.getLength());
		assertFalse(at.isSingleValue());
		assertFalse(at.isCollective());
		assertFalse(at.isNonUserModificable());
		assertEquals(AttributeUsage.USER_APPLICATIONS, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);
	}

	public void testSearchGuide() throws Exception {
		// # Obsoleted by enhancedSearchGuide
		// attributetype ( 2.5.4.14 NAME 'searchGuide'
		// DESC 'RFC2256: search guide, obsoleted by enhancedSearchGuide'
		// SYNTAX 1.3.6.1.4.1.1466.115.121.1.25 )

		AttributeType at = parseAttribute("searchGuide.txt");

		assertNotNull("It cannot parse attributetype", at);
		assertEquals("2.5.4.14", at.getNumericoid());
		final List names = at.getNamesAsList();
		assertEquals(1, names.size());
		assertTrue(names.contains("searchGuide"));
		assertFalse(at.isObsolete());
		assertEquals("RFC2256: search guide, obsoleted by enhancedSearchGuide",
				at.getDesc());
		assertEquals("", at.getSuperAttr());
		assertEquals("", at.getEqMatchRule());
		assertEquals("", at.getSubMatchRule());
		assertEquals("", at.getOrdMatchRule());
		assertEquals("1.3.6.1.4.1.1466.115.121.1.25", at.getSyntax());
		assertFalse(at.isSingleValue());
		assertFalse(at.isCollective());
		assertFalse(at.isNonUserModificable());
		assertEquals(AttributeUsage.USER_APPLICATIONS, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);
	}

	public void testDnQualifier() throws Exception {
		// attributetype ( 2.5.4.46 NAME 'dnQualifier'
		// DESC 'RFC2256: DN qualifier'
		// EQUALITY caseIgnoreMatch
		// ORDERING caseIgnoreOrderingMatch
		// SUBSTR caseIgnoreSubstringsMatch
		// SYNTAX 1.3.6.1.4.1.1466.115.121.1.44 )
		AttributeType at = parseAttribute("dnQualifier.txt");

		assertNotNull("It cannot parse attributetype", at);
		assertEquals("2.5.4.46", at.getNumericoid());
		final List names = at.getNamesAsList();
		assertEquals(1, names.size());
		assertTrue(names.contains("dnQualifier"));
		assertFalse(at.isObsolete());
		assertEquals("RFC2256: DN qualifier", at.getDesc());
		assertEquals("", at.getSuperAttr());
		assertEquals("caseIgnoreMatch", at.getEqMatchRule());
		assertEquals("caseIgnoreSubstringsMatch", at.getSubMatchRule());
		assertEquals("caseIgnoreOrderingMatch", at.getOrdMatchRule());
		assertEquals("1.3.6.1.4.1.1466.115.121.1.44", at.getSyntax());
		assertFalse(at.isSingleValue());
		assertFalse(at.isCollective());
		assertFalse(at.isNonUserModificable());
		assertEquals(AttributeUsage.USER_APPLICATIONS, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);

	}

	public void testDistinguishedName() throws Exception {
		// # 2.5.4.49 is defined above as it's used for subtyping
		// attributetype ( 2.5.4.49 NAME 'distinguishedName'
		// EQUALITY distinguishedNameMatch
		// SYNTAX 1.3.6.1.4.1.1466.115.121.1.12 )
		AttributeType at = parseAttribute("distinguishedName.txt");

		assertNotNull("It cannot parse attributetype", at);
		assertEquals("2.5.4.49", at.getNumericoid());
		final List names = at.getNamesAsList();
		assertEquals(1, names.size());
		assertTrue(names.contains("distinguishedName"));
		assertFalse(at.isObsolete());
		assertEquals("", at.getDesc());
		assertEquals("", at.getSuperAttr());
		assertEquals("distinguishedNameMatch", at.getEqMatchRule());
		assertEquals("", at.getSubMatchRule());
		assertEquals("", at.getOrdMatchRule());
		assertEquals("1.3.6.1.4.1.1466.115.121.1.12", at.getSyntax());
		assertFalse(at.isSingleValue());
		assertFalse(at.isCollective());
		assertFalse(at.isNonUserModificable());
		assertEquals(AttributeUsage.USER_APPLICATIONS, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);

	}

	public void testPostOfficeBox() throws Exception {
		// attributetype ( 2.5.4.18 NAME 'postOfficeBox' DESC 'RFC2256: Post
		// Office Box' EQUALITY caseIgnoreMatch SUBSTR
		// caseIgnoreSubstringMatch SYNTAX 1.3.6.1.4.1.1466.115.121.1.15{40}
		// )
		SchemaParser parser = createSchemaParser("postOfficeBox.txt");
		AttributeType at = parser.attributetype(schema);
		assertNotNull(at);
		assertEquals("2.5.4.18", at.getNumericoid());
		final List names = Arrays.asList(at.getNames());
		assertEquals(1, names.size());
		assertTrue(names.contains("postOfficeBox"));
		assertEquals("RFC2256: Post Office Box", at.getDesc());
		assertEquals("caseIgnoreMatch", at.getEqMatchRule());
		assertEquals("caseIgnoreSubstringMatch", at.getSubMatchRule());
		assertEquals("1.3.6.1.4.1.1466.115.121.1.15", at.getSyntax());
		assertEquals(40, at.getLength());
		assertFalse(at.isSingleValue());
		assertFalse(at.isCollective());
		assertFalse(at.isNonUserModificable());
		assertEquals(AttributeUsage.USER_APPLICATIONS, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);
	}

	public void testCorbaIor() throws Exception {
		// attributetype ( 1.3.6.1.4.1.42.2.27.4.1.14
		// NAME 'corbaIor'
		// DESC 'Stringified interoperable object reference of a CORBA object'
		// EQUALITY caseIgnoreIA5Match
		// SYNTAX 1.3.6.1.4.1.1466.115.121.1.26
		// SINGLE-VALUE )
		AttributeType at = parseAttribute("corbaIor.txt");

		assertNotNull("It cannot parse attributetype", at);
		assertEquals("1.3.6.1.4.1.42.2.27.4.1.14", at.getNumericoid());
		final List names = at.getNamesAsList();
		assertEquals(1, names.size());
		assertTrue(names.contains("corbaIor"));
		assertFalse(at.isObsolete());
		assertEquals(
				"Stringified interoperable object reference of a CORBA object",
				at.getDesc());
		assertEquals("", at.getSuperAttr());
		assertEquals("caseIgnoreIA5Match", at.getEqMatchRule());
		assertEquals("", at.getSubMatchRule());
		assertEquals("", at.getOrdMatchRule());
		assertEquals("1.3.6.1.4.1.1466.115.121.1.26", at.getSyntax());
		assertTrue(at.isSingleValue());
		assertFalse(at.isCollective());
		assertFalse(at.isNonUserModificable());
		assertEquals(AttributeUsage.USER_APPLICATIONS, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);
	}

	public void testHomeTelephoneNumber() throws Exception {
		// attributetype ( 0.9.2342.19200300.100.1.20
		// DESC 'RFC1274: home telephone number'
		// NAME ( 'homePhone' 'homeTelephoneNumber' )
		// EQUALITY telephoneNumberMatch
		// SUBSTR telephoneNumberSubstringsMatch
		// SYNTAX 1.3.6.1.4.1.1466.115.121.1.50 )

		AttributeType at = parseAttribute("homeTelephoneNumber.txt");

		assertNotNull("It cannot parse attributetype", at);
		assertEquals("0.9.2342.19200300.100.1.20", at.getNumericoid());
		final List names = at.getNamesAsList();
		assertEquals(2, names.size());
		assertTrue(names.contains("homePhone"));
		assertTrue(names.contains("homeTelephoneNumber"));
		assertFalse(at.isObsolete());
		assertEquals("RFC1274: home telephone number", at.getDesc());
		assertEquals("", at.getSuperAttr());
		assertEquals("telephoneNumberMatch", at.getEqMatchRule());
		assertEquals("telephoneNumberSubstringsMatch", at.getSubMatchRule());
		assertEquals("", at.getOrdMatchRule());
		assertEquals("1.3.6.1.4.1.1466.115.121.1.50", at.getSyntax());
		assertFalse(at.isSingleValue());
		assertFalse(at.isCollective());
		assertFalse(at.isNonUserModificable());
		assertEquals(AttributeUsage.USER_APPLICATIONS, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);
	}

	public void testTitleNoUserModification() throws Exception {
		// attributetype ( 2.5.4.12 NAME 'title'
		// DESC 'RFC2256: title associated with the entity'
		// SUP name )

		AttributeType at = parseAttribute("titleNonModificable.txt");

		assertNotNull("It cannot parse attributetype", at);
		assertEquals("2.5.4.12", at.getNumericoid());
		final List names = at.getNamesAsList();
		assertEquals(1, names.size());
		assertTrue(names.contains("title"));
		assertFalse(at.isObsolete());
		assertEquals("RFC2256: title associated with the entity", at.getDesc());
		assertEquals("name", at.getSuperAttr());
		assertEquals("", at.getEqMatchRule());
		assertEquals("", at.getSubMatchRule());
		assertEquals("", at.getOrdMatchRule());
		assertEquals("", at.getSyntax());
		assertFalse(at.isSingleValue());
		assertFalse(at.isCollective());
		assertTrue(at.isNonUserModificable());
		assertEquals(AttributeUsage.USER_APPLICATIONS, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);
	}

	public void testTitleCollective() throws Exception {
		// attributetype ( 2.5.4.12 NAME 'title'
		// DESC 'RFC2256: title associated with the entity'
		// SUP name )

		AttributeType at = parseAttribute("titleCollective.txt");

		assertNotNull("It cannot parse attributetype", at);
		assertEquals("2.5.4.12", at.getNumericoid());
		final List names = at.getNamesAsList();
		assertEquals(1, names.size());
		assertTrue(names.contains("title"));
		assertFalse(at.isObsolete());
		assertEquals("RFC2256: title associated with the entity", at.getDesc());
		assertEquals("name", at.getSuperAttr());
		assertEquals("", at.getEqMatchRule());
		assertEquals("", at.getSubMatchRule());
		assertEquals("", at.getOrdMatchRule());
		assertEquals("", at.getSyntax());
		assertFalse(at.isSingleValue());
		assertTrue(at.isCollective());
		assertFalse(at.isNonUserModificable());
		assertEquals(AttributeUsage.USER_APPLICATIONS, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);
	}
	public void testLastModifiedTime() throws Exception {
		// # 9.3.19. Last Modified Time
		// #
		// # The Last Modified Time attribute type specifies the last time, in
		// UTC
		// # time, that an entry was modified. Ideally, this attribute should be
		// # maintained by the DSA.
		// #
		// # lastModifiedTime ATTRIBUTE
		// # WITH ATTRIBUTE-SYNTAX
		// # uTCTimeSyntax
		// # ::= {pilotAttributeType 23}
		// #
		// ## OBSOLETE
		// attributetype ( 0.9.2342.19200300.100.1.23 NAME 'lastModifiedTime'
		// DESC 'RFC1274: time of last modify, replaced by modifyTimestamp'
		// OBSOLETE
		// SYNTAX 1.3.6.1.4.1.1466.115.121.1.53
		// USAGE directoryOperation )
		AttributeType at = parseAttribute("lastModifiedTime.txt");

		assertNotNull("It cannot parse attributetype", at);
		assertEquals("0.9.2342.19200300.100.1.23", at.getNumericoid());
		final List names = at.getNamesAsList();
		assertEquals(1, names.size());
		assertTrue(names.contains("lastModifiedTime"));
		assertTrue(at.isObsolete());
		assertEquals(
				"RFC1274: time of last modify, replaced by modifyTimestamp", at
						.getDesc());
		assertEquals("", at.getSuperAttr());
		assertEquals("", at.getEqMatchRule());
		assertEquals("", at.getSubMatchRule());
		assertEquals("", at.getOrdMatchRule());
		assertEquals("1.3.6.1.4.1.1466.115.121.1.53", at.getSyntax());
		assertFalse(at.isSingleValue());
		assertFalse(at.isCollective());
		assertFalse(at.isNonUserModificable());
		assertEquals(AttributeUsage.DIRECTORY_OPERATION, at.getUsage());
		checkAttributeRegisteredInSchema(at, names);

	}
}
