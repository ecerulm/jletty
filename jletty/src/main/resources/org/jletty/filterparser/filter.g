//   <filter> ::= '(' <filtercomp> ')'
//     <filtercomp> ::= <and> | <or> | <not> | <item>
//     <and> ::= '&' <filterlist>
//     <or> ::= '|' <filterlist>
//     <not> ::= '!' <filter>
//     <filterlist> ::= <filter> | <filter> <filterlist>
//     <item> ::= <simple> | <present> | <substring>
//     <simple> ::= <attr> <filtertype> <value>
//     <filtertype> ::= <equal> | <approx> | <greater> | <less>
//     <equal> ::= '='
//     <approx> ::= '~='
//     <greater> ::= '>='
//     <less> ::= '<='
//     <present> ::= <attr> '=*'
//     <substring> ::= <attr> '=' <initial> <any> <final>
//     <initial> ::= NULL | <value>
//     <any> ::= '*' <starval>
//     <starval> ::= NULL | <value> '*' <starval>
//     <final> ::= NULL | <value> */
     
header {
package org.jletty.filterparser;

import antlr.*;
import org.jletty.ldapstackldapops.*;


}      
     
class FilterParser extends Parser;

options {
	k=2;
	defaultErrorHandler=false;
}
{
	/** the token stream selector used for multiplexing the underlying stream */
    TokenStreamSelector selector;
    
	public void setSelector(TokenStreamSelector selector) {
		this.selector = selector;
	}
}
filter returns [Filter f = null;]
{
	Filter filter;
}
    : (LPAREN!)filter=filtercomp {f=filter;} 
    ;
    
filtercomp returns [Filter f = null;]   //     <filtercomp> ::= <and> | <or> | <not> | <item>
{
	Filter filter;
}
    : filter=and { f = filter; } 
      | filter=or {f = filter; }
      | filter=not {f = filter; }
      | filter=item {f = filter; }
    ;

and returns [Filter f = null;]
{
	Filter[] list;
	
}
    : AND list=filterlist RPAREN {f = new AndFilter(list);}
    ;
    
or returns [Filter f = null;]
{
	Filter[] list;
}
    : OR list=filterlist RPAREN { f = new OrFilter(list);}
    ;

not returns [Filter f = null;]
{
	Filter arg;
}
    : NOT arg=filter RPAREN { f = new NotFilter(arg);}
    ;
    
filterlist returns [Filter[] list = null]
{
	Filter arg;
	java.util.List acc = new java.util.ArrayList();
}
    : (arg=filter { acc.add(arg);}
      )+ {list = (Filter[])acc.toArray(new Filter[acc.size()]);}
    ;
    

item returns [Filter retFilter=null]
: retFilter=simple|retFilter=extensible;

extensible returns [Filter retFilter=null]
{
	  String attrName=null;
	  boolean dnAttrs=false;
	  String matchingRuleId = null;
}
: (
   (attr:ATTRNAME { attrName = attr.getText(); }
   (DN { dnAttrs=true;})?
   (COLON matchingrule:ATTRNAME {
  	                                matchingRuleId = matchingrule.getText().trim();
  	                                int idx = matchingRuleId.indexOf( ';' );
                                  if ( idx != -1 )
                                  {
                                    String msg = "matchingRule OIDs cannot have options: ";
                                    msg += matchingRuleId;
                                    throw new RecognitionException( msg, matchingRuleId, 0, idx );
                                  }
                                 })?)   
   | ((DN { dnAttrs=true;})?
      (COLON matchingrule2:ATTRNAME {
  	                                   matchingRuleId = matchingrule2.getText().trim();
  	                                   int idx = matchingRuleId.indexOf( ';' );
                                     if ( idx != -1 )
                                     {
                                       String msg = "matchingRule OIDs cannot have options: ";
                                       msg += matchingRuleId;
                                       throw new RecognitionException( msg, matchingRuleId, 0, idx );
                                     }
                                   }))   
  )                               
  COLONEQUALS 
    {
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
    };
      

			 

simple returns [Filter retFilter=null;]
{
	String attrName;	
	int type = -1;
}
: ((attr:ATTRNAME) { attrName = attr.getText(); }    
      ( EQUAL {type = FilterType.EQUAL;}
        |GREATER { type = FilterType.GREATER;}
        |LESS { type = FilterType.LESS; }
        |APPROX { type = FilterType.APPROX;} 
       )
  )
      {      	
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
      }
      	 
      ;


class FilterLexer extends Lexer;

options {
    k=3; // needed for newline junk    
    charVocabulary='\u0000'..'\u007F'; // allow ascii
}


WS  :   (   ' '
        |   '\t'
        |   '\r' '\n' { newline(); }
        |   '\n'      { newline(); }
        |   '\r'      { newline(); }
        )
        {$setType(Token.SKIP);} //ignore this token
    ;
    

AND : "&";
OR  : "|";
NOT : "!";        
LPAREN  : "(";
RPAREN  : ")";
STAR    : "*";
EQUAL   : "=" ;
APPROX  : "~=";
GREATER : ">=";
LESS    : "<=";
COLON   : ":";
COLONEQUALS: ":=";
DN      : ":dn";

protected ALPHA : ('a'..'z'|'A'..'Z') ;
protected DIGIT: '0' | LDIGIT;
protected LDIGIT: '1'..'9';
protected DESCR: ALPHA ( ALPHA | DIGIT | '-' )*;
protected OPTION: (ALPHA | DIGIT | '-' )+;
protected OPTIONS: (';' OPTION)*;
protected NUMBER: DIGIT | ( LDIGIT ( DIGIT )+ );
protected NUMERICOID: NUMBER ( '.' NUMBER )+;
protected OID: NUMERICOID | DESCR; 	
ATTRNAME: OID OPTIONS;


