package org.jletty.aci;

import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import antlr.Token;
import antlr.TokenStreamSelector;
import junit.framework.TestCase;


/**
 * Test for aci parsing. see {@link http://docs.sun.com/source/816-6698-10/aci.html#19201}
 * @author ecerulm
 *
 */
public class AciParsingTest extends TestCase {
	
	AciParserImpl hlparser = null;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		hlparser = new AciParserImpl();
	}

	public void testAciParsingBasic() {
		Aci aci = hlparser.parse("(target=\"ldap:///uid=bjensen,dc=example,dc=com\") (version 3.0; acl \"example\"; allow (write) userdn=\"ldap:///self\";)");
		Target target = aci.getTargets()[0];
		assertEquals(TargetType.TARGET, target.getType());
		String targetStringActual = target.getTarget();
		String targetStringExpected = "ldap:///uid=bjensen,dc=example,dc=com";
		assertEquals(targetStringExpected,targetStringActual);
		
		assertEquals(TargetVersion.VERSION3_0, aci.getVersion());
		assertEquals("example",aci.getName());
		Permission[] perms = aci.getPermissions();
		assertEquals(1, perms.length);
		assertEquals(PermissionType.ALLOW, perms[0].getType());
		assertEquals(PermissionRight.WRITE, perms[0].getRights()[0]);
		BindRule brule = perms[0].getBindRule();
		assertEquals(BindRuleType.USERDN, brule.getType());
		assertEquals(BindRuleEqualsType.EQUALS, brule.getEquals());
		assertEquals("ldap:///self",brule.getExpression());
	}
	public void testAciParsing2TargetsBasic() {
		Aci aci = hlparser.parse("(target=\"ldap:///uid=bjensen,dc=example,dc=com\") (targetattr=\"*\") (version 3.0; acl \"example\"; allow (write) userdn=\"ldap:///self\";)");
		Target[] targets = aci.getTargets();
		assertEquals(2,targets.length);		
		assertEquals(TargetType.TARGET, targets[0].getType());
		String targetStringActual = targets[0].getTarget();
		String targetStringExpected = "ldap:///uid=bjensen,dc=example,dc=com";
		assertEquals(targetStringExpected,targetStringActual);
		
		assertEquals(TargetType.TARGETATTR, targets[1].getType());
		targetStringActual = targets[1].getTarget();
		targetStringExpected = "*";
		assertEquals(targetStringExpected,targetStringActual);
		
		
		assertEquals(TargetVersion.VERSION3_0, aci.getVersion());
		assertEquals("example",aci.getName());
		Permission[] perms = aci.getPermissions();
		assertEquals(1, perms.length);
		assertEquals(PermissionType.ALLOW, perms[0].getType());
		assertEquals(PermissionRight.WRITE, perms[0].getRights()[0]);
		BindRule brule = perms[0].getBindRule();
		assertEquals(BindRuleType.USERDN, brule.getType());
		assertEquals(BindRuleEqualsType.EQUALS, brule.getEquals());
		assertEquals("ldap:///self",brule.getExpression());
	}
	
	public void testTarget() throws Exception{
		{
			Target target = setUpParser("(target=\"ldap:///uid=bjensen,ou=People,dc=example,dc=com\")").target();
			assertEquals(TargetType.TARGET, target.getType());
			assertEquals(TargetEqualsType.EQUALS, target.getEquals());
			assertEquals("ldap:///uid=bjensen,ou=People,dc=example,dc=com", target.getTarget());
		}
		{
			Target target = setUpParser("(target!=\"ldap:///uid=bjensen,ou=People,dc=example,dc=com\")").target();
			assertEquals(TargetType.TARGET, target.getType());
			assertEquals(TargetEqualsType.NEQUALS, target.getEquals());
			assertEquals("ldap:///uid=bjensen,ou=People,dc=example,dc=com", target.getTarget());
		}

		{
			try {
				Target target = setUpParser("(target!=\"file:///uid=bjensen,ou=People,dc=example,dc=com\")").target();
				fail("file:// is not a valid expression it must begin with ldap:///");
			} catch (IllegalArgumentException e) {
				//ignore
			}
		}
	}
	
	public void testTargetAttr() throws Exception{
		{
			Target target = setUpParser("(targetattr=\"cn\")").target();
			assertEquals(TargetType.TARGETATTR, target.getType());
			assertEquals(TargetEqualsType.EQUALS, target.getEquals());
			assertEquals("cn", target.getTarget());
		}
		{
			Target target = setUpParser("(targetattr=\"cn || sn || uid\")").target();
			assertEquals(TargetType.TARGETATTR, target.getType());
			assertEquals(TargetEqualsType.EQUALS, target.getEquals());
			assertEquals("cn || sn || uid", target.getTarget());
		}
		
	}
	public void testTargetFilter() throws Exception {
		Target target = setUpParser("(targetfilter=\"(|(status=contractor)(fulltime<=79))\")").target();
		assertEquals(TargetType.TARGETFILTER, target.getType());
		assertEquals(TargetEqualsType.EQUALS, target.getEquals());
		assertEquals("(|(status=contractor)(fulltime<=79))", target.getTarget());
	}
	public void testTargetAttrFilters() throws Exception {
		Target target = setUpParser("(targattrfilters=\"add=nsroleDN:(!(nsRoleDN=cn=superAdmin)) && telephoneNumber:(telephoneNumber=123*)\")").target();
		assertEquals(TargetType.TARGATATTRFILTERS, target.getType());
		assertEquals(TargetEqualsType.EQUALS, target.getEquals());
		assertEquals("add=nsroleDN:(!(nsRoleDN=cn=superAdmin)) && telephoneNumber:(telephoneNumber=123*)", target.getTarget());
	}
	
	public void testPermissionsAllowAll() throws Exception {
		Permission permission = setUpParser("allow (read, write, add, delete, search, compare, selfwrite, proxy)").permission();
		assertEquals(PermissionType.ALLOW,permission.getType());
		Collection<PermissionRight> rights = new HashSet<PermissionRight>(Arrays.asList(permission.getRights()));
		assertEquals(8,rights.size());
		assertTrue(rights.contains(PermissionRight.READ));
		assertTrue(rights.contains(PermissionRight.WRITE));
		assertTrue(rights.contains(PermissionRight.ADD));
		assertTrue(rights.contains(PermissionRight.DELETE));
		assertTrue(rights.contains(PermissionRight.SEARCH));
		assertTrue(rights.contains(PermissionRight.COMPARE));
		assertTrue(rights.contains(PermissionRight.SELFWRITE));
		assertTrue(rights.contains(PermissionRight.PROXY));
		
		permission = setUpParser("allow (all)").permission();
		assertEquals(PermissionType.ALLOW,permission.getType());
		rights = Arrays.asList(permission.getRights());
		assertEquals(1,rights.size());
		assertTrue(rights.contains(PermissionRight.ALL));
		
	}
	public void testPermissionsAllowWrite() throws Exception {
		Permission permission = setUpParser("allow (write)").permission();
		assertEquals(PermissionType.ALLOW,permission.getType());
		Collection<PermissionRight> rights = new HashSet<PermissionRight>(Arrays.asList(permission.getRights()));
		assertEquals(1,rights.size());
		assertTrue(rights.contains(PermissionRight.WRITE));
	}
	public void testPermissionsDenyAll() throws Exception {
		Permission permission = setUpParser("deny (read, write, add, delete, search, compare, selfwrite, proxy)").permission();
		assertEquals(PermissionType.DENY,permission.getType());
		Collection<PermissionRight> rights = new HashSet<PermissionRight>(Arrays.asList(permission.getRights()));
		assertEquals(8,rights.size());
		assertTrue(rights.contains(PermissionRight.READ));
		assertTrue(rights.contains(PermissionRight.WRITE));
		assertTrue(rights.contains(PermissionRight.ADD));
		assertTrue(rights.contains(PermissionRight.DELETE));
		assertTrue(rights.contains(PermissionRight.SEARCH));
		assertTrue(rights.contains(PermissionRight.COMPARE));
		assertTrue(rights.contains(PermissionRight.SELFWRITE));
		assertTrue(rights.contains(PermissionRight.PROXY));
		
		permission = setUpParser("deny (all)").permission();
		assertEquals(PermissionType.DENY,permission.getType());
		rights = Arrays.asList(permission.getRights());
		assertEquals(1,rights.size());
		assertTrue(rights.contains(PermissionRight.ALL));
		
	}

	public void testPermissionsDenyWrite() throws Exception {
		Permission permission = setUpParser("deny (write)").permission();
		assertEquals(PermissionType.DENY,permission.getType());
		Collection<PermissionRight> rights = new HashSet<PermissionRight>(Arrays.asList(permission.getRights()));
		assertEquals(1,rights.size());
		assertTrue(rights.contains(PermissionRight.WRITE));
	}
	
	public void testBindRuleUserDn() throws Exception {
		BindRule rule = setUpParser("userdn = \"ldap:///anyone\" ").bindrule();
		assertEquals(BindRuleType.USERDN, rule.getType());
		assertEquals(BindRuleEqualsType.EQUALS, rule.getEquals());
		assertEquals("ldap:///anyone",rule.getExpression());
	}

	public void testBindRuleGroupDn() throws Exception {
		BindRule rule = setUpParser("groupdn=\"ldap:///cn=Administrators,dc=example,dc=com\"").bindrule();
		assertEquals(BindRuleType.GROUPDN, rule.getType());
		assertEquals(BindRuleEqualsType.EQUALS, rule.getEquals());
		assertEquals("ldap:///cn=Administrators,dc=example,dc=com",rule.getExpression());
	}
	
	public void testAciLexer() throws Exception {
		AciLexer lexer = new AciLexer(new StringReader("roledn"));
		Token token = lexer.nextToken();
		assertEquals(AciLexerTokenTypes.BINDRULEKEYWORD,token.getType());
	}

	public void testBindRuleRoleDn() throws Exception {
		BindRule rule = setUpParser("roledn=\"ldap:///cn=DirectoryAdmin,ou=Company333, ou=corporate-clients,dc=example,dc=com\"").bindrule();
		assertEquals(BindRuleType.ROLEDN, rule.getType());
		assertEquals(BindRuleEqualsType.EQUALS, rule.getEquals());
		assertEquals("ldap:///cn=DirectoryAdmin,ou=Company333, ou=corporate-clients,dc=example,dc=com",rule.getExpression());
	}

	public void testBindRuleUserAttr() throws Exception {
		BindRule rule = setUpParser("userattr=\"cn#Ruben\"").bindrule();
		assertEquals(BindRuleType.USERATTR, rule.getType());
		assertEquals(BindRuleEqualsType.EQUALS, rule.getEquals());
		assertEquals("cn#Ruben",rule.getExpression());
	}

	public void testBindRuleIp() throws Exception {
		BindRule rule = setUpParser("ip=\"127.0.0.1\"").bindrule();
		assertEquals(BindRuleType.IP, rule.getType());
		assertEquals(BindRuleEqualsType.EQUALS, rule.getEquals());
		assertEquals("127.0.0.1",rule.getExpression());
	}
	
	public void testBindRuleDns() throws Exception {
		BindRule rule = setUpParser("dns=\"www.google.com\"").bindrule();
		assertEquals(BindRuleType.DNS, rule.getType());
		assertEquals(BindRuleEqualsType.EQUALS, rule.getEquals());
		assertEquals("www.google.com",rule.getExpression());
	}
	
	public void testBindRuleDayOfWeek() throws Exception {
		//TODO add checks for sun, sat, etc
		BindRule rule = setUpParser("dayofweek=\"sun\"").bindrule();
		assertEquals(BindRuleType.DAYOFWEEK, rule.getType());
		assertEquals(BindRuleEqualsType.EQUALS, rule.getEquals());
		assertEquals("sun",rule.getExpression());
	}
	
	public void testBindRuleTimeOfDay() throws Exception {
		//TODO add checks 0000-2359
		BindRule rule = setUpParser("timeofday=\"1000\"").bindrule();
		assertEquals(BindRuleType.TIMEOFDAY, rule.getType());
		assertEquals(BindRuleEqualsType.EQUALS, rule.getEquals());
		assertEquals("1000",rule.getExpression());
	}
	public void testBindRuleAuthMethod() throws Exception {
		//TODO add checks ssl etc
		BindRule rule = setUpParser("authmethod=\"ssl\"").bindrule();
		assertEquals(BindRuleType.AUTHMETHOD, rule.getType());
		assertEquals(BindRuleEqualsType.EQUALS, rule.getEquals());
		assertEquals("ssl",rule.getExpression());
	}
	
	private AntlrAciParser setUpParser(String toParse) {
		
		Reader sr = new StringReader(toParse);
		// create filter lexer
		AciLexer aciLexer = new AciLexer(sr);

		TokenStreamSelector selector = new TokenStreamSelector();
		selector.addInputStream(aciLexer, "acilexer");
		AntlrAciParser parser = new AntlrAciParser(selector);
		parser.setSelector(selector);
		selector.select("acilexer");
		return parser;
	}

}
