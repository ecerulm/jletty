header {
package org.jletty.dn;

import antlr.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import org.apache.log4j.Logger;
  
}     

class AttributeTypeLexer extends Lexer;

options {
    k=1; // needed for newline junk    
    	charVocabulary='\u0001'..'\u0127'; // allow ascii
    defaultErrorHandler = false; // Don't generate parser error handlers
    importVocab=Common;
    exportVocab=AttributeTypeLexer;
}



{
  	Logger logger = Logger.getLogger("org.jletty.DnParsing");	 
}


protected WS        : ' ';
protected DIGIT     : '0' .. '9';
protected ALPHA     : 'A' .. 'Z' | 'a' .. 'z';
protected KEYCHAR   : ALPHA | DIGIT | '-';
protected OID       : (DIGIT)+ ('.' (DIGIT)+)*;
ATTRIBUTETYPE       : ((WS)*)! (ALPHA (KEYCHAR)*)  | OID;

