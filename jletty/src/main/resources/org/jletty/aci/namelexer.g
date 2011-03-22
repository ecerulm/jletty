header {
	package org.jletty.aci; 
	
	import org.apache.log4j.Logger; 
}     

class NameLexer extends Lexer;

options {
    k=2; // needed for newline junk    
    charVocabulary='\u0001'..'\u0127'; // allow ascii
    defaultErrorHandler = false; // Don't generate parser error handlers
    exportVocab=NameLexer;
}



{
  	Logger logger = Logger.getLogger("org.jletty.DnParsing");	 
}


protected WS        : ' ';
protected DIGIT     : '0' .. '9';
protected ALPHA     : 'A' .. 'Z' | 'a' .. 'z';
protected KEYCHAR   : ALPHA | DIGIT | '-';
//protected OID       : (DIGIT)+ ('.' (DIGIT)+)*;
NAME       : ((WS)*)! (ALPHA (KEYCHAR)*);//  | OID;

