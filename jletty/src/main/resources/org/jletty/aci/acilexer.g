header {
	package org.jletty.aci;
}
class AciLexer extends Lexer;

options {
    k=6; // needed for newline junk
    charVocabulary='\u0000'..'\u007F'; // allow ascii
    exportVocab=AciLexer;
}

LPAREN         : '(' ;
RPAREN         : ')' ;
PLUS           : '+' ;
MINUS          : '-' ;
STAR           : '*' ;
INT            : ('0'..'9')+ ;
VERSION3       : "version 3.0" ;
SEMICOLON      : ';' ;
COMMA          : ',' ;
ACL            : "acl" ;
EQUALS         : '=' ;
NEQUALS        : "!=" ;
QUOTE          : '"' ;
STRING_LITERAL :	'"' (ESC|~('"'))* '"'	;
TARGETTYPE     : "targ" (("attrfilters")|("et" ("attr" |"filter")?)) ;
LDAP           : "ldap://" ; 
DISAMBIGUATOR
               :
                      USERDNBINDRULE     {$setType(BINDRULEKEYWORD);}
                    | USERATTRBINDRULE   {$setType(BINDRULEKEYWORD);}
                    | GROUPDNBINDRULE    {$setType(BINDRULEKEYWORD);}
                    | IPBINDRULE         {$setType(BINDRULEKEYWORD);}
                    | TIMEOFDAYBINDRULE  {$setType(BINDRULEKEYWORD);}
                    | AUTHMETHODBINDRULE {$setType(BINDRULEKEYWORD);}
                    | DAYOFWEEKBINDRULE  {$setType(BINDRULEKEYWORD);}
                    | DNSBINDRULE        {$setType(BINDRULEKEYWORD);}
                    | ROLEDNBINDRULE     {$setType(BINDRULEKEYWORD);} 
                    | PERMTYPE {$setType(PERMTYPE);}
                    | RIGHTALL {$setType(RIGHTALL);}
                    | RIGHT {$setType(RIGHT);}
                    | READRIGHT {$setType(RIGHT);}  
               ;
WS    : ( ' '
        | '\r' '\n'
        | '\n'
        | '\t'
        )
        {$setType(Token.SKIP);}
      ;    

// escape sequence -- note that this is protected; it can only be called
//   from another lexer rule -- it will not ever directly return a token to
//   the parser
// There are various ambiguities hushed in this rule.  The optional
// '0'...'9' digit matches should be matched here rather than letting
// them go back to STRING_LITERAL to be matched.  ANTLR does the
// right thing by matching immediately; hence, it's ok to shut off
// the FOLLOW ambig warnings.
protected
ESC 
	:	'\\'
		(	
			'"'
		|	'\\'
		)
	;
	
// roledn missing

protected USERDNBINDRULE    : "userdn";
protected USERATTRBINDRULE  : "userattr";                            
protected GROUPDNBINDRULE   : "groupdn";
protected IPBINDRULE        : "ip";
protected TIMEOFDAYBINDRULE : "timeofday";
protected AUTHMETHODBINDRULE: "authmethod";
protected DAYOFWEEKBINDRULE : "dayofweek";
protected DNSBINDRULE       : "dns";
protected ROLEDNBINDRULE    : "roledn";

protected RIGHT          :  "write" | "add" | "delete" | ("se" ("arch" |"lfwrite")) | "compare" | "proxy"  ; 
protected READRIGHT      :  "read";
protected PERMTYPE       : "allow" 
                           | "deny"                  
                           ;
protected RIGHTALL       : "all" ;

protected BINDRULEKEYWORD : ;

protected DAYOFWEEKVALUES :   "sun"
                            | "mon"
                            | "tue"
                            | "wed"
                            | "thu"
                            | "fri"
                            | "sat"
                            ;