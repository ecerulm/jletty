// $ANTLR 2.7.6 (2005-12-22): "guidesyntaxparser.g" -> "GuideSyntaxParser.java"$

package org.jletty.schema;

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

public class GuideSyntaxParser extends antlr.LLkParser       implements GuideSyntaxParserTokenTypes
 {

protected GuideSyntaxParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public GuideSyntaxParser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected GuideSyntaxParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public GuideSyntaxParser(TokenStream lexer) {
  this(lexer,2);
}

public GuideSyntaxParser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
}

	public final void guidesyntax() throws RecognitionException, TokenStreamException {
		
		
		{
		switch ( LA(1)) {
		case OID:
		case DESCSTRING:
		{
			woid();
			match(DASH);
			break;
		}
		case NOT:
		case LPAREN:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		criteria();
	}
	
	public final void woid() throws RecognitionException, TokenStreamException {
		
		
		switch ( LA(1)) {
		case OID:
		{
			{
			match(OID);
			}
			break;
		}
		case DESCSTRING:
		{
			{
			match(DESCSTRING);
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
	}
	
	public final void criteria() throws RecognitionException, TokenStreamException {
		
		
		{
		switch ( LA(1)) {
		case LPAREN:
		{
			{
			simplecriteria();
			}
			break;
		}
		case NOT:
		{
			{
			match(NOT);
			simplecriteria();
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
	}
	
	public final void simplecriteria() throws RecognitionException, TokenStreamException {
		
		
		{
		if ((LA(1)==LPAREN) && (LA(2)==DESCSTRING)) {
			criteriaitem();
		}
		else if ((LA(1)==LPAREN) && (LA(2)==NOT||LA(2)==LPAREN)) {
			criteriaset();
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
	}
	
	public final void criteriaitem() throws RecognitionException, TokenStreamException {
		
		
		match(LPAREN);
		attributetype();
		match(DOLLAR);
		matchtype();
		match(RPAREN);
	}
	
	public final void criteriaset() throws RecognitionException, TokenStreamException {
		
		
		match(LPAREN);
		criteria();
		{
		switch ( LA(1)) {
		case AND:
		{
			{
			{
			int _cnt19=0;
			_loop19:
			do {
				if ((LA(1)==AND)) {
					match(AND);
					criteria();
				}
				else {
					if ( _cnt19>=1 ) { break _loop19; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt19++;
			} while (true);
			}
			}
			break;
		}
		case OR:
		{
			{
			match(OR);
			criteria();
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(RPAREN);
	}
	
	public final void attributetype() throws RecognitionException, TokenStreamException {
		
		
		match(DESCSTRING);
	}
	
	public final void matchtype() throws RecognitionException, TokenStreamException {
		
		
		match(DESCSTRING);
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"DASH",
		"OID",
		"DESCSTRING",
		"NOT",
		"LPAREN",
		"DOLLAR",
		"RPAREN",
		"AND",
		"OR",
		"WS",
		"LBRACKET",
		"RBRACKET",
		"ALPHA",
		"LDIGIT",
		"DIGIT",
		"NUMBER",
		"NUMERICOID"
	};
	
	
	}
