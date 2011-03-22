/*
 * Created on 15-feb-2005
 *
 */
package org.jletty.schemaparser;

import java.io.InputStream;

import org.jletty.schema.Schema;

import antlr.ANTLRException;
import antlr.TokenStreamSelector;

/**
 * @author Ruben
 * 
 */
public final class Parser {

	private Parser() {
	} // forbid instantiation

	public static void parse(InputStream is, Schema schema) {
		try {
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
			parser.schema(schema);
		} catch (ANTLRException e) {
			throw new RuntimeException(e);
		}
	}

}
