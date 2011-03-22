header {
package org.jletty.aci;

import antlr.*;
import java.util.List;
import java.util.ArrayList;
  
} 

class AntlrAciParser extends Parser;

options
{	
	k=2;	
	defaultErrorHandler=true;
	importVocab=AciLexer;
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


aci returns [Aci aci = new Aci();] 
{
	Target t;
	Permission p;
	BindRule b;
	List perms = new ArrayList();
}
	: 
	  (t=target 
		  {
		  	System.out.println("target parsed");
		  	aci.addTarget(t);
		  }
	  )+
	  LPAREN VERSION3 SEMICOLON
	  {
	  	System.out.println("version parsed");
	  	aci.setVersion(TargetVersion.VERSION3_0);
	  }
	  ACL name:STRING_LITERAL SEMICOLON
	  {
	  	System.out.println("acl parsed");
      	String nameString = name.getText();
      	nameString = nameString.substring(1,nameString.length()-1);
	  	aci.setName(nameString);
	  }
	  (((p=permission b=bindrule SEMICOLON)
	    {
	    	p.setBindRule(b);
	    	perms.add(p);
	    }
	    )
	   +)
	   {
	    	//list to array
		    // Create an array containing the elements in a list
    	    Permission[] permissionsArray = (Permission[])perms.toArray(new Permission[perms.size()]);
        	aci.setPermissions(permissionsArray);    
	   }
	;

target returns [Target target = new Target();]
{
	TargetType tt;
}
	:
      LPAREN 
      (targettype:TARGETTYPE) 
      {          	
      	target.setType(TargetType.getEnum(targettype.getText()));
      }
      ( EQUALS 
        {
        	target.setEquals(TargetEqualsType.EQUALS);  
        }
        | NEQUALS
        {
        	target.setEquals(TargetEqualsType.NEQUALS);
        }
      ) 
      
      targetliteral:STRING_LITERAL
      {
      	String literal = targetliteral.getText();
      	literal = literal.substring(1,literal.length()-1);
        target.setTarget(literal);
      } 
      
      RPAREN 
	  ;
	
permission returns [Permission p = new Permission();]
	:
 	  pt:PERMTYPE {p.setType(PermissionType.getEnum(pt.getText()));}
 	  LPAREN 
 	  (( r:RIGHT {p.addRight(PermissionRight.getEnum(r.getText()));} 
 	    (COMMA r2:RIGHT {p.addRight(PermissionRight.getEnum(r2.getText()));} )*
 	   )
 	  | r3:RIGHTALL {p.addRight(PermissionRight.getEnum(r3.getText()));}
 	  )
 	  RPAREN 
	  {
	  	System.out.println("permission parsed");
	  }
	;	
	
bindrule returns [BindRule b = new BindRule();]
    : 
	  brk:BINDRULEKEYWORD 
	  {
	  	System.out.println("bindrule keyword parsed");
	  	b.setType(BindRuleType.getEnum(brk.getText()));
	  }
	  ( EQUALS 
		{
		  	b.setEquals(BindRuleEqualsType.EQUALS);
		}
	    | NEQUALS
		{
		  	b.setEquals(BindRuleEqualsType.NEQUALS);
		}
	  ) 
	  {
	  	System.out.println("equals parsed");
	  }
	  l:STRING_LITERAL 
	  {
	    String literal = l.getText();
	    String expression = literal.substring(1,literal.length()-1);
	    b.setExpression(expression);
	  }
    ;
