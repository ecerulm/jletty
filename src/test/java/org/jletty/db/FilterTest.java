/*
 * Created on 09-dic-2004
 *
 */
package org.jletty.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.naming.directory.InvalidSearchFilterException;

import junit.framework.TestCase;
import junitx.framework.ArrayAssert;

import org.jletty.filterparser.FilterLexer;
import org.jletty.filterparser.FilterParser;
import org.jletty.filterparser.FilterValueLexer;
import org.jletty.filterparser.JlettyFilterParser;
import org.jletty.ldapstackldapops.AndFilter;
import org.jletty.ldapstackldapops.ApproxMatchFilter;
import org.jletty.ldapstackldapops.EqMatchFilter;
import org.jletty.ldapstackldapops.ExtensibleMatchFilter;
import org.jletty.ldapstackldapops.Filter;
import org.jletty.ldapstackldapops.GreaterOrEqualFilter;
import org.jletty.ldapstackldapops.LdapEntry;
import org.jletty.ldapstackldapops.LessOrEqualFilter;
import org.jletty.ldapstackldapops.NotFilter;
import org.jletty.ldapstackldapops.OrFilter;
import org.jletty.ldapstackldapops.PresentFilter;
import org.jletty.ldapstackldapops.SubstringFilter;
import org.jletty.ldapstackldapops.SubstringValue;
import org.jletty.messageprocessor.TestUtils;
import org.jletty.schema.AttributeType;
import org.jletty.schema.AttributeUsage;
import org.jletty.schema.MatchResult;
import org.jletty.schema.Schema;
import org.jletty.schemaparser.SchemaLexer;
import org.jletty.schemaparser.SchemaParser;
import org.jletty.util.HexUtils;
import org.jletty.util.Log4jTestsConfigurator;

import antlr.ANTLRException;
import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.TokenStreamSelector;

/**
 * @author Ruben
 * 
 */
public class FilterTest extends TestCase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		Log4jTestsConfigurator.configure();
		try {
			Schema schema = Schema.getInstance();
			schema.clear();
			SchemaParser schemaParser = createSchemaParser("/org/jletty/ldapstackparsersimplementation/core.schema");
			schemaParser.schema(schema);

			schema.resolveDependencies();
		} catch (ANTLRException e) {
			throw new RuntimeException(e);
		}

	}

	protected SchemaParser createSchemaParser(String filename) {
		InputStream is = getClass().getResourceAsStream(filename);

		// create filter lexer
		SchemaLexer mainLexer = new SchemaLexer(is);
		// create valuelexer attache to input state of filterlexer
		// FilterValueLexer valueLexer = new FilterValueLexer(mainLexer
		// .getInputState());
		TokenStreamSelector selector = new TokenStreamSelector();

		selector.addInputStream(mainLexer, "main");
		// selector.addInputStream(valueLexer, "valueLexer");
		SchemaParser parser = new SchemaParser(selector);
		parser.setSelector(selector);
		selector.select("main");
		return parser;
	}

	public void testEqualsFilter() throws Exception {
		String filter = "(cn=New _Entry)";

		Filter f = parse(filter);
		assertEquals(filter, f.toString());
	}

	public void testEqualsFilterTrue() {
		EqMatchFilter f = new EqMatchFilter("sn", "personsn1");

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.TRUE, f.match(entry));

	}

	public void testEqualsFilterFalse() {
		EqMatchFilter f = new EqMatchFilter("sn", "personsn2");

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.FALSE, f.match(entry));

	}

	public void testEqualsFilterUndefAttrDescNotRecognized() {
		// the attribute description is not recognized by the server
		EqMatchFilter f = new EqMatchFilter("nonExistenAttrDesc", "personsn2");

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.UNDEF, f.match(entry));

	}

	public void testEqualsFilterUndefAssertionValueCannotBeParsed() {
		// the assertion value cannot be parsed
		byte[] assertionValue = HexUtils.fromHexString("80 10 aa bb");
		EqMatchFilter f = new EqMatchFilter("sn", assertionValue);

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.UNDEF, f.match(entry));
	}

	public void testAll() throws Exception {
		String filter = "(|(&(cn=value1)(dnQualifier<=value2)(cn~=value3)(cn=*val*ue*4*))(!(cn=value5)))";
		Filter f = parse(filter);
		assertEquals(filter, f.toString());
	}

	public void testGreaterOrEqualsFilterTrue1() {
		setUpSchemaNotStandard();

		GreaterOrEqualFilter f = new GreaterOrEqualFilter("sn", "personsn0");
		// personsn1 >= personsn0 true
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.TRUE, f.match(entry));
	}

	public void testGreaterOrEqualsFilterTrue2() {
		setUpSchemaNotStandard();

		GreaterOrEqualFilter f = new GreaterOrEqualFilter("sn", "personsn1");
		// personsn1 >= personsn1 true
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.TRUE, f.match(entry));
	}

	public void testGreaterOrEqualsFilterFalse() {
		setUpSchemaNotStandard();

		GreaterOrEqualFilter f = new GreaterOrEqualFilter("sn", "personsn3");
		// personsn1 >= personsn3 false
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.FALSE, f.match(entry));
	}

	public void testGreaterOrEqualsFilterUndef() {
		// attr desc not recognized
		GreaterOrEqualFilter f = new GreaterOrEqualFilter(
				"attrDescNotRecognized", "personsn0");
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.UNDEF, f.match(entry));
	}

	public void testGreaterOrEqualsFilterUndefNoOrderingMatchRule() {
		// sn has no orderingMatchRule
		GreaterOrEqualFilter f = new GreaterOrEqualFilter("sn", "personsn0");
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.UNDEF, f.match(entry));
	}

	public void testGreaterOrEqualsFilterUndefAssertionValueCannotBeParsed() {
		byte[] assertionValue = HexUtils.fromHexString("80 10 aa bb");
		GreaterOrEqualFilter f = new GreaterOrEqualFilter("sn", assertionValue);
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.UNDEF, f.match(entry));
	}

	public void testLessOrEqualsFilterTrue1() {
		setUpSchemaNotStandard();

		LessOrEqualFilter f = new LessOrEqualFilter("sn", "another_personsn2");
		// personsn2 <= personsn0 true
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.TRUE, f.match(entry));
	}

	public void testLessOrEqualsFilterTrue2() {
		setUpSchemaNotStandard();

		LessOrEqualFilter f = new LessOrEqualFilter("sn", "another_personsn1");
		// personsn1 <= personsn1 true
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.TRUE, f.match(entry));
	}

	public void testLessOrEqualsFilterFalse() {
		setUpSchemaNotStandard();

		LessOrEqualFilter f = new LessOrEqualFilter("sn", "another_personsn0");
		// personsn1 <= personsn0 false
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.FALSE, f.match(entry));
	}

	public void testLessOrEqualsFilterUndef() {
		// attr desc not recognized
		LessOrEqualFilter f = new LessOrEqualFilter("attrDescNotRecognized",
				"personsn0");
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.UNDEF, f.match(entry));
	}

	public void testLessOrEqualsFilterUndefNoOrderingMatchRule() {
		// sn has no orderingMatchRule
		LessOrEqualFilter f = new LessOrEqualFilter("sn", "personsn0");
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.UNDEF, f.match(entry));
	}

	public void testLessOrEqualsFilterUndefAssertionValueCannotBeParsed() {
		byte[] assertionValue = HexUtils.fromHexString("80 10 aa bb");
		LessOrEqualFilter f = new LessOrEqualFilter("sn", assertionValue);
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.UNDEF, f.match(entry));
	}

	public void testApproxFilterTrue() {
		ApproxMatchFilter f = new ApproxMatchFilter("sn", "personnsn1");
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.TRUE, f.match(entry));
	}

	public void testApproxFilterFalse() {
		ApproxMatchFilter f = new ApproxMatchFilter("sn", "doesntmatch");
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.FALSE, f.match(entry));
	}

	public void testApproxFilterUndefUnrecognizedAttrDesc() {
		ApproxMatchFilter f = new ApproxMatchFilter("snNotExist", "personsn1");
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.UNDEF, f.match(entry));
	}

	public void testApproxFilterUndefAttributeValueCannotBeParsed() {
		byte[] assertionValue = HexUtils.fromHexString("80 10 aa bb");
		ApproxMatchFilter f = new ApproxMatchFilter("sn", assertionValue);
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.UNDEF, f.match(entry));
	}

	public void testItemFilter() throws Exception {
		String filter = "( ou ~=people)";
		Filter f = parse(filter);
		assertEquals(filter.replaceAll("\\s", ""), f.toString());
		assertTrue(f instanceof ApproxMatchFilter);
		ApproxMatchFilter res = (ApproxMatchFilter) f;
		assertEquals("ou", res.getAttrDesc());
		assertEquals("people", res.getAssertionValueAsString());
	}

	public void testAndFilter() throws Exception {
		String filter = "(&(ou~=people)(dnQualifier>=30)) ";
		Filter f = parse(filter);
		assertEquals(filter.replaceAll("\\s", ""), f.toString());
		assertTrue(f instanceof AndFilter);
		AndFilter res = (AndFilter) f;
		assertEquals(2, res.getChildren().size());
	}

	public void testAndFilter3Elements() throws Exception {
		String filter = "(&(ou~=people)(dnQualifier>=30)(cn=3))";
		Filter f = parse(filter);
		assertEquals(filter.replaceAll("\\s", ""), f.toString());

	}

	public void testAndFilterTrue() throws Exception {
		ArrayList list = new ArrayList();
		list.add(new Filter() {
			public MatchResult match(LdapEntry entry) {
				return MatchResult.TRUE;
			}

		});

		list.add(new Filter() {
			public MatchResult match(LdapEntry entry) {
				return MatchResult.TRUE;
			}

		});

		list.add(new Filter() {
			public MatchResult match(LdapEntry entry) {
				return MatchResult.TRUE;
			}

		});

		AndFilter f = new AndFilter((Filter[]) list.toArray(new Filter[list
				.size()]));

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");

		assertEquals(MatchResult.TRUE, f.match(entry));
	}

	public void testAndFilterFalse() throws Exception {
		ArrayList list = new ArrayList();
		list.add(new Filter() {
			public MatchResult match(LdapEntry entry) {
				return MatchResult.TRUE;
			}

		});

		list.add(new Filter() {
			public MatchResult match(LdapEntry entry) {
				return MatchResult.TRUE;
			}

		});

		list.add(new Filter() {
			public MatchResult match(LdapEntry entry) {
				return MatchResult.FALSE;
			}

		});

		AndFilter f = new AndFilter((Filter[]) list.toArray(new Filter[list
				.size()]));

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");

		assertEquals(MatchResult.FALSE, f.match(entry));
	}

	public void testAndFilterUndef() throws Exception {
		ArrayList list = new ArrayList();
		list.add(new Filter() {
			public MatchResult match(LdapEntry entry) {
				return MatchResult.TRUE;
			}

		});

		list.add(new Filter() {
			public MatchResult match(LdapEntry entry) {
				return MatchResult.TRUE;
			}

		});

		list.add(new Filter() {
			public MatchResult match(LdapEntry entry) {
				return MatchResult.UNDEF;
			}

		});

		AndFilter f = new AndFilter((Filter[]) list.toArray(new Filter[list
				.size()]));

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");

		assertEquals(MatchResult.UNDEF, f.match(entry));
	}

	public void testAndFilter2OrFilters() throws Exception {
		String filter = "(&(|(ou=people)(dnQualifier>=30))(|(cn=3)(cn=2))) ";
		Filter f = parse(filter);
		assertEquals(filter.replaceAll("\\s", ""), f.toString());
	}

	public void testNotFilterComplex() throws Exception {
		String filter = "(|(&(ou=value1)(cn<=value2)(sn~=value3)))";
		Filter f = parse(filter);
		assertEquals(filter.replaceAll("\\s", ""), f.toString());
	}

	public void testNotFilterTrue() {
		Filter inputFilter = new Filter() {
			public MatchResult match(LdapEntry entry) {
				return MatchResult.FALSE;
			}

		};
		NotFilter f = new NotFilter(inputFilter);

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");

		assertEquals(MatchResult.TRUE, f.match(entry));
	}

	public void testNotFilterFalse() {

		Filter inputFilter = new Filter() {
			public MatchResult match(LdapEntry entry) {
				return MatchResult.TRUE;
			}

		};
		NotFilter f = new NotFilter(inputFilter);

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");

		assertEquals(MatchResult.FALSE, f.match(entry));
	}

	public void testNotFilterUndef() {
		Filter inputFilter = new Filter() {
			public MatchResult match(LdapEntry entry) {
				return MatchResult.UNDEF;
			}

		};
		NotFilter f = new NotFilter(inputFilter);

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");

		assertEquals(MatchResult.UNDEF, f.match(entry));
	}

	public void testOrFilter() throws Exception {
		String filter = "(| ( ou ~=people) (dnQualifier>=30) ) ";
		Filter f = parse(filter);
		assertEquals(filter.replaceAll("\\s", ""), f.toString());
		assertTrue(f instanceof OrFilter);
		OrFilter res = (OrFilter) f;
		assertEquals(2, res.getChildren().size());

	}

	public void testOrFilterTrue() {
		// A filter of the "or" choice is FALSE if all
		// of the filters in the SET OF evaluate to FALSE, TRUE if at least
		// one filter is TRUE, and Undefined otherwise.
		ArrayList list = new ArrayList();
		list.add(new Filter() {
			public MatchResult match(LdapEntry entry) {
				return MatchResult.TRUE;
			}

		});

		list.add(new Filter() {
			public MatchResult match(LdapEntry entry) {
				return MatchResult.FALSE;
			}

		});

		list.add(new Filter() {
			public MatchResult match(LdapEntry entry) {
				return MatchResult.UNDEF;
			}

		});

		OrFilter f = new OrFilter((Filter[]) list.toArray(new Filter[list
				.size()]));

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");

		assertEquals(MatchResult.TRUE, f.match(entry));
	}

	public void testOrFilterFalse() {
		// A filter of the "or" choice is FALSE if all
		// of the filters in the SET OF evaluate to FALSE, TRUE if at least
		// one filter is TRUE, and Undefined otherwise.
		ArrayList list = new ArrayList();
		list.add(new Filter() {
			public MatchResult match(LdapEntry entry) {
				return MatchResult.FALSE;
			}

		});

		list.add(new Filter() {
			public MatchResult match(LdapEntry entry) {
				return MatchResult.FALSE;
			}

		});

		list.add(new Filter() {
			public MatchResult match(LdapEntry entry) {
				return MatchResult.FALSE;
			}

		});

		OrFilter f = new OrFilter((Filter[]) list.toArray(new Filter[list
				.size()]));

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");

		assertEquals(MatchResult.FALSE, f.match(entry));
	}

	public void testOrFilterUndef() {
		// A filter of the "or" choice is FALSE if all
		// of the filters in the SET OF evaluate to FALSE, TRUE if at least
		// one filter is TRUE, and Undefined otherwise.
		ArrayList list = new ArrayList();
		list.add(new Filter() {
			public MatchResult match(LdapEntry entry) {
				return MatchResult.FALSE;
			}

		});

		list.add(new Filter() {
			public MatchResult match(LdapEntry entry) {
				return MatchResult.FALSE;
			}

		});

		list.add(new Filter() {
			public MatchResult match(LdapEntry entry) {
				return MatchResult.UNDEF;
			}

		});

		OrFilter f = new OrFilter((Filter[]) list.toArray(new Filter[list
				.size()]));

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");

		assertEquals(MatchResult.UNDEF, f.match(entry));
	}

	public void testNotFilter() throws Exception {
		String filter = "( ! (& ( ou ~=people) (dnQualifier>=30)) )";
		Filter f = parse(filter);
		assertEquals(filter.replaceAll("\\s", ""), f.toString());
		assertTrue(f instanceof NotFilter);
		NotFilter res = (NotFilter) f;
		assertTrue(res.getChild() instanceof AndFilter);
	}

	public void testEscapeStarSubstring() throws Exception {
		String filter = "(cn=*\\2a*)";
		Filter f = parse(filter);

		assertTrue(f instanceof SubstringFilter);
		SubstringFilter res = (SubstringFilter) f;
		assertEquals("cn", res.getAttrDesc());
		assertEquals(1, res.getAny().size());
		assertTrue(res.getAny().contains("*"));

	}

	public void testEscapeStarEquals() throws Exception {
		String filter = "(cn=\\2A)";
		Filter f = parse(filter);

		assertTrue("parser returned an " + f.getClass()
				+ " instead of a EqMatchFilter", f instanceof EqMatchFilter);
		EqMatchFilter res = (EqMatchFilter) f;
		assertEquals("cn", res.getAttrDesc());
		assertEquals("*", res.getAssertionValueAsString());
	}

	public void testEscapeParens() throws Exception {
		String filter = "(o=Parens R Us \\28for all your parenthetical needs\\29)";
		Filter f = parse(filter);

		assertTrue(f instanceof EqMatchFilter);
		EqMatchFilter res = (EqMatchFilter) f;
		assertEquals("o", res.getAttrDesc());
		assertEquals("Parens R Us (for all your parenthetical needs)", res
				.getAssertionValueAsString());

	}

	public void testEscapeSlash1() throws Exception {
		String filter = "(cn=C:\\5cMyFile)";
		Filter f = parse(filter);

		assertTrue(f instanceof EqMatchFilter);
		EqMatchFilter res = (EqMatchFilter) f;
		assertEquals("cn", res.getAttrDesc());
		assertEquals("C:\\MyFile", res.getAssertionValueAsString());
	}

	public void testEscapeSlash2() throws Exception {
		String filter = "(cn=\\00\\00\\00\\04))";
		Filter f = parse(filter);

		assertTrue(f instanceof EqMatchFilter);
		EqMatchFilter res = (EqMatchFilter) f;
		assertEquals("cn", res.getAttrDesc());
		ArrayAssert.assertEquals(new byte[] { 0, 0, 0, 4 }, res
				.getAssertionValue());
		//      
		//      

	}

	public void testEscapeSlash3() throws Exception {
		String filter = "(cn=Gabriel Garc\\eda M\\e1rquez)";
		Filter f = parse(filter);
		assertTrue(f instanceof EqMatchFilter);
		EqMatchFilter res = (EqMatchFilter) f;
		assertEquals("cn", res.getAttrDesc());
		assertEquals("Gabriel García Márquez", res.getAssertionValueAsString());

	}

	//
	public void testOptionAndEscapesFilter() throws Exception {
		String filter = "( ou;lang-es >=Gabriel Garc\\eda M\\e1rquez)";
		Filter f = parse(filter);
		assertTrue(f instanceof GreaterOrEqualFilter);
		GreaterOrEqualFilter res = (GreaterOrEqualFilter) f;
		assertEquals("ou;lang-es", res.getAttrDesc());
		assertEquals("Gabriel García Márquez", res.getAssertionValueAsString());

	}

	public void testOptionsAndEscapesFilter() throws Exception {
		String filter = "( ou;lang-es;version-124 >=Gabriel Garc\\eda M\\e1rquez)";
		Filter f = parse(filter);
		assertTrue(f instanceof GreaterOrEqualFilter);
		GreaterOrEqualFilter res = (GreaterOrEqualFilter) f;
		assertEquals("ou;lang-es;version-124", res.getAttrDesc());
		assertEquals("Gabriel García Márquez", res.getAssertionValueAsString());

	}

	public void testNumericoidOptionsAndEscapesFilter() throws Exception {

		String filter = "( 1.3.4.2;lang-es;version-124 >=Gabriel Garc\\eda M\\e1rquez)";
		Filter f = parse(filter);
		assertTrue(f instanceof GreaterOrEqualFilter);
		GreaterOrEqualFilter res = (GreaterOrEqualFilter) f;
		assertEquals("1.3.4.2;lang-es;version-124", res.getAttrDesc());
		assertEquals("Gabriel García Márquez", res.getAssertionValueAsString());

	}

	public void testPresentFilter() throws Exception {

		String filter = "( ou =*)";
		Filter f = parse(filter);
		assertEquals(filter.replaceAll("\\s", ""), f.toString());
		assertTrue(f instanceof PresentFilter);
		PresentFilter res = (PresentFilter) f;
		assertEquals("ou", res.getAttrDesc());

		f = parse(filter);
		assertTrue(f instanceof PresentFilter);
		res = (PresentFilter) f;
		assertEquals("ou", res.getAttrDesc());

		f = parse(filter);
		assertTrue(f instanceof PresentFilter);
		res = (PresentFilter) f;
		assertEquals("ou", res.getAttrDesc());

		f = parse(filter);
		assertTrue(f instanceof PresentFilter);
		res = (PresentFilter) f;
		assertEquals("ou", res.getAttrDesc());

		f = parse(filter);
		assertTrue(f instanceof PresentFilter);
		res = (PresentFilter) f;
		assertEquals("ou", res.getAttrDesc());

	}

	public void testPresentFilterTrue() {
		PresentFilter f = new PresentFilter("sn");
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");

		assertEquals(MatchResult.TRUE, f.match(entry));
	}

	public void testPresentFilterFalse() {
		PresentFilter f = new PresentFilter("serialNumber");
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");

		assertEquals(MatchResult.FALSE, f.match(entry));
	}

	public void testNumericoidPresentFilter() throws Exception

	{

		String filter = "( 1.2.3.4 =*)";
		Filter f = parse(filter);
		assertEquals(filter.replaceAll("\\s", ""), f.toString());
		assertTrue(f instanceof PresentFilter);
		PresentFilter res = (PresentFilter) f;
		assertEquals("1.2.3.4", res.getAttrDesc());

		f = parse(filter);
		assertTrue(f instanceof PresentFilter);
		res = (PresentFilter) f;
		assertEquals("1.2.3.4", res.getAttrDesc());

		f = parse(filter);
		assertTrue(f instanceof PresentFilter);
		res = (PresentFilter) f;
		assertEquals("1.2.3.4", res.getAttrDesc());

		f = parse(filter);
		assertTrue(f instanceof PresentFilter);
		res = (PresentFilter) f;
		assertEquals("1.2.3.4", res.getAttrDesc());

		f = parse(filter);
		assertTrue(f instanceof PresentFilter);
		res = (PresentFilter) f;
		assertEquals("1.2.3.4", res.getAttrDesc());

	}

	public void testEqualsWithForwardSlashFilter() throws Exception {
		String filter = "( ou =people/in/my/company)";
		Filter f = parse(filter);
		assertTrue(f instanceof EqMatchFilter);
		EqMatchFilter res = (EqMatchFilter) f;
		assertEquals("ou", res.getAttrDesc());
		assertEquals("people/in/my/company", res.getAssertionValueAsString());

	}

	public void testExtensibleFilterForm1() throws Exception {
		String filter = "( ou :dn :stupidMatch :=Gabriel Garc\\eda M\\e1rquez)";
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;
		assertEquals("ou", res.getAttrDesc());
		assertEquals("Gabriel García Márquez", res.getAssertionValueAsString());
		assertTrue(res.isDnAttributes());
		assertEquals("stupidMatch", res.getMatchingRuleId());
	}

	public void testExtensibleFilterForm1WithNumericOid() throws Exception {
		String filter = "( 1.2.3.4 :dn :1.3434.23.2 :=Gabriel Garc\\eda M\\e1rquez)";
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;
		assertEquals("1.2.3.4", res.getAttrDesc());
		assertEquals("Gabriel García Márquez", res.getAssertionValueAsString());
		assertTrue(res.isDnAttributes());
		assertEquals("1.3434.23.2", res.getMatchingRuleId());
	}

	public void testExtensibleFilterForm1NoDnAttr() throws Exception

	{
		String filter = "( ou:stupidMatch :=Gabriel Garc\\eda M\\e1rquez)";
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;
		assertEquals("ou", res.getAttrDesc());
		assertEquals("Gabriel García Márquez", res.getAssertionValueAsString());
		assertFalse(res.isDnAttributes());
		assertEquals("stupidMatch", res.getMatchingRuleId());
	}

	public void testExtensibleFilterForm1OptionOnRule() throws Exception {
		try {
			String filter = "( ou:stupidMatch;lang-es :=Gabriel Garc\\eda M\\e1rquez)";
			Filter f = parse(filter);
			fail("it should trow an exception");
		} catch (InvalidSearchFilterException e) {
			// ignore
		}

	}

	public void testExtensibleFilterForm1NoAttrNoMatchingRule()
			throws Exception {
		String filter = "( ou :=Gabriel Garc\\eda M\\e1rquez)";
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;
		assertEquals("ou", res.getAttrDesc());
		assertEquals("Gabriel García Márquez", res.getAssertionValueAsString());
		assertFalse(res.isDnAttributes());
		assertEquals(null, res.getMatchingRuleId());
	}

	public void testExtensibleFilterForm1NoDnAttrWithNumericOidNoAttr()
			throws Exception {
		String filter = "( 1.2.3.4 :1.3434.23.2 :=Gabriel Garc\\eda M\\e1rquez)";
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;
		assertEquals("1.2.3.4", res.getAttrDesc());
		assertEquals("Gabriel García Márquez", res.getAssertionValueAsString());
		assertFalse(res.isDnAttributes());
		assertEquals("1.3434.23.2", res.getMatchingRuleId());
	}

	public void testExtensibleFilterForm2() throws Exception {
		String filter = "( :dn :stupidMatch :=Gabriel Garc\\eda M\\e1rquez)";
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;
		assertEquals(null, res.getAttrDesc());
		assertEquals("Gabriel García Márquez", res.getAssertionValueAsString());
		assertTrue(res.isDnAttributes());
		assertEquals("stupidMatch", res.getMatchingRuleId());
	}

	public void testExtensibleFilterForm2OptionOnRule() throws Exception {
		try {
			String filter = "( :dn :stupidMatch;lang-en :=Gabriel Garc\\eda M\\e1rquez)";
			Filter f = parse(filter);
			fail("it should throw an exception");
		} catch (InvalidSearchFilterException e) {
			// ignore
		}
	}

	public void testExtensibleFilterForm2WithNumericOid() throws Exception {
		String filter = "( :dn :1.3434.23.2 :=Gabriel Garc\\eda M\\e1rquez)";
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;
		assertEquals(null, res.getAttrDesc());
		assertEquals("Gabriel García Márquez", res.getAssertionValueAsString());
		assertTrue(res.isDnAttributes());
		assertEquals("1.3434.23.2", res.getMatchingRuleId());
	}

	public void testExtensibleFilterForm2NoDnAttr() throws Exception {
		String filter = "( :stupidMatch :=Gabriel Garc\\eda M\\e1rquez)";
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;
		assertEquals(null, res.getAttrDesc());
		assertEquals("Gabriel García Márquez", res.getAssertionValueAsString());
		assertFalse(res.isDnAttributes());
		assertEquals("stupidMatch", res.getMatchingRuleId());
	}

	public void testExtensibleFilterForm2NoDnAttrWithNumericOidNoAttr()
			throws Exception {
		String filter = "( :1.3434.23.2 :=Gabriel Garc\\eda M\\e1rquez)";
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;
		assertEquals(null, res.getAttrDesc());
		assertEquals("Gabriel García Márquez", res.getAssertionValueAsString());
		assertFalse(res.isDnAttributes());
		assertEquals("1.3434.23.2", res.getMatchingRuleId());
	}

	public void testExtensibleFilterNoAttributeNoMatchingRuleThrowsException()
			throws Exception {
		String filter = "( :dn :=Gabriel Garc\\eda M\\e1rquez)";
		try {
			Filter f = parse(filter);
			fail("It should throw an exception because there is no attr description nor matchingrule but returned "
					+ f);
		} catch (InvalidSearchFilterException e) {
			// ignore
		}

		filter = "( :=Gabriel Garc\\eda M\\e1rquez)";
		try {
			Filter f = parse(filter);
			fail("It should throw an exception because there is no attr description nor matchingrule");
		} catch (InvalidSearchFilterException e) {
			// ignore
		}

	}

	public void testExtensibleMatchFilterTrue1() throws Exception {
		String filter = "( sn:caseIgnoreMatch :=PERsonsn1)";
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");

		assertEquals(MatchResult.TRUE, res.match(entry));
	}

	public void testExtensibleMatchFilterFalse1() throws Exception {
		String filter = "( sn:caseIgnoreMatch :=PERsonsn2)";
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");

		assertEquals(MatchResult.FALSE, res.match(entry));
	}

	public void testExtensibleMatchFilterTrue2() throws Exception {
		String filter = "( sn:2.5.13.2 :=PERsonsn1)";
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");

		assertEquals(MatchResult.TRUE, res.match(entry));
	}

	public void testExtensibleMatchFilterFalse2() throws Exception {
		String filter = "( sn:2.5.13.5 :=PERsonsn1)"; // caseExactMatch will
		// not match personsn1
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");

		assertEquals(MatchResult.FALSE, res.match(entry));
	}

	public void testExtensibleMatchFilterTrue3() throws Exception {
		String filter = "( sn:dn:caseIgnoreMatch :=PERsonCn1)"; // cn=personcn1
		// should match
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("personcn1",
				"o=org1,c=US");

		assertEquals(MatchResult.TRUE, res.match(entry));

		// same but with a dnAttrs with matchingrule
		//
		// fail();
	}

	public void testExtensibleMatchFilterFalse3() throws Exception {
		String filter = "( sn:dn:caseExactMatch :=PERsonCn1)"; // cn=personcn1
		// should match
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("personcn1",
				"o=org1,c=US");

		assertEquals(MatchResult.FALSE, res.match(entry));

		// same but with a dnAttrs with matchingrule
		//
		// fail();
	}

	public void testExtensibleMatchFilterTrue3MultivaluedRdn() throws Exception {
		// cn=personcn1+telephonenumber=\\+41 1 268 1540 should match
		String filter = "( sn:dn:telephoneNumberMatch :=+41 1 268 1540)";
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;
		LdapEntry entry = TestUtils.createLdapEntryForCN_person(
				"personcn1+telephonenumber=\\+41 1 268 1540", "o=org1,c=US");
		assertEquals(MatchResult.TRUE, res.match(entry));
	}

	public void testExtensibleMatchFilterFalse3MultivaluedRdn()
			throws Exception {
		String filter = "( sn:dn:caseIgnoreMatch :=personsn2)"; // telephone
		// will not
		// match
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;
		LdapEntry entry = TestUtils.createLdapEntryForCN_person(
				"personcn1+telephonenumber=\\+41 1 268 1540", "o=org1,c=US");
		assertEquals(MatchResult.FALSE, res.match(entry));
	}

	public void testExtensibleMatchFilterTrue4() throws Exception {
		String filter = "( sn:dn :=personcn1)"; // same but with a dnAttrs
		// without matchingrule
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("personcn1",
				"o=org1,c=US");

		assertEquals(MatchResult.TRUE, res.match(entry));
	}

	public void testExtensibleMatchFilterFalse4() throws Exception {
		String filter = "( sn:dn :=personcn2)"; // same but with a dnAttrs
		// without matchingrule
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("personcn1",
				"o=org1,c=US");

		assertEquals(MatchResult.FALSE, res.match(entry));
	}

	public void testExtensibleMatchFilterTrue5() throws Exception {
		// should match any attribute that can do "2.5.13.2"/CASE_IGNORE_MATCH
		// as matchingRule
		String filter = "( :2.5.13.2 :=PERsonsn1)";
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("personcn1",
				"o=org1,c=US");

		assertEquals(MatchResult.TRUE, res.match(entry));

	}

	public void testExtensibleMatchFilterTrue6() throws Exception {
		// should match any attribute that can do "2.5.13.2"/CASE_IGNORE_MATCH
		// as matchingRule looking in dn also
		String filter = "( :dn:2.5.13.2 :=PerSoNCn1)";
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("personcn1",
				"o=org1,c=US");
		assertEquals(MatchResult.TRUE, res.match(entry));

	}

	public void testExtensibleMatchFilterFalse6() throws Exception {
		// should match any attribute that can do "2.5.13.5"/CASE_EXACT_MATCH
		// as matchingRule looking in dn also, but will not match
		String filter = "( :dn:2.5.13.5 :=PerSoNCn1)";
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("personcn1",
				"o=org1,c=US");
		assertEquals(MatchResult.FALSE, res.match(entry));

	}

	public void testExtensibleMatchFilterTrue7() throws Exception {
		// should match with equalityMatch caseIgnoreMatch the sn
		String filter = "( sn :=personsn1)";
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("personcn1",
				"o=org1,c=US");
		assertEquals(MatchResult.TRUE, res.match(entry));
	}

	public void testExtensibleMatchFilterFalse7() throws Exception {
		// should match with equalityMatch caseIgnoreMatch for sn attribute, but
		// will not match
		String filter = "( sn :=personSn2)";
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("personcn1",
				"o=org1,c=US");
		assertEquals(MatchResult.FALSE, res.match(entry));
	}

	public void testExtensibleMatchFilterUndefUnrecognizedMatchingRule1()
			throws Exception {
		// unrecognized matchingRuleId
		String filter = "( :1.1.1.1 :=PERsonsn1)";
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("personcn1",
				"o=org1,c=US");

		assertEquals(MatchResult.UNDEF, res.match(entry));
	}

	public void testExtensibleMatchFilterUndefUnrecognizedMatchingRule2()
			throws Exception {
		// unrecognized matchingRuleId
		String filter = "( :dn:1.1.1.1 :=PERsonsn1)";
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("personcn1",
				"o=org1,c=US");

		assertEquals(MatchResult.UNDEF, res.match(entry));
	}

	public void testExtensibleMatchFilterUndefUnrecognizedMatchingRule3()
			throws Exception {
		// unrecognized matchingRuleId
		String filter = "( sn:dn:1.1.1.1 :=PERsonsn1)";
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("personcn1",
				"o=org1,c=US");

		assertEquals(MatchResult.UNDEF, res.match(entry));
	}

	public void testExtensibleMatchFilterUndefUnparseableAssertionValue()
			throws Exception {
		// value not parseable
		String filter = "( sn:dn:2.5.13.8 :=nonumericstring)"; // matching rule
		// numericStringMatch
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("personcn1",
				"o=org1,c=US");

		assertEquals(MatchResult.UNDEF, res.match(entry));
	}

	public void testExtensibleMatchFilterUndefMatchingRuleNotSupportedByAttribute()
			throws Exception {
		// unsuported matching rule for this attribute
		String filter = "( sn:2.5.13.8 :=54333)"; // matching rule
		// numericStringMatch
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("personcn1",
				"o=org1,c=US");

		assertEquals(MatchResult.UNDEF, res.match(entry));

	}

	public void testExtensibleMatchFilterUndefMatchingRuleNotSupportedByAttributeNorDN()
			throws Exception {
		// unsuported matching rule for this attribute
		String filter = "( sn:dn:2.5.13.8 :=54333)"; // matching rule
		// numericStringMatch
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("personcn1",
				"o=org1,c=US");

		assertEquals(MatchResult.UNDEF, res.match(entry));

	}

	public void testExtensibleMatchFilterTrueMatchingRuleNotSupportedByDnAttributeOnly()
			throws Exception {
		// unsuported matching rule for this attribute
		String filter = "( sn:dn:2.5.13.8 :=54333)"; // matching rule
		// numericStringMatch
		Filter f = parse(filter);
		assertTrue(f instanceof ExtensibleMatchFilter);
		ExtensibleMatchFilter res = (ExtensibleMatchFilter) f;

		LdapEntry entry = TestUtils.createLdapEntryForCN_person(
				"personcn1+internationaliSDNNumber=54333", "o=org1,c=US");

		assertEquals(MatchResult.TRUE, res.match(entry));

	}

	public void testReuseParser() throws Exception {
		PipedInputStream is = new PipedInputStream();
		PipedOutputStream out = new PipedOutputStream(is);

		// create filter lexer
		FilterLexer mainLexer = new FilterLexer(is);
		// create valuelexer attache to input state of filterlexer
		FilterValueLexer valueLexer = new FilterValueLexer(mainLexer
				.getInputState());
		TokenStreamSelector selector = new TokenStreamSelector();

		selector.addInputStream(mainLexer, "main");
		selector.addInputStream(valueLexer, "valueLexer");
		FilterParser parser = new FilterParser(selector);
		parser.setSelector(selector);
		selector.select("main");

		parse(parser, out, "( ou ~= people )");
		parse(parser, out, "(& ( ou ~= people ) (dnQualifier>=30) ) ");
		parse(parser, out, "(| ( ou ~= people ) (dnQualifier>=30) ) ");
		parse(parser, out, "( ! (& ( ou ~= people ) (dnQualifier>=30) ) )");
		parse(parser, out, "( ou;lang-de >= \\23\\42asdl fkajsd )");
		parse(parser, out, "( ou;lang-de;version-124 >= \\23\\42asdl fkajsd )");
		parse(parser, out,
				"( 1.3.4.2;lang-de;version-124 >= \\23\\42asdl fkajsd )");
		parse(parser, out, "( ou =* )");
		parse(parser, out, "( 1.2.3.4 = * )");
		parse(parser, out, "( ou = people )");
		parse(parser, out, "( ou = people/in/my/company )");
		parse(parser, out, "( ou :dn :stupidMatch := dummyAssertion\\23\\ac )");
		parse(parser, out,
				"( 1.2.3.4 :dn :1.3434.23.2 := dummyAssertion\\23\\ac )");
		parse(parser, out, "( ou :stupidMatch := dummyAssertion\\23\\ac )");
		parse(parser, out, "( ou := dummyAssertion\\23\\ac )");
		parse(parser, out, "( 1.2.3.4 :1.3434.23.2 := dummyAssertion\\23\\ac )");
		parse(parser, out, "( :dn :stupidMatch := dummyAssertion\\23\\ac )");
		parse(parser, out, "( :dn :1.3434.23.2 := dummyAssertion\\23\\ac )");
		parse(parser, out, "( :stupidMatch := dummyAssertion\\23\\ac )");
		parse(parser, out, "( :1.3434.23.2 := dummyAssertion\\23\\ac )");
	}

	public void testEmptyString() throws Exception {
		try {
			assertNull(parse(""));
			fail("should throw InvalidSearchFilterException");
		} catch (InvalidSearchFilterException e) {
			// ignore
		}

	}

	public void testSubstringNoAnyNoFinal() throws Exception {
		String filter = "( ou = foo* )";
		Filter f = parse(filter);
		assertEquals(filter.replaceAll("\\s", ""), f.toString());
		assertTrue(f instanceof SubstringFilter);
		SubstringFilter res = (SubstringFilter) f;
		assertEquals("ou", res.getAttrDesc());
		assertEquals(0, res.getAny().size());
		List any = res.getAny();
		assertFalse(any.contains(""));
		assertEquals("foo", res.getInitial());
		assertEquals(null, res.getFin());
	}

	public void testSubstringNoAny() throws Exception {
		String filter = "( ou = foo*bar )";
		Filter f = parse(filter);
		assertEquals(filter.replaceAll("\\s", ""), f.toString());
		assertTrue(f instanceof SubstringFilter);
		SubstringFilter res = (SubstringFilter) f;
		assertEquals("ou", res.getAttrDesc());
		assertEquals(0, res.getAny().size());
		List any = res.getAny();
		assertFalse(any.contains(""));
		assertEquals("foo", res.getInitial());
		assertEquals("bar", res.getFin());
	}

	public void testSubstringNoAnyNoIni() throws Exception {
		String filter = "( ou = *bar )";
		Filter f = parse(filter);
		assertEquals(filter.replaceAll("\\s", ""), f.toString());
		assertTrue(f instanceof SubstringFilter);
		SubstringFilter res = (SubstringFilter) f;
		assertEquals("ou", res.getAttrDesc());
		assertEquals(0, res.getAny().size());
		List any = res.getAny();
		assertFalse(any.contains(""));
		assertEquals(null, res.getInitial());
		assertEquals("bar", res.getFin());

	}

	public void testSubstringOneAny() throws Exception {
		String filter = "( ou = foo*guy*bar )";
		Filter f = parse(filter);
		assertEquals(filter.replaceAll("\\s", ""), f.toString());
		assertTrue(f instanceof SubstringFilter);
		SubstringFilter res = (SubstringFilter) f;
		assertEquals("ou", res.getAttrDesc());
		assertEquals(1, res.getAny().size());
		List any = res.getAny();
		assertFalse(any.contains(""));
		assertTrue(any.contains("guy"));
		assertEquals("foo", res.getInitial());
		assertEquals("bar", res.getFin());
	}

	public void testSubstringManyAny() throws Exception {
		String filter = "( ou =a*b*c*d*e*f )";
		Filter f = parse(filter);
		assertEquals(filter.replaceAll("\\s", ""), f.toString());
		assertTrue(f instanceof SubstringFilter);
		SubstringFilter res = (SubstringFilter) f;
		assertEquals("ou", res.getAttrDesc());
		assertEquals(4, res.getAny().size());
		List any = res.getAny();
		assertFalse(any.contains(""));
		assertTrue(any.contains("e"));
		assertTrue(any.contains("b"));
		assertTrue(any.contains("c"));
		assertTrue(any.contains("d"));
		assertEquals("a", res.getInitial());
		assertEquals("f", res.getFin());

	}

	public void testSubstringNoIniManyAny() throws Exception {
		String filter = "( ou =*b*c*d*e*f )";
		Filter f = parse(filter);
		assertEquals(filter.replaceAll("\\s", ""), f.toString());
		assertTrue(f instanceof SubstringFilter);
		SubstringFilter res = (SubstringFilter) f;
		assertEquals("ou", res.getAttrDesc());
		assertEquals(4, res.getAny().size());
		List any = res.getAny();
		assertFalse(any.contains(""));
		assertTrue(any.contains("e"));
		assertTrue(any.contains("b"));
		assertTrue(any.contains("c"));
		assertTrue(any.contains("d"));
		assertEquals(null, res.getInitial());
		assertEquals("f", res.getFin());

	}

	public void testSubstringManyAnyNoFinal() throws Exception {
		String filter = "( ou =a*b*c*d*e* )";
		Filter f = parse(filter);
		assertEquals(filter.replaceAll("\\s", ""), f.toString());
		assertTrue(f instanceof SubstringFilter);
		SubstringFilter res = (SubstringFilter) f;
		assertEquals("ou", res.getAttrDesc());
		assertEquals(4, res.getAny().size());
		List any = res.getAny();
		assertFalse(any.contains(""));
		assertTrue(any.contains("e"));
		assertTrue(any.contains("b"));
		assertTrue(any.contains("c"));
		assertTrue(any.contains("d"));
		assertEquals("a", res.getInitial());
		assertEquals(null, res.getFin());
	}

	public void testSubstringNoIniManyAnyNoFinal() throws Exception {
		String filter = "( ou = *b*c*d*e* )";
		Filter f = parse(filter);
		assertEquals(filter.replaceAll("\\s", ""), f.toString());
		assertTrue(f instanceof SubstringFilter);
		SubstringFilter res = (SubstringFilter) f;
		assertEquals("ou", res.getAttrDesc());
		assertEquals(4, res.getAny().size());
		List any = res.getAny();
		assertFalse(any.contains(""));
		assertTrue(any.contains("e"));
		assertTrue(any.contains("b"));
		assertTrue(any.contains("c"));
		assertTrue(any.contains("d"));
		assertEquals(null, res.getInitial());
		assertEquals(null, res.getFin());
	}

	public void testSubstringNoAnyDoubleSpaceStar() throws Exception {
		String filter = "( ou = foo* *bar )";
		Filter f = parse(filter);
		assertEquals("(ou=foo*bar)", f.toString());
		assertTrue(f instanceof SubstringFilter);
		SubstringFilter res = (SubstringFilter) f;
		assertEquals("ou", res.getAttrDesc());
		assertEquals(0, res.getAny().size());
		assertEquals("foo", res.getInitial());
		assertEquals("bar", res.getFin());
	}

	public void testSubstringAnyDoubleSpaceStar() throws Exception {
		String filter = "( ou = foo* a *bar )";
		Filter f = parse(filter);
		assertEquals("(ou=foo*a*bar)", f.toString());
		assertTrue(f instanceof SubstringFilter);
		SubstringFilter res = (SubstringFilter) f;
		assertEquals("ou", res.getAttrDesc());
		assertEquals(1, res.getAny().size());
		assertTrue(res.getAny().contains("a"));
		assertEquals("foo", res.getInitial());
		assertEquals("bar", res.getFin());
	}

	public void testSubstringStarAnyStar() throws Exception {
		String filter = "( ou =*foo*)";
		Filter f = parse(filter);
		assertEquals(filter.replaceAll("\\s", ""), f.toString());
		assertTrue(f instanceof SubstringFilter);
		SubstringFilter res = (SubstringFilter) f;
		assertEquals("ou", res.getAttrDesc());
		assertEquals(1, res.getAny().size());
		assertTrue(res.getAny().contains("foo"));
		assertNull(res.getInitial());
		assertNull(res.getFin());
	}

	public void testSubstringFilterInitialTrue() {
		SubstringFilter f = new SubstringFilter("sn", new SubstringValue("per",
				(String[]) null, null)); // per*

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.TRUE, f.match(entry));

	}

	public void testSubstringFilterInitialFinalTrue() {
		SubstringFilter f = new SubstringFilter("sn", new SubstringValue("per",
				(String[]) null, "sn1")); // per*sn1

		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.TRUE, f.match(entry));
	}

	public void testSubstringFilterFinalTrue() {
		SubstringFilter f = new SubstringFilter("sn", new SubstringValue(null,
				(String[]) null, "sn1")); // *sn1
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.TRUE, f.match(entry));
	}

	public void testSubstringFilterInitialAnyFinalrue() {
		SubstringFilter f = new SubstringFilter("sn", new SubstringValue("p",
				new String[] { "r", "o", "s" }, "1")); // p*r*o*s*1
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.TRUE, f.match(entry));
	}

	public void testSubstringFilterAnyFinalTrue() {
		SubstringFilter f = new SubstringFilter("sn", new SubstringValue(null,
				new String[] { "r", "o", "s" }, "1")); // *r*o*s*1
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.TRUE, f.match(entry));
	}

	public void testSubstringFilterInitialAnyTrue() {
		SubstringFilter f = new SubstringFilter("sn", new SubstringValue("p",
				new String[] { "r", "o", "s" }, null)); // p*r*o*s*
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.TRUE, f.match(entry));
	}

	public void testSubstringFilterAnyTrue() {
		SubstringFilter f = new SubstringFilter("sn", new SubstringValue(null,
				new String[] { "r", "o", "s" }, null)); // *r*o*s*
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.TRUE, f.match(entry));
	}

	public void testSubstringFilterFalse() {
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");

		SubstringFilter f = new SubstringFilter("sn", new SubstringValue(
				"perl", (String[]) null, null)); // perl*
		assertEquals(MatchResult.FALSE, f.match(entry));

		f = new SubstringFilter("sn", new SubstringValue("per",
				(String[]) null, "sn2")); // per*sn2
		assertEquals(MatchResult.FALSE, f.match(entry));

		f = new SubstringFilter("sn", new SubstringValue(null, (String[]) null,
				"sn2")); // *sn2
		assertEquals(MatchResult.FALSE, f.match(entry));

		f = new SubstringFilter("sn", new SubstringValue("p", new String[] {
				"r", "x", "s" }, "1")); // p*r*x*s*1
		assertEquals(MatchResult.FALSE, f.match(entry));

		f = new SubstringFilter("sn", new SubstringValue(null, new String[] {
				"r", "x", "s" }, "1")); // *r*x*s*1
		assertEquals(MatchResult.FALSE, f.match(entry));

		f = new SubstringFilter("sn", new SubstringValue("p", new String[] {
				"r", "x", "s" }, null)); // p*r*x*s*
		assertEquals(MatchResult.FALSE, f.match(entry));

		f = new SubstringFilter("sn", new SubstringValue(null, new String[] {
				"r", "x", "s" }, "1")); // *r*x*s*
		assertEquals(MatchResult.FALSE, f.match(entry));

	};

	public void testSubstringFilterUndefAttrDescNotRecog() {
		// attr desc not recognized

		SubstringFilter f = new SubstringFilter("snAttrDescNotRecognized",
				new SubstringValue("p", new String[] { "r", "o", "s" }, "1")); // p*r*o*s*1
		LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
				"o=org1,c=US");
		assertEquals(MatchResult.UNDEF, f.match(entry));
	};
	
	
	// public void testSubstringFilterUndefAssertionValueCannotBeParsed() {
	// //TODO assertion value cannot be parsed
	// SubstringFilter f = new SubstringFilter("sn", new SubstringValue("p",
	// new String[]{"r","o","s"}, "1")); // p*r*o*s*1
	// LdapEntry entry = TestUtils.createLdapEntryForCN_person("person1",
	// "o=org1,c=US");
	// assertEquals(MatchResult.UNDEF, f.match(entry));
	// }

	private Filter parse(String filter) throws InvalidSearchFilterException {
		Filter f;
		f = JlettyFilterParser.parse(filter);
		return f;
	}

	/**
	 * @param parser
	 * @param out
	 * @param string
	 * @throws IOException
	 * @throws TokenStreamException
	 * @throws RecognitionException
	 */
	private Filter parse(FilterParser parser, PipedOutputStream out,
			String string) throws IOException, RecognitionException,
			TokenStreamException {
		out.write(string.getBytes());
		Filter f = parser.filter();
		return f;
	}

	private void setUpSchemaNotStandard() {

		// uses a unstandard name attribute with caseIgnoreOrderingMatch (name
		// hasn't ordering match rule)
		try {
			Schema schema = Schema.getInstance();
			schema.clear();
			// FIXME change the core.schema to a separate package
			SchemaParser schemaParser = createSchemaParser("/org/jletty/ldapstackparsersimplementation/core.schema");
			schemaParser.schema(schema);
			AttributeType nameAttrType = new AttributeType("2.5.4.41",
					new String[] { "name" }, "", false, "", "",
					"caseIgnoreOrderingMatch", "caseIgnoreSubstringsMatch",
					"1.3.6.1.4.1.1466.115.121.1.15", 32768, false, false,
					false, AttributeUsage.USER_APPLICATIONS);
			schema.addAttributeType(nameAttrType);

			schema.resolveDependencies();
		} catch (ANTLRException e) {
			throw new RuntimeException(e);
		}
	}
}
