//distinguishedName = [name]                    ; may be empty string
//
//name       = name-component *("," name-component)
//
//name-component = attributeTypeAndValue *("+" attributeTypeAndValue)
//
//attributeTypeAndValue = attributeType "=" attributeValue
//
//attributeType = (ALPHA 1*keychar) / oid
//keychar    = ALPHA / DIGIT / "-"
//
//oid        = 1*DIGIT *("." 1*DIGIT)
//
//attributeValue = string
//
//string     = *( stringchar / pair )
//             / "#" hexstring
//             / QUOTATION *( quotechar / pair ) QUOTATION ; only from v2
//
//quotechar     = <any character except "\" or QUOTATION >

//special    = "," / "=" / "+" / "<" /  ">" / "#" / ";"

//pair       = "\" ( special / "\" / QUOTATION / hexpair )
//stringchar = <any character except one of special, "\" or QUOTATION >

//hexstring  = 1*hexpair
//hexpair    = hexchar hexchar

//hexchar    = DIGIT / "A" / "B" / "C" / "D" / "E" / "F"
//             / "a" / "b" / "c" / "d" / "e" / "f"

//ALPHA      =  <any ASCII alphabetic character>
//                                         ; (decimal 65-90 and 97-122)
//DIGIT      =  <any ASCII decimal digit>  ; (decimal 48-57)
//QUOTATION  =  <the ASCII double quotation mark character '"' decimal 34>

header {
package org.jletty.dn;

import antlr.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import org.apache.log4j.Logger;
import org.jletty.util.HexUtils;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharacterCodingException;
  
}     








class AttributeValueLexer extends Lexer;

options {
    k=2; // needed for newline junk    
    	charVocabulary='\u0001'..'\u0127'; // allow ascii
    defaultErrorHandler = false; // Don't generate parser error handlers
    importVocab=Common;
    exportVocab=AttributeValueLexer;
}

{
  	Logger logger = Logger.getLogger("org.jletty.DnParsing");	 
}
protected WS : ' ';
protected QUOTATION : '"';
protected DIGIT     : '0' .. '9';
protected ALPHA     : 'A' .. 'Z' | 'a' .. 'z';
protected HEXCHAR   : DIGIT | 'A' .. 'F' | 'a' .. 'f';
protected HEXPAIR   : HEXCHAR HEXCHAR;
protected HEXSTRING : (HEXPAIR)+;
//protected SPECIAL   : ',' | '=' | '+' | '<' | '>' | '#' | ';';
protected PAIR      : '\\' (  ',' {$setText(",");}
                            | '=' {$setText("=");} //should remain escaped for multivalued rdns {$setText("=");}
                            | '+' {$setText("+");}//should remain escaped for multivalued rdns {$setText("+");}
                            | '<' {$setText("<");}
                            | '>' {$setText(">");}
                            //| '#' {$setText("#");} 
                            | ';' {$setText(";");}
                            | '"' {$setText("\"");}
                            | '\\' {$setText("\\");}
//                            | hex:HEXPAIR { String tmp = hex.getText();
//                            	                byte[] bTmp = HexUtils.fromHexString(tmp);
//                            	                byte value = bTmp[0];
//                            	                if (
//                            	                
//                            	                String newString = new String(bTmp);
//                            	                $setText(newString);
//                            	              }
                            	);
protected UTF8SEQ   
 {
 	   java.nio.ByteBuffer bbuf = java.nio.ByteBuffer.allocate(512);
    
 }
                    : ('\\' hex:HEXPAIR{
                    	                     byte[] b = HexUtils.fromHexString(hex.getText());
                    	                     bbuf.put(b);
                    	                   }
                    	  )+ {
                         	  	Charset charset = Charset.forName("UTF-8");
                           CharsetDecoder decoder = charset.newDecoder();
                           //java.nio.CharBuffer cbuf = java.nio.CharBuffer.allocate(512);
                           try {
                           	  bbuf.flip();
                    	  	       java.nio.CharBuffer cbuf = decoder.decode(bbuf);
                      	  	     $setText(cbuf.toString());
                           } catch (CharacterCodingException e) {
                           	  throw new TokenStreamException("while trying to decode unicode escape sequence");
                           }
                    	  	   };
protected QUOTECHAR : ~( '\\' | '"');
protected STRINGCHAR: ~( ',' | '=' | '+' | '<' | '>' | '#' | ';' | '\\' | '"'); //<any character except one of special, "\" or QUOTATION >
protected ESCSP : ('\\' ' ') ;
protected BEGINNING : ( '\\' (' ' | '#'));
protected STRING    : ( ( ((BEGINNING)?(STRINGCHAR | PAIR | UTF8SEQ )*) //{String tmp = $getText; $setText(tmp.trim());} 
                                                    (ESCSP)?  
                          ) {String tmp = $getText;if (!tmp.endsWith("\\ ")) {$setText(tmp.trim());} else {$setText(tmp.replaceFirst("\\\\ $"," "));}}
                        | ('#' HEXSTRING) 
                        //| ( QUOTATION ( QUOTECHAR | PAIR )* QUOTATION) // only for LDAPv2
                      );

//ATTRIBUTEVALUE      : STRING;

ATTRIBUTEVALUE      :  STRING ;


        
