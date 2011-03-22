package org.jletty.messageprocessor;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import javax.naming.NamingException;

import junit.framework.TestCase;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.jletty.aci.Aci;
import org.jletty.aci.AciContext;
import org.jletty.aci.AciEvaluationResultType;
import org.jletty.aci.AciParserImpl;
import org.jletty.aci.PermissionRight;
import org.jletty.messageprocessorstage.MessageProcessorStage;
import org.jletty.schema.AttributeType;
import org.jletty.schema.AttributeUsage;
import org.jletty.schema.Schema;
import org.jletty.schemaparser.Parser;

public class MessageProcessorStageSecurityTest extends TestCase {

	private MessageProcessorStage stage;

	private HashMap backend;

	private AciParserImpl hlparser;

	public MessageProcessorStageSecurityTest() {
		super();
		init();
	}

	public MessageProcessorStageSecurityTest(String arg0) {
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
		backend = new HashMap();
		stage = new MessageProcessorStage(backend);
		// BasicConfigurator.configure();
		LogManager.resetConfiguration();
		Properties logProps = new Properties();
		logProps.put("log4j.threshold", "OFF");
		PropertyConfigurator.configure(logProps);
		hlparser = new AciParserImpl();
		// handlerControl = MockControl.createControl(ResponseHandler.class);
		// handlerMock = (ResponseHandler) handlerControl.getMock();
		// eventControl =
		// MockControl.createStrictControl(LdapMessageEvent.class);
		// eventMock = (LdapMessageEvent) eventControl.getMock();
		// eventControl.reset();
		// handlerControl.reset();
	}


	public void testAciTrue() throws Exception {
		Aci aci = hlparser.parse("(target=\"ldap:///uid=bjensen,dc=example,dc=com\") " +
		"(version 3.0; acl \"example\"; allow (write) userdn=\"ldap:///self\";)");
		AciContext context = new AciContext();
		context.setEntry("uid=bjensen,dc=example,dc=com");
		AciEvaluationResultType result = aci.evaluate(PermissionRight.WRITE,context);
		assertEquals(AciEvaluationResultType.ALLOW,result);
	}
	
	public void testAciTrue2() throws Exception {
		//the same as testAciTrue but the context entry dn is not normalized (spaces, etc). 
		Aci aci = hlparser.parse("(target=\"ldap:///uid=bjensen,dc=example,dc=com\") " +
		"(version 3.0; acl \"example\"; allow (write) userdn=\"ldap:///self\";)");
		AciContext context = new AciContext();
		context.setEntry("uid=bjensen  ,dc=example,dc=com");
		AciEvaluationResultType result = aci.evaluate(PermissionRight.WRITE,context);
		assertEquals(AciEvaluationResultType.ALLOW,result);
	}

	public void testAciFalse() throws Exception {
		Aci aci = hlparser.parse("(target=\"ldap:///uid=bjensen,dc=example,dc=com\") " +
		"(version 3.0; acl \"example\"; deny (write) userdn=\"ldap:///self\";)");
		AciContext context = new AciContext();
		context.setEntry("uid=bjensen,dc=example,dc=com");
		AciEvaluationResultType result = aci.evaluate(PermissionRight.WRITE,context);
		assertEquals(AciEvaluationResultType.DENY,result);
	}

	public void testAciDefaultUnspecified() {
		Aci aci = hlparser.parse("(target=\"ldap:///uid=bjensen,dc=example,dc=com\") " +
		"(version 3.0; acl \"example\"; allow (write) userdn=\"ldap:///self\";)");
		AciContext context = new AciContext();
		AciEvaluationResultType result = aci.evaluate(PermissionRight.PROXY,context);
		assertEquals(AciEvaluationResultType.UNSPECIFIED,result);
	}
	
	/**
	 * This rule should return UNSPECIFIED because the target in the context doesn't match the target in the rule
	 * @throws Exception 
	 */
	public void testTargetNotMatch() throws Exception {
		Aci aci = hlparser.parse("(target=\"ldap:///uid=bjensen,dc=example,dc=com\") " +
		"(version 3.0; acl \"example\"; allow (proxy) userdn=\"ldap:///self\";)");
		AciContext context = new AciContext();
		context.setEntry("uid=another,dc=example,dc=com");
		AciEvaluationResultType result = aci.evaluate(PermissionRight.PROXY,context);
		assertEquals(AciEvaluationResultType.UNSPECIFIED,result);
	}
	public void testTargetMatchWhenCanonicalized() throws Exception {
		Aci aci = hlparser.parse("(target=\"ldap:///uid=bjensen,dc=example,dc=com\") " +
		"(version 3.0; acl \"example\"; allow (proxy) userdn=\"ldap:///self\";)");
		AciContext context = new AciContext();
		context.setEntry("uid=bjensen ,dc=example,dc=com");
		AciEvaluationResultType result = aci.evaluate(PermissionRight.PROXY,context);
		assertEquals(AciEvaluationResultType.ALLOW,result);
	}

	public void testTargetWildcard1() throws Exception {
		// You can also use a wildcard in the DN to target any number of entries
		// that match the LDAP URL. The following are legal examples of wildcard
		// usage:

		// * (target="ldap:///uid=*,dc=example,dc=com")

		// Matches every entry in the entire example.com tree that has the uid
		// attribute in the entry's RDN. This target will match entries at any
		// depth in the tree, for example:

		// uid=tmorris,ou=sales,dc=example,dc=com
		// uid=yyorgens,ou=marketing,dc=example,dc=com
		// uid=bjensen,ou=eng,ou=east,dc=example,dc=com
		Aci aci = hlparser.parse("(target=\"ldap:///uid=*,dc=example,dc=com\") " +
		"(version 3.0; acl \"example\"; allow (proxy) userdn=\"ldap:///self\";)");
		AciContext context = new AciContext();
		context.setEntry("uid=bjensen ,dc=example,dc=com");
		AciEvaluationResultType result = aci.evaluate(PermissionRight.PROXY,context);
		assertEquals(AciEvaluationResultType.ALLOW,result);

	}
	
	public void testTargetWildcardwrongsuffix() throws Exception {
		//suffix don't match
		
	}
	public void testTargetWildcardWrongAttr() throws Exception {
		//uid=*,c=es
		//cn=ruben,c=es
	}

	public void testTargetWildcard2() {

		// * (target="ldap:///uid=*Anderson,ou=People,dc=example,dc=com")

		// Matches every entry in the ou=People branch with a uid ending in
		// Anderson.

	}

	public void testTargetWildcard3() {

		// * (target="ldap:///*Anderson,ou=People,dc=example,dc=com")

		// Matches every entry in the ou=People branch whose RDN ends with
		// Anderson, regardless of the naming attribute.

	}

	public void testTargetWildcard4() {

		// Multiple wildcards are allowed, such as in
		// uid=*,ou=*,dc=example,dc=com. This example matches every entry in the
		// example.com tree whose distinguished name contains only the uid and
		// ou attributes.

	}

	public void testTargetWildcard5() {

		// Note

		// You cannot use wildcards in the suffix part of a distinguished name.
		// That is, if your directory uses the suffixes c=US and c=GB, then you
		// cannot use the following target to reference both suffixes:

		// (target="ldap:///dc=example,c=*").

	}

	public void testTargetWildcard6() {

		// Neither can you use a target such as uid=bjensen,o=*.com.
	}

	
	public void test6() {
		fail();
		//TODO test targetattrs 
	}
	public void test7() {
		fail();
		//TODO test both target and targetattrs 
	}
	public void test8() {
		fail();
		//TODO test targetfilter 
	}
	public void test9() {
		fail();
		//TODO test con varios permission (precedencia del deny) 
	}


	public void test1() {
		//TODO, varios permission rules
		fail();
	}
	public void test2() {
		//TODO, una que no se cumpla porque el userdn no coincide con el que esta en el AciContext
		fail();
	}
	public void test3() {
		//TODO, una que no se cumpla porque el targetdn no matchea con l que esta en el AciContext
		fail();
	}

}
