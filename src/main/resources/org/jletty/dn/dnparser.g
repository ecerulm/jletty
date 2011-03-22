header {
package org.jletty.dn;

import antlr.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import org.apache.log4j.Logger;
import javax.naming.Name;
import javax.naming.InvalidNameException;
import org.jletty.jndiprovider.*;
  
}    

class AntlrDnParser extends Parser;

options
{
	k=2;
    importVocab=Common;
    defaultErrorHandler=false;
}

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
}

distinguishedname returns [DistinguishedName n = new DistinguishedName();] throws InvalidNameException 
{
	   DnNameComponent nc=null;
} 
   : nc=namecomponent (WS)* {n.add(0,nc);}
     (COMMA nc=namecomponent{n.add(0,nc);})*;


namecomponent returns [DnNameComponent comp = new DnNameComponent();] throws InvalidNameException
{
	 DnAtom attrTaV = null;
} 
   :  ( attrTaV=attributeTypeAndValue {comp.add(attrTaV);}
        (
         (PLUS attrTaV=attributeTypeAndValue {comp.add(attrTaV);})*
        )
      ) ;



attributeTypeAndValue returns [DnAtom atom = null;] throws InvalidNameException
{
	  selector.select("attributetypelexer");
	  String attributeType = "";
	  String attributeValue = "";
}
   :  ((attrType:ATTRIBUTETYPE) {attributeType = attrType.getText().trim(); selector.select("dnlexer"); }
      EQUALS {selector.select("attributevaluelexer");}
      (attrValue:ATTRIBUTEVALUE) { attributeValue = attrValue.getText();selector.select("dnlexer");})
        {
        	   atom = new DnAtom(attributeType,attributeValue);
        }
      ;
   
   


class DnLexer extends Lexer;

options {
	k=1;
		charVocabulary='\u0001'..'\u0127'; // allow ascii
defaultErrorHandler = false; // Don't generate parser error handlers
}

{
  	Logger logger = Logger.getLogger("org.jletty.DnParsing");	 
}

WS    : ' ' {$setType(Token.SKIP);};
COMMA : ',';
	PLUS  : '+';
	EQUALS : '=';   