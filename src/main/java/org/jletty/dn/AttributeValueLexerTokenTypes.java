// $ANTLR 2.7.6 (2005-12-22): "attributevaluelexer.g" -> "AttributeValueLexer.java"$

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
  

public interface AttributeValueLexerTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int ATTRIBUTETYPE = 4;
	int ATTRIBUTEVALUE = 5;
	int WS = 6;
	int QUOTATION = 7;
	int DIGIT = 8;
	int ALPHA = 9;
	int HEXCHAR = 10;
	int HEXPAIR = 11;
	int HEXSTRING = 12;
	int PAIR = 13;
	int UTF8SEQ = 14;
	int QUOTECHAR = 15;
	int STRINGCHAR = 16;
	int ESCSP = 17;
	int BEGINNING = 18;
	int STRING = 19;
}
