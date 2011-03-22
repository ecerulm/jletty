// $ANTLR 2.7.6 (2005-12-22): "schema.g" -> "SchemaParser.java"$

package org.jletty.schemaparser;

import antlr.*;
import org.jletty.schema.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import org.apache.log4j.Logger;
import java.io.EOFException;
  

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

public class SchemaParser extends antlr.LLkParser       implements SchemaParserTokenTypes
 {

  	Logger logger = Logger.getLogger("org.jletty.SchemaParsing");
  	
	  /** the token stream selector used for multiplexing the underlying stream */
    TokenStreamSelector selector;    
    
	  public void setSelector(TokenStreamSelector selector) {
		    this.selector = selector;
	  }

protected SchemaParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public SchemaParser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected SchemaParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public SchemaParser(TokenStream lexer) {
  this(lexer,2);
}

public SchemaParser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
}

	public final void schema(
		Schema schema
	) throws RecognitionException, TokenStreamException {
		
		
		{
		_loop3:
		do {
			switch ( LA(1)) {
			case ATTRTYPEDESC:
			{
				attributetype(schema);
				break;
			}
			case OBJECTCLASS:
			{
				objectclass(schema);
				break;
			}
			default:
			{
				break _loop3;
			}
			}
		} while (true);
		}
	}
	
	public final AttributeType  attributetype(
		Schema schema
	) throws RecognitionException, TokenStreamException {
		AttributeType toReturn=null;
		
		Token  numericoid = null;
		Token  name = null;
		Token  name2 = null;
		Token  d = null;
		Token  sclass = null;
		Token  eq = null;
		Token  ord = null;
		Token  substr = null;
		Token  syn = null;
		Token  length = null;
		Token  us = null;
		
			  AttributeType attrType=null;
			  int len=-1;
			  String subMatchRule = "";
			  String ordMatchRule = "";
			  String[] names =null;
			  ArrayList namesList = new ArrayList();
			  boolean isSingleValue=false;
			  boolean nonUserModificable=false;
			  String superclass="";
			  String eqMatchRule="";
			  String syntax="";
			  String desc="";
			  boolean isObsolete=false;
			  boolean isCollective=false;
			  AttributeUsage usage = 				AttributeUsage.USER_APPLICATIONS;
		
		
		match(ATTRTYPEDESC);
		match(LPAREN);
		numericoid = LT(1);
		match(NUMERICOID);
		{
		_loop23:
		do {
			switch ( LA(1)) {
			case NAME:
			{
				{
				match(NAME);
				{
				switch ( LA(1)) {
				case QDSTRING:
				{
					name = LT(1);
					match(QDSTRING);
					names=new String[]{name.getText().trim()};
					break;
				}
				case LPAREN:
				{
					{
					match(LPAREN);
					{
					int _cnt10=0;
					_loop10:
					do {
						if ((LA(1)==QDSTRING)) {
							name2 = LT(1);
							match(QDSTRING);
							namesList.add(name2.getText().trim());
						}
						else {
							if ( _cnt10>=1 ) { break _loop10; } else {throw new NoViableAltException(LT(1), getFilename());}
						}
						
						_cnt10++;
					} while (true);
					}
					match(RPAREN);
					}
					names = (String [])namesList.toArray(new String[namesList.size()]);
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
			case DESC:
			{
				{
				match(DESC);
				d = LT(1);
				match(QDSTRING);
				desc=d.getText().trim();
				}
				break;
			}
			case OBSOLETE:
			{
				{
				match(OBSOLETE);
				isObsolete=true;
				}
				break;
			}
			case SUP:
			{
				{
				match(SUP);
				sclass = LT(1);
				match(STRING);
				superclass=sclass.getText().trim();
				}
				break;
			}
			case EQUALITY:
			{
				{
				match(EQUALITY);
				eq = LT(1);
				match(STRING);
				eqMatchRule=eq.getText().trim();
				}
				break;
			}
			case ORDERING:
			{
				{
				match(ORDERING);
				ord = LT(1);
				match(STRING);
				ordMatchRule=ord.getText().trim();
				}
				break;
			}
			case SUBSTR:
			{
				{
				match(SUBSTR);
				substr = LT(1);
				match(STRING);
				subMatchRule = substr.getText().trim();
				}
				break;
			}
			case SYNTAX:
			{
				{
				match(SYNTAX);
				syn = LT(1);
				match(NUMERICOID);
				syntax=syn.getText().trim();
				{
				switch ( LA(1)) {
				case LEN:
				{
					length = LT(1);
					match(LEN);
					len=Integer.parseInt(length.getText().trim());
					break;
				}
				case NAME:
				case RPAREN:
				case DESC:
				case OBSOLETE:
				case SUP:
				case EQUALITY:
				case ORDERING:
				case SUBSTR:
				case SYNTAX:
				case SINGLEVALUE:
				case NOUSERMODIFICATION:
				case COLLECTIVE:
				case USAGE:
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
			case SINGLEVALUE:
			{
				{
				match(SINGLEVALUE);
				isSingleValue=true;
				}
				break;
			}
			case NOUSERMODIFICATION:
			{
				{
				match(NOUSERMODIFICATION);
				nonUserModificable=true;
				}
				break;
			}
			case COLLECTIVE:
			{
				{
				match(COLLECTIVE);
				isCollective=true;
				}
				break;
			}
			case USAGE:
			{
				{
				match(USAGE);
				us = LT(1);
				match(STRING);
				
					                    usage=AttributeUsage.getEnum(us.getText().trim());
					                    if (null==usage) throw new RuntimeException("Unrecognized USAGE "+us.getText().trim());
				}
				break;
			}
			default:
			{
				break _loop23;
			}
			}
		} while (true);
		}
		match(RPAREN);
		
			final String syntaxS = syntax+len;
			if (logger.isDebugEnabled()) {
			     logger.debug("numericoid:"+numericoid.getText().trim());	   
			   	  StringBuffer sb = new StringBuffer();
			     for(int i =0; i < names.length;i++) {
			   	    sb.append(" " + names[i] +" ");
			     }
			     logger.debug("NAME(s): " + sb);	      
			     logger.debug("DESC:"+desc);
			     logger.debug("EQUALITY:"+eqMatchRule);
			     logger.debug("SUBSTR:"+subMatchRule);
			     
			     logger.debug("SYNTAX:"+syntaxS);
			}
			
			   toReturn = new AttributeType(
			                      numericoid.getText(),
			                      names,
			                      								desc, 
			                      								isObsolete, 
			                      								superclass,
			                      				eqMatchRule, 
			                      				ordMatchRule, 
			                      				subMatchRule,
						syntax,
						len, 
						isSingleValue, 
						isCollective, 
						nonUserModificable,
						usage);
			   schema.addAttributeType(toReturn);                     			
			   logger.info("Attribute type: \"" + toReturn + "\" added to schema");
		
		return toReturn;
	}
	
	public final ObjectClass  objectclass(
		Schema schema
	) throws RecognitionException, TokenStreamException {
		ObjectClass toReturn=null;
		
		Token  numericoid = null;
		Token  name = null;
		Token  name2 = null;
		Token  d = null;
		
			  String[] names = null;
			  String desc="";
		List l_mustoids=Collections.EMPTY_LIST;
			  List l_mayoids=Collections.EMPTY_LIST;
			  ArrayList namesList=new ArrayList();
			  ArrayList l_sups=null;
			  boolean isObsolete=false;
			  String numoid="";
			  ObjectClassType type = ObjectClassType.ABSTRACT;
			  
		
		
		match(OBJECTCLASS);
		match(LPAREN);
		numericoid = LT(1);
		match(NUMERICOID);
		numoid = numericoid.getText().trim();
		{
		_loop38:
		do {
			switch ( LA(1)) {
			case NAME:
			{
				{
				match(NAME);
				{
				switch ( LA(1)) {
				case QDSTRING:
				{
					name = LT(1);
					match(QDSTRING);
					names=new String[]{name.getText().trim()};
					break;
				}
				case LPAREN:
				{
					{
					match(LPAREN);
					{
					int _cnt30=0;
					_loop30:
					do {
						if ((LA(1)==QDSTRING)) {
							name2 = LT(1);
							match(QDSTRING);
							namesList.add(name2.getText().trim());
						}
						else {
							if ( _cnt30>=1 ) { break _loop30; } else {throw new NoViableAltException(LT(1), getFilename());}
						}
						
						_cnt30++;
					} while (true);
					}
					match(RPAREN);
					}
					names = (String [])namesList.toArray(new String[namesList.size()]);
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
			case DESC:
			{
				{
				match(DESC);
				d = LT(1);
				match(QDSTRING);
				desc=d.getText().trim();
				}
				break;
			}
			case SUP:
			{
				{
				match(SUP);
				l_sups=oids();
				}
				break;
			}
			case OBSOLETE:
			{
				{
				match(OBSOLETE);
				isObsolete=true;
				}
				break;
			}
			case ABSTRACT:
			case STRUCTURAL:
			case AUXILIARY:
			{
				{
				{
				switch ( LA(1)) {
				case ABSTRACT:
				{
					match(ABSTRACT);
					type=ObjectClassType.ABSTRACT;
					break;
				}
				case STRUCTURAL:
				{
					match(STRUCTURAL);
					type=ObjectClassType.STRUCTURAL;
					break;
				}
				case AUXILIARY:
				{
					match(AUXILIARY);
					type=ObjectClassType.AUXILIARY;
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
			case MUST:
			{
				{
				match(MUST);
				l_mustoids=oids();
				}
				break;
			}
			case MAY:
			{
				{
				match(MAY);
				l_mayoids=oids();
				}
				logger.debug("Found MAY parsing attribute names");
				break;
			}
			default:
			{
				break _loop38;
			}
			}
		} while (true);
		}
		match(RPAREN);
		
			  String[] mustOids = (String [])l_mustoids.toArray(new String[l_mustoids.size()]);
				  String[] mayOids = (String [])l_mayoids.toArray(new String[l_mayoids.size()]);
			  String[] sups = (String[])l_sups.toArray(new String[l_sups.size()]);
			  toReturn = new ObjectClass(numoid,names,desc,sups,isObsolete,type, mustOids,mayOids);
			  schema.addObjectclass(toReturn);
			  logger.debug("Objectclass parsed");
		
		return toReturn;
	}
	
	public final ArrayList  oids() throws RecognitionException, TokenStreamException {
		ArrayList toReturn=null;
		
		Token  id = null;
		Token  id2 = null;
		Token  id3 = null;
		
			   toReturn=new ArrayList();
		
		
		{
		switch ( LA(1)) {
		case LPAREN:
		{
			{
			match(LPAREN);
			id = LT(1);
			match(STRING);
			toReturn.add(id.getText().trim());
			{
			_loop43:
			do {
				if ((LA(1)==DOLLAR)) {
					match(DOLLAR);
					id2 = LT(1);
					match(STRING);
					toReturn.add(id2.getText().trim());
				}
				else {
					break _loop43;
				}
				
			} while (true);
			}
			match(RPAREN);
			}
			break;
		}
		case STRING:
		{
			{
			id3 = LT(1);
			match(STRING);
			toReturn.add(id3.getText().trim());
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		logger.debug("list of oids parsed length " + toReturn.size());
		return toReturn;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"attributetype\"",
		"LPAREN",
		"NUMERICOID",
		"\"NAME\"",
		"QDSTRING",
		"RPAREN",
		"\"DESC\"",
		"\"OBSOLETE\"",
		"\"SUP\"",
		"STRING",
		"\"EQUALITY\"",
		"\"ORDERING\"",
		"\"SUBSTR\"",
		"\"SYNTAX\"",
		"LEN",
		"\"SINGLE-VALUE\"",
		"\"NO-USER-MODIFICATION\"",
		"\"COLLECTIVE\"",
		"\"USAGE\"",
		"\"objectclass\"",
		"\"ABSTRACT\"",
		"\"STRUCTURAL\"",
		"\"AUXILIARY\"",
		"\"MUST\"",
		"\"MAY\"",
		"DOLLAR",
		"COMMENT",
		"WS",
		"QUOTE",
		"DIGIT",
		"LDIGIT",
		"ALPHA"
	};
	
	
	}
