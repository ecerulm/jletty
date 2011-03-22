//      AttributeTypeDescription = "(" whsp
//            numericoid whsp              ; AttributeType identifier
//          [ "NAME" qdescrs ]             ; name used in AttributeType
//          [ "DESC" qdstring ]            ; description
//          [ "OBSOLETE" whsp ]
//          [ "SUP" woid ]                 ; derived from this other
//                                         ; AttributeType
//          [ "EQUALITY" woid              ; Matching Rule name
//          [ "ORDERING" woid              ; Matching Rule name
//          [ "SUBSTR" woid ]              ; Matching Rule name
//          [ "SYNTAX" whsp noidlen whsp ] ; see section 4.3
//          [ "SINGLE-VALUE" whsp ]        ; default multi-valued
//          [ "COLLECTIVE" whsp ]          ; default not collective
//          [ "NO-USER-MODIFICATION" whsp ]; default user modifiable
//          [ "USAGE" whsp AttributeUsage ]; default userApplications
//          whsp ")"

//      AttributeUsage =
//          "userApplications"     /
//          "directoryOperation"   /
//          "distributedOperation" / ; DSA-shared
//          "dSAOperation"          ; DSA-specific, value depends on server     

header {
package org.jletty.schemaparser;

import antlr.*;
import org.jletty.schema.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import org.apache.log4j.Logger;
import java.io.EOFException;
  
}      
     
class SchemaParser extends Parser;
  options {
	    k=2;
	    defaultErrorHandler=false;
  }

{
  	Logger logger = Logger.getLogger("org.jletty.SchemaParsing");
  	
	  /** the token stream selector used for multiplexing the underlying stream */
    TokenStreamSelector selector;    
    
	  public void setSelector(TokenStreamSelector selector) {
		    this.selector = selector;
	  }
}

schema [Schema schema] : (attributetype[schema]|objectclass[schema])*;

attributetype [Schema schema] returns [AttributeType toReturn=null]
{
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
}
: ATTRTYPEDESC 
   LPAREN  
   numericoid:NUMERICOID  
   ((NAME (name:QDSTRING {names=new String[]{name.getText().trim()};}|(LPAREN (name2:QDSTRING {namesList.add(name2.getText().trim());})+ RPAREN) {names = (String [])namesList.toArray(new String[namesList.size()]);}))  
    |(DESC d:QDSTRING {desc=d.getText().trim();})
    |(OBSOLETE {isObsolete=true;})
    |(SUP sclass:STRING {superclass=sclass.getText().trim();})     
    |(EQUALITY  eq:STRING {eqMatchRule=eq.getText().trim();} ) 
    |(ORDERING  ord:STRING {ordMatchRule=ord.getText().trim();} ) 
    |(SUBSTR  substr:STRING { subMatchRule = substr.getText().trim();})  
    |(SYNTAX syn:NUMERICOID {syntax=syn.getText().trim();} (length:LEN {len=Integer.parseInt(length.getText().trim());})?)
    |(SINGLEVALUE {isSingleValue=true;})
    |(NOUSERMODIFICATION {nonUserModificable=true;})
    |(COLLECTIVE {isCollective=true;})
    |(USAGE us:STRING {
    	                    usage=AttributeUsage.getEnum(us.getText().trim());
    	                    if (null==usage) throw new RuntimeException("Unrecognized USAGE "+us.getText().trim());}
    	 )
    )* 
   RPAREN 
{
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
};

//      ObjectClassDescription = "(" whsp
//          numericoid whsp      ; ObjectClass identifier
//          [ "NAME" qdescrs ]
//          [ "DESC" qdstring ]
//          [ "OBSOLETE" whsp ]
//          [ "SUP" oids ]       ; Superior ObjectClasses
//          [ ( "ABSTRACT" / "STRUCTURAL" / "AUXILIARY" ) whsp ]
//                               ; default structural
//          [ "MUST" oids ]      ; AttributeTypes
//          [ "MAY" oids ]       ; AttributeTypes
//      whsp ")"
objectclass [Schema schema] returns [ObjectClass toReturn=null]
{
	  String[] names = null;
	  String desc="";
  List l_mustoids=Collections.EMPTY_LIST;
	  List l_mayoids=Collections.EMPTY_LIST;
	  ArrayList namesList=new ArrayList();
	  ArrayList l_sups=null;
	  boolean isObsolete=false;
	  String numoid="";
	  ObjectClassType type = ObjectClassType.ABSTRACT;
	  
}
: OBJECTCLASS LPAREN numericoid:NUMERICOID {numoid = numericoid.getText().trim();}  
   ((NAME (name:QDSTRING {names=new String[]{name.getText().trim()};}|(LPAREN (name2:QDSTRING {namesList.add(name2.getText().trim());})+ RPAREN) {names = (String [])namesList.toArray(new String[namesList.size()]);}))  
    |(DESC d:QDSTRING {desc=d.getText().trim();})
    |(SUP l_sups=oids)     
    |(OBSOLETE {isObsolete=true;})
    |((ABSTRACT {type=ObjectClassType.ABSTRACT;}|STRUCTURAL {type=ObjectClassType.STRUCTURAL;}|AUXILIARY {type=ObjectClassType.AUXILIARY;}))
    |(MUST l_mustoids=oids)    
    |(MAY l_mayoids=oids)  {logger.debug("Found MAY parsing attribute names");}
   )*
   RPAREN 
{
	  String[] mustOids = (String [])l_mustoids.toArray(new String[l_mustoids.size()]);
		  String[] mayOids = (String [])l_mayoids.toArray(new String[l_mayoids.size()]);
	  String[] sups = (String[])l_sups.toArray(new String[l_sups.size()]);
	  toReturn = new ObjectClass(numoid,names,desc,sups,isObsolete,type, mustOids,mayOids);
	  schema.addObjectclass(toReturn);
	  logger.debug("Objectclass parsed");
}   
;


oids returns [ArrayList toReturn=null]
{
	   toReturn=new ArrayList();
}
:
 (
  (LPAREN
     id:STRING { toReturn.add(id.getText().trim());}
     ( DOLLAR id2:STRING { toReturn.add(id2.getText().trim());})*
   RPAREN)
 | (id3:STRING { toReturn.add(id3.getText().trim());})
 ) {logger.debug("list of oids parsed length " + toReturn.size());};


class SchemaLexer extends Lexer;

options {
    k=3; // needed for newline junk    
    	charVocabulary='\u0001'..'\u0127'; // allow ascii
    defaultErrorHandler = false; // Don't generate parser error handlers
}

tokens {
 	 DESC="DESC";  	
  	NAME="NAME";
  OBSOLETE="OBSOLETE";
  SUP="SUP";
  EQUALITY="EQUALITY";
  ORDERING="ORDERING";
  SUBSTR="SUBSTR";
  SYNTAX= "SYNTAX";
  SINGLEVALUE= "SINGLE-VALUE";
  COLLECTIVE= "COLLECTIVE";
  NOUSERMODIFICATION= "NO-USER-MODIFICATION";
  USAGE= "USAGE";
  ATTRTYPEDESC= "attributetype";
  OBJECTCLASS="objectclass";
  STRUCTURAL="STRUCTURAL";
  AUXILIARY="AUXILIARY";
  ABSTRACT="ABSTRACT";
  MUST="MUST";
  MAY="MAY";
}

{
  	Logger logger = Logger.getLogger("org.jletty.SchemaParsing");	 
  
	//  public void uponEOF() throws TokenStreamException, CharStreamException {
//    throw new CharStreamIOException(new EOFException());   
//  	}

}

COMMENT : ('#' (~'\n')* '\n') { newline(); $setType(Token.SKIP );};

WS  :   (   ' '
        |   '\t'
        |   '\r' '\n' { newline(); }
        |   '\n'      { newline(); }
        |   '\r'      { newline(); }
        )    {$setType(Token.SKIP );}    
    ;   



QUOTE: "'";
LPAREN  : "(";
RPAREN  : ")";
DOLLAR : "$";


protected DIGIT: '0' | LDIGIT;

protected LDIGIT: '1'..'9';

protected ALPHA: 'A'..'Z' | 'a'..'z';

//protected NUMBER: DIGIT | ( LDIGIT ( DIGIT )+ );



NUMERICOID: (DIGIT)+ ( '.' (DIGIT)+ )+ {logger.debug("Found NUMERICOID: '" + getText()+"'");}; 	
QDSTRING: QUOTE! (~'\'')+ QUOTE! {logger.debug("Found QSTRING: '" + getText()+"'");};
STRING options {testLiterals=true;}: ALPHA ( ALPHA | DIGIT | '-')* {setText(getText().trim());logger.debug("Found token STRING: " + getText());};
//NOIDLEN: NUMERICOID('{'(NUMBER)+'}')?;
LEN: '{'!(DIGIT)+'}'! {logger.debug("Found LEN: " + getText());}; 

//ATTRNAME: OID OPTIONS;

    

