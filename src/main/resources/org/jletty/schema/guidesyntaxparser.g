header {
package org.jletty.schema;
}

class GuideSyntaxParser extends Parser;

options {
	k=2;
			defaultErrorHandler=false;
}

guidesyntax : (woid DASH)? criteria;

woid : (OID)|(DESCSTRING);
//objectclass: DESCSTRING;

criteria : ((simplecriteria)| (NOT simplecriteria));
simplecriteria : (criteriaitem| criteriaset);

criteriaitem : LPAREN attributetype DOLLAR matchtype RPAREN;

attributetype: DESCSTRING;
matchtype: DESCSTRING;

criteriaset : LPAREN criteria (((AND criteria)+)|(OR criteria)) RPAREN;

class GuideSyntaxLexer extends Lexer;
options {
	k=2;
		charVocabulary='\u0001'..'\u0127'; // allow ascii
		defaultErrorHandler=false;
}

WS     : ' ';
LPAREN : '(';
RPAREN : ')';
LBRACKET : '{';
RBRACKET : '}';
AND    : '&';
OR     : '|';
NOT    : '!';
DOLLAR : '$';
DASH   : '#';
protected ALPHA : ('a'..'z'|'A'..'Z') ;
protected LDIGIT: '1'..'9';
protected DIGIT: '0' | LDIGIT;
protected NUMBER: DIGIT | ( LDIGIT ( DIGIT )+ );
protected NUMERICOID: NUMBER ( '.' NUMBER )+;
OID : NUMERICOID ( LBRACKET NUMBER RBRACKET)?;
//protected OID: NUMERICOID | DESCR; 	
DESCSTRING: ALPHA ( ALPHA | DIGIT | '-' )*;

