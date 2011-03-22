// $ANTLR 2.7.6 (2005-12-22): "dnparser.g" -> "AntlrDnParser.java"$

package org.jletty.dn;

import antlr.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import org.apache.log4j.Logger;
import javax.naming.Name;
import javax.naming.InvalidNameException;
import org.jletty.jndiprovider.*;
  

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

public class AntlrDnParser extends antlr.LLkParser       implements AntlrDnParserTokenTypes
 {

    /** the token stream selector used for multiplexing the underlying stream */
    TokenStreamSelector selector;

    /**
     * Sets the token stream selector used for multiplexing the underlying stream.
     *
     * @param selector the token stream selector used for multiplexing
     */
    public void setSelector( TokenStreamSelector selector )
    {
        this.selector = selector;
    }

protected AntlrDnParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public AntlrDnParser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected AntlrDnParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public AntlrDnParser(TokenStream lexer) {
  this(lexer,2);
}

public AntlrDnParser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
}

	public final DistinguishedName  distinguishedname() throws RecognitionException, TokenStreamException, InvalidNameException {
		DistinguishedName n = new DistinguishedName();;
		
		
			   DnNameComponent nc=null;
		
		
		nc=namecomponent();
		{
		_loop3:
		do {
			if ((LA(1)==WS)) {
				match(WS);
			}
			else {
				break _loop3;
			}
			
		} while (true);
		}
		n.add(0,nc);
		{
		_loop5:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				nc=namecomponent();
				n.add(0,nc);
			}
			else {
				break _loop5;
			}
			
		} while (true);
		}
		return n;
	}
	
	public final DnNameComponent  namecomponent() throws RecognitionException, TokenStreamException, InvalidNameException {
		DnNameComponent comp = new DnNameComponent();;
		
		
			 DnAtom attrTaV = null;
		
		
		{
		attrTaV=attributeTypeAndValue();
		comp.add(attrTaV);
		{
		{
		_loop10:
		do {
			if ((LA(1)==PLUS)) {
				match(PLUS);
				attrTaV=attributeTypeAndValue();
				comp.add(attrTaV);
			}
			else {
				break _loop10;
			}
			
		} while (true);
		}
		}
		}
		return comp;
	}
	
	public final DnAtom  attributeTypeAndValue() throws RecognitionException, TokenStreamException, InvalidNameException {
		DnAtom atom = null;;
		
		Token  attrType = null;
		Token  attrValue = null;
		
			  selector.select("attributetypelexer");
			  String attributeType = "";
			  String attributeValue = "";
		
		
		{
		{
		attrType = LT(1);
		match(ATTRIBUTETYPE);
		}
		attributeType = attrType.getText().trim(); selector.select("dnlexer");
		match(EQUALS);
		selector.select("attributevaluelexer");
		{
		attrValue = LT(1);
		match(ATTRIBUTEVALUE);
		}
		attributeValue = attrValue.getText();selector.select("dnlexer");
		}
		
			   atom = new DnAtom(attributeType,attributeValue);
		
		return atom;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"ATTRIBUTETYPE",
		"ATTRIBUTEVALUE",
		"WS",
		"COMMA",
		"PLUS",
		"EQUALS"
	};
	
	
	}
