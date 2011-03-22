package org.jletty.dn;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameParser;

import antlr.ANTLRException;
import antlr.TokenStreamSelector;

public class Rfc2253NameParserImpl implements NameParser {

	public Rfc2253NameParserImpl() {

	}

	public DnNameComponent parseDnNameComponent(String dnNameComponentString)
			throws InvalidNameException {
		Reader sr = new StringReader(dnNameComponentString);
		AntlrDnParser parser = setUpParser(sr);

		try {
			DnNameComponent toReturn = parser.namecomponent();
			if (sr.read() != -1) {
				throw new InvalidNameException("extra charactes to be parsed");
			}
			return toReturn;
		} catch (ANTLRException e) {
			final InvalidNameException invalidNameException = new InvalidNameException();
			invalidNameException.setRootCause(e);
			throw invalidNameException;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
	//TODO add a new method (not in NameParser interface) called parseDN that returns a DistinguishedName instead of Name 

	public Name parse(String nameString) throws InvalidNameException {

		try {
			if ("".equals(nameString)) {
				return new DistinguishedName();
			}
			Reader sr = new StringReader(nameString);

			AntlrDnParser parser = setUpParser(sr);
			DistinguishedName name = parser.distinguishedname();
			if (sr.read() != -1) {
				throw new InvalidNameException("extra charactes to be parsed");
			}
			return name;
		} catch (ANTLRException e) {
			final InvalidNameException invalidNameException = new InvalidNameException();
			invalidNameException.setRootCause(e);
			throw invalidNameException;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private AntlrDnParser setUpParser(Reader sr) {
		// create filter lexer
		DnLexer dnLexer = new DnLexer(sr);
		// create valuelexer attache to input state of filterlexer
		AttributeTypeLexer attributeTypeLexer = new AttributeTypeLexer(dnLexer
				.getInputState());
		AttributeValueLexer attributeValueLexer = new AttributeValueLexer(
				dnLexer.getInputState());
		TokenStreamSelector selector = new TokenStreamSelector();

		selector.addInputStream(dnLexer, "dnlexer");
		selector.addInputStream(attributeTypeLexer, "attributetypelexer");
		selector.addInputStream(attributeValueLexer, "attributevaluelexer");
		AntlrDnParser parser = new AntlrDnParser(selector);
		parser.setSelector(selector);
		selector.select("dnlexer");
		return parser;
	}

}
