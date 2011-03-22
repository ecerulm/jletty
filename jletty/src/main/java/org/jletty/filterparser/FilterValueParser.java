// $ANTLR 2.7.6 (2005-12-22): "filtervalue.g" -> "FilterValueParser.java"$

package org.jletty.filterparser;
import org.jletty.ldapstackldapops.*;
import java.util.Collections;
import org.apache.commons.lang.StringEscapeUtils;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;

public class FilterValueParser extends antlr.LLkParser       implements FilterValueParserTokenTypes
 {

protected FilterValueParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public FilterValueParser(TokenBuffer tokenBuf) {
  this(tokenBuf,3);
}

protected FilterValueParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public FilterValueParser(TokenStream lexer) {
  this(lexer,3);
}

public FilterValueParser(ParserSharedInputState state) {
  super(state,3);
  tokenNames = _tokenNames;
}

	public final Object  value() throws RecognitionException, TokenStreamException {
		Object value=null;;
		
		
		{
		if ((LA(1)==ATTRVALUE) && (LA(2)==RPAREN)) {
			value=equals();
		}
		else if ((LA(1)==STAR) && (LA(2)==RPAREN)) {
			value=presence();
		}
		else if ((LA(1)==ATTRVALUE||LA(1)==STAR) && (LA(2)==ATTRVALUE||LA(2)==STAR)) {
			value=substring();
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		return value;
	}
	
	public final String  equals() throws RecognitionException, TokenStreamException {
		String value=null;;
		
		Token  val = null;
		
		val = LT(1);
		match(ATTRVALUE);
		match(RPAREN);
		value = val.getText();
		return value;
	}
	
	public final String  presence() throws RecognitionException, TokenStreamException {
		String value=null;;
		
		
		match(STAR);
		match(RPAREN);
		value = "*";
		return value;
	}
	
	public final SubstringValue  substring() throws RecognitionException, TokenStreamException {
		SubstringValue value=null;;
		
		
		{
		if ((LA(1)==ATTRVALUE) && (LA(2)==STAR) && (LA(3)==RPAREN)) {
			value=alt1();
		}
		else if ((LA(1)==STAR)) {
			value=alt2();
		}
		else if ((LA(1)==ATTRVALUE) && (LA(2)==STAR) && (LA(3)==ATTRVALUE)) {
			value=alt3();
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		return value;
	}
	
	public final SubstringValue  alt1() throws RecognitionException, TokenStreamException {
		SubstringValue value=null;
		
		Token  t0 = null;
		
		t0 = LT(1);
		match(ATTRVALUE);
		match(STAR);
		match(RPAREN);
		value = new SubstringValue(t0.getText(),Collections.EMPTY_LIST,null);
		return value;
	}
	
	public final SubstringValue  alt2() throws RecognitionException, TokenStreamException {
		SubstringValue value=null;
		
		Token  t0 = null;
		
			String tmp=null;
			java.util.List any = new java.util.ArrayList();
		
		
		{
		int _cnt10=0;
		_loop10:
		do {
			if ((LA(1)==STAR) && (LA(2)==ATTRVALUE)) {
				match(STAR);
				t0 = LT(1);
				match(ATTRVALUE);
				
					 if (tmp != null) {
						    any.add(tmp);
					  }	
					    tmp = StringEscapeUtils.unescapeJava(t0.getText().trim());
				
			}
			else {
				if ( _cnt10>=1 ) { break _loop10; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt10++;
		} while (true);
		}
		{
		switch ( LA(1)) {
		case STAR:
		{
			match(STAR);
			// no final value
			any.add(tmp);
			tmp = null;
			
			break;
		}
		case RPAREN:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(RPAREN);
		
		//			String[] tmp2 = (String[])any.toArray(new String[any.size()]);
					value = new SubstringValue(null,any,tmp);
				
		return value;
	}
	
	public final SubstringValue  alt3() throws RecognitionException, TokenStreamException {
		SubstringValue value=null;;
		
		Token  t0 = null;
		Token  t1 = null;
		
			String initial=null;
			String tmp=null;
			java.util.List any = new java.util.ArrayList();
		
		
		t0 = LT(1);
		match(ATTRVALUE);
		initial = StringEscapeUtils.unescapeJava(t0.getText().trim());
		{
		int _cnt14=0;
		_loop14:
		do {
			if ((LA(1)==STAR)) {
				match(STAR);
				t1 = LT(1);
				match(ATTRVALUE);
				
										                     if (tmp != null && !tmp.equals("")) {
											any.add(tmp);
										}
										tmp = StringEscapeUtils.unescapeJava(t1.getText().trim());
				
			}
			else {
				if ( _cnt14>=1 ) { break _loop14; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt14++;
		} while (true);
		}
		match(RPAREN);
		
					//String[] tmp2 = (String[])any.toArray(new String[any.size()]);
					if (initial.equals("")) initial = null;
					   if (tmp.equals("")) tmp = null;
					value = new SubstringValue(initial,any,tmp);
		
		return value;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"ATTRVALUE",
		"RPAREN",
		"STAR",
		"ESC",
		"HEX",
		"ESCAPEDCHAR",
		"NORMAL",
		"UTF1SUBSET",
		"UTF8",
		"UTFMB",
		"UTF0",
		"UTF1",
		"UTF2",
		"UTF3",
		"UTF4"
	};
	
	
	}
