/*
 * Created on 26-sep-2004
 *
 */
package com.rubenlaguna.tests.performance;

import jletty.ldapstack.parsers.implementation.AbandonRequestParserTest;
import jletty.ldapstack.parsers.implementation.AddRequestParserTest;
import jletty.ldapstack.parsers.implementation.BindRequestParserTest;
import jletty.ldapstack.parsers.implementation.CompareRequestParserTest;
import jletty.ldapstack.parsers.implementation.DeleteRequestParserTest;
import jletty.ldapstack.parsers.implementation.ModifyRDNRequestParserTest;
import jletty.ldapstack.parsers.implementation.ModifyRequestParserTest;
import jletty.ldapstack.parsers.implementation.SearchRequestParserTest;
import jletty.ldapstack.parsers.implementation.UnbindRequestParserTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * @author Ruben
 *  
 */
public class TopLevelRequestPerformanceTest extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new BindRequestParserTest("testPerformance"));
		suite.addTest(new AddRequestParserTest("testPerformance"));
		suite.addTest(new SearchRequestParserTest("testPerformance"));
		suite.addTest(new ModifyRequestParserTest("testPerformance"));
		suite.addTest(new ModifyRDNRequestParserTest("testPerformance"));
		suite.addTest(new DeleteRequestParserTest("testPerformance"));
		suite.addTest(new CompareRequestParserTest("testPerformance"));
		suite.addTest(new AbandonRequestParserTest("testPerformance"));
		suite.addTest(new UnbindRequestParserTest("testPerformance"));

		return suite;
	}

}