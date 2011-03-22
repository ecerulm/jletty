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
  

import java.io.InputStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.ANTLRException;
import java.io.Reader;
import java.util.Hashtable;
import antlr.CharScanner;
import antlr.InputBuffer;
import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.Token;
import antlr.CommonToken;
import antlr.RecognitionException;
import antlr.NoViableAltForCharException;
import antlr.MismatchedCharException;
import antlr.TokenStream;
import antlr.ANTLRHashString;
import antlr.LexerSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.SemanticException;

public class AttributeValueLexer extends antlr.CharScanner implements AttributeValueLexerTokenTypes, TokenStream
 {

  	Logger logger = Logger.getLogger("org.jletty.DnParsing");	 
public AttributeValueLexer(InputStream in) {
	this(new ByteBuffer(in));
}
public AttributeValueLexer(Reader in) {
	this(new CharBuffer(in));
}
public AttributeValueLexer(InputBuffer ib) {
	this(new LexerSharedInputState(ib));
}
public AttributeValueLexer(LexerSharedInputState state) {
	super(state);
	caseSensitiveLiterals = true;
	setCaseSensitive(true);
	literals = new Hashtable();
}

public Token nextToken() throws TokenStreamException {
	Token theRetToken=null;
tryAgain:
	for (;;) {
		Token _token = null;
		int _ttype = Token.INVALID_TYPE;
		resetText();
		try {   // for char stream error handling
			try {   // for lexical error handling
				{
					mATTRIBUTEVALUE(true);
					theRetToken=_returnToken;
				}
				
				if ( _returnToken==null ) continue tryAgain; // found SKIP token
				_ttype = _returnToken.getType();
				_ttype = testLiteralsTable(_ttype);
				_returnToken.setType(_ttype);
				return _returnToken;
			}
			catch (RecognitionException e) {
				throw new TokenStreamRecognitionException(e);
			}
		}
		catch (CharStreamException cse) {
			if ( cse instanceof CharStreamIOException ) {
				throw new TokenStreamIOException(((CharStreamIOException)cse).io);
			}
			else {
				throw new TokenStreamException(cse.getMessage());
			}
		}
	}
}

	protected final void mWS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = WS;
		int _saveIndex;
		
		match(' ');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mQUOTATION(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = QUOTATION;
		int _saveIndex;
		
		match('"');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mDIGIT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DIGIT;
		int _saveIndex;
		
		matchRange('0','9');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mALPHA(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ALPHA;
		int _saveIndex;
		
		switch ( LA(1)) {
		case 'A':  case 'B':  case 'C':  case 'D':
		case 'E':  case 'F':  case 'G':  case 'H':
		case 'I':  case 'J':  case 'K':  case 'L':
		case 'M':  case 'N':  case 'O':  case 'P':
		case 'Q':  case 'R':  case 'S':  case 'T':
		case 'U':  case 'V':  case 'W':  case 'X':
		case 'Y':  case 'Z':
		{
			matchRange('A','Z');
			break;
		}
		case 'a':  case 'b':  case 'c':  case 'd':
		case 'e':  case 'f':  case 'g':  case 'h':
		case 'i':  case 'j':  case 'k':  case 'l':
		case 'm':  case 'n':  case 'o':  case 'p':
		case 'q':  case 'r':  case 's':  case 't':
		case 'u':  case 'v':  case 'w':  case 'x':
		case 'y':  case 'z':
		{
			matchRange('a','z');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mHEXCHAR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = HEXCHAR;
		int _saveIndex;
		
		switch ( LA(1)) {
		case '0':  case '1':  case '2':  case '3':
		case '4':  case '5':  case '6':  case '7':
		case '8':  case '9':
		{
			mDIGIT(false);
			break;
		}
		case 'A':  case 'B':  case 'C':  case 'D':
		case 'E':  case 'F':
		{
			matchRange('A','F');
			break;
		}
		case 'a':  case 'b':  case 'c':  case 'd':
		case 'e':  case 'f':
		{
			matchRange('a','f');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mHEXPAIR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = HEXPAIR;
		int _saveIndex;
		
		mHEXCHAR(false);
		mHEXCHAR(false);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mHEXSTRING(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = HEXSTRING;
		int _saveIndex;
		
		{
		int _cnt9=0;
		_loop9:
		do {
			if ((_tokenSet_0.member(LA(1)))) {
				mHEXPAIR(false);
			}
			else {
				if ( _cnt9>=1 ) { break _loop9; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			
			_cnt9++;
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mPAIR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = PAIR;
		int _saveIndex;
		
		match('\\');
		{
		switch ( LA(1)) {
		case ',':
		{
			match(',');
			text.setLength(_begin); text.append(",");
			break;
		}
		case '=':
		{
			match('=');
			text.setLength(_begin); text.append("=");
			break;
		}
		case '+':
		{
			match('+');
			text.setLength(_begin); text.append("+");
			break;
		}
		case '<':
		{
			match('<');
			text.setLength(_begin); text.append("<");
			break;
		}
		case '>':
		{
			match('>');
			text.setLength(_begin); text.append(">");
			break;
		}
		case ';':
		{
			match(';');
			text.setLength(_begin); text.append(";");
			break;
		}
		case '"':
		{
			match('"');
			text.setLength(_begin); text.append("\"");
			break;
		}
		case '\\':
		{
			match('\\');
			text.setLength(_begin); text.append("\\");
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mUTF8SEQ(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = UTF8SEQ;
		int _saveIndex;
		Token hex=null;
		
			   java.nio.ByteBuffer bbuf = java.nio.ByteBuffer.allocate(512);
		
		
		
		{
		int _cnt14=0;
		_loop14:
		do {
			if ((LA(1)=='\\') && (_tokenSet_0.member(LA(2)))) {
				match('\\');
				mHEXPAIR(true);
				hex=_returnToken;
				
					                     byte[] b = HexUtils.fromHexString(hex.getText());
					                     bbuf.put(b);
					
			}
			else {
				if ( _cnt14>=1 ) { break _loop14; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			
			_cnt14++;
		} while (true);
		}
		
			  	Charset charset = Charset.forName("UTF-8");
		CharsetDecoder decoder = charset.newDecoder();
		//java.nio.CharBuffer cbuf = java.nio.CharBuffer.allocate(512);
		try {
			  bbuf.flip();
			  	       java.nio.CharBuffer cbuf = decoder.decode(bbuf);
			  	     text.setLength(_begin); text.append(cbuf.toString());
		} catch (CharacterCodingException e) {
			  throw new TokenStreamException("while trying to decode unicode escape sequence");
		}
			  	
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mQUOTECHAR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = QUOTECHAR;
		int _saveIndex;
		
		{
		match(_tokenSet_1);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mSTRINGCHAR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STRINGCHAR;
		int _saveIndex;
		
		{
		match(_tokenSet_2);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mESCSP(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ESCSP;
		int _saveIndex;
		
		{
		match('\\');
		match(' ');
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mBEGINNING(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BEGINNING;
		int _saveIndex;
		
		{
		match('\\');
		{
		switch ( LA(1)) {
		case ' ':
		{
			match(' ');
			break;
		}
		case '#':
		{
			match('#');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mSTRING(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STRING;
		int _saveIndex;
		
		{
		if ((LA(1)=='#')) {
			{
			match('#');
			mHEXSTRING(false);
			}
		}
		else {
			{
			{
			{
			if ((LA(1)=='\\') && (LA(2)==' '||LA(2)=='#')) {
				mBEGINNING(false);
			}
			else {
			}
			
			}
			{
			_loop30:
			do {
				if ((LA(1)=='\\') && (_tokenSet_3.member(LA(2)))) {
					mPAIR(false);
				}
				else if ((LA(1)=='\\') && (_tokenSet_0.member(LA(2)))) {
					mUTF8SEQ(false);
				}
				else if ((_tokenSet_2.member(LA(1)))) {
					mSTRINGCHAR(false);
				}
				else {
					break _loop30;
				}
				
			} while (true);
			}
			}
			{
			if ((LA(1)=='\\')) {
				mESCSP(false);
			}
			else {
			}
			
			}
			}
			String tmp = new String(text.getBuffer(),_begin,text.length()-_begin);if (!tmp.endsWith("\\ ")) {text.setLength(_begin); text.append(tmp.trim());} else {text.setLength(_begin); text.append(tmp.replaceFirst("\\\\ $"," "));}
		}
		
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mATTRIBUTEVALUE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ATTRIBUTEVALUE;
		int _saveIndex;
		
		mSTRING(false);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	
	private static final long[] mk_tokenSet_0() {
		long[] data = new long[9];
		data[0]=287948901175001088L;
		data[1]=541165879422L;
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = new long[16];
		data[0]=-17179869186L;
		data[1]=-268435457L;
		for (int i = 2; i<=3; i++) { data[i]=-1L; }
		data[4]=1099511627775L;
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = new long[16];
		data[0]=-8646937724370026498L;
		data[1]=-268435457L;
		for (int i = 2; i<=3; i++) { data[i]=-1L; }
		data[4]=1099511627775L;
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = new long[9];
		data[0]=8646937690010288128L;
		data[1]=268435456L;
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	
	}
