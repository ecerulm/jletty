/*
 * Created on 08-dic-2004
 *
 */
package org.jletty.filterparser;

import java.io.StringReader;

import javax.naming.directory.InvalidSearchFilterException;

import org.jletty.ldapstackldapops.Filter;

import antlr.ANTLRException;
import antlr.TokenStreamSelector;

/**
 * @author Ruben
 * 
 */
public class JlettyFilterParser {

	private JlettyFilterParser() {
		// make this class non instantiable
	}

	/**
	 * @param f
	 * @return
	 * @throws InvalidSearchFilterException
	 */
	public static Filter parse(String filter)
			throws InvalidSearchFilterException {

		FilterParser parser = createFilterParser(filter);
		try {
			Filter f = parser.filter();
			if (null == f) {
				throw new RuntimeException("Filter parsing returned null");
			}

			return f;
		} catch (ANTLRException e) {
			final InvalidSearchFilterException invalidSearchFilterException = new InvalidSearchFilterException(
					"filter:" + filter + " is invalid");
			invalidSearchFilterException.setRootCause(e);
			throw invalidSearchFilterException;
		}

	}

	private static FilterParser createFilterParser(String filter) {
		StringReader sReader = new StringReader(filter);

		// create filter lexer
		FilterLexer mainLexer = new FilterLexer(sReader);
		// create valuelexer attache to input state of filterlexer
		FilterValueLexer valueLexer = new FilterValueLexer(mainLexer
				.getInputState());
		TokenStreamSelector selector = new TokenStreamSelector();

		selector.addInputStream(mainLexer, "main");
		selector.addInputStream(valueLexer, "valueLexer");
		FilterParser parser = new FilterParser(selector);
		parser.setSelector(selector);
		selector.select("main");
		return parser;
	}

}
