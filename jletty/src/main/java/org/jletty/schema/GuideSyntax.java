package org.jletty.schema;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.CharacterCodingException;

import org.jletty.ldapstackldapops.LdapInvalidAttributeSyntaxException;
import org.jletty.util.StringUtil;

import antlr.RecognitionException;
import antlr.TokenStreamException;

//guide-value = [ object-class "#" ] criteria
//
//object-class = woid
//
//criteria = criteria-item / criteria-set / ( "!" criteria )
//
//criteria-set = ( [ "(" ] criteria "&" criteria-set [ ")" ] ) /
//
//               ( [ "(" ] criteria "|" criteria-set [ ")" ] )
//
//criteria-item = [ "(" ] attributetype "$" match-type [ ")" ]
//
//match-type = "EQ" / "SUBSTR" / "GE" / "LE" / "APPROX"
//a = %x41-5A / %x61-7A
//d = %x30-39 
//hex-digit =  d / "a" / "b" / "c" / "d" / "e" / "f"
//k = a / d / "-" / ";"
//p = a / d / """ / "(" / ")" / "+" / "," / "-" / "." /
//    "/" / ":" / "?" / " "
//letterstring    = 1*a
//numericstring   = 1*d
//anhstring       = 1*k
//keystring       = a [ anhstring ]
//printablestring = 1*p
//space           = 1*" "
//whsp            = [ space ]
//utf8            = <any sequence of octets formed from the UTF-8 [9]
//                   transformation of a character from ISO10646 [10]>
//dstring         = 1*utf8
//qdstring        = whsp "'" dstring "'" whsp
//qdstringlist    = [ qdstring *( qdstring ) ]
//qdstrings       = qdstring / ( whsp "(" qdstringlist ")" whsp )
public class GuideSyntax implements Syntax {
	// TODO add unit test for this syntax
	public AttributeValue get(byte[] octetstring)
			throws LdapInvalidAttributeSyntaxException {
		try {
			String tmp = StringUtil.byteArrayToString("UTF-8", octetstring);

			final StringReader stringReader = new StringReader(tmp);
			new GuideSyntaxParser(new GuideSyntaxLexer(stringReader))
					.guidesyntax();
			if (stringReader.read() != -1) {
				throw new LdapInvalidAttributeSyntaxException(
						"Invalid syntax for guide");
			}
			return new GuideValue(tmp);
		} catch (RecognitionException e) {
			throw new LdapInvalidAttributeSyntaxException(e);
		} catch (TokenStreamException e) {
			throw new LdapInvalidAttributeSyntaxException(e);
		} catch (CharacterCodingException e) {
			throw new LdapInvalidAttributeSyntaxException(e);
		} catch (IOException e) {
			throw new LdapInvalidAttributeSyntaxException(e);
		}
	}

	public String getName() {
		return "1.3.6.1.4.1.1466.115.121.1.25";
	}

}
