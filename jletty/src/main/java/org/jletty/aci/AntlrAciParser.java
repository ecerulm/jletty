// $ANTLR 2.7.6 (2005-12-22): "aciparser.g" -> "AntlrAciParser.java"$

package org.jletty.aci;

import antlr.*;
import java.util.List;
import java.util.ArrayList;
  

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

public class AntlrAciParser extends antlr.LLkParser       implements AntlrAciParserTokenTypes
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

protected AntlrAciParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public AntlrAciParser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected AntlrAciParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public AntlrAciParser(TokenStream lexer) {
  this(lexer,2);
}

public AntlrAciParser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
}

	public final Aci  aci() throws RecognitionException, TokenStreamException {
		Aci aci = new Aci();;
		
		Token  name = null;
		
			Target t;
			Permission p;
			BindRule b;
			List perms = new ArrayList();
		
		
		try {      // for error handling
			{
			int _cnt3=0;
			_loop3:
			do {
				if ((LA(1)==LPAREN) && (LA(2)==TARGETTYPE)) {
					t=target();
					
							  	System.out.println("target parsed");
							  	aci.addTarget(t);
							
				}
				else {
					if ( _cnt3>=1 ) { break _loop3; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt3++;
			} while (true);
			}
			match(LPAREN);
			match(VERSION3);
			match(SEMICOLON);
			
				  	System.out.println("version parsed");
				  	aci.setVersion(TargetVersion.VERSION3_0);
				
			match(ACL);
			name = LT(1);
			match(STRING_LITERAL);
			match(SEMICOLON);
			
				  	System.out.println("acl parsed");
				String nameString = name.getText();
				nameString = nameString.substring(1,nameString.length()-1);
				  	aci.setName(nameString);
				
			{
			{
			int _cnt7=0;
			_loop7:
			do {
				if ((LA(1)==PERMTYPE)) {
					{
					p=permission();
					b=bindrule();
					match(SEMICOLON);
					}
					
						    	p.setBindRule(b);
						    	perms.add(p);
						
				}
				else {
					if ( _cnt7>=1 ) { break _loop7; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt7++;
			} while (true);
			}
			}
			
				    	//list to array
					    // Create an array containing the elements in a list
				    Permission[] permissionsArray = (Permission[])perms.toArray(new Permission[perms.size()]);
				aci.setPermissions(permissionsArray);    
				
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		return aci;
	}
	
	public final Target  target() throws RecognitionException, TokenStreamException {
		Target target = new Target();;
		
		Token  targettype = null;
		Token  targetliteral = null;
		
			TargetType tt;
		
		
		try {      // for error handling
			match(LPAREN);
			{
			targettype = LT(1);
			match(TARGETTYPE);
			}
				
				target.setType(TargetType.getEnum(targettype.getText()));
			
			{
			switch ( LA(1)) {
			case EQUALS:
			{
				match(EQUALS);
				
					target.setEquals(TargetEqualsType.EQUALS);  
				
				break;
			}
			case NEQUALS:
			{
				match(NEQUALS);
				
					target.setEquals(TargetEqualsType.NEQUALS);
				
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			targetliteral = LT(1);
			match(STRING_LITERAL);
			
				String literal = targetliteral.getText();
				literal = literal.substring(1,literal.length()-1);
			target.setTarget(literal);
			
			match(RPAREN);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		return target;
	}
	
	public final Permission  permission() throws RecognitionException, TokenStreamException {
		Permission p = new Permission();;
		
		Token  pt = null;
		Token  r = null;
		Token  r2 = null;
		Token  r3 = null;
		
		try {      // for error handling
			pt = LT(1);
			match(PERMTYPE);
			p.setType(PermissionType.getEnum(pt.getText()));
			match(LPAREN);
			{
			switch ( LA(1)) {
			case RIGHT:
			{
				{
				r = LT(1);
				match(RIGHT);
				p.addRight(PermissionRight.getEnum(r.getText()));
				{
				_loop15:
				do {
					if ((LA(1)==COMMA)) {
						match(COMMA);
						r2 = LT(1);
						match(RIGHT);
						p.addRight(PermissionRight.getEnum(r2.getText()));
					}
					else {
						break _loop15;
					}
					
				} while (true);
				}
				}
				break;
			}
			case RIGHTALL:
			{
				r3 = LT(1);
				match(RIGHTALL);
				p.addRight(PermissionRight.getEnum(r3.getText()));
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(RPAREN);
			
				  	System.out.println("permission parsed");
				
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
		return p;
	}
	
	public final BindRule  bindrule() throws RecognitionException, TokenStreamException {
		BindRule b = new BindRule();;
		
		Token  brk = null;
		Token  l = null;
		
		try {      // for error handling
			brk = LT(1);
			match(BINDRULEKEYWORD);
			
				  	System.out.println("bindrule keyword parsed");
				  	b.setType(BindRuleType.getEnum(brk.getText()));
				
			{
			switch ( LA(1)) {
			case EQUALS:
			{
				match(EQUALS);
				
						  	b.setEquals(BindRuleEqualsType.EQUALS);
						
				break;
			}
			case NEQUALS:
			{
				match(NEQUALS);
				
						  	b.setEquals(BindRuleEqualsType.NEQUALS);
						
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			
				  	System.out.println("equals parsed");
				
			l = LT(1);
			match(STRING_LITERAL);
			
				    String literal = l.getText();
				    String expression = literal.substring(1,literal.length()-1);
				    b.setExpression(expression);
				
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		return b;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"LPAREN",
		"RPAREN",
		"PLUS",
		"MINUS",
		"STAR",
		"INT",
		"VERSION3",
		"SEMICOLON",
		"COMMA",
		"ACL",
		"EQUALS",
		"NEQUALS",
		"QUOTE",
		"STRING_LITERAL",
		"TARGETTYPE",
		"LDAP",
		"DISAMBIGUATOR",
		"WS",
		"ESC",
		"USERDNBINDRULE",
		"USERATTRBINDRULE",
		"GROUPDNBINDRULE",
		"IPBINDRULE",
		"TIMEOFDAYBINDRULE",
		"AUTHMETHODBINDRULE",
		"DAYOFWEEKBINDRULE",
		"DNSBINDRULE",
		"ROLEDNBINDRULE",
		"RIGHT",
		"READRIGHT",
		"PERMTYPE",
		"RIGHTALL",
		"BINDRULEKEYWORD",
		"DAYOFWEEKVALUES"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 16L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 68719476736L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 2048L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	
	}
