/*
 * Created on 08-dic-2004
 *
 */
package org.jletty.db;

import java.io.InputStream;

import org.jletty.filterparser.FilterLexer;
import org.jletty.filterparser.FilterParser;
import org.jletty.filterparser.FilterValueLexer;
import org.jletty.ldapstackldapops.Filter;

import antlr.TokenStreamSelector;


/**
 * @author Ruben
 * 
 */
public class MainTree {

	public static void main(String[] args) throws Exception {
		// String filter =
		// "(|(&(attr1=value1)(attr2<=value2)(attr3~=value3)(attr4=*val*ue*4*))(!(attr5=value5)))";
		String filter = "(cn=New _Entry)";
		System.out.println("orig is  " + filter);
		InputStream is = new java.io.ByteArrayInputStream(filter.getBytes());
		// FilterLexer lexer = new FilterLexer(is);
		// FilterParser parser = new FilterParser(lexer);
		//		
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
		try {
			System.out.println("begin parsing...");
			Filter filter2 = parser.filter();
			System.out.println("The filter was " + filter2);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
