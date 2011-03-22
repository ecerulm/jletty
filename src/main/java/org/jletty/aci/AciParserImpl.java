/* Don delete it's not generated */
package org.jletty.aci;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import antlr.ANTLRException;
import antlr.TokenStreamSelector;

public class AciParserImpl {

	public AciParserImpl() {

	}

	public Aci parse(String aciString) {

		try {
			if ((null == aciString) || ("".equals(aciString))) {
				throw new IllegalArgumentException(
						"aciString cannot be empty or null");
			}
			Reader sr = new StringReader(aciString);

			AntlrAciParser parser = setUpParser(sr);
			Aci name = parser.aci();
			if (sr.read() != -1) {
				// TODO change exception
				throw new RuntimeException("extra charactes to be parsed");
			}
			return name;
		} catch (ANTLRException e) {
			// TODO: throw an appropiate exception
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private AntlrAciParser setUpParser(Reader sr) {
		// create filter lexer
		AciLexer aciLexer = new AciLexer(sr); 
		// create valuelexer attache to input state of filterlexer
		// AttributeTypeLexer attributeTypeLexer = new
		// AttributeTypeLexer(dnLexer
		// .getInputState());
		// AttributeValueLexer attributeValueLexer = new AttributeValueLexer(
		// dnLexer.getInputState());
		TokenStreamSelector selector = new TokenStreamSelector();

		selector.addInputStream(aciLexer, "acilexer");
		// selector.addInputStream(attributeTypeLexer, "attributetypelexer");
		// selector.addInputStream(attributeValueLexer, "attributevaluelexer");
		AntlrAciParser parser = new AntlrAciParser(selector);
		parser.setSelector(selector);
		selector.select("acilexer");
		return parser;
	}

}
