package jletty.performancetest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jletty.ldapstackparsersimplementation.AbandonRequestParserTest;
import org.jletty.ldapstackparsersimplementation.AddRequestParserTest;
import org.jletty.ldapstackparsersimplementation.AndFilterParserTest;
import org.jletty.ldapstackparsersimplementation.ApproxMatchFilterParserTest;
import org.jletty.ldapstackparsersimplementation.AttributeDescriptionListParserTest;
import org.jletty.ldapstackparsersimplementation.AttributeTypeAndValuesListParserTest;
import org.jletty.ldapstackparsersimplementation.AttributeTypeAndValuesParserTest;
import org.jletty.ldapstackparsersimplementation.AttributeValuesParserTest;
import org.jletty.ldapstackparsersimplementation.AuthenticationChoiceParserTest;
import org.jletty.ldapstackparsersimplementation.BERIntegerParserTest;
import org.jletty.ldapstackparsersimplementation.BEROctetStringParserTest;
import org.jletty.ldapstackparsersimplementation.BindRequestParserTest;
import org.jletty.ldapstackparsersimplementation.CompareRequestParserTest;
import org.jletty.ldapstackparsersimplementation.DeleteRequestParserTest;
import org.jletty.ldapstackparsersimplementation.DerefAliasesParserTest;
import org.jletty.ldapstackparsersimplementation.ExtensibleMatchFilterParserTest;
import org.jletty.ldapstackparsersimplementation.LDAPMessageParserTest;
import org.jletty.ldapstackparsersimplementation.LDAPStringParserTest;
import org.jletty.ldapstackparsersimplementation.ModificationParserTest;
import org.jletty.ldapstackparsersimplementation.ModificationTypeParserTest;
import org.jletty.ldapstackparsersimplementation.ModifyRDNRequestParserTest;
import org.jletty.ldapstackparsersimplementation.ModifyRequestParserTest;
import org.jletty.ldapstackparsersimplementation.NotFilterParserTest;
import org.jletty.ldapstackparsersimplementation.OrFilterParserTest;
import org.jletty.ldapstackparsersimplementation.PresentFilterParserTest;
import org.jletty.ldapstackparsersimplementation.ScopeParserTest;
import org.jletty.ldapstackparsersimplementation.SearchRequestParserTest;
import org.jletty.ldapstackparsersimplementation.SequenceofModificationsParserTest;
import org.jletty.ldapstackparsersimplementation.SetOfFiltersParserTest;
import org.jletty.ldapstackparsersimplementation.SubstringFilterParserTest;
import org.jletty.ldapstackparsersimplementation.UnbindRequestParserTest;
import org.jletty.ldapstackparsersimplementation.greaterOrEqualFilterParserTest;
import org.jletty.ldapstackparsersimplementation.lessOrEqualFilterParserTest;

public class PerformanceTest extends TestCase {
	private static int numCycles = 1;

	private String myDate;

	public PerformanceTest() {
		super();
		init();
	}

	public PerformanceTest(String arg0) {
		super(arg0);
		init();
	}

	private void init() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		myDate = formatter.format(date);
	}

	public void testPerformanceTopLevelRequest() {
		List tests = new ArrayList();
		numCycles = 100000;
		tests.add(new BindRequestParserTest("BindRequestParserTest"));
		tests.add(new AddRequestParserTest("AddRequestParserTest"));
		tests.add(new SearchRequestParserTest("SearchRequestParserTest"));
		tests.add(new ModifyRDNRequestParserTest("ModifyRDNRequestParserTest"));
		tests.add(new ModifyRequestParserTest("ModifyRequestParserTest"));
		tests.add(new CompareRequestParserTest("CompareRequestParserTest"));
		tests.add(new DeleteRequestParserTest("DeleteRequestParserTest"));
		tests.add(new AbandonRequestParserTest("AbandonRequestParserTest"));
		tests.add(new UnbindRequestParserTest("UnbindRequestParserTest"));

		iterateOverTests(tests);
	}

	public void testPerformanceFilters() {
		List tests = new ArrayList();
		numCycles = 100000;
		tests.add(new greaterOrEqualFilterParserTest());
		tests.add(new NotFilterParserTest());
		tests.add(new AndFilterParserTest());
		tests.add(new ApproxMatchFilterParserTest());
		tests.add(new ExtensibleMatchFilterParserTest());
		tests.add(new PresentFilterParserTest());
		tests.add(new OrFilterParserTest());
		tests.add(new SubstringFilterParserTest());
		tests.add(new lessOrEqualFilterParserTest());

		iterateOverTests(tests);
	}

	public void testPerformanceLdapMessage() {
		List tests = new ArrayList();
		numCycles = 10000;
		tests.add(new LDAPMessageParserTest());

		iterateOverTests(tests);
	}

	public void testPerformanceComponents() {
		List tests = new ArrayList();
		numCycles = 100000;
		tests.add(new SequenceofModificationsParserTest());
		tests.add(new AttributeTypeAndValuesParserTest());
		tests.add(new BERIntegerParserTest());
		tests.add(new DerefAliasesParserTest());
		tests.add(new ScopeParserTest());
		tests.add(new AttributeDescriptionListParserTest());
		tests.add(new LDAPStringParserTest());
		tests.add(new AttributeValuesParserTest());
		tests.add(new AuthenticationChoiceParserTest());
		tests.add(new BEROctetStringParserTest());
		tests.add(new ModificationTypeParserTest());
		tests.add(new ModificationParserTest());
		tests.add(new SetOfFiltersParserTest());
		tests.add(new AttributeTypeAndValuesListParserTest());

		iterateOverTests(tests);
	}

	// public void testPerformanceAttributeTypeAndValuesList() {
	// List tests = new ArrayList();
	// numCycles = 10000;
	// tests.add(new AttributeTypeAndValuesListParserTest());
	// iterateOverTests(tests);
	// }

	private void iterateOverTests(List test) {

		for (Iterator iter = test.iterator(); iter.hasNext();) {
			PerformanceMeasurable element = (PerformanceMeasurable) iter.next();
			element.getPerformanceMeasures(numCycles); // warmup
			final Map performanceMeasures = element
					.getPerformanceMeasures(numCycles); // measures
			String name = element.getName();
			if (name == null) {
				name = element.getClass().getName();
			}
			printMeasures(name, myDate, performanceMeasures);
		}
	}

	private void printMeasures(String name, String myDate, Map result) {
		if (name.lastIndexOf('.') > 0) {
			name = name.substring(name.lastIndexOf('.') + 1); // Map$Entry
		}
		final long operationsPerSecond = ((Long) result
				.get("operationsPerSecond")).longValue();
		final long bytesPerSecond = ((Long) result.get("bytesPerSecond"))
				.longValue();
		final long bytesPerOperation = ((Long) result.get("bytesPerOperation"))
				.longValue();
		System.out.println("[" + name + "] [" + myDate + "] [op/s "
				+ operationsPerSecond + "] [bytes/sec " + bytesPerSecond
				+ "] [bytes/op " + bytesPerOperation + "]");
	}

	public static void main(String[] args) {
		// TestSuite suite = new TestSuite(PerformanceTest.class);
		TestSuite suite = new TestSuite(PerformanceTest.class);
		// suite.addTest(new
		// PerformanceTest("testPerformanceAttributeTypeAndValuesList"));

		junit.textui.TestRunner.run(suite);
	}

	protected void setUp() throws Exception {
		super.setUp();
		System.out.println();
	}

}
