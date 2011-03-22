// $ANTLR 2.7.6 (2005-12-22): "filtervalue.g" -> "FilterValueLexer.java"$

package org.jletty.filterparser;
import org.jletty.ldapstackldapops.*;
import java.util.Collections;
import org.apache.commons.lang.StringEscapeUtils;

public interface FilterValueParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int ATTRVALUE = 4;
	int RPAREN = 5;
	int STAR = 6;
	int ESC = 7;
	int HEX = 8;
	int ESCAPEDCHAR = 9;
	int NORMAL = 10;
	int UTF1SUBSET = 11;
	int UTF8 = 12;
	int UTFMB = 13;
	int UTF0 = 14;
	int UTF1 = 15;
	int UTF2 = 16;
	int UTF3 = 17;
	int UTF4 = 18;
}
