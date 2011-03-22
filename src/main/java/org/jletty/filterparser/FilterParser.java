// $ANTLR 2.7.6 (2005-12-22): "filter.g" -> "FilterParser.java"$

package org.jletty.filterparser;

import antlr.*;
import org.jletty.ldapstackldapops.*;



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

public class FilterParser extends antlr.LLkParser       implements FilterParserTokenTypes
 {

	/** the token stream selector used for multiplexing the underlying stream */
    TokenStreamSelector selector;
    
	public void setSelector(TokenStreamSelector selector) {
		this.selector = selector;
	}

protected FilterParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public FilterParser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected FilterParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public FilterParser(TokenStream lexer) {
  this(lexer,2);
}

public FilterParser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
}

	public final Filter  filter() throws RecognitionException, TokenStreamException {
		Filter f = null;;
		
		
			Filter filter;
		
		
		{
		match(LPAREN);
		}
		filter=filtercomp();
		f=filter;
		return f;
	}
	
	public final Filter  filtercomp() throws RecognitionException, TokenStreamException {
		Filter f = null;;
		
		
			Filter filter;
		
		
		switch ( LA(1)) {
		case AND:
		{
			filter=and();
			f = filter;
			break;
		}
		case OR:
		{
			filter=or();
			f = filter;
			break;
		}
		case NOT:
		{
			filter=not();
			f = filter;
			break;
		}
		case ATTRNAME:
		case DN:
		case COLON:
		{
			filter=item();
			f = filter;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		return f;
	}
	
	public final Filter  and() throws RecognitionException, TokenStreamException {
		Filter f = null;;
		
		
			Filter[] list;
			
		
		
		match(AND);
		list=filterlist();
		match(RPAREN);
		f = new AndFilter(list);
		return f;
	}
	
	public final Filter  or() throws RecognitionException, TokenStreamException {
		Filter f = null;;
		
		
			Filter[] list;
		
		
		match(OR);
		list=filterlist();
		match(RPAREN);
		f = new OrFilter(list);
		return f;
	}
	
	public final Filter  not() throws RecognitionException, TokenStreamException {
		Filter f = null;;
		
		
			Filter arg;
		
		
		match(NOT);
		arg=filter();
		match(RPAREN);
		f = new NotFilter(arg);
		return f;
	}
	
	public final Filter  item() throws RecognitionException, TokenStreamException {
		Filter retFilter=null;
		
		
		if ((LA(1)==ATTRNAME) && ((LA(2) >= EQUAL && LA(2) <= APPROX))) {
			retFilter=simple();
		}
		else if (((LA(1) >= ATTRNAME && LA(1) <= COLON)) && ((LA(2) >= ATTRNAME && LA(2) <= COLONEQUALS))) {
			retFilter=extensible();
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		return retFilter;
	}
	
	public final Filter[]  filterlist() throws RecognitionException, TokenStreamException {
		Filter[] list = null;
		
		
			Filter arg;
			java.util.List acc = new java.util.ArrayList();
		
		
		{
		int _cnt9=0;
		_loop9:
		do {
			if ((LA(1)==LPAREN)) {
				arg=filter();
				acc.add(arg);
			}
			else {
				if ( _cnt9>=1 ) { break _loop9; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt9++;
		} while (true);
		}
		list = (Filter[])acc.toArray(new Filter[acc.size()]);
		return list;
	}
	
	public final Filter  simple() throws RecognitionException, TokenStreamException {
		Filter retFilter=null;;
		
		Token  attr = null;
		
			String attrName;	
			int type = -1;
		
		
		{
		{
		attr = LT(1);
		match(ATTRNAME);
		}
		attrName = attr.getText();
		{
		switch ( LA(1)) {
		case EQUAL:
		{
			match(EQUAL);
			type = FilterType.EQUAL;
			break;
		}
		case GREATER:
		{
			match(GREATER);
			type = FilterType.GREATER;
			break;
		}
		case LESS:
		{
			match(LESS);
			type = FilterType.LESS;
			break;
		}
		case APPROX:
		{
			match(APPROX);
			type = FilterType.APPROX;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		}
			
			  FilterValueParser valueParser =
		new FilterValueParser(getInputState());
		selector.select("valueLexer");
		Object value = valueParser.value(); // go parse the value
		selector.select("main");
			  if (value instanceof SubstringValue) {
				    retFilter = new SubstringFilter(attrName,(SubstringValue)value);
			  } else {
			  	//FIXME remove the trim
				  String v = ((String)value);
				
				if (v.equals("*")) {
					retFilter = new PresentFilter(attrName);
				  } else {
					v = org.apache.commons.lang.StringEscapeUtils.unescapeJava(v);
					switch (type) {
						  case FilterType.EQUAL:
							      retFilter = new EqMatchFilter(attrName,v);
						      break;
						case FilterType.GREATER:
							retFilter = new GreaterOrEqualFilter(attrName,v);
						    break;
						case FilterType.LESS:
						      retFilter = new LessOrEqualFilter(attrName,v);
						break;
						    case FilterType.APPROX:
						    retFilter = new ApproxMatchFilter(attrName,v);
						    break;
						  default:
						      throw new RuntimeException("Unknown filter type " + type);
					    }
				  }
			}
		
		return retFilter;
	}
	
	public final Filter  extensible() throws RecognitionException, TokenStreamException {
		Filter retFilter=null;
		
		Token  attr = null;
		Token  matchingrule = null;
		Token  matchingrule2 = null;
		
			  String attrName=null;
			  boolean dnAttrs=false;
			  String matchingRuleId = null;
		
		
		{
		switch ( LA(1)) {
		case ATTRNAME:
		{
			{
			attr = LT(1);
			match(ATTRNAME);
			attrName = attr.getText();
			{
			switch ( LA(1)) {
			case DN:
			{
				match(DN);
				dnAttrs=true;
				break;
			}
			case COLON:
			case COLONEQUALS:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case COLON:
			{
				match(COLON);
				matchingrule = LT(1);
				match(ATTRNAME);
				
					                                matchingRuleId = matchingrule.getText().trim();
					                                int idx = matchingRuleId.indexOf( ';' );
				if ( idx != -1 )
				{
				String msg = "matchingRule OIDs cannot have options: ";
				msg += matchingRuleId;
				throw new RecognitionException( msg, matchingRuleId, 0, idx );
				}
				
				break;
			}
			case COLONEQUALS:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			}
			break;
		}
		case DN:
		case COLON:
		{
			{
			{
			switch ( LA(1)) {
			case DN:
			{
				match(DN);
				dnAttrs=true;
				break;
			}
			case COLON:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			match(COLON);
			matchingrule2 = LT(1);
			match(ATTRNAME);
			
				                                   matchingRuleId = matchingrule2.getText().trim();
				                                   int idx = matchingRuleId.indexOf( ';' );
			if ( idx != -1 )
			{
			String msg = "matchingRule OIDs cannot have options: ";
			msg += matchingRuleId;
			throw new RecognitionException( msg, matchingRuleId, 0, idx );
			}
			
			}
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(COLONEQUALS);
		
			   FilterValueParser valueParser =
		new FilterValueParser(getInputState());
		selector.select("valueLexer");
		Object value = valueParser.value(); // go parse the value
		selector.select("main");
		String v;
		if (value instanceof SubstringValue) {
			  v = ((SubstringValue)value).toString();
		} else {
		v = ((String)value);
		}
					v = org.apache.commons.lang.StringEscapeUtils.unescapeJava(v);
		retFilter = new ExtensibleMatchFilter(attrName,matchingRuleId, v, dnAttrs); 	  	    
		
		return retFilter;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"LPAREN",
		"AND",
		"RPAREN",
		"OR",
		"NOT",
		"ATTRNAME",
		"DN",
		"COLON",
		"COLONEQUALS",
		"EQUAL",
		"GREATER",
		"LESS",
		"APPROX",
		"WS",
		"STAR",
		"ALPHA",
		"DIGIT",
		"LDIGIT",
		"DESCR",
		"OPTION",
		"OPTIONS",
		"NUMBER",
		"NUMERICOID",
		"OID"
	};
	
	
	}
