/*
 * Created on 05-feb-2005
 *
 */

package org.jletty.db.schema.schemas;

import org.jletty.db.schema.SchemaTestCase;
import org.jletty.schemaparser.SchemaParser;
import org.jletty.util.Log4jTestsConfigurator;


/**
 * @author Ruben
 * 
 */
public class WholeSchemaTestCase extends SchemaTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		Log4jTestsConfigurator.configure();
		
	}

	public void testCoreSchema() throws Exception {
		SchemaParser parser = createSchemaParser("core.schema");

		parser.schema(schema);
		assertNotNull(schema.getObjectClass("alias"));
		assertNotNull(schema.getObjectClass("country"));
		assertNotNull(schema.getObjectClass("locality"));
		assertNotNull(schema.getObjectClass("organization"));
		assertNotNull(schema.getObjectClass("organizationalUnit"));
		assertNotNull(schema.getObjectClass("person"));
		assertNotNull(schema.getObjectClass("organizationalPerson"));
		assertNotNull(schema.getObjectClass("organizationalRole"));
		assertNotNull(schema.getObjectClass("groupOfNames"));
		assertNotNull(schema.getObjectClass("residentialPerson"));
		assertNotNull(schema.getObjectClass("applicationProcess"));
		assertNotNull(schema.getObjectClass("applicationEntity"));
		assertNotNull(schema.getObjectClass("dSA"));
		assertNotNull(schema.getObjectClass("device"));
		assertNotNull(schema.getObjectClass("strongAuthenticationUser"));
		assertNotNull(schema.getObjectClass("certificationAuthority"));
		assertNotNull(schema.getObjectClass("groupOfUniqueNames"));
		assertNotNull(schema.getObjectClass("userSecurityInformation"));
		assertNotNull(schema.getObjectClass("certificationAuthority"));
		assertNotNull(schema.getObjectClass("cRLDistributionPoint"));
		assertNotNull(schema.getObjectClass("dmd"));
		assertNotNull(schema.getObjectClass("pkiUser"));
		assertNotNull(schema.getObjectClass("pkiCA"));
		assertNotNull(schema.getObjectClass("deltaCRL"));
		assertNotNull(schema.getObjectClass("labeledURIObject"));
		assertNotNull(schema.getObjectClass("simpleSecurityObject"));
		assertNotNull(schema.getObjectClass("dcObject"));
		assertNotNull(schema.getObjectClass("uidObject"));

		assertNotNull(schema.getAttributeType("objectClass"));
		assertNotNull(schema.getAttributeType("aliasedObjectName"));
		assertNotNull(schema.getAttributeType("aliasedEntryName"));
		assertNotNull(schema.getAttributeType("knowledgeInformation"));
		assertNotNull(schema.getAttributeType("cn"));
		assertNotNull(schema.getAttributeType("commonName"));
		assertNotNull(schema.getAttributeType("sn"));
		assertNotNull(schema.getAttributeType("surname"));
		assertNotNull(schema.getAttributeType("serialNumber"));
		assertNotNull(schema.getAttributeType("c"));
		assertNotNull(schema.getAttributeType("countryName"));
		assertNotNull(schema.getAttributeType("l"));
		assertNotNull(schema.getAttributeType("localityName"));
		assertNotNull(schema.getAttributeType("st"));
		assertNotNull(schema.getAttributeType("stateOrProvinceName"));
		assertNotNull(schema.getAttributeType("street"));
		assertNotNull(schema.getAttributeType("streetAddress"));
		assertNotNull(schema.getAttributeType("o"));
		assertNotNull(schema.getAttributeType("organizationName"));
		assertNotNull(schema.getAttributeType("ou"));
		assertNotNull(schema.getAttributeType("organizationalUnitName"));
		assertNotNull(schema.getAttributeType("title"));
		assertNotNull(schema.getAttributeType("description"));
		assertNotNull(schema.getAttributeType("searchGuide"));
		assertNotNull(schema.getAttributeType("businessCategory"));
		assertNotNull(schema.getAttributeType("postalAddress"));
		assertNotNull(schema.getAttributeType("postalCode"));
		assertNotNull(schema.getAttributeType("postOfficeBox"));
		assertNotNull(schema.getAttributeType("physicalDeliveryOfficeName"));
		assertNotNull(schema.getAttributeType("telephoneNumber"));
		assertNotNull(schema.getAttributeType("telexNumber"));
		assertNotNull(schema.getAttributeType("teletexTerminalIdentifier"));
		assertNotNull(schema.getAttributeType("facsimileTelephoneNumber"));
		assertNotNull(schema.getAttributeType("fax"));
		assertNotNull(schema.getAttributeType("x121Address"));
		assertNotNull(schema.getAttributeType("internationaliSDNNumber"));
		assertNotNull(schema.getAttributeType("registeredAddress"));
		assertNotNull(schema.getAttributeType("destinationIndicator"));
		assertNotNull(schema.getAttributeType("preferredDeliveryMethod"));
		assertNotNull(schema.getAttributeType("presentationAddress"));
		assertNotNull(schema.getAttributeType("supportedApplicationContext"));
		assertNotNull(schema.getAttributeType("member"));
		assertNotNull(schema.getAttributeType("owner"));
		assertNotNull(schema.getAttributeType("roleOccupant"));
		assertNotNull(schema.getAttributeType("seeAlso"));
		assertNotNull(schema.getAttributeType("userPassword"));
		assertNotNull(schema.getAttributeType("userCertificate"));
		assertNotNull(schema.getAttributeType("cACertificate"));
		assertNotNull(schema.getAttributeType("authorityRevocationList"));
		assertNotNull(schema.getAttributeType("certificateRevocationList"));
		assertNotNull(schema.getAttributeType("crossCertificatePair"));
		assertNotNull(schema.getAttributeType("name"));
		assertNotNull(schema.getAttributeType("givenName"));
		assertNotNull(schema.getAttributeType("gn"));
		assertNotNull(schema.getAttributeType("initials"));
		assertNotNull(schema.getAttributeType("generationQualifier"));
		assertNotNull(schema.getAttributeType("x500UniqueIdentifier"));
		assertNotNull(schema.getAttributeType("dnQualifier"));
		assertNotNull(schema.getAttributeType("enhancedSearchGuide"));
		assertNotNull(schema.getAttributeType("protocolInformation"));
		assertNotNull(schema.getAttributeType("distinguishedName"));
		assertNotNull(schema.getAttributeType("uniqueMember"));
		assertNotNull(schema.getAttributeType("houseIdentifier"));
		assertNotNull(schema.getAttributeType("supportedAlgorithms"));
		assertNotNull(schema.getAttributeType("deltaRevocationList"));
		assertNotNull(schema.getAttributeType("dmdName"));
		assertNotNull(schema.getAttributeType("labeledURI"));
		
		schema.resolveDependencies();

	}
	
	

}
