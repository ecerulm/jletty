/*
 * Created on 03-nov-2004
 *
 */
package org.jletty.messageprocessor;

import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import junit.framework.TestCase;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.EnumerationUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.easymock.MockControl;
import org.jletty.dn.DistinguishedName;
import org.jletty.dn.Rfc2253NameParserImpl;
import org.jletty.jndiprovider.LdapAttribute;
import org.jletty.jndiprovider.LdapAttributes;
import org.jletty.jndiprovider.LdapEntryImpl;
import org.jletty.ldapstackldapops.AbandonRequest;
import org.jletty.ldapstackldapops.AddRequest;
import org.jletty.ldapstackldapops.AddResponse;
import org.jletty.ldapstackldapops.ApproxMatchFilter;
import org.jletty.ldapstackldapops.AttributeTypeAndValues;
import org.jletty.ldapstackldapops.AttributeTypeAndValuesList;
import org.jletty.ldapstackldapops.AttributeValues;
import org.jletty.ldapstackldapops.BindRequest;
import org.jletty.ldapstackldapops.BindResponse;
import org.jletty.ldapstackldapops.CompareRequest;
import org.jletty.ldapstackldapops.CompareResponse;
import org.jletty.ldapstackldapops.DelResponse;
import org.jletty.ldapstackldapops.DeleteRequest;
import org.jletty.ldapstackldapops.DerefAliases;
import org.jletty.ldapstackldapops.EqMatchFilter;
import org.jletty.ldapstackldapops.Filter;
import org.jletty.ldapstackldapops.GreaterOrEqualFilter;
import org.jletty.ldapstackldapops.LDAPMessage;
import org.jletty.ldapstackldapops.LDAPResponse;
import org.jletty.ldapstackldapops.LDAPResultCode;
import org.jletty.ldapstackldapops.LdapEntry;
import org.jletty.ldapstackldapops.LdapInvalidDnSyntaxException;
import org.jletty.ldapstackldapops.LessOrEqualFilter;
import org.jletty.ldapstackldapops.Modification;
import org.jletty.ldapstackldapops.ModificationType;
import org.jletty.ldapstackldapops.ModifyRequest;
import org.jletty.ldapstackldapops.ModifyResponse;
import org.jletty.ldapstackldapops.PresentFilter;
import org.jletty.ldapstackldapops.Scope;
import org.jletty.ldapstackldapops.SearchRequest;
import org.jletty.ldapstackldapops.SearchResultDone;
import org.jletty.ldapstackldapops.SearchResultEntry;
import org.jletty.ldapstackldapops.SubstringFilter;
import org.jletty.ldapstackldapops.SubstringValue;
import org.jletty.messageprocessorstage.EventForMessageProcessor;
import org.jletty.messageprocessorstage.LdapMessageEvent;
import org.jletty.messageprocessorstage.MessageProcessor;
import org.jletty.messageprocessorstage.MessageProcessorStage;
import org.jletty.messageprocessorstage.ResponseHandler;
import org.jletty.schema.AttributeType;
import org.jletty.schema.AttributeUsage;
import org.jletty.schema.DeliveryMethod;
import org.jletty.schema.DirectoryString;
import org.jletty.schema.IA5String;
import org.jletty.schema.NumericString;
import org.jletty.schema.ObjectClass;
import org.jletty.schema.ObjectClassType;
import org.jletty.schema.Schema;
import org.jletty.schema.TelephoneNumber;
import org.jletty.schema.TelexNumber;
import org.jletty.schemaparser.Parser;
import org.jletty.util.HexUtils;

/**
 * @author Ruben
 * 
 */
public class MessageProcessorStageTest extends TestCase {
	class LdapMessageEventImpl implements LdapMessageEvent {

		private LDAPMessage mes;

		private ResponseHandler handler;

		LdapMessageEventImpl(LDAPMessage mes, ResponseHandler handler) {
			this.mes = mes;
			this.handler = handler;
		}

		public LDAPMessage getMessage() {
			// return );
			return mes;
		}

		public ResponseHandler getResponseHandler() {

			return handler;
		}

	}

	private MessageProcessorStage stage;

	private int resultMessagesReceived;

	private MockControl handlerControl;

	private ResponseHandler handlerMock;

	private MockControl eventControl;

	private LdapMessageEvent eventMock;

	private Map backend = new HashMap();

	/**
	 * 
	 */
	public MessageProcessorStageTest() {
		super();
		init();
	}

	/**
	 * @param arg0
	 */
	public MessageProcessorStageTest(String arg0) {
		super(arg0);
		init();
	}

	private void init() {
		Schema schema = Schema.getInstance();
		schema.clear();
		AttributeType nameAttrType = new AttributeType("2.5.4.41",
				new String[] { "name" }, "", false, "", "", "",
				"caseIgnoreSubstringsMatch", "1.3.6.1.4.1.1466.115.121.1.15",
				32768, false, false, false, AttributeUsage.USER_APPLICATIONS);
		schema.addAttributeType(nameAttrType);
		InputStream schemaStream = getClass()
				.getResourceAsStream("core.schema");

		Parser.parse(schemaStream, schema);
		schema.resolveDependencies();
	}

	protected void setUp() throws Exception {
		stage = new MessageProcessorStage(backend);
		resultMessagesReceived = 0;
		// BasicConfigurator.configure();
		LogManager.resetConfiguration();
		Properties logProps = new Properties();
		logProps.put("log4j.threshold", "OFF");
		PropertyConfigurator.configure(logProps);
		handlerControl = MockControl.createControl(ResponseHandler.class);
		handlerMock = (ResponseHandler) handlerControl.getMock();
		eventControl = MockControl.createStrictControl(LdapMessageEvent.class);
		eventMock = (LdapMessageEvent) eventControl.getMock();
		eventControl.reset();
		handlerControl.reset();
	}

	public void testBindRequestRootUser() {
		LDAPMessage message = new LDAPMessage(1, new BindRequest(3,
				"cn=Directory Manager", "secret".getBytes()));
		ResponseHandler responseHandler = new ResponseHandler() {

			public void sendResponse(LDAPResponse response) {
				assertTrue(response instanceof BindResponse);
				BindResponse br = (BindResponse) response;
				assertEquals(LDAPResultCode.SUCCESS, br.getResult());

			}
		};
		final LdapMessageEvent event = new LdapMessageEventImpl(message,
				responseHandler);
		EventForMessageProcessor ev = createEventForMessageProcessor(event);

		stage.enqueue(ev);
	}

	public void testBindRequestRootUserWrongPassword() {
		// wrong password should be secret
		LDAPMessage message = new LDAPMessage(1, new BindRequest(3,
				"cn=Directory Manager", "secret2".getBytes()));
		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock.sendResponse(new BindResponse(
				LDAPResultCode.INVALID_CREDENTIALS));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();
	}

	public void testBindRequestUnknownUser() {

		LDAPMessage message = new LDAPMessage(1, new BindRequest(3,
				"cn=UnknownUser", "secret".getBytes()));
		ResponseHandler responseHandler = new ResponseHandler() {

			public void sendResponse(LDAPResponse response) {
				assertTrue(response instanceof BindResponse);
				BindResponse br = (BindResponse) response;
				assertEquals(LDAPResultCode.INVALID_CREDENTIALS, br.getResult());

			}
		};

		final LdapMessageEvent event = new LdapMessageEventImpl(message,
				responseHandler);

		EventForMessageProcessor ev = createEventForMessageProcessor(event);

		stage.enqueue(ev);
	}

	public void testBindRequestNormalUserRightPasswordCrypt() {
		populateBranchEntries();
		populateOrgPersonEntries(5);
		// wrong password should be secret
		LDAPMessage message = new LDAPMessage(1, new BindRequest(3,
				"cn=orgperson1,o=org1,c=US", "cryptpasswd".getBytes()));
		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock.sendResponse(new ModifyResponse(LDAPResultCode.SUCCESS));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

	}

	public void testBindRequestNormalUserWrongPasswordCrypt() {
		populateBranchEntries();
		populateOrgPersonEntries(5);
		// wrong password. take into account that traditional crypt only
		// uses the 8 first character to verify password
		LDAPMessage message = new LDAPMessage(1, new BindRequest(3,
				"cn=orgperson1,o=org1,c=US", "cryptpa1sswd".getBytes()));
		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock.sendResponse(new ModifyResponse(
				LDAPResultCode.INVALID_CREDENTIALS));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();
	}

	public void testBindRequestNormalUserRightPasswordMd5() {
		populateBranchEntries();
		populateOrgPersonEntries(5);
		populateOrgPersonEntriesWithMd5Passwords(5);
		// wrong password should be secret
		LDAPMessage message = new LDAPMessage(1, new BindRequest(3,
				"cn=orgpersonmd51,o=org1,c=US", "md5password".getBytes()));
		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock.sendResponse(new ModifyResponse(LDAPResultCode.SUCCESS));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();
	}

	public void testBindRequestNormalUserWrongPasswordMd5() {
		populateBranchEntries();
		populateOrgPersonEntries(5);
		populateOrgPersonEntriesWithMd5Passwords(5);
		// wrong password should be secret
		LDAPMessage message = new LDAPMessage(1, new BindRequest(3,
				"cn=orgpersonmd51,o=org1,c=US", "md5password1".getBytes()));
		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock.sendResponse(new ModifyResponse(
				LDAPResultCode.INVALID_CREDENTIALS));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();
	}

	public void testBindRequestNormalUserRightPasswordSha1() {
		populateBranchEntries();
		populateOrgPersonEntries(5);
		populateOrgPersonEntriesWithSha1Passwords(5);
		// wrong password should be secret
		LDAPMessage message = new LDAPMessage(1, new BindRequest(3,
				"cn=orgpersonsha11,o=org1,c=US", "sha1password".getBytes()));
		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock.sendResponse(new ModifyResponse(LDAPResultCode.SUCCESS));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();
	}

	public void testBindRequestNormalUserWrongPasswordSha1() {
		populateBranchEntries();
		populateOrgPersonEntries(5);
		populateOrgPersonEntriesWithSha1Passwords(5);
		// wrong password should be secret
		LDAPMessage message = new LDAPMessage(1, new BindRequest(3,
				"cn=orgpersonsha1,o=org1,c=US", "sha1password1".getBytes()));
		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock.sendResponse(new ModifyResponse(
				LDAPResultCode.INVALID_CREDENTIALS));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();
	}

	/**
	 * Test that AddRequest on a fresh databse suceeds
	 * 
	 */
	public void testAddRequest() {

		LDAPMessage message = createAddRequestMessage();

		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		handlerMock.sendResponse(new AddResponse(LDAPResultCode.SUCCESS));
		handlerControl.replay();

		stage.enqueue(createEventForMessageProcessor(eventMock));
		eventControl.verify();
		handlerControl.verify();
	}

	public void testAddAlreadyExistingEntry() {
		testAddRequest();
		LDAPMessage message = createAddRequestMessage();

		eventControl.reset();
		handlerControl.reset();

		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		handlerMock.sendResponse(new AddResponse(
				LDAPResultCode.ENTRY_ALREADY_EXISTS));
		handlerControl.replay();

		stage.enqueue(createEventForMessageProcessor(eventMock));
		eventControl.verify();
		handlerControl.verify();

	}

	/**
	 * Here the dn in the AddRequest is escaped "cn=New\\20E\\6etry" and should
	 * land in the database as "cn=New entry"
	 * 
	 * @throws NamingException
	 */
	public void testAddEscapedCharInDN() throws NamingException {
		List attrs = new ArrayList();
		attrs.add(new AttributeTypeAndValues("objectclass",
				new AttributeValues().add("top").add("organizationalPerson")
						.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("cn", new AttributeValues().add(
				"New Entry").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("email", new AttributeValues()
				.add("nobody@nodomain.com").getValuesAsByteArray()));

		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);
		LDAPMessage message = new LDAPMessage(1, new AddRequest(
				"cn=New\\20E\\6etry", attrList));

		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		handlerMock.sendResponse(new AddResponse(LDAPResultCode.SUCCESS));
		handlerControl.replay();

		stage.enqueue(createEventForMessageProcessor(eventMock));
		eventControl.verify();
		handlerControl.verify();

		Object lookup = backend.get(dn("cn=New Entry"));
		assertTrue(lookup instanceof LdapEntry);
		LdapEntry d = (LdapEntry) lookup;
		LdapAttributes attributes = d.getAttributes();
		final LdapAttribute attributeObjectclass = attributes
				.get("objectclass");
		assertTrue(attributeObjectclass.contains("top"));
		assertTrue(attributeObjectclass.contains("organizationalPerson"));
		assertTrue(attributes.get("cn").contains("New Entry"));
		assertTrue(attributes.get("email").contains("nobody@nodomain.com"));
	}

	private DistinguishedName dn(String dn) {
		try {
			return (DistinguishedName) new Rfc2253NameParserImpl().parse(dn);
		} catch (InvalidNameException e) {
			fail(dn + " is not a valid dn");
			return null;
		}
	}

	public void testAddSpaceInDnIgnored() throws NamingException {
		backend.put(dn("c=us"), new LdapEntryImpl(dn("c=us"), null));
		List attrs = new ArrayList();
		attrs.add(new AttributeTypeAndValues("objectclass",
				new AttributeValues().add("top").add("organizationalPerson")
						.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("cn", new AttributeValues().add(
				"New Entry").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("email", new AttributeValues()
				.add("nobody@nodomain.com").getValuesAsByteArray()));

		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);
		LDAPMessage message = new LDAPMessage(1, new AddRequest(
				"cn=New Entry ,c=us", attrList));

		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		handlerMock.sendResponse(new AddResponse(LDAPResultCode.SUCCESS));
		handlerControl.replay();

		stage.enqueue(createEventForMessageProcessor(eventMock));
		eventControl.verify();
		handlerControl.verify();

		Object lookup = backend.get(dn("cn=New Entry,c=us"));
		// TODO lookup is an LdapEntry not DirContext
		assertTrue(lookup instanceof LdapEntry);
		LdapEntry d = (LdapEntry) lookup;
		LdapAttributes attributes = d.getAttributes();
		assertTrue(attributes.get("objectclass").contains("top"));
		assertTrue(attributes.get("objectclass").contains(
				"organizationalPerson"));
		assertTrue(attributes.get("cn").contains("New Entry"));
		assertTrue(attributes.get("email").contains("nobody@nodomain.com"));
	}

	public void testAddSpaceInDnNotIgnored() throws NamingException {
		createC_us_entry();
		List attrs = new ArrayList();
		attrs.add(new AttributeTypeAndValues("objectclass",
				new AttributeValues().add("top").add("organizationalPerson")
						.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("cn", new AttributeValues().add(
				"New Entry ").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("email", new AttributeValues()
				.add("nobody@nodomain.com").getValuesAsByteArray()));

		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);
		LDAPMessage message = new LDAPMessage(1, new AddRequest(
				"cn=New Entry\\ ,c=us", attrList));

		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		handlerMock.sendResponse(new AddResponse(LDAPResultCode.SUCCESS));
		handlerControl.replay();

		stage.enqueue(createEventForMessageProcessor(eventMock));
		eventControl.verify();
		handlerControl.verify();
		Object lookup = backend.get(dn("cn=New Entry,c=us"));
		assertNull(lookup);

		lookup = backend.get(dn("cn=New Entry\\ ,c=us"));
		assertTrue(lookup instanceof LdapEntry);
		LdapEntry d = (LdapEntry) lookup;
		LdapAttributes attributes = d.getAttributes();
		assertTrue(attributes.get("objectclass").contains("top"));
		assertTrue(attributes.get("objectclass").contains(
				"organizationalPerson"));
		assertTrue(attributes.get("cn").contains("New Entry "));
		assertTrue(attributes.get("email").contains("nobody@nodomain.com"));
	}

	private void createC_us_entry() {
		createEntry(dn("c=us"), null);
	}

	public void testAddSuffixDoesnExists() throws Exception {
		List attrs = new ArrayList();
		attrs.add(new AttributeTypeAndValues("objectclass",
				new AttributeValues().add("top").add("organizationalPerson")
						.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("cn", new AttributeValues().add(
				"New Entry").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("email", new AttributeValues()
				.add("nobody@nodomain.com").getValuesAsByteArray()));

		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);
		LDAPMessage message = new LDAPMessage(1, new AddRequest(
				"cn=New entry,c=DOESNEXISTS", attrList));

		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		handlerMock.sendResponse(new AddResponse(LDAPResultCode.NO_SUCH_OBJECT,
				"", "parent entry doesn't exist"));
		handlerControl.replay();

		stage.enqueue(createEventForMessageProcessor(eventMock));
		eventControl.verify();
		handlerControl.verify();

		Object lookup = backend.get(dn("cn=New Entry,c=DOESNEXISTS"));
		assertNull(lookup);
	}

	public void testAddSuffixDoesnExists2() throws Exception {
		createC_us_entry();

		List attrs = new ArrayList();
		attrs.add(new AttributeTypeAndValues("objectclass",
				new AttributeValues().add("top").add("organizationalPerson")
						.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("cn", new AttributeValues().add(
				"New Entry").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("email", new AttributeValues()
				.add("nobody@nodomain.com").getValuesAsByteArray()));

		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);
		LDAPMessage message = new LDAPMessage(1, new AddRequest(
				"cn=New entry,c=DOESNEXISTS,c=us", attrList));

		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		handlerMock.sendResponse(new AddResponse(LDAPResultCode.NO_SUCH_OBJECT,
				"c=us", "parent entry doesn't exist"));
		handlerControl.replay();

		stage.enqueue(createEventForMessageProcessor(eventMock));
		eventControl.verify();
		handlerControl.verify();

		Object lookup = backend.get(dn("cn=New Entry,c=DOESNEXISTS,c=us"));
		assertNull("this entry should be never added", lookup);
	}

	public void testAddCreatorsName() throws Exception {

		String attrName = "creatorsName";
		String attrValue = "creatorsNameValue1";
		operationalAttributeTest(attrName, attrValue);
	}

	public void testAddCreatorsName2() throws Exception {
		operationalAttributeTest("creatorsname", "creatorsNameValue1");
	}

	public void testAddCreateTimeStamp() throws Exception {
		operationalAttributeTest("createTimeStamp", "createtimestampvalue1");
	}

	public void testAddCreateTimeStamp2() throws Exception {
		operationalAttributeTest("createtimestamp", "createtimestampvalue1");
	}

	public void testAddModifiersName() throws Exception {
		operationalAttributeTest("modifiersName", "modifiersNamevalue1");
	}

	public void testAddModifiersName2() throws Exception {
		operationalAttributeTest("modifiersname", "modifiersNamevalue1");
	}

	public void testAddModifyTimeStamp() throws Exception {
		operationalAttributeTest("modifyTimeStamp", "modifytimestampvalue1");
	}

	public void testAddModifyTimeStamp2() throws Exception {
		operationalAttributeTest("modifytimestamp", "modifytimestampvalue1");
	}

	public void testAddSubschemaSubentry() throws Exception {
		operationalAttributeTest("subschemaSubentry", "subschemasubentryvalue1");
	}

	public void testAddSubschemaSubentry2() throws Exception {
		operationalAttributeTest("subschemasubentry", "subschemasubentryvalue1");
	}

	public void testAddMissingObjectclass() throws Exception {
		createC_us_entry();
		stage.enableSchemaChecking();

		List attrs = new ArrayList();
		attrs.add(new AttributeTypeAndValues("cn", new AttributeValues().add(
				"New Entry").getValuesAsByteArray()));

		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);
		LDAPMessage message = new LDAPMessage(1, new AddRequest(
				"cn=New Entry,c=us", attrList));

		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		handlerMock.sendResponse(new AddResponse(
				LDAPResultCode.CONSTRAINT_VIOLATION, "",
				"entry has no objectClass attribute"));
		handlerControl.replay();

		stage.enqueue(createEventForMessageProcessor(eventMock));
		eventControl.verify();
		handlerControl.verify();
	}

	public void testAddMissingObjectclass2() throws Exception {
		createC_us_entry();
		stage.enableSchemaChecking();

		List attrs = new ArrayList();
		attrs.add(new AttributeTypeAndValues("cn", new AttributeValues().add(
				"New Entry").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("objectclass",
				new AttributeValues().add("").getValuesAsByteArray()));

		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);
		LDAPMessage message = new LDAPMessage(1, new AddRequest(
				"cn=New Entry,c=us", attrList));

		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		handlerMock.sendResponse(new AddResponse(
				LDAPResultCode.INVALID_ATTR_SYNTAX, "",
				"value #0 invalid per syntax"));
		handlerControl.replay();

		stage.enqueue(createEventForMessageProcessor(eventMock));
		eventControl.verify();
		handlerControl.verify();
	}

	public void testAddAttributeWithNoValues() throws Exception {
		createC_us_entry();
		stage.enableSchemaChecking();

		List attrs = new ArrayList();
		attrs.add(new AttributeTypeAndValues("cn", new AttributeValues().add(
				"New Entry").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("objectclass",
				new AttributeValues().getValuesAsByteArray()));

		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);
		LDAPMessage message = new LDAPMessage(1, new AddRequest(
				"cn=New Entry,c=us", attrList));

		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		handlerMock.sendResponse(new AddResponse(
				LDAPResultCode.INVALID_ATTR_SYNTAX, "",
				"attribute objectclass cannot be empty"));
		handlerControl.replay();

		stage.enqueue(createEventForMessageProcessor(eventMock));
		eventControl.verify();
		handlerControl.verify();
	}

	/**
	 * Tests that we can add an entry specifying the objectclass of the entry as
	 * 2.5.4.0=testObjectclass instead of merely saying
	 * objectclass=testObjectclass
	 * 
	 * @throws Exception
	 */
	public void testAddObjectclassAsOid() throws Exception {
		createC_us_entry();
		stage.enableSchemaChecking();
		Schema schema = Schema.getInstance();
		schema.addAttributeType(new AttributeType("1.3.6.1.3.2",
				new String[] { "attr1" }, "a test attr", false, null,
				"caseIgnoreMatch", "caseIgnoreOrderingMatch",
				"caseIgnoreSubstringsMatch", "1.3.6.1.4.1.1466.115.121.1.26",
				-1, false, true, false, AttributeUsage.USER_APPLICATIONS));
		schema.addAttributeType(new AttributeType("1.3.6.1.3.3",
				new String[] { "attr2" }, "a test attr", false, null,
				"caseIgnoreMatch", "caseIgnoreOrderingMatch",
				"caseIgnoreSubstringsMatch", "1.3.6.1.4.1.1466.115.121.1.26",
				-1, false, true, false, AttributeUsage.USER_APPLICATIONS));
		schema.addObjectclass(new ObjectClass("1.3.6.1.3.1",
				new String[] { "testObjectclass" }, "a test objectclass", null,
				false, ObjectClassType.STRUCTURAL, new String[] { "attr1" },
				null));
		schema.resolveDependencies();

		List attrs = new ArrayList();
		attrs.add(new AttributeTypeAndValues("2.5.4.0", new AttributeValues()
				.add("testObjectclass").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("attr1", new AttributeValues()
				.add("attr1value").getValuesAsByteArray()));

		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);
		LDAPMessage message = new LDAPMessage(1, new AddRequest(
				"attr1=attr1value,c=us", attrList));

		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		handlerMock.sendResponse(new AddResponse(LDAPResultCode.SUCCESS));
		handlerControl.replay();

		stage.enqueue(createEventForMessageProcessor(eventMock));
		eventControl.verify();
		handlerControl.verify();

		Object lookup = backend.get(dn("attr1=attr1value,c=us"));
		assertTrue(lookup instanceof LdapEntry);
		LdapEntry d = (LdapEntry) lookup;
		LdapAttributes attributes = d.getAttributes();
		assertTrue(attributes.get("objectclass").contains("testObjectclass"));
		assertTrue(attributes.get("attr1").contains("attr1value"));
	}

	public void testAddMissingAttribute() throws Exception {
		createC_us_entry();
		stage.enableSchemaChecking();
		Schema schema = Schema.getInstance();
		schema.addAttributeType(new AttributeType("1.3.6.1.3.1",
				new String[] { "attr1" }, "a test attr", false, null,
				"caseIgnoreMatch", "caseIgnoreOrderingMatch",
				"caseIgnoreSubstringsMatch", "1.3.6.1.4.1.1466.115.121.1.26",
				-1, false, true, false, AttributeUsage.USER_APPLICATIONS));
		schema.addObjectclass(new ObjectClass("1.3.6.1.3.1",
				new String[] { "testObjectclass" }, "a test objectclass", null,
				false, ObjectClassType.STRUCTURAL, new String[] { "attr1" },
				null));
		schema.resolveDependencies();

		List attrs = new ArrayList();
		attrs.add(new AttributeTypeAndValues("cn", new AttributeValues().add(
				"New Entry").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("objectclass",
				new AttributeValues().add("testObjectclass")
						.getValuesAsByteArray()));

		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);
		LDAPMessage message = new LDAPMessage(1, new AddRequest(
				"cn=New Entry,c=us", attrList));

		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		handlerMock.sendResponse(new AddResponse(
				LDAPResultCode.OBJECTCLASS_VIOLATION, "",
				"object class '[testObjectclass]' requires attribute 'attr1'"));
		handlerControl.replay();

		stage.enqueue(createEventForMessageProcessor(eventMock));
		eventControl.verify();
		handlerControl.verify();
	}

	public void testAddMissingAttribute2() throws Exception {
		createC_us_entry();
		stage.enableSchemaChecking();
		init();
		Schema schema = Schema.getInstance();
		schema.addAttributeType(new AttributeType("1.3.6.1.3.2",
				new String[] { "attr1" }, "a test attr", false, null,
				"caseIgnoreMatch", "caseIgnoreOrderingMatch",
				"caseIgnoreSubstringsMatch", "1.3.6.1.4.1.1466.115.121.1.26",
				-1, false, true, false, AttributeUsage.USER_APPLICATIONS));
		schema.addObjectclass(new ObjectClass("1.3.6.1.3.1",
				new String[] { "testObjectclass" }, "a test objectclass",
				new String[] { "testObjectclass2" }, false,
				ObjectClassType.STRUCTURAL, new String[] { "attr1" }, null));
		schema.addAttributeType(new AttributeType("1.3.6.1.3.3",
				new String[] { "attr2" }, "a test attr", false, null,
				"caseIgnoreMatch", "caseIgnoreOrderingMatch",
				"caseIgnoreSubstringsMatch", "1.3.6.1.4.1.1466.115.121.1.26",
				-1, false, true, false, AttributeUsage.USER_APPLICATIONS));
		schema.addObjectclass(new ObjectClass("1.3.6.1.3.4",
				new String[] { "testObjectclass2" }, "a test objectclass",
				null, false, ObjectClassType.STRUCTURAL,
				new String[] { "attr2" }, null));
		schema.resolveDependencies();

		List attrs = new ArrayList();
		attrs.add(new AttributeTypeAndValues("objectclass",
				new AttributeValues().add("testObjectclass")
						.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("attr1", new AttributeValues()
				.add("attr1value").getValuesAsByteArray()));

		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);
		LDAPMessage message = new LDAPMessage(1, new AddRequest(
				"cn=New Entry,c=us", attrList));

		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		handlerMock.sendResponse(new AddResponse(
				LDAPResultCode.OBJECTCLASS_VIOLATION, "",
				"object class '[testObjectclass]' requires attribute 'attr2'"));
		handlerControl.replay();

		stage.enqueue(createEventForMessageProcessor(eventMock));
		eventControl.verify();
		handlerControl.verify();
	}

	public void testAddNotAllowedAttribute() throws Exception {
		createC_us_entry();
		stage.enableSchemaChecking();
		Schema schema = Schema.getInstance();
		schema.addAttributeType(new AttributeType("1.3.6.1.3.2",
				new String[] { "attr1" }, "a test attr", false, null,
				"caseIgnoreMatch", "caseIgnoreOrderingMatch",
				"caseIgnoreSubstringsMatch", "1.3.6.1.4.1.1466.115.121.1.26",
				-1, false, true, false, AttributeUsage.USER_APPLICATIONS));
		schema.addAttributeType(new AttributeType("1.3.6.1.3.3",
				new String[] { "attr2" }, "a test attr", false, null,
				"caseIgnoreMatch", "caseIgnoreOrderingMatch",
				"caseIgnoreSubstringsMatch", "1.3.6.1.4.1.1466.115.121.1.26",
				-1, false, true, false, AttributeUsage.USER_APPLICATIONS));
		schema.addObjectclass(new ObjectClass("1.3.6.1.3.1",
				new String[] { "testObjectclass" }, "a test objectclass", null,
				false, ObjectClassType.STRUCTURAL,
				new String[] { "attr1", "cn" }, null));
		schema.resolveDependencies();

		List attrs = new ArrayList();
		attrs.add(new AttributeTypeAndValues("cn", new AttributeValues().add(
				"New Entry").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("objectclass",
				new AttributeValues().add("testObjectclass")
						.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("attr1", new AttributeValues()
				.add("attr1value").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("attr2", new AttributeValues()
				.add("attr2value").getValuesAsByteArray()));

		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);
		LDAPMessage message = new LDAPMessage(1, new AddRequest(
				"cn=New Entry,c=us", attrList));

		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		handlerMock
				.sendResponse(new AddResponse(
						LDAPResultCode.OBJECTCLASS_VIOLATION, "",
						"attribute 'attr2' not allowed by objectclasses '[testObjectclass]'"));
		handlerControl.replay();

		stage.enqueue(createEventForMessageProcessor(eventMock));
		eventControl.verify();
		handlerControl.verify();
	}

	public void testAddUndefinedAttributeType() throws Exception {
		createC_us_entry();
		stage.enableSchemaChecking();
		init();
		Schema schema = Schema.getInstance();
		schema.addAttributeType(new AttributeType("1.3.6.1.3.2",
				new String[] { "attr1" }, "a test attr", false, null,
				"caseIgnoreMatch", "caseIgnoreOrderingMatch",
				"caseIgnoreSubstringsMatch", "1.3.6.1.4.1.1466.115.121.1.26",
				-1, false, true, false, AttributeUsage.USER_APPLICATIONS));
		schema.addObjectclass(new ObjectClass("1.3.6.1.3.1",
				new String[] { "testObjectclass" }, "a test objectclass", null,
				false, ObjectClassType.STRUCTURAL, new String[] { "attr1" },
				null));
		schema.resolveDependencies();

		List attrs = new ArrayList();
		attrs.add(new AttributeTypeAndValues("cn", new AttributeValues().add(
				"New Entry").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("objectclass",
				new AttributeValues().add("testObjectclass")
						.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("attr1", new AttributeValues()
				.add("attr1value").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("attr2", new AttributeValues()
				.add("attr2value").getValuesAsByteArray()));

		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);
		LDAPMessage message = new LDAPMessage(1, new AddRequest(
				"cn=New Entry,c=us", attrList));

		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		handlerMock.sendResponse(new AddResponse(
				LDAPResultCode.UNDEF_ATTR_TYPE, "",
				"attr2: attribute type undefined"));
		handlerControl.replay();

		stage.enqueue(createEventForMessageProcessor(eventMock));
		eventControl.verify();
		handlerControl.verify();
	}

	public void testAddDnOids() throws Exception {
		createC_us_entry();
		stage.enableSchemaChecking();
		init();
		Schema schema = Schema.getInstance();
		schema.addAttributeType(new AttributeType("1.3.6.1.3.2",
				new String[] { "attr1" }, "a test attr", false, null,
				"caseIgnoreMatch", "caseIgnoreOrderingMatch",
				"caseIgnoreSubstringsMatch", "1.3.6.1.4.1.1466.115.121.1.26",
				-1, false, true, false, AttributeUsage.USER_APPLICATIONS));
		schema.addObjectclass(new ObjectClass("1.3.6.1.3.1",
				new String[] { "testObjectclass" }, "a test objectclass",
				new String[] { "testObjectclass2" }, false,
				ObjectClassType.STRUCTURAL, new String[] { "attr1" }, null));
		schema.addAttributeType(new AttributeType("1.3.6.1.3.3",
				new String[] { "attr2" }, "a test attr", false, null,
				"caseIgnoreMatch", "caseIgnoreOrderingMatch",
				"caseIgnoreSubstringsMatch", "1.3.6.1.4.1.1466.115.121.1.26",
				-1, false, true, false, AttributeUsage.USER_APPLICATIONS));
		schema.addObjectclass(new ObjectClass("1.3.6.1.3.4",
				new String[] { "testObjectclass2" }, "a test objectclass",
				null, false, ObjectClassType.STRUCTURAL,
				new String[] { "attr2" }, null));
		schema.resolveDependencies();

		List attrs = new ArrayList();
		attrs.add(new AttributeTypeAndValues("objectclass",
				new AttributeValues().add("testObjectclass")
						.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("attr1", new AttributeValues()
				.add("attr1value").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("attr2", new AttributeValues()
				.add("attr2value").getValuesAsByteArray()));

		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);
		LDAPMessage message = new LDAPMessage(1, new AddRequest(
				"1.3.6.1.3.2=attr1value,c=us", attrList));

		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		handlerMock.sendResponse(new AddResponse(LDAPResultCode.SUCCESS));
		handlerControl.replay();

		stage.enqueue(createEventForMessageProcessor(eventMock));
		eventControl.verify();
		handlerControl.verify();

		Object lookup = backend.get(dn("attr1=attr1value,c=us"));
		assertTrue(lookup instanceof LdapEntry);
		LdapEntry d = (LdapEntry) lookup;
		LdapAttributes attributes = d.getAttributes();
		assertTrue(attributes.get("objectclass").contains("testObjectclass"));
		assertTrue(attributes.get("attr1").contains("attr1value"));
		assertTrue(attributes.get("attr2").contains("attr2value"));

	}

	public void testAddInvalidDn() throws Exception {
		createC_us_entry();
		List attrs = new ArrayList();
		attrs.add(new AttributeTypeAndValues("objectclass",
				new AttributeValues().add("top").add("organizationalPerson")
						.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("cn", new AttributeValues().add(
				"New Entry").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("email", new AttributeValues()
				.add("nobody@nodomain.com").getValuesAsByteArray()));

		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);
		LDAPMessage message = new LDAPMessage(1, new AddRequest(
				"cn=New Entry ,c=us", attrList));

		stage.setNameParser(new NameParser() {

			public Name parse(String name) throws NamingException {
				throw new LdapInvalidDnSyntaxException();
			}
		});

		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		handlerMock.sendResponse(new AddResponse(
				LDAPResultCode.INVALID_DN_SYNTAX, "", "Invalid DN"));
		handlerControl.replay();

		stage.enqueue(createEventForMessageProcessor(eventMock));
		eventControl.verify();
		handlerControl.verify();

		stage.setNameParser(new Rfc2253NameParserImpl());
		Object lookup = backend.get(dn("cn=New Entry,c=us"));
		assertNull(lookup);

	}

	public void testAddRequestInvalidDn2() throws Exception {
		// attributes in the rdn must be present on the entry
		/*
		 * For result codes of noSuchObject, aliasProblem, invalidDNSyntax and
		 * aliasDereferencingProblem, the matchedDN field is set to the name of
		 * the lowest entry (object or alias) in the directory that was matched.
		 * If no aliases were dereferenced while attempting to locate the entry,
		 * this will be a truncated form of the name provided, or if aliases
		 * were dereferenced, of the resulting name, as defined in section 12.5
		 * of X.511 [8]. The matchedDN field is to be set to a zero length
		 * string with all other result codes.
		 */
		{
			stage.enableSchemaChecking();
			populateBranchEntries();
			List attrs = new ArrayList();
			attrs
					.add(new AttributeTypeAndValues("objectclass",
							new AttributeValues().add("top").add(
									"organizationalPerson")
									.getValuesAsByteArray()));
			attrs.add(new AttributeTypeAndValues("cn", new AttributeValues()
					.add("New Entry").getValuesAsByteArray()));
			attrs.add(new AttributeTypeAndValues("sn", new AttributeValues()
					.add("sn1").getValuesAsByteArray()));
			attrs.add(new AttributeTypeAndValues("telephoneNumber",
					new AttributeValues().add("916885114")
							.getValuesAsByteArray()));

			AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
					attrs);
			LDAPMessage message = new LDAPMessage(1, new AddRequest(
					"cn=New Entry1,c=US", attrList)); // the entry has no
			// cn=New Entry1

			eventMock.getMessage();
			eventControl.setReturnValue(message);
			eventMock.getResponseHandler();
			eventControl.setReturnValue(handlerMock);
			eventControl.replay();

			handlerMock.sendResponse(new AddResponse(
					LDAPResultCode.INVALID_DN_SYNTAX, "",
					"DN should contain attribute 'cn'"));
			handlerControl.replay();

			stage.enqueue(createEventForMessageProcessor(eventMock));
			eventControl.verify();
			handlerControl.verify();

			stage.setNameParser(new Rfc2253NameParserImpl());
			Object lookup = backend.get(dn("cn=New Entry,c=us"));
			assertNull(lookup);

		}
	}

	private void operationalAttributeTest(String attrName, String attrValue)
			throws NamingException {
		createC_us_entry();
		List attrs = new ArrayList();
		attrs.add(new AttributeTypeAndValues("objectclass",
				new AttributeValues().add("top").add("organizationalPerson")
						.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("cn", new AttributeValues().add(
				"New Entry").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues(attrName, new AttributeValues()
				.add(attrValue).getValuesAsByteArray()));
		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);
		LDAPMessage message = new LDAPMessage(1, new AddRequest(
				"cn=New entry,c=us", attrList));
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();
		handlerMock.sendResponse(new AddResponse(
				LDAPResultCode.CONSTRAINT_VIOLATION, "", attrName
						+ ": no user modification allowed"));
		handlerControl.replay();
		stage.enqueue(createEventForMessageProcessor(eventMock));
		eventControl.verify();
		handlerControl.verify();
		Object lookup = backend.get(dn("cn=New Entry,c=us"));
		assertNull("this entry should be never added", lookup);
	}

	public void testSearchRequestEquals() {
		testAddRequest();
		LDAPMessage message = createSearchRequest();

		// TODO change to use handlerMock
		ResponseHandler responseHandler = new ResponseHandler() {

			public void sendResponse(LDAPResponse response) {
				resultMessagesReceived++;
				if (response instanceof SearchResultDone) {
					SearchResultDone srd = (SearchResultDone) response;
					assertEquals(LDAPResultCode.SUCCESS, srd.getResult());
				} else if (response instanceof SearchResultEntry) {
					SearchResultEntry sre = (SearchResultEntry) response;
					assertEquals("cn=New Entry", sre.getObjectName());
				} else {
					fail("Unexpected response " + response);
				}
			}
		};

		final LdapMessageEvent event = new LdapMessageEventImpl(message,
				responseHandler);

		stage.enqueue(createEventForMessageProcessor(event));
		assertEquals(2, resultMessagesReceived);
	}

	public void testSearchRequestEqualsNotFound() {
		populate();

		LDAPMessage message = new LDAPMessage(1, new SearchRequest("",
				Scope.WHOLE_SUBTREE, DerefAliases.NEVER_DEREF_ALIASES, 0, 0,
				false, new EqMatchFilter("cn", "noexists"), null));

		ResponseHandler responseHandler = new ResponseHandler() {

			public void sendResponse(LDAPResponse response) {
				resultMessagesReceived++;
				assertTrue(resultMessagesReceived <= 2);
				if (response instanceof SearchResultDone) {
					SearchResultDone srd = (SearchResultDone) response;
					assertEquals(1, resultMessagesReceived);
					assertEquals(LDAPResultCode.SUCCESS, srd.getResult());
				} else if (response instanceof SearchResultEntry) {
					SearchResultEntry sre = (SearchResultEntry) response;
					assertEquals(2, resultMessagesReceived);
					fail("It shouldn't find any entry but found "
							+ sre.getObjectName());
				} else {
					fail("Unexpected response " + response);
				}
			}
		};

		final LdapMessageEvent event = new LdapMessageEventImpl(message,
				responseHandler);

		stage.enqueue(createEventForMessageProcessor(event));
		assertEquals(1, resultMessagesReceived);

	}

	public void testSearchRequestEqualsBaseDn() {
		populate();
		LDAPMessage message = createSearchRequest("ou=My organization1",
				new EqMatchFilter("sn", "snPerson1"));

		ResponseHandler responseHandler = new ResponseHandler() {

			// TODO change using handlerMock, etc
			public void sendResponse(LDAPResponse response) {
				resultMessagesReceived++;
				if (response instanceof SearchResultDone) {
					SearchResultDone srd = (SearchResultDone) response;
					assertEquals(LDAPResultCode.SUCCESS, srd.getResult());
				} else if (response instanceof SearchResultEntry) {
					SearchResultEntry sre = (SearchResultEntry) response;
					assertEquals("cn=Person1,ou=My organization1", sre
							.getObjectName());
				} else {
					fail("Unexpected response " + response);
				}
			}
		};

		final LdapMessageEvent event = new LdapMessageEventImpl(message,
				responseHandler);

		stage.enqueue(createEventForMessageProcessor(event));
		assertEquals(2, resultMessagesReceived);

	}

	public void testSearchRequestEqualsMultivaluedAttr() {
		populate();
		LDAPMessage message = createSearchRequest("ou=My organization1",
				new EqMatchFilter("telephoneNumber", "11111111-2"));

		ResponseHandler responseHandler = new ResponseHandler() {

			public void sendResponse(LDAPResponse response) {
				resultMessagesReceived++;
				if (response instanceof SearchResultDone) {
					SearchResultDone srd = (SearchResultDone) response;
					assertEquals(LDAPResultCode.SUCCESS, srd.getResult());
				} else if (response instanceof SearchResultEntry) {
					SearchResultEntry sre = (SearchResultEntry) response;
					assertEquals("cn=Person1,ou=My organization1", sre
							.getObjectName());
				} else {
					fail("Unexpected response " + response);
				}
			}
		};

		final LdapMessageEvent event = new LdapMessageEventImpl(message,
				responseHandler);

		stage.enqueue(createEventForMessageProcessor(event));
		assertEquals(2, resultMessagesReceived);
	}

	public void testSearchRequestPresence() {
		testAddRequest();
		LDAPMessage message = new LDAPMessage(1, new SearchRequest("",
				Scope.WHOLE_SUBTREE, DerefAliases.NEVER_DEREF_ALIASES, 0, 0,
				false, new PresentFilter("email"), null));
		;

		ResponseHandler responseHandler = new ResponseHandler() {

			public void sendResponse(LDAPResponse response) {
				resultMessagesReceived++;
				if (response instanceof SearchResultDone) {
					SearchResultDone srd = (SearchResultDone) response;
					assertEquals(LDAPResultCode.SUCCESS, srd.getResult());
				} else if (response instanceof SearchResultEntry) {
					SearchResultEntry sre = (SearchResultEntry) response;
					assertEquals("cn=New Entry", sre.getObjectName());
				} else {
					fail("Unexpected response " + response);
				}
			}
		};

		final LdapMessageEvent event = new LdapMessageEventImpl(message,
				responseHandler);

		stage.enqueue(createEventForMessageProcessor(event));
		assertEquals(2, resultMessagesReceived);

	}

	public void testSearchRequestGreater() {
		populate();
		LDAPMessage message = new LDAPMessage(1, new SearchRequest("",
				Scope.WHOLE_SUBTREE, DerefAliases.NEVER_DEREF_ALIASES, 0, 0,
				false, new GreaterOrEqualFilter("dnQualifier", "qualifier1"),
				null));

		// cn=person1,ou=My organization1 should match because dnQualifier
		// {"qualifier0","qualifier2"}
		// cn=person2, ou=My organization2 should match because dnQualifier
		// {"qualifier0","qualifier3"}

		ResponseHandler responseHandler = new ResponseHandler() {

			public void sendResponse(LDAPResponse response) {
				resultMessagesReceived++;
				assertTrue(resultMessagesReceived <= 3);
				if (response instanceof SearchResultDone) {
					SearchResultDone srd = (SearchResultDone) response;
					assertEquals(3, resultMessagesReceived);
					assertEquals(LDAPResultCode.SUCCESS, srd.getResult());
				} else if (response instanceof SearchResultEntry) {
					SearchResultEntry sre = (SearchResultEntry) response;
					assertTrue((resultMessagesReceived == 1)
							|| (resultMessagesReceived == 2));
					assertTrue(sre.getObjectName()
							+ " doesn't start with cn=person", sre
							.getObjectName().startsWith("cn=Person"));
				} else {
					fail("Unexpected response " + response);
				}
			}
		};

		final LdapMessageEvent event = new LdapMessageEventImpl(message,
				responseHandler);

		stage.enqueue(createEventForMessageProcessor(event));
		assertEquals(3, resultMessagesReceived);

	}

	public void testSearchRequestLess() {
		populate();
		LDAPMessage message = new LDAPMessage(1,
				new SearchRequest("", Scope.WHOLE_SUBTREE,
						DerefAliases.NEVER_DEREF_ALIASES, 0, 0, false,
						new LessOrEqualFilter("dnQualifier", "qualifier2"),
						null));

		ResponseHandler responseHandler = new ResponseHandler() {

			public void sendResponse(LDAPResponse response) {
				resultMessagesReceived++;
				assertTrue(resultMessagesReceived <= 3);
				if (response instanceof SearchResultDone) {
					SearchResultDone srd = (SearchResultDone) response;
					assertEquals(3, resultMessagesReceived);
					assertEquals(LDAPResultCode.SUCCESS, srd.getResult());
				} else if (response instanceof SearchResultEntry) {
					SearchResultEntry sre = (SearchResultEntry) response;
					assertTrue((resultMessagesReceived == 1)
							|| (resultMessagesReceived == 2));
					assertTrue(sre.getObjectName()
							+ " doesn't start with cn=person", sre
							.getObjectName().startsWith("cn=Person"));
				} else {
					fail("Unexpected response " + response);
				}
			}
		};

		final LdapMessageEvent event = new LdapMessageEventImpl(message,
				responseHandler);

		stage.enqueue(createEventForMessageProcessor(event));
		assertEquals(3, resultMessagesReceived);
	}

	public void testSearchRequestSubstringInitial() {
		populate();
		LDAPMessage message = new LDAPMessage(1, new SearchRequest("",
				Scope.WHOLE_SUBTREE, DerefAliases.NEVER_DEREF_ALIASES, 0, 0,
				false, new SubstringFilter("cn", new SubstringValue("Person",
						new String[0], null)), null));

		ResponseHandler responseHandler = new ResponseHandler() {

			public void sendResponse(LDAPResponse response) {
				resultMessagesReceived++;
				assertTrue(resultMessagesReceived <= 3);
				if (response instanceof SearchResultDone) {
					SearchResultDone srd = (SearchResultDone) response;
					assertEquals(3, resultMessagesReceived);
					assertEquals(LDAPResultCode.SUCCESS, srd.getResult());
				} else if (response instanceof SearchResultEntry) {
					SearchResultEntry sre = (SearchResultEntry) response;
					assertTrue((resultMessagesReceived == 1)
							|| (resultMessagesReceived == 2));
					assertTrue(sre.getObjectName()
							+ " doesn't start with cn=person", sre
							.getObjectName().startsWith("cn=Person"));
				} else {
					fail("Unexpected response " + response);
				}
			}
		};

		final LdapMessageEvent event = new LdapMessageEventImpl(message,
				responseHandler);

		stage.enqueue(createEventForMessageProcessor(event));
		assertEquals(3, resultMessagesReceived);
	}

	public void testSearchRequestSubstringAny() {
		populate();
		LDAPMessage message = new LDAPMessage(1, new SearchRequest("",
				Scope.WHOLE_SUBTREE, DerefAliases.NEVER_DEREF_ALIASES, 0, 0,
				false, new SubstringFilter("cn", new SubstringValue(null,
						new String[] { "erson" }, null)), null));

		ResponseHandler responseHandler = new ResponseHandler() {

			public void sendResponse(LDAPResponse response) {
				resultMessagesReceived++;
				assertTrue(resultMessagesReceived <= 3);
				if (response instanceof SearchResultDone) {
					SearchResultDone srd = (SearchResultDone) response;
					assertEquals(3, resultMessagesReceived);
					assertEquals(LDAPResultCode.SUCCESS, srd.getResult());
				} else if (response instanceof SearchResultEntry) {
					SearchResultEntry sre = (SearchResultEntry) response;
					assertTrue((resultMessagesReceived == 1)
							|| (resultMessagesReceived == 2));
					assertTrue(sre.getObjectName()
							+ " doesn't start with cn=person", sre
							.getObjectName().startsWith("cn=Person"));
				} else {
					fail("Unexpected response " + response);
				}
			}
		};

		final LdapMessageEvent event = new LdapMessageEventImpl(message,
				responseHandler);

		stage.enqueue(createEventForMessageProcessor(event));
		assertEquals(3, resultMessagesReceived);
	}

	public void testSearchRequestSubstringFinal() {
		populate();
		LDAPMessage message = new LDAPMessage(1, new SearchRequest("",
				Scope.WHOLE_SUBTREE, DerefAliases.NEVER_DEREF_ALIASES, 0, 0,
				false, new SubstringFilter("cn", new SubstringValue(null,
						new String[0], "2")), null));

		ResponseHandler responseHandler = new ResponseHandler() {

			public void sendResponse(LDAPResponse response) {
				resultMessagesReceived++;
				assertTrue(resultMessagesReceived <= 3);
				if (response instanceof SearchResultDone) {
					SearchResultDone srd = (SearchResultDone) response;
					assertEquals(2, resultMessagesReceived);
					assertEquals(LDAPResultCode.SUCCESS, srd.getResult());
				} else if (response instanceof SearchResultEntry) {

					assertTrue(resultMessagesReceived == 1);
					SearchResultEntry sre = (SearchResultEntry) response;
					assertEquals("cn=Person2,ou=My organization2", sre
							.getObjectName());
				} else {
					fail("Unexpected response " + response);
				}
			}
		};

		final LdapMessageEvent event = new LdapMessageEventImpl(message,
				responseHandler);

		stage.enqueue(createEventForMessageProcessor(event));
		assertEquals(2, resultMessagesReceived);
	}

	public void testSearchRequestSubstringInitialAnyFinal() {
		populate();
		LDAPMessage message = new LDAPMessage(1, new SearchRequest("",
				Scope.WHOLE_SUBTREE, DerefAliases.NEVER_DEREF_ALIASES, 0, 0,
				false, new SubstringFilter("cn", new SubstringValue("p",
						new String[] { "e" }, "2")), null));

		ResponseHandler responseHandler = new ResponseHandler() {

			public void sendResponse(LDAPResponse response) {
				resultMessagesReceived++;
				assertTrue(resultMessagesReceived <= 2);
				if (response instanceof SearchResultDone) {
					SearchResultDone srd = (SearchResultDone) response;
					assertEquals(2, resultMessagesReceived);
					assertEquals(LDAPResultCode.SUCCESS, srd.getResult());
				} else if (response instanceof SearchResultEntry) {

					assertTrue(resultMessagesReceived == 1);
					SearchResultEntry sre = (SearchResultEntry) response;
					assertEquals("cn=Person2,ou=My organization2", sre
							.getObjectName());
				} else {
					fail("Unexpected response " + response);
				}
			}
		};

		final LdapMessageEvent event = new LdapMessageEventImpl(message,
				responseHandler);

		stage.enqueue(createEventForMessageProcessor(event));
		assertEquals(2, resultMessagesReceived);
	}

	public void testSearchRequestApprox() {
		populate();
		LDAPMessage message = new LDAPMessage(1, new SearchRequest("",
				Scope.WHOLE_SUBTREE, DerefAliases.NEVER_DEREF_ALIASES, 0, 0,
				false, new ApproxMatchFilter("sn", "Gimenez"), null));

		ResponseHandler responseHandler = new ResponseHandler() {

			public void sendResponse(LDAPResponse response) {
				resultMessagesReceived++;
				assertTrue(resultMessagesReceived <= 2);
				if (response instanceof SearchResultDone) {
					SearchResultDone srd = (SearchResultDone) response;
					assertEquals(2, resultMessagesReceived);
					assertEquals(LDAPResultCode.SUCCESS, srd.getResult());
				} else if (response instanceof SearchResultEntry) {

					assertTrue(resultMessagesReceived == 1);
					SearchResultEntry sre = (SearchResultEntry) response;
					assertEquals("cn=Person2,ou=My organization2", sre
							.getObjectName());
				} else {
					fail("Unexpected response " + response);
				}
			}
		};

		final LdapMessageEvent event = new LdapMessageEventImpl(message,
				responseHandler);

		stage.enqueue(createEventForMessageProcessor(event));
		assertEquals(2, resultMessagesReceived);
	}

	public void testSearchRequestBaseObjectScope() throws Exception {
		populateBranchEntries();
		populatePersonEntries(2);
		LDAPMessage message = new LDAPMessage(1, new SearchRequest("c=US",
				Scope.BASE_OBJECT, DerefAliases.NEVER_DEREF_ALIASES, 0, 0,
				false, new PresentFilter("objectclass"), null));

		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		AttributeTypeAndValuesList attributeList = new AttributeTypeAndValuesList();
		LdapAttributes attrsforC_US = TestUtils.createAttrsForC_US();
		attributeList.addAll(TestUtils.convertToNamingAttributes(attrsforC_US));
		handlerMock.sendResponse(new SearchResultEntry("c=US", attributeList));
		handlerMock.sendResponse(new SearchResultDone(LDAPResultCode.SUCCESS));
		handlerControl.replay();

		stage.enqueue(createEventForMessageProcessor(eventMock));
		eventControl.verify();
		handlerControl.verify();
	}

	public void testSearchRequestOneLevelScope() throws Exception {
		populateBranchEntries();
		populatePersonEntries(2);
		LDAPMessage message = new LDAPMessage(1, new SearchRequest("c=US",
				Scope.ONE_LEVEL, DerefAliases.NEVER_DEREF_ALIASES, 0, 0, false,
				new PresentFilter("objectclass"), null));

		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		AttributeTypeAndValuesList attributeList = null;
		attributeList = new AttributeTypeAndValuesList();
		attributeList.addAll(TestUtils.convertToNamingAttributes(TestUtils
				.createAttrsForO_org("org2")));
		handlerMock.sendResponse(new SearchResultEntry("o=org2,c=US",
				attributeList));
		attributeList = new AttributeTypeAndValuesList();
		attributeList.addAll(TestUtils.convertToNamingAttributes(TestUtils
				.createAttrsForO_org("org1")));
		handlerMock.sendResponse(new SearchResultEntry("o=org1,c=US",
				attributeList));

		handlerMock.sendResponse(new SearchResultDone(LDAPResultCode.SUCCESS));
		handlerControl.replay();

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

	}

	public void testSearchRequestWholeSubtreeScope() throws Exception {
		populateBranchEntries();
		populatePersonEntries(2);
		LDAPMessage message = new LDAPMessage(1, new SearchRequest("c=US",
				Scope.WHOLE_SUBTREE, DerefAliases.NEVER_DEREF_ALIASES, 0, 0,
				false, new PresentFilter("objectclass"), null));

		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		AttributeTypeAndValuesList attributeList = null;

		// c=US
		attributeList = new AttributeTypeAndValuesList();
		attributeList.addAll(TestUtils.convertToNamingAttributes(TestUtils
				.createAttrsForC_US()));
		final SearchResultEntry searchResultEntryC_US = new SearchResultEntry(
				"c=US", attributeList);
		handlerMock.sendResponse(searchResultEntryC_US);

		// o=org2,c=US
		attributeList = new AttributeTypeAndValuesList();
		attributeList.addAll(TestUtils.convertToNamingAttributes(TestUtils
				.createAttrsForO_org("org2")));
		final SearchResultEntry searchResultEntryO_org2_C_US = new SearchResultEntry(
				"o=org2,c=US", attributeList);
		handlerMock.sendResponse(searchResultEntryO_org2_C_US);

		// cn=person2,o=org2,c=US
		attributeList = new AttributeTypeAndValuesList();
		attributeList.addAll(TestUtils.convertToNamingAttributes(TestUtils
				.createAttrsForCN_person("person2")));
		final SearchResultEntry searchResultEntryCN_person2_O_org2_C_US = new SearchResultEntry(
				"cn=person2,o=org2,c=US", attributeList);
		handlerMock.sendResponse(searchResultEntryCN_person2_O_org2_C_US);
		// cn=person1,o=org2,c=US
		attributeList = new AttributeTypeAndValuesList();
		attributeList.addAll(TestUtils.convertToNamingAttributes(TestUtils
				.createAttrsForCN_person("person1")));
		final SearchResultEntry searchResultEntryCN_person1_O_org2_C_US = new SearchResultEntry(
				"cn=person1,o=org2,c=US", attributeList);
		handlerMock.sendResponse(searchResultEntryCN_person1_O_org2_C_US);

		// o=org1,c=US
		attributeList = new AttributeTypeAndValuesList();
		attributeList.addAll(TestUtils.convertToNamingAttributes(TestUtils
				.createAttrsForO_org("org1")));
		final SearchResultEntry searchResultEntry_O_org1_C_US = new SearchResultEntry(
				"o=org1,c=US", attributeList);
		handlerMock.sendResponse(searchResultEntry_O_org1_C_US);

		// cn=person2,o=org1,c=US
		attributeList = new AttributeTypeAndValuesList();
		attributeList.addAll(TestUtils.convertToNamingAttributes(TestUtils
				.createAttrsForCN_person("person2")));
		final SearchResultEntry searchResultEntry_CN_person1_O_org1_C_US = new SearchResultEntry(
				"cn=person2,o=org1,c=US", attributeList);
		handlerMock.sendResponse(searchResultEntry_CN_person1_O_org1_C_US);

		// cn=person1,o=org1,c=US
		attributeList = new AttributeTypeAndValuesList();
		attributeList.addAll(TestUtils.convertToNamingAttributes(TestUtils
				.createAttrsForCN_person("person1")));
		final SearchResultEntry searchResultEntryCN_person1_O_org1_C_us = new SearchResultEntry(
				"cn=person1,o=org1,c=US", attributeList);
		handlerMock.sendResponse(searchResultEntryCN_person1_O_org1_C_us);

		handlerMock.sendResponse(new SearchResultDone(LDAPResultCode.SUCCESS));
		handlerControl.replay();

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

	}

	public void testSearchRequestSizeLimit() throws Exception {
		populateBranchEntries();
		populatePersonEntries(50);

		LDAPMessage message = new LDAPMessage(1, new SearchRequest("c=US",
				Scope.WHOLE_SUBTREE, DerefAliases.NEVER_DEREF_ALIASES, 10, 0,
				false, new PresentFilter("objectclass"), null));

		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		AttributeTypeAndValuesList attributeList = null;
		attributeList = new AttributeTypeAndValuesList();
		attributeList.addAll(TestUtils.convertToNamingAttributes(TestUtils
				.createAttrsForO_org("org2")));
		handlerMock.sendResponse(new SearchResultEntry("o=org2,c=US",
				attributeList));
		handlerControl.setMatcher(MockControl.ALWAYS_MATCHER);
		handlerControl.setVoidCallable(10);

		handlerMock.sendResponse(new SearchResultDone(LDAPResultCode.SUCCESS));
		handlerControl.replay();

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

	}

	public void testSearchRequestTimeLimit() throws Exception {
		populateBranchEntries();
		populatePersonEntries(50);

		// final JlettyHierCtx ctx2 = new JlettyHierCtx(null);
		Map myBackend = (Map) Proxy.newProxyInstance(
				Map.class.getClassLoader(), new Class[] { Map.class },
				new InvocationHandler() {
					public Object invoke(Object arg0, Method arg1, Object[] arg2)
							throws Throwable {
						Thread.sleep(1001); // wait
						return arg1.invoke(backend, arg2);
					}
				});
		stage = new MessageProcessorStage(myBackend);

		handlerControl = MockControl.createNiceControl(ResponseHandler.class);
		handlerMock = (ResponseHandler) handlerControl.getMock();

		LDAPMessage message = new LDAPMessage(1, new SearchRequest("c=US",
				Scope.WHOLE_SUBTREE, DerefAliases.NEVER_DEREF_ALIASES, 0, 1,
				false, new PresentFilter("objectclass"), null));

		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		handlerMock.sendResponse(new SearchResultDone(
				LDAPResultCode.TIME_LIMIT_EXCEEDED));
		handlerControl.replay();

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

	}

	public void testSearchRequestTypesOnly() throws Exception {
		populateBranchEntries();
		populatePersonEntries(2);
		LDAPMessage message = new LDAPMessage(1, new SearchRequest("c=US",
				Scope.BASE_OBJECT, DerefAliases.NEVER_DEREF_ALIASES, 0, 0,
				true, new PresentFilter("objectclass"), null));

		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		AttributeTypeAndValuesList attributeList = new AttributeTypeAndValuesList();
		attributeList.addAll(attrsOnly(TestUtils
				.convertToNamingAttributes(TestUtils.createAttrsForC_US())));
		handlerMock.sendResponse(new SearchResultEntry("c=US", attributeList));
		handlerMock.sendResponse(new SearchResultDone(LDAPResultCode.SUCCESS));
		handlerControl.replay();

		stage.enqueue(createEventForMessageProcessor(eventMock));
		eventControl.verify();
		handlerControl.verify();
	}

	public void testSearchRequestAttributes() throws Exception {
		populateBranchEntries();
		populatePersonEntries(2);
		List attributes = Arrays.asList(new String[] { "searchGuide" });
		LDAPMessage message = new LDAPMessage(1, new SearchRequest("c=US",
				Scope.ONE_LEVEL, DerefAliases.NEVER_DEREF_ALIASES, 0, 0, false,
				new PresentFilter("objectclass"), attributes));

		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		AttributeTypeAndValuesList attributeList = null;
		attributeList = new AttributeTypeAndValuesList();
		attributeList.addAll(attributes(TestUtils
				.convertToNamingAttributes(TestUtils
						.createAttrsForO_org("org2")), attributes));
		handlerMock.sendResponse(new SearchResultEntry("o=org2,c=US",
				attributeList));
		attributeList = new AttributeTypeAndValuesList();
		attributeList.addAll(attributes(TestUtils
				.convertToNamingAttributes(TestUtils
						.createAttrsForO_org("org1")), attributes));
		handlerMock.sendResponse(new SearchResultEntry("o=org1,c=US",
				attributeList));
		handlerMock.sendResponse(new SearchResultDone(LDAPResultCode.SUCCESS));
		handlerControl.replay();

		stage.enqueue(createEventForMessageProcessor(eventMock));
		eventControl.verify();
		handlerControl.verify();
	}

	private Attributes attributes(Attributes attributes,
			final Collection onlyThisAttrs) {
		NamingEnumeration allAttrsEnumeration = attributes.getAll();
		List allAttrs = EnumerationUtils.toList(allAttrsEnumeration);
		Collection matchingAttrs = CollectionUtils.select(allAttrs,
				new Predicate() {
					public boolean evaluate(Object object) {
						Attribute attr = (Attribute) object;
						return onlyThisAttrs.contains(attr.getID());
					}
				});
		final BasicAttributes toReturn = new BasicAttributes();
		CollectionUtils.forAllDo(matchingAttrs, new Closure() {
			public void execute(Object input) {
				toReturn.put((Attribute) input);
			}
		});
		return toReturn;
	}

	private Attributes attrsOnly(Attributes attributes) {
		final BasicAttributes toReturn = new BasicAttributes();
		NamingEnumeration allIds = attributes.getIDs();
		HashSet allIdsSet = new HashSet();
		CollectionUtils.addAll(allIdsSet, allIds);
		CollectionUtils.forAllDo(allIdsSet, new Closure() {
			public void execute(Object input) {
				toReturn.put(new BasicAttribute(input.toString()));
			};
		});
		return toReturn;
	}

	public void testDeleteRequest() throws NamingException {
		populateBranchEntries();
		populateOrgPersonEntries(5);
		stage.enableSchemaChecking();

		LDAPMessage message = new LDAPMessage(1, new DeleteRequest(
				"cn=orgperson1,o=org1,c=US"));

		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock.sendResponse(new DelResponse(LDAPResultCode.SUCCESS));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

		Object toCheck = backend.get(dn("cn=orgperson1,o=org1,c=US"));
		assertNull("the entry should be deleted", toCheck);

	}

	public void testDeleteRequestFalseEntryHasChildren() throws NamingException {
		populateBranchEntries();
		populateOrgPersonEntries(5);
		stage.enableSchemaChecking();

		LDAPMessage message = new LDAPMessage(1, new DeleteRequest(
				"o=org1,c=US"));

		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock.sendResponse(new DelResponse(
				LDAPResultCode.NOT_ALLOWED_ON_NON_LEAF));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

		// check that the no changes has been performed on the dc=com,c=us
		// entry
		final DistinguishedName dnToCheck = dn("o=org1,c=US");
		LdapEntry toCheck = (LdapEntry) backend.get(dnToCheck);
		assertNotNull(toCheck);
		int i = CollectionUtils.countMatches(backend.keySet(), new Predicate() {
			public boolean evaluate(Object object) {
				DistinguishedName name = (DistinguishedName) object;
				return name.startsWith(dnToCheck) && !name.equals(dnToCheck);
			}
		});
		assertEquals(5, i);

	}

	public void testDeleteRequestFalseEntryNoSuchEntry() throws NamingException {
		populateBranchEntries();
		populateOrgPersonEntries(5);
		stage.enableSchemaChecking();

		LDAPMessage message = new LDAPMessage(1, new DeleteRequest(
				"o=org3,c=US"));

		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock.sendResponse(new DelResponse(LDAPResultCode.NO_SUCH_OBJECT,
				"c=US", "parent entry doesn't exist"));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

		// check that the no changes has been performed on the dc=com,c=us
		// entry
		final DistinguishedName dnToCheck = dn("o=org1,c=US");
		LdapEntry toCheck = (LdapEntry) backend.get(dnToCheck);
		assertNotNull(toCheck);
		int i = CollectionUtils.countMatches(backend.keySet(), new Predicate() {

			public boolean evaluate(Object object) {
				return ((Name) object).startsWith(dnToCheck)
						&& !dnToCheck.equals(object);
			}

		});
		assertEquals(5, i);

	}

	public void testDeleteRequestFalseEntryWithChildren2()
			throws NamingException {
		populateBranchEntries();
		populateOrgPersonEntries(1);
		stage.enableSchemaChecking();

		LDAPMessage message1 = new LDAPMessage(1, new DeleteRequest(
				"o=org1,c=US"));
		LDAPMessage message2 = new LDAPMessage(2, new DeleteRequest(
				"cn=orgperson1,o=org1,c=US"));
		LDAPMessage message3 = new LDAPMessage(3, new DeleteRequest(
				"o=org1,c=US"));
		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message1);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventMock.getMessage();
		eventControl.setReturnValue(message2);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventMock.getMessage();
		eventControl.setReturnValue(message3);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock.sendResponse(new DelResponse(
				LDAPResultCode.NOT_ALLOWED_ON_NON_LEAF));
		handlerMock.sendResponse(new DelResponse(LDAPResultCode.SUCCESS));
		handlerMock.sendResponse(new DelResponse(LDAPResultCode.SUCCESS));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus

		stage.enqueue(createEventForMessageProcessor(eventMock));
		stage.enqueue(createEventForMessageProcessor(eventMock));
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

		Object toCheck = backend.get(dn("o=org1,c=US"));
		assertNull(toCheck);

	}

	public void testModifyRequest() {
		populate();
		Modification[] mods = { new Modification(ModificationType.REPLACE,
				"description", new AttributeValues().add("another description")
						.getValuesAsByteArray()) };
		LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
				"cn=Person1,ou=My organization1", mods));

		ResponseHandler responseHandler = new ResponseHandler() {

			public void sendResponse(LDAPResponse response) {
				resultMessagesReceived++;
				assertTrue(resultMessagesReceived <= 1);
				if (response instanceof ModifyResponse) {
					ModifyResponse result = (ModifyResponse) response;
					assertEquals(1, resultMessagesReceived);
					assertEquals(LDAPResultCode.SUCCESS, result.getResult());
				} else {
					fail("Unexpected response " + response);
				}
			}
		};

		final LdapMessageEvent event = new LdapMessageEventImpl(message,
				responseHandler);

		stage.enqueue(createEventForMessageProcessor(event));
		assertEquals(1, resultMessagesReceived);
	}

	public void testModifyRequestAddTrue() throws Exception {
		populateBranchEntries();
		populatePersonEntries(5);
		Modification[] mods = { new Modification(ModificationType.ADD,
				"description", new AttributeValues().add("another description")
						.getValuesAsByteArray()) };
		LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
				"cn=person1,o=org1,c=US", mods));
		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock.sendResponse(new ModifyResponse(LDAPResultCode.SUCCESS));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

		// check that the change has been performed in the DIT
		LdapEntry toCheck = (LdapEntry) backend
				.get(dn("cn=person1,o=org1,c=US"));
		LdapAttributes attributes = toCheck.getAttributes();
		LdapAttribute attribute = attributes.get("description");
		assertEquals(2, attribute.size());
		assertTrue(attribute
				.contains(new DirectoryString("another description")));
		assertTrue(attribute.contains(new DirectoryString(
				"the person1 description")));
	}

	public void testModifyRequestAddFalseSingleValuedAttribute()
			throws Exception {
		// what problems can arise in modifying an entry.
		// add a value in a single valued attribute
		// preferredDeliveryMethod is a single-valued attribute but has no eq
		// match rule defined
		// aliasedObjectName is single-value and has eq match
		// presentationAddress is single-valued and has eq match
		// domainComponent is single-valued and has eq match
		// should throw constraintviolation attribute 'dc' cannot have multiple
		// values
		// organizationalPerson has this attribute
		populateBranchEntries();
		populatePersonEntries(5);
		populateDcCom();
		stage.enableSchemaChecking();

		Modification[] mods = { new Modification(ModificationType.ADD, "dc",
				new AttributeValues().add("anotherDcComponent")
						.getValuesAsByteArray()) };
		LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
				"dc=com,c=US", mods));
		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock.sendResponse(new ModifyResponse(
				LDAPResultCode.CONSTRAINT_VIOLATION, "",
				"attribute 'dc' cannot have multiple values"));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

		// check that no changes has been performed on the dc=com,c=us entry
		LdapEntry toCheck = (LdapEntry) backend.get(dn("dc=com,c=US"));
		LdapAttributes attributes = toCheck.getAttributes();
		LdapAttribute attribute = attributes.get("dc");
		assertEquals(1, attribute.size());
		assertTrue(attribute.contains(new IA5String("com")));
	}

	public void testModifyRequestAddFalseAttributeNotAllowedBySchema()
			throws Exception {
		populateBranchEntries();
		populatePersonEntries(5);
		stage.enableSchemaChecking();

		// person doesn't allow dc, should throw object class violation
		// attribute 'dc' not allowed
		Modification[] mods = { new Modification(ModificationType.ADD, "dc",
				new AttributeValues().add("com").getValuesAsByteArray()) };
		LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
				"cn=person1,o=org1,c=US", mods));
		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock.sendResponse(new ModifyResponse(
				LDAPResultCode.OBJECTCLASS_VIOLATION, "",
				"attribute 'dc' not allowed by objectclasses '[person]'"));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

		// check that the no changes has been performed on the dc=com,c=us entry
		LdapEntry toCheck = getEntry("cn=person1,o=org1,c=US");
		LdapAttributes attributes = toCheck.getAttributes();
		LdapAttribute attribute = attributes.get("dc");
		assertNull(attribute);
	}

	private LdapEntry getEntry(String entryDn) {
		LdapEntry toReturn = (LdapEntry) backend.get(dn(entryDn));
		return toReturn;
	}

	public void testModifyRequestAddFalseInvalidSyntax() throws Exception {
		// invalid syntax like telephoneNumber that has no digits
		populateBranchEntries();
		populatePersonEntries(5);

		Modification[] mods = { new Modification(ModificationType.ADD,
				"internationaliSDNNumber", new AttributeValues().add(
						"notatelephonenumber").getValuesAsByteArray()) };
		LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
				"cn=person1,o=org1,c=US", mods));

		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock
				.sendResponse(new ModifyResponse(
						LDAPResultCode.INVALID_ATTR_SYNTAX, "",
						"modify/add: internationaliSDNNumber: value #0 invalid per syntax"));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

		// check that the no changes has been performed on the dc=com,c=us entry
		LdapEntry toCheck = getEntry("cn=person1,o=org1,c=US");
		LdapAttributes attributes = toCheck.getAttributes();
		LdapAttribute attribute = attributes.get("dc");
		assertNull(attribute);
	}

	public void testModifyRequestAddFalseNoEqMatch() throws Exception {
		// telexNumber has no eqmatching rule
		// inappropiate matching (18)
		// additional info: modify/add: telexNumber: no equality matching rule
		populateBranchEntries();
		populateOrgPersonEntries(5);

		Modification[] mods = { new Modification(ModificationType.ADD,
				"telexNumber", new AttributeValues().add("aaa$bbb$ccc")
						.getValuesAsByteArray()) };
		LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
				"cn=orgperson1,o=org1,c=US", mods));

		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock.sendResponse(new ModifyResponse(
				LDAPResultCode.INAPPROPIATE_MATCHING, "",
				"modify/add: telexNumber: no equality matching rule"));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

		// check that the no changes has been performed on the dc=com,c=us entry
		LdapEntry toCheck = getEntry("cn=orgperson1,o=org1,c=US");
		LdapAttributes attributes = toCheck.getAttributes();
		LdapAttribute attribute = attributes.get("telexNumber");
		assertNotNull(attribute);
		assertEquals(1, attribute.size());
		assertTrue(attribute.contains(new TelexNumber("1212312$ES$3322333")));

	}

	public void testModifyRequestAddValueAlreadyExists() throws Exception {
		// Adding just 1 value
		// attribute with equalitymatch rule : telephoneNumber
		populateBranchEntries();
		populatePersonEntries(5); // the entries have telephoneNumber: +41 1
		// 268 1540
		// populateOrgPerson(5);

		Modification[] mods = { new Modification(ModificationType.ADD,
				"telephoneNumber", new AttributeValues().add("+41 1 268 1540")
						.getValuesAsByteArray()) };
		LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
				"cn=person1,o=org1,c=US", mods));

		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock.sendResponse(new ModifyResponse(
				LDAPResultCode.ATTR_OR_VALUE_EXISTS, "",
				"modify/add: telephoneNumber: value #0 already exists"));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

		// check that the no changes has been performed on the dc=com,c=us entry
		LdapEntry toCheck = getEntry("cn=person1,o=org1,c=US");
		LdapAttributes attributes = toCheck.getAttributes();
		LdapAttribute attribute = attributes.get("dc");
		assertNull(attribute);
	}

	public void testModifyRequestAddValueAlreadyExists2() throws Exception {
		// adding two values
		// attribute with equalitymatch rule : telephoneNumber
		populateBranchEntries();
		populatePersonEntries(5);
		// the entries have telephoneNumber: +41 1 268 1540

		Modification[] mods = { new Modification(ModificationType.ADD,
				"telephoneNumber", new AttributeValues().add("+41 1 268 1541")
						.add("+41 1 268 1540").getValuesAsByteArray()) };
		LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
				"cn=person1,o=org1,c=US", mods));

		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock.sendResponse(new ModifyResponse(
				LDAPResultCode.ATTR_OR_VALUE_EXISTS, "",
				"modify/add: telephoneNumber: value #1 already exists"));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

		// check that the no changes has been performed on the entry
		LdapEntry toCheck = getEntry("cn=person1,o=org1,c=US");
		LdapAttributes attributes = toCheck.getAttributes();
		LdapAttribute attribute = attributes.get("telephoneNumber");
		assertNotNull(attribute);
		assertEquals(1, attribute.size());
		assertTrue(attribute.contains(new TelephoneNumber("+41 1 268 1540"
				.getBytes("UTF-8"))));
	}

	public void testModifyRequestReplaceFalseInvalidSyntax() throws Exception {
		// creating an attribute if it did not already exists
		populateBranchEntries();
		populateOrgPersonEntries(5);

		// invalid because numericstring only allows digits
		Modification[] mods = { new Modification(ModificationType.REPLACE,
				"internationaliSDNNumber", new AttributeValues().add(
						"4112 681 542").getValuesAsByteArray()) };
		LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
				"cn=orgperson1,o=org1,c=US", mods));

		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock
				.sendResponse(new ModifyResponse(
						LDAPResultCode.INVALID_ATTR_SYNTAX, "",
						"modify/replace: internationaliSDNNumber: value #0 invalid per syntax"));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

		// check that the no changes has been performed on the dc=com,c=us entry
		LdapEntry toCheck = getEntry("cn=orgperson1,o=org1,c=US");
		LdapAttributes attributes = toCheck.getAttributes();
		LdapAttribute attribute = attributes.get("internationaliSDNNumber");
		assertNotNull(attribute);
		assertEquals(1, attribute.size());
		assertTrue(attribute.contains(new NumericString("999999999")));
	}

	public void testModifyRequestReplaceTrueNoEqMatch() throws Exception {
		// telexNumber has no equality matching rule
		populateBranchEntries();
		populateOrgPersonEntries(5);
		stage.enableSchemaChecking();

		Modification[] mods = { new Modification(ModificationType.REPLACE,
				"telexNumber", new AttributeValues().add("aaa$bbb$ccc")
						.getValuesAsByteArray()) };
		LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
				"cn=orgperson1,o=org1,c=US", mods));

		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock.sendResponse(new ModifyResponse(LDAPResultCode.SUCCESS));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

		// check that the no changes has been performed on the dc=com,c=us
		// entry
		LdapEntry toCheck = getEntry("cn=orgperson1,o=org1,c=US");
		LdapAttributes attributes = toCheck.getAttributes();
		LdapAttribute attribute = attributes.get("telexNumber");
		assertNotNull(attribute);
		assertEquals(1, attribute.size());
		assertTrue(attribute.contains(new TelexNumber("aaa$bbb$ccc")));
	}

	public void testModifyRequestReplaceFalseNotAllowed() throws Exception {
		// false because the attribute is not allowed
		{
			populateBranchEntries();
			populatePersonEntries(5);
			stage.enableSchemaChecking();

			// person doesn't allow dc, should throw object class violation
			// attribute 'dc' not allowed
			Modification[] mods = { new Modification(ModificationType.REPLACE,
					"dc", new AttributeValues().add("com")
							.getValuesAsByteArray()) };
			LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
					"cn=person1,o=org1,c=US", mods));
			// Setting mocks
			eventMock.getMessage();
			eventControl.setReturnValue(message);
			eventMock.getResponseHandler();
			eventControl.setReturnValue(handlerMock);
			eventControl.replay(); // eventControl&eventMock setted and ready

			handlerMock.sendResponse(new ModifyResponse(
					LDAPResultCode.OBJECTCLASS_VIOLATION, "",
					"attribute 'dc' not allowed by objectclasses '[person]'"));
			handlerControl.replay(); // handlerControl setted and ready

			// main test corpus
			stage.enqueue(createEventForMessageProcessor(eventMock));

			// verify mocks
			eventControl.verify();
			handlerControl.verify();

			// check that the no changes has been performed on the dc=com,c=us
			// entry
			LdapEntry toCheck = getEntry("cn=person1,o=org1,c=US");
			LdapAttributes attributes = toCheck.getAttributes();
			LdapAttribute attribute = attributes.get("dc");
			assertNull(attribute);
		}
	}

	public void testModifyRequestReplaceFalseNotMultivalued() throws Exception {
		// false because the attribute is single-value and we are replacing it
		// with multivalue
		{
			populateBranchEntries();
			populateOrgPersonEntries(5);
			stage.enableSchemaChecking();

			Modification[] mods = { new Modification(ModificationType.REPLACE,
					"preferredDeliveryMethod", new AttributeValues().add(
							"telephone").add("physical").getValuesAsByteArray()) };
			LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
					"cn=orgperson1,o=org1,c=US", mods));
			// Setting mocks
			eventMock.getMessage();
			eventControl.setReturnValue(message);
			eventMock.getResponseHandler();
			eventControl.setReturnValue(handlerMock);
			eventControl.replay(); // eventControl&eventMock setted and ready

			handlerMock
					.sendResponse(new ModifyResponse(
							LDAPResultCode.CONSTRAINT_VIOLATION, "",
							"attribute 'preferredDeliveryMethod' cannot have multiple values"));
			handlerControl.replay(); // handlerControl setted and ready

			// main test corpus
			stage.enqueue(createEventForMessageProcessor(eventMock));

			// verify mocks
			eventControl.verify();
			handlerControl.verify();

			// check that the no changes has been performed on the dc=com,c=us
			// entry
			LdapEntry toCheck = getEntry("cn=orgperson1,o=org1,c=US");
			LdapAttributes attributes = toCheck.getAttributes();
			LdapAttribute attribute = attributes.get("preferredDeliveryMethod");
			assertEquals(1, attribute.size());
			assertTrue(attribute.contains(new DeliveryMethod("telephone")));
		}
	}

	public void testModifyRequestDeleteRdnAttribute() throws Exception {
		{
			populateBranchEntries();
			populatePersonEntries(5);

			Modification[] mods = { new Modification(ModificationType.DELETE,
					"cn", new AttributeValues().add("another_person1")
							.getValuesAsByteArray()) };
			LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
					"cn=person1,o=org1,c=US", mods));
			// Setting mocks
			eventMock.getMessage();
			eventControl.setReturnValue(message);
			eventMock.getResponseHandler();
			eventControl.setReturnValue(handlerMock);
			eventControl.replay(); // eventControl&eventMock setted and ready

			handlerMock
					.sendResponse(new ModifyResponse(LDAPResultCode.SUCCESS));
			handlerControl.replay(); // handlerControl setted and ready

			// main test corpus
			stage.enqueue(createEventForMessageProcessor(eventMock));

			// verify mocks
			eventControl.verify();
			handlerControl.verify();

			// check that the no changes has been performed on the dc=com,c=us
			// entry
			LdapEntry toCheck = getEntry("cn=person1,o=org1,c=US");

			LdapAttributes attributes = toCheck.getAttributes();
			LdapAttribute attribute = attributes.get("cn");
			assertEquals(1, attribute.size());
			assertTrue(attribute.contains(new DirectoryString("person1")));
		}
	}

	public void testModifyRequestDeleteFalseNotAllowedOnRDN() throws Exception {
		// false because we cannot modify the rdn of an entry with modify
		/*
		 * The Modify Operation cannot be used to remove from an entry any of
		 * its distinguished values, those values which form the entry's
		 * relative distinguished name. An attempt to do so will result in the
		 * server returning the error notAllowedOnRDN. The Modify DN Operation
		 * described in section 4.9 is used to rename an entry.
		 */
		{
			populateBranchEntries();
			populatePersonEntries(5);
			stage.enableSchemaChecking();

			Modification[] mods = { new Modification(ModificationType.DELETE,
					"cn", new AttributeValues().add("person1")
							.getValuesAsByteArray()) };
			LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
					"cn=person1,o=org1,c=US", mods));
			// Setting mocks
			eventMock.getMessage();
			eventControl.setReturnValue(message);
			eventMock.getResponseHandler();
			eventControl.setReturnValue(handlerMock);
			eventControl.replay(); // eventControl&eventMock setted and ready

			handlerMock
					.sendResponse(new ModifyResponse(
							LDAPResultCode.NOT_ALLOWED_ON_RDN, "",
							"DN should contain attribute 'cn': this will lead to rdn modification"));
			handlerControl.replay(); // handlerControl setted and ready

			// main test corpus
			stage.enqueue(createEventForMessageProcessor(eventMock));

			// verify mocks
			eventControl.verify();
			handlerControl.verify();

			// check that the no changes has been performed on the dc=com,c=us
			// entry
			LdapEntry toCheck = getEntry("cn=person1,o=org1,c=US");
			LdapAttributes attributes = toCheck.getAttributes();
			LdapAttribute attribute = attributes.get("cn");
			assertEquals(2, attribute.size());
			assertTrue(attribute.contains(new DirectoryString("person1")));
			assertTrue(attribute
					.contains(new DirectoryString("another_person1")));
		}
	}

	public void testModifyRequestReplaceFalseNotAllowedOnRDN() throws Exception {
		// false because we cannot modify the rdn of an entry with modify
		/*
		 * The Modify Operation cannot be used to remove from an entry any of
		 * its distinguished values, those values which form the entry's
		 * relative distinguished name. An attempt to do so will result in the
		 * server returning the error notAllowedOnRDN. The Modify DN Operation
		 * described in section 4.9 is used to rename an entry.
		 */
		{
			populateBranchEntries();
			populatePersonEntries(5);
			stage.enableSchemaChecking();

			Modification[] mods = { new Modification(ModificationType.REPLACE,
					"cn", new AttributeValues().add("personx")
							.getValuesAsByteArray()) };
			LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
					"cn=person1,o=org1,c=US", mods));
			// Setting mocks
			eventMock.getMessage();
			eventControl.setReturnValue(message);
			eventMock.getResponseHandler();
			eventControl.setReturnValue(handlerMock);
			eventControl.replay(); // eventControl&eventMock setted and ready

			handlerMock
					.sendResponse(new ModifyResponse(
							LDAPResultCode.NOT_ALLOWED_ON_RDN, "",
							"DN should contain attribute 'cn': this will lead to rdn modification"));
			handlerControl.replay(); // handlerControl setted and ready

			// main test corpus
			stage.enqueue(createEventForMessageProcessor(eventMock));

			// verify mocks
			eventControl.verify();
			handlerControl.verify();

			// check that the no changes has been performed on the dc=com,c=us
			// entry
			LdapEntry toCheck = getEntry("cn=person1,o=org1,c=US");
			LdapAttributes attributes = toCheck.getAttributes();
			LdapAttribute attribute = attributes.get("cn");
			assertEquals(2, attribute.size());
			assertTrue(attribute.contains(new DirectoryString("person1")));
			assertTrue(attribute
					.contains(new DirectoryString("another_person1")));
		}
	}

	public void testModifyRequestReplaceTrueRdnAttribute() throws Exception {
		// false because we cannot modify the rdn of an entry with modify
		/*
		 * The Modify Operation cannot be used to remove from an entry any of
		 * its distinguished values, those values which form the entry's
		 * relative distinguished name. An attempt to do so will result in the
		 * server returning the error notAllowedOnRDN. The Modify DN Operation
		 * described in section 4.9 is used to rename an entry.
		 */

		{
			populateBranchEntries();
			populatePersonEntries(5);

			Modification[] mods = { new Modification(ModificationType.REPLACE,
					"cn", new AttributeValues().add("personx").add("person1")
							.getValuesAsByteArray()) };
			LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
					"cn=person1,o=org1,c=US", mods));
			// Setting mocks
			eventMock.getMessage();
			eventControl.setReturnValue(message);
			eventMock.getResponseHandler();
			eventControl.setReturnValue(handlerMock);
			eventControl.replay(); // eventControl&eventMock setted and ready

			handlerMock
					.sendResponse(new ModifyResponse(LDAPResultCode.SUCCESS));
			handlerControl.replay(); // handlerControl setted and ready

			// main test corpus
			stage.enqueue(createEventForMessageProcessor(eventMock));

			// verify mocks
			eventControl.verify();
			handlerControl.verify();

			// check that the no changes has been performed on the dc=com,c=us
			// entry
			LdapEntry toCheck = getEntry("cn=person1,o=org1,c=US");
			LdapAttributes attributes = toCheck.getAttributes();
			LdapAttribute attribute = attributes.get("cn");
			assertEquals(2, attribute.size());
			assertTrue(attribute.contains(new DirectoryString("person1")));
			assertTrue(attribute.contains(new DirectoryString("personx")));
		}
	}

	public void testModifyRequestReplaceTrue() throws Exception {
		// replace all values with the new ones
		populateBranchEntries();
		populatePersonEntries(5);

		Modification[] mods = { new Modification(ModificationType.REPLACE,
				"telephoneNumber", new AttributeValues().add("+41 1 268 1541")
						.add("+41 1 268 1542").getValuesAsByteArray()) };
		LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
				"cn=person1,o=org1,c=US", mods));

		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock.sendResponse(new ModifyResponse(LDAPResultCode.SUCCESS));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

		// check that the no changes has been performed on the dc=com,c=us entry
		LdapEntry toCheck = getEntry("cn=person1,o=org1,c=US");
		LdapAttributes attributes = toCheck.getAttributes();
		LdapAttribute attribute = attributes.get("telephoneNumber");
		assertNotNull(attribute);
		assertEquals(2, attribute.size());
		final TelephoneNumber expectedTelephoneNumber1 = new TelephoneNumber(
				"+41 1 268 1541".getBytes());
		assertTrue(attribute.contains(expectedTelephoneNumber1));
		final TelephoneNumber expectedTelephoneNumber2 = new TelephoneNumber(
				"+41 1 268 1542".getBytes());
		assertTrue(attribute.contains(expectedTelephoneNumber2));
	}

	public void testModifyRequestReplaceTrueCreatingAttribute()
			throws Exception {
		// creating an attribute if it did not already exists
		populateBranchEntries();
		populateOrgPersonEntries(5);

		Modification[] mods = { new Modification(ModificationType.REPLACE,
				"internationaliSDNNumber", new AttributeValues().add(
						"4112681541").add("4112681542").getValuesAsByteArray()) };
		LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
				"cn=orgperson1,o=org1,c=US", mods));

		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock.sendResponse(new ModifyResponse(LDAPResultCode.SUCCESS));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

		// check that the no changes has been performed on the dc=com,c=us entry
		LdapEntry toCheck = getEntry("cn=orgperson1,o=org1,c=US");
		LdapAttributes attributes = toCheck.getAttributes();
		LdapAttribute attribute = attributes.get("internationaliSDNNumber");
		assertNotNull(attribute);
		assertEquals(2, attribute.size());
		final NumericString expectedISDN1 = new NumericString("4112681541");
		assertTrue(attribute.contains(expectedISDN1));
		final NumericString expectedISDN2 = new NumericString("4112681542");
		assertTrue(attribute.contains(expectedISDN2));
	}

	public void testModifyRequestReplaceTrueDeletingAttribute()
			throws Exception {
		// deleting an attribute replacing it with no value
		populateBranchEntries();
		populatePersonEntries(5);

		Modification[] mods = { new Modification(ModificationType.REPLACE,
				"telephoneNumber", new AttributeValues().getValuesAsByteArray()) };
		LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
				"cn=person1,o=org1,c=US", mods));

		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay(); // eventControl&eventMock setted and ready

		handlerMock.sendResponse(new ModifyResponse(LDAPResultCode.SUCCESS));
		handlerControl.replay(); // handlerControl setted and ready

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

		// check that the no changes has been performed on the dc=com,c=us entry
		LdapEntry toCheck = getEntry("cn=person1,o=org1,c=US");
		LdapAttributes attributes = toCheck.getAttributes();
		LdapAttribute attribute = attributes.get("telephoneNumber");
		assertNull(attribute);
	}

	public void testModifyRequestReplaceTrueIgnoringNoValue() throws Exception {
		// ignore a replace with no value to an attribute that it doesn't exists
		{
			// deleting an attribute replacing it with no value
			populateBranchEntries();
			populateOrgPersonEntries(5);

			Modification[] mods = { new Modification(ModificationType.REPLACE,
					"internationaliSDNNumber", new AttributeValues()
							.getValuesAsByteArray()) };
			LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
					"cn=orgperson1,o=org1,c=US", mods));

			// Setting mocks
			eventMock.getMessage();
			eventControl.setReturnValue(message);
			eventMock.getResponseHandler();
			eventControl.setReturnValue(handlerMock);
			eventControl.replay(); // eventControl&eventMock setted and ready

			handlerMock
					.sendResponse(new ModifyResponse(LDAPResultCode.SUCCESS));
			handlerControl.replay(); // handlerControl setted and ready

			// main test corpus
			stage.enqueue(createEventForMessageProcessor(eventMock));

			// verify mocks
			eventControl.verify();
			handlerControl.verify();

			// check that the no changes has been performed on the dc=com,c=us
			// entry
			LdapEntry toCheck = getEntry("cn=orgperson1,o=org1,c=US");
			LdapAttributes attributes = toCheck.getAttributes();
			LdapAttribute attribute = attributes.get("internationaliSDNNumber");
			assertNull(attribute);
		}
	}

	public void testModifyRequestDeleteTrueOneValue() throws Exception {
		/*
		 * delete: delete values listed from the given attribute, removing the
		 * entire attribute if no values are listed, or if all current values of
		 * the attribute are listed for deletion;
		 */
		// true
		{
			populateBranchEntries();
			populatePersonEntries(5);
			Modification[] mods = { new Modification(ModificationType.DELETE,
					"sn", new AttributeValues().add("another_personsn1")
							.getValuesAsByteArray()) };
			LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
					"cn=person1,o=org1,c=US", mods));
			// Setting mocks
			eventMock.getMessage();
			eventControl.setReturnValue(message);
			eventMock.getResponseHandler();
			eventControl.setReturnValue(handlerMock);
			eventControl.replay(); // eventControl&eventMock setted and ready

			handlerMock
					.sendResponse(new ModifyResponse(LDAPResultCode.SUCCESS));
			handlerControl.replay(); // handlerControl setted and ready

			// main test corpus
			stage.enqueue(createEventForMessageProcessor(eventMock));

			// verify mocks
			eventControl.verify();
			handlerControl.verify();

			// check that the change has been performed in the DIT
			LdapEntry toCheck = getEntry("cn=person1,o=org1,c=US");
			LdapAttributes attributes = toCheck.getAttributes();
			LdapAttribute attribute = attributes.get("sn");
			assertNotNull(attribute);
			assertEquals(1, attribute.size());
			assertTrue(attribute.contains(new DirectoryString("personsn1")));
		}
	}

	public void testModifyRequestDeleteTrueWholeAttribute() throws Exception {
		/*
		 * delete: delete values listed from the given attribute, removing the
		 * entire attribute if no values are listed, or if all current values of
		 * the attribute are listed for deletion;
		 */
		// true
		// false due to InvalidSyntax
		// false due because the attribute doesn't have an eq match
		{
			populateBranchEntries();
			populatePersonEntries(5);
			Modification[] mods = { new Modification(ModificationType.DELETE,
					"sn", new AttributeValues().add("another_personsn1").add(
							"personsn1").getValuesAsByteArray()) };
			LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
					"cn=person1,o=org1,c=US", mods));
			// Setting mocks
			eventMock.getMessage();
			eventControl.setReturnValue(message);
			eventMock.getResponseHandler();
			eventControl.setReturnValue(handlerMock);
			eventControl.replay(); // eventControl&eventMock setted and ready

			handlerMock
					.sendResponse(new ModifyResponse(LDAPResultCode.SUCCESS));
			handlerControl.replay(); // handlerControl setted and ready

			// main test corpus
			stage.enqueue(createEventForMessageProcessor(eventMock));

			// verify mocks
			eventControl.verify();
			handlerControl.verify();

			// check that the change has been performed in the DIT
			LdapEntry toCheck = getEntry("cn=person1,o=org1,c=US");
			LdapAttributes attributes = toCheck.getAttributes();
			LdapAttribute attribute = attributes.get("sn");
			assertNull(attribute);
		}
	}

	public void testModifyRequestDeleteFalseInvalidSyntax() throws Exception {
		/*
		 * delete: delete values listed from the given attribute, removing the
		 * entire attribute if no values are listed, or if all current values of
		 * the attribute are listed for deletion;
		 */
		{
			// creating an attribute if it did not already exists
			populateBranchEntries();
			populateOrgPersonEntries(5);

			// invalid because numericstring only allows digits
			Modification[] mods = { new Modification(ModificationType.DELETE,
					"internationaliSDNNumber", new AttributeValues().add(
							"4112 681 542").getValuesAsByteArray()) };
			LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
					"cn=orgperson1,o=org1,c=US", mods));

			// Setting mocks
			eventMock.getMessage();
			eventControl.setReturnValue(message);
			eventMock.getResponseHandler();
			eventControl.setReturnValue(handlerMock);
			eventControl.replay(); // eventControl&eventMock setted and ready

			handlerMock
					.sendResponse(new ModifyResponse(
							LDAPResultCode.INVALID_ATTR_SYNTAX, "",
							"modify/delete: internationaliSDNNumber: value #0 invalid per syntax"));
			handlerControl.replay(); // handlerControl setted and ready

			// main test corpus
			stage.enqueue(createEventForMessageProcessor(eventMock));

			// verify mocks
			eventControl.verify();
			handlerControl.verify();

			// check that the no changes has been performed on the dc=com,c=us
			// entry
			LdapEntry toCheck = getEntry("cn=orgperson1,o=org1,c=US");
			LdapAttributes attributes = toCheck.getAttributes();
			LdapAttribute attribute = attributes.get("internationaliSDNNumber");
			assertNotNull(attribute);
			assertEquals(1, attribute.size());
			assertTrue(attribute.contains(new NumericString("999999999")));
		}

	}

	public void testModifyRequestDeleteFalseNoEqMatch() throws Exception {
		/*
		 * delete: delete values listed from the given attribute, removing the
		 * entire attribute if no values are listed, or if all current values of
		 * the attribute are listed for deletion;
		 */
		// true
		// false due to InvalidSyntax
		// false due because the attribute doesn't have an eq match
		// false because the value doesn't exists
		{
			// telexNumber has no equality matching rule
			// inappropiate matching (18)
			// additional info: modify/add: telexNumber: no equality matching
			// rule
			populateBranchEntries();
			populateOrgPersonEntries(5);

			Modification[] mods = { new Modification(ModificationType.DELETE,
					"telexNumber", new AttributeValues().add(
							"1212312$ES$3322333").getValuesAsByteArray()) };
			LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
					"cn=orgperson1,o=org1,c=US", mods));

			// Setting mocks
			eventMock.getMessage();
			eventControl.setReturnValue(message);
			eventMock.getResponseHandler();
			eventControl.setReturnValue(handlerMock);
			eventControl.replay(); // eventControl&eventMock setted and ready

			handlerMock.sendResponse(new ModifyResponse(
					LDAPResultCode.INAPPROPIATE_MATCHING, "",
					"modify/delete: telexNumber: no equality matching rule"));
			handlerControl.replay(); // handlerControl setted and ready

			// main test corpus
			stage.enqueue(createEventForMessageProcessor(eventMock));

			// verify mocks
			eventControl.verify();
			handlerControl.verify();

			// check that the no changes has been performed on the dc=com,c=us
			// entry
			LdapEntry toCheck = getEntry("cn=orgperson1,o=org1,c=US");
			LdapAttributes attributes = toCheck.getAttributes();
			LdapAttribute attribute = attributes.get("telexNumber");
			assertNotNull(attribute);
			assertEquals(1, attribute.size());
			assertTrue(attribute
					.contains(new TelexNumber("1212312$ES$3322333")));
		}
	}

	public void testModifyRequestDeleteFalseValueDoesntExists()
			throws Exception {
		/*
		 * delete: delete values listed from the given attribute, removing the
		 * entire attribute if no values are listed, or if all current values of
		 * the attribute are listed for deletion;
		 */
		{
			// telexNumber has no equality matching rule
			// inappropiate matching (18)
			// additional info: modify/add: telexNumber: no equality matching
			// rule
			populateBranchEntries();
			populateOrgPersonEntries(5);
			stage.enableSchemaChecking();

			Modification[] mods = { new Modification(ModificationType.DELETE,
					"sn", new AttributeValues().add("nonexistent")
							.getValuesAsByteArray()) };
			LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
					"cn=orgperson1,o=org1,c=US", mods));

			// Setting mocks
			eventMock.getMessage();
			eventControl.setReturnValue(message);
			eventMock.getResponseHandler();
			eventControl.setReturnValue(handlerMock);
			eventControl.replay(); // eventControl&eventMock setted and ready

			handlerMock.sendResponse(new ModifyResponse(
					LDAPResultCode.NO_SUCH_ATTR, "",
					"modify/delete: sn: no such value"));
			handlerControl.replay(); // handlerControl setted and ready

			// main test corpus
			stage.enqueue(createEventForMessageProcessor(eventMock));

			// verify mocks
			eventControl.verify();
			handlerControl.verify();

			// check that the no changes has been performed on the dc=com,c=us
			// entry
			LdapEntry toCheck = getEntry("cn=orgperson1,o=org1,c=US");
			LdapAttributes attributes = toCheck.getAttributes();
			LdapAttribute attribute = attributes.get("sn");
			assertNotNull(attribute);
			assertEquals(1, attribute.size());
			assertTrue(attribute.contains(new DirectoryString("personsn1")));
		}
	}

	public void testModifyRequestDeleteFalseAttributeDontExists()
			throws Exception {
		/*
		 * delete: delete values listed from the given attribute, removing the
		 * entire attribute if no values are listed, or if all current values of
		 * the attribute are listed for deletion;
		 */
		{
			// telexNumber has no equality matching rule
			// inappropiate matching (18)
			// additional info: modify/add: telexNumber: no equality matching
			// rule
			populateBranchEntries();
			populateOrgPersonEntries(5);
			stage.enableSchemaChecking();

			Modification[] mods = { new Modification(ModificationType.DELETE,
					"dc", new AttributeValues().getValuesAsByteArray()) };
			LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
					"cn=orgperson1,o=org1,c=US", mods));

			// Setting mocks
			eventMock.getMessage();
			eventControl.setReturnValue(message);
			eventMock.getResponseHandler();
			eventControl.setReturnValue(handlerMock);
			eventControl.replay(); // eventControl&eventMock setted and ready

			handlerMock.sendResponse(new ModifyResponse(
					LDAPResultCode.NO_SUCH_ATTR, "",
					"modify/delete: dc: no such attribute"));
			handlerControl.replay(); // handlerControl setted and ready

			// main test corpus
			stage.enqueue(createEventForMessageProcessor(eventMock));

			// verify mocks
			eventControl.verify();
			handlerControl.verify();

			// check that the no changes has been performed on the dc=com,c=us
			// entry
			LdapEntry toCheck = getEntry("cn=orgperson1,o=org1,c=US");
			LdapAttributes attributes = toCheck.getAttributes();
			LdapAttribute attribute = attributes.get("sn");
			assertNotNull(attribute);
			assertEquals(1, attribute.size());
			assertTrue(attribute.contains(new DirectoryString("personsn1")));
		}
	}

	public void testModifyRequestDeleteFalseRequiredAttributes()
			throws Exception {
		/*
		 * delete: delete values listed from the given attribute, removing the
		 * entire attribute if no values are listed, or if all current values of
		 * the attribute are listed for deletion;
		 */
		{
			// telexNumber has no equality matching rule
			// inappropiate matching (18)
			// additional info: modify/add: telexNumber: no equality matching
			// rule
			populateBranchEntries();
			populateOrgPersonEntries(5);
			stage.enableSchemaChecking();

			Modification[] mods = { new Modification(ModificationType.DELETE,
					"sn", new AttributeValues().add("personsn1")
							.getValuesAsByteArray()) };
			LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
					"cn=orgperson1,o=org1,c=US", mods));

			// Setting mocks
			eventMock.getMessage();
			eventControl.setReturnValue(message);
			eventMock.getResponseHandler();
			eventControl.setReturnValue(handlerMock);
			eventControl.replay(); // eventControl&eventMock setted and ready

			handlerMock
					.sendResponse(new ModifyResponse(
							LDAPResultCode.OBJECTCLASS_VIOLATION, "",
							"object class '[organizationalPerson]' requires attribute 'sn'"));
			handlerControl.replay(); // handlerControl setted and ready

			// main test corpus
			stage.enqueue(createEventForMessageProcessor(eventMock));

			// verify mocks
			eventControl.verify();
			handlerControl.verify();

			// check that the no changes has been performed on the dc=com,c=us
			// entry
			LdapEntry toCheck = getEntry("cn=orgperson1,o=org1,c=US");
			LdapAttributes attributes = toCheck.getAttributes();
			LdapAttribute attribute = attributes.get("sn");
			assertNotNull(attribute);
			assertEquals(1, attribute.size());
			assertTrue(attribute.contains(new DirectoryString("personsn1")));
		}
	}

	public void testModifyRequestDeleteFalseRequiredAttributes2()
			throws Exception {
		/*
		 * delete: delete values listed from the given attribute, removing the
		 * entire attribute if no values are listed, or if all current values of
		 * the attribute are listed for deletion;
		 */
		{
			populateBranchEntries();
			populateOrgPersonEntries(5);
			stage.enableSchemaChecking();

			Modification[] mods = { new Modification(ModificationType.DELETE,
					"sn", new AttributeValues().getValuesAsByteArray()) }; // delete
			// all
			// sn
			// values
			LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
					"cn=orgperson1,o=org1,c=US", mods));

			// Setting mocks
			eventMock.getMessage();
			eventControl.setReturnValue(message);
			eventMock.getResponseHandler();
			eventControl.setReturnValue(handlerMock);
			eventControl.replay(); // eventControl&eventMock setted and ready

			handlerMock
					.sendResponse(new ModifyResponse(
							LDAPResultCode.OBJECTCLASS_VIOLATION, "",
							"object class '[organizationalPerson]' requires attribute 'sn'"));
			handlerControl.replay(); // handlerControl setted and ready

			// main test corpus
			stage.enqueue(createEventForMessageProcessor(eventMock));

			// verify mocks
			eventControl.verify();
			handlerControl.verify();

			// check that the no changes has been performed on the dc=com,c=us
			// entry
			LdapEntry toCheck = getEntry("cn=orgperson1,o=org1,c=US");
			LdapAttributes attributes = toCheck.getAttributes();
			LdapAttribute attribute = attributes.get("sn");
			assertNotNull(attribute);
			assertEquals(1, attribute.size());
			assertTrue(attribute.contains(new DirectoryString("personsn1")));
		}
	}

	public void testModifyRequestTemporalSchemaViolation() throws Exception {
		{
			populateBranchEntries();
			populatePersonEntries(5);
			final Modification mod1 = new Modification(ModificationType.DELETE,
					"sn", new AttributeValues().add("another_personsn1").add(
							"personsn1").getValuesAsByteArray());
			final Modification mod2 = new Modification(ModificationType.ADD,
					"sn", new AttributeValues().add("another_personsn1_new")
							.add("personsn1_new").getValuesAsByteArray());
			Modification[] mods = { mod1, mod2 };
			LDAPMessage message = new LDAPMessage(1, new ModifyRequest(
					"cn=person1,o=org1,c=US", mods));
			// Setting mocks
			eventMock.getMessage();
			eventControl.setReturnValue(message);
			eventMock.getResponseHandler();
			eventControl.setReturnValue(handlerMock);
			eventControl.replay(); // eventControl&eventMock setted and ready

			handlerMock
					.sendResponse(new ModifyResponse(LDAPResultCode.SUCCESS));
			handlerControl.replay(); // handlerControl setted and ready

			// main test corpus
			stage.enqueue(createEventForMessageProcessor(eventMock));

			// verify mocks
			eventControl.verify();
			handlerControl.verify();

			// check that the change has been performed in the DIT
			LdapEntry toCheck = getEntry("cn=person1,o=org1,c=US");
			LdapAttributes attributes = toCheck.getAttributes();
			LdapAttribute attribute = attributes.get("sn");
			assertNotNull(attribute);
			assertEquals(2, attribute.size());
			assertTrue(attribute.contains(new DirectoryString(
					"another_personsn1_new")));
			assertTrue(attribute.contains(new DirectoryString("personsn1_new")));
		}
	}

	public void testCompareRequest() {
		populate();

		LDAPMessage message = new LDAPMessage(1, new CompareRequest(
				"cn=Person1,ou=My organization1", "dnQualifier", "qualifier0"));

		ResponseHandler responseHandler = new ResponseHandler() {

			public void sendResponse(LDAPResponse response) {
				resultMessagesReceived++;
				assertTrue("was " + resultMessagesReceived,
						resultMessagesReceived <= 1);
				if (response instanceof CompareResponse) {
					CompareResponse result = (CompareResponse) response;
					assertEquals(1, resultMessagesReceived);
					assertEquals(LDAPResultCode.COMPARE_TRUE, result
							.getResult());
				} else {
					fail("Unexpected response " + response);
				}
			}
		};

		final LdapMessageEvent event = new LdapMessageEventImpl(message,
				responseHandler);

		stage.enqueue(createEventForMessageProcessor(event));
		assertEquals(1, resultMessagesReceived);
	}

	public void testCompareRequestFalse() {
		populateBranchEntries();
		populatePersonEntries(5);

		LDAPMessage message = new LDAPMessage(1, new CompareRequest(
				"cn=person1,o=org1,c=US", "sn", "personsn2"));

		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		handlerMock.sendResponse(new CompareResponse(
				LDAPResultCode.COMPARE_FALSE));
		handlerControl.replay();

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

	}

	public void testCompareRequestFalseAttrDescNotRecognized() {
		populateBranchEntries();
		populatePersonEntries(5);

		LDAPMessage message = new LDAPMessage(1, new CompareRequest(
				"cn=person1,o=org1,c=US", "snNotRecognized", "personsn1"));

		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		handlerMock.sendResponse(new CompareResponse(
				LDAPResultCode.COMPARE_FALSE));
		handlerControl.replay();

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

	}

	public void testCompareRequestFalseAttrValueAssertionNotParseable() {
		populateBranchEntries();
		populatePersonEntries(5);

		byte[] assertionValue = HexUtils.fromHexString("80 10 aa bb");
		LDAPMessage message = new LDAPMessage(1, new CompareRequest(
				"cn=person1,o=org1,c=US", "sn", assertionValue));

		// Setting mocks
		eventMock.getMessage();
		eventControl.setReturnValue(message);
		eventMock.getResponseHandler();
		eventControl.setReturnValue(handlerMock);
		eventControl.replay();

		handlerMock.sendResponse(new CompareResponse(
				LDAPResultCode.INVALID_ATTR_SYNTAX, "",
				"invalid attribute syntax for attribute 'sn'"));
		handlerControl.replay();

		// main test corpus
		stage.enqueue(createEventForMessageProcessor(eventMock));

		// verify mocks
		eventControl.verify();
		handlerControl.verify();

	}

	public void testAbandonRequest() {
		LDAPMessage message = new LDAPMessage(1, new AbandonRequest(1));

		ResponseHandler responseHandler = new ResponseHandler() {
			public void sendResponse(LDAPResponse response) {
				fail("AbandonRequest should not be received");
			}
		};

		final LdapMessageEvent event = new LdapMessageEventImpl(message,
				responseHandler);

		stage.enqueue(createEventForMessageProcessor(event));
		assertEquals(0, resultMessagesReceived);

	}

	/**
	 * 
	 */
	private void populate() {

		// ou=My organization1
		// cn=person1,ou=My organization1
		// ou=My organization2
		// cn=person2, ou=My organization2

		LDAPMessage[] messages = new LDAPMessage[] { createAddRequestOu1(),
				createAddRequestOu2(), createAddRequestEntry1(),
				createAddRequestEntry2() };
		for (int i = 0; i < messages.length; i++) {
			LDAPMessage message = messages[i];
			ResponseHandler responseHandler = new ResponseHandler() {

				public void sendResponse(LDAPResponse response) {
					assertTrue(response instanceof AddResponse);
					AddResponse br = (AddResponse) response;
					assertEquals("while adding entry ", LDAPResultCode.SUCCESS,
							br.getResult());
				}
			};

			final LdapMessageEvent event = new LdapMessageEventImpl(message,
					responseHandler);

			stage.enqueue(createEventForMessageProcessor(event));

		}
	}

	/**
	 * Creates a fake DIT with two main branches o=org1,c=US and o=org2,c=US and
	 * fill each one with fake person entries.
	 * 
	 * @param n
	 *            number of entries below each branch
	 */
	private void populatePersonEntries(int n) {
		try {
			for (int i = 1; i <= n; i++) {
				// cn=personx,o=org1,c=US
				LdapAttributes attrs = TestUtils
						.createAttrsForCN_person("person" + i);
				createEntry("cn=" + "person" + i + ",o=org1,c=US", attrs);
			}

			for (int i = 1; i <= n; i++) {
				// cn=personx,o=org2,c=US
				LdapAttributes attrs = TestUtils
						.createAttrsForCN_person("person" + i);
				createEntry("cn=person" + i + ",o=org2,c=US", attrs);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private void populateBranchEntries() {
		try {
			// c=US
			createEntry(dn("c=US"), TestUtils.createAttrsForC_US());

			// o=org1,c=US
			createEntry(dn("o=org1,c=US"), TestUtils
					.createAttrsForO_org("org1"));

			// o=org2,c=US
			createEntry(dn("o=org2,c=US"), TestUtils
					.createAttrsForO_org("org2"));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private void createEntry(DistinguishedName dn, LdapAttributes ldapAttributes) {
		backend.put(dn, new LdapEntryImpl(dn, ldapAttributes));
	}

	private void createEntry(String dnName, LdapAttributes ldapAttributes) {
		createEntry(dn(dnName), ldapAttributes);
	}

	private void populateDcCom() {
		LdapAttributes attrs = TestUtils.createAttrsForDc_Com();
		createEntry("dc=com,c=US", attrs);
	}

	private void populateOrgPersonEntries(int n) {
		try {
			for (int i = 1; i <= n; i++) {
				LdapAttributes attrs = TestUtils.createAttrsForOrgPerson(
						"orgperson" + i, "CRYPT");
				// cn=orgpersonx,o=org1,c=US
				createEntry("cn=" + "orgperson" + i + "," + "o=org1,c=US",
						attrs);
				// cn=orgpersonx,o=org1,c=US
				createEntry("cn=" + "orgperson" + i + "," + "o=org2,c=US",
						attrs);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void populateOrgPersonEntriesWithMd5Passwords(int n) {
		try {
			for (int i = 1; i <= n; i++) {
				LdapAttributes attrs = TestUtils.createAttrsForOrgPerson(
						"orgpersonmd5" + i, "MD5");
				// cn=orgpersonx,o=org1,c=US
				createEntry("cn=" + "orgpersonmd5" + i + "," + "o=org1,c=US",
						attrs);
				// cn=orgpersonx,o=org1,c=US
				createEntry("cn=" + "orgpersonmd5" + i + "," + "o=org2,c=US",
						attrs);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void populateOrgPersonEntriesWithSha1Passwords(int n) {
		try {
			for (int i = 1; i <= n; i++) {
				LdapAttributes attrs = TestUtils.createAttrsForOrgPerson(
						"orgpersonsha1" + i, "SHA1");
				// cn=orgpersonx,o=org1,c=US
				createEntry("cn=" + "orgpersonsha1" + i + "," + "o=org1,c=US",
						attrs);
				// cn=orgpersonx,o=org1,c=US
				createEntry("cn=" + "orgpersonsha1" + i + "," + "o=org2,c=US",
						attrs);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return
	 */
	private LDAPMessage createSearchRequest() {
		return new LDAPMessage(1, new SearchRequest("", Scope.WHOLE_SUBTREE,
				DerefAliases.NEVER_DEREF_ALIASES, 0, 0, false,
				new EqMatchFilter("cn", "New Entry"), null));
	}

	private LDAPMessage createSearchRequest(String base, Filter f) {
		return new LDAPMessage(1, new SearchRequest(base, Scope.WHOLE_SUBTREE,
				DerefAliases.NEVER_DEREF_ALIASES, 0, 0, false, f, null));
	}

	private EventForMessageProcessor createEventForMessageProcessor(
			final LdapMessageEvent event) {
		EventForMessageProcessor ev = new EventForMessageProcessor() {
			public void visit(MessageProcessor mp) {
				mp.process(event);
			}

			public Map getData() {
				throw new RuntimeException(
						"should not be called in this junit test");
			}
		};
		return ev;
	}

	private LDAPMessage createAddRequestMessage() {
		List attrs = new ArrayList();
		final byte[][] objectclassValues = new AttributeValues().add("top")
				.add("organizationalPerson").getValuesAsByteArray();
		final AttributeTypeAndValues objectClass = new AttributeTypeAndValues(
				"objectclass", objectclassValues);
		attrs.add(objectClass);
		attrs.add(new AttributeTypeAndValues("cn", new AttributeValues().add(
				"New Entry").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("email", new AttributeValues()
				.add("nobody@nodomain.com").getValuesAsByteArray()));

		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);
		return new LDAPMessage(1, new AddRequest("cn=New Entry", attrList));
	}

	private LDAPMessage createAddRequestOu1() {
		List attrs = new ArrayList();
		attrs.add(new AttributeTypeAndValues("objectclass",
				new AttributeValues().add("top").add("organizationalUnit")
						.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("ou", new AttributeValues().add(
				"My organization1").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("email", new AttributeValues()
				.add("organization1@nodomain.com").getValuesAsByteArray()));

		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);
		return new LDAPMessage(1, new AddRequest("ou=My organization1",
				attrList));
	}

	private LDAPMessage createAddRequestOu2() {

		List attrs = new ArrayList();
		attrs.add(new AttributeTypeAndValues("objectclass",
				new AttributeValues().add("top").add("organizationalUnit")
						.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("ou", new AttributeValues().add(
				"My organization2").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("email", new AttributeValues()
				.add("organization2@nodomain.com").getValuesAsByteArray()));

		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);
		return new LDAPMessage(1, new AddRequest("ou=My organization2",
				attrList));
	}

	private LDAPMessage createAddRequestEntry1() {
		List attrs = new ArrayList();
		attrs.add(new AttributeTypeAndValues("objectclass",
				new AttributeValues().add("top").add("organizationalPerson")
						.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("cn", new AttributeValues().add(
				"Person1").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("sn", new AttributeValues().add(
				"snPerson1").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("description",
				new AttributeValues().add("a Person").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("telephoneNumber",
				new AttributeValues().add("11111111-1").add("11111111-2")
						.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("dnQualifier",
				new AttributeValues().add("qualifier0").add("qualifier2")
						.getValuesAsByteArray()));

		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);
		return new LDAPMessage(1, new AddRequest(
				"cn=Person1, ou=My organization1", attrList));
	}

	private LDAPMessage createAddRequestEntry2() {
		List attrs = new ArrayList();
		attrs.add(new AttributeTypeAndValues("objectclass",
				new AttributeValues().add("top").add("organizationalPerson")
						.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("cn", new AttributeValues().add(
				"Person2").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("sn", new AttributeValues().add(
				"J�menez").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("street", new AttributeValues()
				.add("street1").getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("telephoneNumber",
				new AttributeValues().add("22222222-1").add("22222222-2")
						.getValuesAsByteArray()));
		attrs.add(new AttributeTypeAndValues("dnQualifier",
				new AttributeValues().add("qualifier0").add("qualifier3")
						.getValuesAsByteArray()));

		AttributeTypeAndValuesList attrList = new AttributeTypeAndValuesList(
				attrs);
		return new LDAPMessage(1, new AddRequest(
				"cn=Person2, ou=My organization2", attrList));
	}
}
