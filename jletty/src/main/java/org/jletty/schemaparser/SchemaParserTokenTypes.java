// $ANTLR 2.7.6 (2005-12-22): "schema.g" -> "SchemaParser.java"$

package org.jletty.schemaparser;

import antlr.*;
import org.jletty.schema.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import org.apache.log4j.Logger;
import java.io.EOFException;
  

public interface SchemaParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int ATTRTYPEDESC = 4;
	int LPAREN = 5;
	int NUMERICOID = 6;
	int NAME = 7;
	int QDSTRING = 8;
	int RPAREN = 9;
	int DESC = 10;
	int OBSOLETE = 11;
	int SUP = 12;
	int STRING = 13;
	int EQUALITY = 14;
	int ORDERING = 15;
	int SUBSTR = 16;
	int SYNTAX = 17;
	int LEN = 18;
	int SINGLEVALUE = 19;
	int NOUSERMODIFICATION = 20;
	int COLLECTIVE = 21;
	int USAGE = 22;
	int OBJECTCLASS = 23;
	int ABSTRACT = 24;
	int STRUCTURAL = 25;
	int AUXILIARY = 26;
	int MUST = 27;
	int MAY = 28;
	int DOLLAR = 29;
	int COMMENT = 30;
	int WS = 31;
	int QUOTE = 32;
	int DIGIT = 33;
	int LDIGIT = 34;
	int ALPHA = 35;
}
