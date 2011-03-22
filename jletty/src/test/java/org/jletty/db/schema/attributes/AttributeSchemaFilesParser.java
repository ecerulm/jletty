/*
 * Created on 24-ene-2005
 *
 */
package org.jletty.db.schema.attributes;

import java.io.EOFException;


import org.apache.log4j.BasicConfigurator;
import org.jletty.db.schema.SchemaTestCase;
import org.jletty.schema.AttributeType;
import org.jletty.schema.AttributeUsage;
import org.jletty.schema.Schema;
import org.jletty.schemaparser.SchemaParser;
import org.jletty.util.Log4jTestsConfigurator;

import antlr.ANTLRException;
import antlr.CharStreamIOException;
import antlr.MismatchedTokenException;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;


/**
 * @author Ruben
 * 
 */
public class AttributeSchemaFilesParser extends SchemaTestCase {

	private Schema schema;

	private AttributeType nameAttrType;

	public void testAllAttributeTypesInCoreShema() throws Exception {
		readAttributesInSchemaResource("coreSchemaAttributes.txt");

		assertNotNull(schema.getAttributeType("objectClass"));
		assertNotNull(schema.getAttributeType("knowledgeInformation"));
		assertNotNull(schema.getAttributeType("commonName"));
		assertNotNull(schema.getAttributeType("surname"));
		assertNotNull(schema.getAttributeType("serialNumber"));
		assertNotNull(schema.getAttributeType("countryName"));
		assertNotNull(schema.getAttributeType("localityName"));
		assertNotNull(schema.getAttributeType("stateOrProvinceName"));
		assertNotNull(schema.getAttributeType("streetAddress"));
		assertNotNull(schema.getAttributeType("organizationName"));
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
	}

	private void readAttributesInSchemaResource(String resourceName) throws RecognitionException, TokenStreamException {
		SchemaParser parser = createSchemaParser(resourceName);
		AttributeType at = null;
	
		try {
			do {
				at = parser.attributetype(schema);
			} while (at != null);
		} catch (MismatchedTokenException e) {
			//assert that we finish due to end of file condition			
			assertEquals(Token.EOF_TYPE,e.token.getType());
		}
	}

	public void testAllAttributeTypesInCorbaSchema() throws Exception {
		readAttributesInSchemaResource("corbaSchemaAttributes.txt");

		assertNotNull(schema.getAttributeType("corbaIor"));
		assertNotNull(schema.getAttributeType("corbaRepositoryId"));
	}

	public void testAllAttributeTypesInCosineSchema() throws Exception {
		readAttributesInSchemaResource("cosineSchemaAttributes.txt");
		
		assertNotNull(schema.getAttributeType("textEncodedORAddress"));
		assertNotNull(schema.getAttributeType("info"));
		assertNotNull(schema.getAttributeType("favouriteDrink"));
		assertNotNull(schema.getAttributeType("roomNumber"));
		assertNotNull(schema.getAttributeType("photo"));
		assertNotNull(schema.getAttributeType("userClass"));
		assertNotNull(schema.getAttributeType("host"));
		assertNotNull(schema.getAttributeType("manager"));
		assertNotNull(schema.getAttributeType("documentIdentifier"));
		assertNotNull(schema.getAttributeType("documentTitle"));
		assertNotNull(schema.getAttributeType("documentVersion"));
		assertNotNull(schema.getAttributeType("documentAuthor"));
		assertNotNull(schema.getAttributeType("documentLocation"));
		assertNotNull(schema.getAttributeType("homeTelephoneNumber"));
		assertNotNull(schema.getAttributeType("secretary"));
		assertNotNull(schema.getAttributeType("otherMailbox"));
		assertNotNull(schema.getAttributeType("lastModifiedTime"));
		assertNotNull(schema.getAttributeType("lastModifiedBy"));
		assertNotNull(schema.getAttributeType("aRecord"));
		assertNotNull(schema.getAttributeType("mDRecord"));
		assertNotNull(schema.getAttributeType("mXRecord"));
		assertNotNull(schema.getAttributeType("nSRecord"));
		assertNotNull(schema.getAttributeType("sOARecord"));
		assertNotNull(schema.getAttributeType("cNAMERecord"));
		assertNotNull(schema.getAttributeType("associatedName"));
		assertNotNull(schema.getAttributeType("homePostalAddress"));
		assertNotNull(schema.getAttributeType("personalTitle"));
		assertNotNull(schema.getAttributeType("mobileTelephoneNumber"));
		assertNotNull(schema.getAttributeType("pagerTelephoneNumber"));
		assertNotNull(schema.getAttributeType("friendlyCountryName"));
		assertNotNull(schema.getAttributeType("uniqueIdentifier"));
		assertNotNull(schema.getAttributeType("organizationalStatus"));
		assertNotNull(schema.getAttributeType("janetMailbox"));
		assertNotNull(schema.getAttributeType("mailPreferenceOption"));
		assertNotNull(schema.getAttributeType("buildingName"));
		assertNotNull(schema.getAttributeType("dSAQuality"));
		assertNotNull(schema.getAttributeType("singleLevelQuality"));
		assertNotNull(schema.getAttributeType("subtreeMinimumQuality"));
		assertNotNull(schema.getAttributeType("subtreeMaximumQuality"));
		assertNotNull(schema.getAttributeType("personalSignature"));
		assertNotNull(schema.getAttributeType("dITRedirect"));
		assertNotNull(schema.getAttributeType("audio"));
		assertNotNull(schema.getAttributeType("documentPublisher"));

	}

	// public void testAllAttributeTypesInDyngroupSchema() throws Exception {
	// AttributeTypeParser parser =
	// createAttributeTypeParser("dyngroupAttributes.txt");
	// AttributeType at = null;
	// do {
	// at = parser.attributetype(schema);
	// } while (at != null);
	//
	// assertNotNull(schema.getAttributeType("memberURL"));
	//		
	// }

	public void testAllAttributeTypesInInetorgPersonSchema() throws Exception {
		readAttributesInSchemaResource("inetorgPersonAttributes.txt");
		

		assertNotNull(schema.getAttributeType("carLicense"));
		assertNotNull(schema.getAttributeType("departmentNumber"));
		assertNotNull(schema.getAttributeType("displayName"));
		assertNotNull(schema.getAttributeType("employeeNumber"));
		assertNotNull(schema.getAttributeType("employeeType"));
		assertNotNull(schema.getAttributeType("jpegPhoto"));
		assertNotNull(schema.getAttributeType("preferredLanguage"));
		assertNotNull(schema.getAttributeType("userSMIMECertificate"));
		assertNotNull(schema.getAttributeType("userPKCS12"));

	}

	public void testAllAttributeTypesInJavaSchema() throws Exception {
		readAttributesInSchemaResource("javaAttributes.txt");
		
		assertNotNull(schema.getAttributeType("javaClassName"));
		assertNotNull(schema.getAttributeType("javaClassNames"));
		assertNotNull(schema.getAttributeType("javaCodebase"));
		assertNotNull(schema.getAttributeType("javaSerializedData"));
		assertNotNull(schema.getAttributeType("javaFactory"));
		assertNotNull(schema.getAttributeType("javaReferenceAddress"));
		assertNotNull(schema.getAttributeType("javaDoc"));

	}

	public void testAllAttributeTypesInMiscSchema() throws Exception {
		readAttributesInSchemaResource("miscAttributes.txt");
	

		assertNotNull(schema.getAttributeType("mailLocalAddress"));
		assertNotNull(schema.getAttributeType("mailHost"));
		assertNotNull(schema.getAttributeType("mailRoutingAddress"));
		assertNotNull(schema.getAttributeType("rfc822MailMember"));

	}
	public void testAllAttributeTypesInNisSchema() throws Exception {
		readAttributesInSchemaResource("nisAttributes.txt");
		
		assertNotNull(schema.getAttributeType("uidNumber"));
		assertNotNull(schema.getAttributeType("gidNumber"));
		assertNotNull(schema.getAttributeType("gecos"));
		assertNotNull(schema.getAttributeType("homeDirectory"));
		assertNotNull(schema.getAttributeType("loginShell"));
		assertNotNull(schema.getAttributeType("shadowLastChange"));
		assertNotNull(schema.getAttributeType("shadowMin"));
		assertNotNull(schema.getAttributeType("shadowMax"));
		assertNotNull(schema.getAttributeType("shadowWarning"));
		assertNotNull(schema.getAttributeType("shadowInactive"));
		assertNotNull(schema.getAttributeType("shadowExpire"));
		assertNotNull(schema.getAttributeType("shadowFlag"));
		assertNotNull(schema.getAttributeType("memberUid"));
		assertNotNull(schema.getAttributeType("memberNisNetgroup"));
		assertNotNull(schema.getAttributeType("nisNetgroupTriple"));
		assertNotNull(schema.getAttributeType("ipServicePort"));
		assertNotNull(schema.getAttributeType("ipServiceProtocol"));
		assertNotNull(schema.getAttributeType("oncRpcNumber"));
		
		
	}

	protected void setUp() throws Exception {
		super.setUp();
		Log4jTestsConfigurator.configure();
		schema = Schema.getInstance();
		nameAttrType = new AttributeType("2.5.4.41", new String[] { "name" },
				"", false, "", "", "", "caseIgnoreSubstringsMatch",
				"1.3.6.1.4.1.1466.115.121.1.15", 32768,false, false, false,
				AttributeUsage.USER_APPLICATIONS);
		schema.addAttributeType(nameAttrType);

	}

}
