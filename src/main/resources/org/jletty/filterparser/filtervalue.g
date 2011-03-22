
//

header {
package org.jletty.filterparser;
import org.jletty.ldapstackldapops.*;
import java.util.Collections;
import org.apache.commons.lang.StringEscapeUtils;
}      
    
class FilterValueParser extends Parser;

options {
	importVocab=FilterValueLexer;
	k=3;
	defaultErrorHandler=false;
}

value returns [Object value=null;]
: (value=equals|value=presence|value=substring);

equals returns [String value=null;]
: val:ATTRVALUE RPAREN {value = val.getText();};

presence returns [String value=null;]
: STAR RPAREN {value = "*";};

substring returns [SubstringValue value=null;]
: (value=alt1|value=alt2|value=alt3);

alt1 returns [SubstringValue value=null] // =value*
: t0:ATTRVALUE STAR RPAREN {value = new SubstringValue(t0.getText(),Collections.EMPTY_LIST,null);};

alt2 returns [SubstringValue value=null] // = *value*final
{
	String tmp=null;
	java.util.List any = new java.util.ArrayList();
}
: ( STAR t0:ATTRVALUE {
                      	 if (tmp != null) {
                     		    any.add(tmp);
                     	  }	
                     	    tmp = StringEscapeUtils.unescapeJava(t0.getText().trim());
                       }
   )+                    
  (STAR   { // no final value
             any.add(tmp);
             tmp = null;
          }
   )?
   RPAREN { 
            //			String[] tmp2 = (String[])any.toArray(new String[any.size()]);
            			value = new SubstringValue(null,any,tmp);
   		       } ;

alt3 returns [SubstringValue value=null;]
{
	String initial=null;
	String tmp=null;
	java.util.List any = new java.util.ArrayList();
}
: t0:ATTRVALUE { initial = StringEscapeUtils.unescapeJava(t0.getText().trim()); }
  (STAR t1:ATTRVALUE {
  						                     if (tmp != null && !tmp.equals("")) {
                         							any.add(tmp);
                       						}
                         						tmp = StringEscapeUtils.unescapeJava(t1.getText().trim());
                       }
  )+
  RPAREN {
           			//String[] tmp2 = (String[])any.toArray(new String[any.size()]);
           			if (initial.equals("")) initial = null;
        			   if (tmp.equals("")) tmp = null;
           			value = new SubstringValue(initial,any,tmp);
         };

//substring returns [SubstringValue value=null]
//{
//	String i = null;
//	java.util.List any = new java.util.ArrayList();
//}	
//: ((initial:ATTRVALUE(STAR))? { i = initial.getText();}
//   (any:ATTRVALUE(STAR) { any.add(any.getText());})*
//   (fin:ATTRVALUE(RPAREN)) {f = fin.getText(); }) 
//	 
//	{
//		String[] tmp = (String[])any.toArray(new String[any.size()]);
//		value = new SubstringValue(initial,tmp,final);
//	};

class FilterValueLexer extends Lexer;

options {
    k=1; // needed for newline junk
    charVocabulary='\u0000'..'\u007F'; // allow ascii
}

RPAREN: ')';
STAR: '*';

protected ESC: '\\';
protected HEX: ('0'..'9')|('a'..'z')|('A'..'Z');
protected ESCAPEDCHAR : ESC a:HEX b:HEX 
                                          {
                                          	//System.out.println("ESCAPEDCHAR token " + getText());
                                          	
                                          	String s="\\u00"+a.getText()+b.getText();                                          	
                                          	//System.out.println("translated to " +s );
                                          	$setText(s);
                                          	//s = org.apache.commons.lang.StringEscapeUtils.unescapeJava(s);
                                          };

// anything besides escapes, 0x00 (NUL), LPAREN, RPAREN, ASTERISK, and ESC
protected NORMAL: UTF1SUBSET | UTFMB;

// UTF1SUBSET excludes 0x00 (NUL), LPAREN, RPAREN, ASTERISK, and ESC
// %x01-27 / %x2B-5B / %x5D-7F and we could defined it like so ==>
// protected UTF1SUBSET: '\u0001'..'\u0039' | '\u0043'..'\u0091' | '\u0093'..'\u0127';
// but we need to define it using an inverted caracter class to make 
// VALUEENCODING work without nondeterminism.
protected UTF1SUBSET: ~( '\\' | '\u0000' | '(' | ')' | '*' | '\u0128'..'\uFFFE' );


// recognizes any UTF-8 encoded Unicode character
protected UTF8: UTF1 | UTFMB;

// the mulitbyte characters
protected UTFMB: UTF2 | UTF3 | UTF4;

// %x80-BF
protected UTF0: '\u0128'..'\u0191';

// %x00-7F      
protected UTF1: '\u0000'..'\u0127';

// %xC2-DF UTF0
protected UTF2: '\u0194'..'\u0223' UTF0;

// %xE0 %xA0-BF UTF0 / %xE1-EC 2(UTF0) / xED %x80-9F UTF0 / %xEE-EF 2(UTF0)
protected UTF3:   ( '\u0224' '\u0160'..'\u0191' UTF0 ) 
				|          ( '\u0225'..'\u0236' UTF0 UTF0 ) 
				| ( '\u0237' '\u0128'..'\u0159' UTF0 ) 
				|          ( '\u0238'..'\u0239' UTF0 UTF0 );

// %xF0 %x90-BF 2(UTF0) / %xF1-F3 3(UTF0) / %xF4 %x80-8F 2(UTF0)
protected UTF4:   ( '\u0240' '\u0144'..'\u0191' UTF0 UTF0 ) 
                |          ( '\u0241'..'\u0243' UTF0 UTF0 UTF0 ) 
				| ( '\u0244' '\u0128'..'\u0143' UTF0 UTF0 );

ATTRVALUE: (NORMAL|ESCAPEDCHAR)+;