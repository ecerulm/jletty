package org.jletty.db;

import java.io.InputStream;

import org.jletty.filterparser.FilterLexer;
import org.jletty.filterparser.FilterParser;

import antlr.collections.AST;


public class MainFilter {
	public static void main(String[] args) throws Exception {
		String filter = "(|(&(attr1=value1)(attr2<=value2)(attr3~=value3)(attr4=*val*ue*4*))(!(attr5=value5)))";
		
		InputStream is = new java.io.ByteArrayInputStream(filter.getBytes());
		FilterLexer lexer = new FilterLexer(is);
		FilterParser parser = new FilterParser(lexer);
		try {
			parser.filter();
			AST t = parser.getAST();
		    System.out.println(t.toStringTree());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}