// $ANTLR 2.7.6 (2005-12-22): "filtervalue.g" -> "FilterValueLexer.java"$

package org.jletty.filterparser;
import org.jletty.ldapstackldapops.*;
import java.util.Collections;
import org.apache.commons.lang.StringEscapeUtils;

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

public class FilterValueLexer extends antlr.CharScanner implements FilterValueParserTokenTypes, TokenStream
 {
public FilterValueLexer(InputStream in) {
	this(new ByteBuffer(in));
}
public FilterValueLexer(Reader in) {
	this(new CharBuffer(in));
}
public FilterValueLexer(InputBuffer ib) {
	this(new LexerSharedInputState(ib));
}
public FilterValueLexer(LexerSharedInputState state) {
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
				switch ( LA(1)) {
				case ')':
				{
					mRPAREN(true);
					theRetToken=_returnToken;
					break;
				}
				case '*':
				{
					mSTAR(true);
					theRetToken=_returnToken;
					break;
				}
				default:
					if ((_tokenSet_0.member(LA(1)))) {
						mATTRVALUE(true);
						theRetToken=_returnToken;
					}
				else {
					if (LA(1)==EOF_CHAR) {uponEOF(); _returnToken = makeToken(Token.EOF_TYPE);}
				else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
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

	public final void mRPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RPAREN;
		int _saveIndex;
		
		match(')');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSTAR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STAR;
		int _saveIndex;
		
		match('*');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mESC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ESC;
		int _saveIndex;
		
		match('\\');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mHEX(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = HEX;
		int _saveIndex;
		
		switch ( LA(1)) {
		case '0':  case '1':  case '2':  case '3':
		case '4':  case '5':  case '6':  case '7':
		case '8':  case '9':
		{
			{
			matchRange('0','9');
			}
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
			{
			matchRange('a','z');
			}
			break;
		}
		case 'A':  case 'B':  case 'C':  case 'D':
		case 'E':  case 'F':  case 'G':  case 'H':
		case 'I':  case 'J':  case 'K':  case 'L':
		case 'M':  case 'N':  case 'O':  case 'P':
		case 'Q':  case 'R':  case 'S':  case 'T':
		case 'U':  case 'V':  case 'W':  case 'X':
		case 'Y':  case 'Z':
		{
			{
			matchRange('A','Z');
			}
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
	
	protected final void mESCAPEDCHAR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ESCAPEDCHAR;
		int _saveIndex;
		Token a=null;
		Token b=null;
		
		mESC(false);
		mHEX(true);
		a=_returnToken;
		mHEX(true);
		b=_returnToken;
		
			//System.out.println("ESCAPEDCHAR token " + getText());
			
			String s="\\u00"+a.getText()+b.getText();                                          	
			//System.out.println("translated to " +s );
			text.setLength(_begin); text.append(s);
			//s = org.apache.commons.lang.StringEscapeUtils.unescapeJava(s);
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mNORMAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = NORMAL;
		int _saveIndex;
		
		if ((_tokenSet_1.member(LA(1)))) {
			mUTF1SUBSET(false);
		}
		else if ((_tokenSet_2.member(LA(1)))) {
			mUTFMB(false);
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mUTF1SUBSET(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = UTF1SUBSET;
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
	
	protected final void mUTFMB(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = UTFMB;
		int _saveIndex;
		
		switch ( LA(1)) {
		case '\u0224':  case '\u0225':  case '\u0226':  case '\u0227':
		case '\u0228':  case '\u0229':  case '\u022a':  case '\u022b':
		case '\u022c':  case '\u022d':  case '\u022e':  case '\u022f':
		case '\u0230':  case '\u0231':  case '\u0232':  case '\u0233':
		case '\u0234':  case '\u0235':  case '\u0236':  case '\u0237':
		case '\u0238':  case '\u0239':
		{
			mUTF3(false);
			break;
		}
		case '\u0240':  case '\u0241':  case '\u0242':  case '\u0243':
		case '\u0244':
		{
			mUTF4(false);
			break;
		}
		default:
			if (((LA(1) >= '\u0194' && LA(1) <= '\u0223'))) {
				mUTF2(false);
			}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mUTF8(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = UTF8;
		int _saveIndex;
		
		if (((LA(1) >= '\u0000' && LA(1) <= '\u0127'))) {
			mUTF1(false);
		}
		else if ((_tokenSet_2.member(LA(1)))) {
			mUTFMB(false);
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mUTF1(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = UTF1;
		int _saveIndex;
		
		matchRange('\u0000','\u0127');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mUTF2(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = UTF2;
		int _saveIndex;
		
		matchRange('\u0194','\u0223');
		mUTF0(false);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mUTF3(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = UTF3;
		int _saveIndex;
		
		switch ( LA(1)) {
		case '\u0224':
		{
			{
			match('\u0224');
			matchRange('\u0160','\u0191');
			mUTF0(false);
			}
			break;
		}
		case '\u0225':  case '\u0226':  case '\u0227':  case '\u0228':
		case '\u0229':  case '\u022a':  case '\u022b':  case '\u022c':
		case '\u022d':  case '\u022e':  case '\u022f':  case '\u0230':
		case '\u0231':  case '\u0232':  case '\u0233':  case '\u0234':
		case '\u0235':  case '\u0236':
		{
			{
			matchRange('\u0225','\u0236');
			mUTF0(false);
			mUTF0(false);
			}
			break;
		}
		case '\u0237':
		{
			{
			match('\u0237');
			matchRange('\u0128','\u0159');
			mUTF0(false);
			}
			break;
		}
		case '\u0238':  case '\u0239':
		{
			{
			matchRange('\u0238','\u0239');
			mUTF0(false);
			mUTF0(false);
			}
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
	
	protected final void mUTF4(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = UTF4;
		int _saveIndex;
		
		switch ( LA(1)) {
		case '\u0240':
		{
			{
			match('\u0240');
			matchRange('\u0144','\u0191');
			mUTF0(false);
			mUTF0(false);
			}
			break;
		}
		case '\u0241':  case '\u0242':  case '\u0243':
		{
			{
			matchRange('\u0241','\u0243');
			mUTF0(false);
			mUTF0(false);
			mUTF0(false);
			}
			break;
		}
		case '\u0244':
		{
			{
			match('\u0244');
			matchRange('\u0128','\u0143');
			mUTF0(false);
			mUTF0(false);
			}
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
	
	protected final void mUTF0(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = UTF0;
		int _saveIndex;
		
		matchRange('\u0128','\u0191');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mATTRVALUE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ATTRVALUE;
		int _saveIndex;
		
		{
		int _cnt42=0;
		_loop42:
		do {
			if ((_tokenSet_3.member(LA(1)))) {
				mNORMAL(false);
			}
			else if ((LA(1)=='\\')) {
				mESCAPEDCHAR(false);
			}
			else {
				if ( _cnt42>=1 ) { break _loop42; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			
			_cnt42++;
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	
	private static final long[] mk_tokenSet_0() {
		long[] data = new long[2560];
		data[0]=-7696581394434L;
		for (int i = 1; i<=3; i++) { data[i]=-1L; }
		data[4]=1099511627775L;
		data[6]=-1048576L;
		data[7]=-1L;
		data[8]=288230376151711743L;
		data[9]=31L;
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = new long[2560];
		data[0]=-7696581394434L;
		data[1]=-268435457L;
		for (int i = 2; i<=3; i++) { data[i]=-1L; }
		data[4]=1099511627775L;
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = new long[1281];
		data[6]=-1048576L;
		data[7]=-1L;
		data[8]=288230376151711743L;
		data[9]=31L;
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = new long[2560];
		data[0]=-7696581394434L;
		data[1]=-268435457L;
		for (int i = 2; i<=3; i++) { data[i]=-1L; }
		data[4]=1099511627775L;
		data[6]=-1048576L;
		data[7]=-1L;
		data[8]=288230376151711743L;
		data[9]=31L;
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	
	}
