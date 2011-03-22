// $ANTLR 2.7.6 (2005-12-22): "acilexer.g" -> "AciLexer.java"$

	package org.jletty.aci;

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

public class AciLexer extends antlr.CharScanner implements AciLexerTokenTypes, TokenStream
 {
public AciLexer(InputStream in) {
	this(new ByteBuffer(in));
}
public AciLexer(Reader in) {
	this(new CharBuffer(in));
}
public AciLexer(InputBuffer ib) {
	this(new LexerSharedInputState(ib));
}
public AciLexer(LexerSharedInputState state) {
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
				case '(':
				{
					mLPAREN(true);
					theRetToken=_returnToken;
					break;
				}
				case ')':
				{
					mRPAREN(true);
					theRetToken=_returnToken;
					break;
				}
				case '+':
				{
					mPLUS(true);
					theRetToken=_returnToken;
					break;
				}
				case '-':
				{
					mMINUS(true);
					theRetToken=_returnToken;
					break;
				}
				case '*':
				{
					mSTAR(true);
					theRetToken=_returnToken;
					break;
				}
				case '0':  case '1':  case '2':  case '3':
				case '4':  case '5':  case '6':  case '7':
				case '8':  case '9':
				{
					mINT(true);
					theRetToken=_returnToken;
					break;
				}
				case 'v':
				{
					mVERSION3(true);
					theRetToken=_returnToken;
					break;
				}
				case ';':
				{
					mSEMICOLON(true);
					theRetToken=_returnToken;
					break;
				}
				case ',':
				{
					mCOMMA(true);
					theRetToken=_returnToken;
					break;
				}
				case '=':
				{
					mEQUALS(true);
					theRetToken=_returnToken;
					break;
				}
				case '!':
				{
					mNEQUALS(true);
					theRetToken=_returnToken;
					break;
				}
				case 'l':
				{
					mLDAP(true);
					theRetToken=_returnToken;
					break;
				}
				case '\t':  case '\n':  case '\r':  case ' ':
				{
					mWS(true);
					theRetToken=_returnToken;
					break;
				}
				default:
					if ((LA(1)=='t') && (LA(2)=='a') && (LA(3)=='r')) {
						mTARGETTYPE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='a') && (LA(2)=='c')) {
						mACL(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='"') && ((LA(2) >= '\u0000' && LA(2) <= '\u007f'))) {
						mSTRING_LITERAL(true);
						theRetToken=_returnToken;
					}
					else if ((_tokenSet_0.member(LA(1))) && (_tokenSet_1.member(LA(2))) && (true)) {
						mDISAMBIGUATOR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='"') && (true)) {
						mQUOTE(true);
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

	public final void mLPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LPAREN;
		int _saveIndex;
		
		match('(');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
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
	
	public final void mPLUS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = PLUS;
		int _saveIndex;
		
		match('+');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mMINUS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = MINUS;
		int _saveIndex;
		
		match('-');
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
	
	public final void mINT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = INT;
		int _saveIndex;
		
		{
		int _cnt8=0;
		_loop8:
		do {
			if (((LA(1) >= '0' && LA(1) <= '9'))) {
				matchRange('0','9');
			}
			else {
				if ( _cnt8>=1 ) { break _loop8; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			
			_cnt8++;
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mVERSION3(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = VERSION3;
		int _saveIndex;
		
		match("version 3.0");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSEMICOLON(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SEMICOLON;
		int _saveIndex;
		
		match(';');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCOMMA(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = COMMA;
		int _saveIndex;
		
		match(',');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mACL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ACL;
		int _saveIndex;
		
		match("acl");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mEQUALS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = EQUALS;
		int _saveIndex;
		
		match('=');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mNEQUALS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = NEQUALS;
		int _saveIndex;
		
		match("!=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mQUOTE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = QUOTE;
		int _saveIndex;
		
		match('"');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSTRING_LITERAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STRING_LITERAL;
		int _saveIndex;
		
		match('"');
		{
		_loop19:
		do {
			if ((LA(1)=='\\') && (LA(2)=='"'||LA(2)=='\\') && ((LA(3) >= '\u0000' && LA(3) <= '\u007f')) && (true) && (true) && (true)) {
				mESC(false);
			}
			else if ((_tokenSet_2.member(LA(1))) && ((LA(2) >= '\u0000' && LA(2) <= '\u007f')) && (true) && (true) && (true) && (true)) {
				{
				match(_tokenSet_2);
				}
			}
			else {
				break _loop19;
			}
			
		} while (true);
		}
		match('"');
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
		{
		switch ( LA(1)) {
		case '"':
		{
			match('"');
			break;
		}
		case '\\':
		{
			match('\\');
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
	
	public final void mTARGETTYPE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = TARGETTYPE;
		int _saveIndex;
		
		match("targ");
		{
		switch ( LA(1)) {
		case 'a':
		{
			{
			match("attrfilters");
			}
			break;
		}
		case 'e':
		{
			{
			match("et");
			{
			switch ( LA(1)) {
			case 'a':
			{
				match("attr");
				break;
			}
			case 'f':
			{
				match("filter");
				break;
			}
			default:
				{
				}
			}
			}
			}
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
	
	public final void mLDAP(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LDAP;
		int _saveIndex;
		
		match("ldap://");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mDISAMBIGUATOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DISAMBIGUATOR;
		int _saveIndex;
		
		switch ( LA(1)) {
		case 'g':
		{
			mGROUPDNBINDRULE(false);
			_ttype = BINDRULEKEYWORD;
			break;
		}
		case 'i':
		{
			mIPBINDRULE(false);
			_ttype = BINDRULEKEYWORD;
			break;
		}
		case 't':
		{
			mTIMEOFDAYBINDRULE(false);
			_ttype = BINDRULEKEYWORD;
			break;
		}
		default:
			if ((LA(1)=='u') && (LA(2)=='s') && (LA(3)=='e') && (LA(4)=='r') && (LA(5)=='d')) {
				mUSERDNBINDRULE(false);
				_ttype = BINDRULEKEYWORD;
			}
			else if ((LA(1)=='u') && (LA(2)=='s') && (LA(3)=='e') && (LA(4)=='r') && (LA(5)=='a')) {
				mUSERATTRBINDRULE(false);
				_ttype = BINDRULEKEYWORD;
			}
			else if ((LA(1)=='a'||LA(1)=='d') && (LA(2)=='e'||LA(2)=='l') && (LA(3)=='l'||LA(3)=='n') && (LA(4)=='o'||LA(4)=='y')) {
				mPERMTYPE(false);
				_ttype = PERMTYPE;
			}
			else if ((LA(1)=='a') && (LA(2)=='l') && (LA(3)=='l') && (true)) {
				mRIGHTALL(false);
				_ttype = RIGHTALL;
			}
			else if ((_tokenSet_3.member(LA(1))) && (_tokenSet_4.member(LA(2))) && (_tokenSet_5.member(LA(3))) && (true)) {
				mRIGHT(false);
				_ttype = RIGHT;
			}
			else if ((LA(1)=='a') && (LA(2)=='u')) {
				mAUTHMETHODBINDRULE(false);
				_ttype = BINDRULEKEYWORD;
			}
			else if ((LA(1)=='d') && (LA(2)=='a')) {
				mDAYOFWEEKBINDRULE(false);
				_ttype = BINDRULEKEYWORD;
			}
			else if ((LA(1)=='d') && (LA(2)=='n')) {
				mDNSBINDRULE(false);
				_ttype = BINDRULEKEYWORD;
			}
			else if ((LA(1)=='r') && (LA(2)=='o')) {
				mROLEDNBINDRULE(false);
				_ttype = BINDRULEKEYWORD;
			}
			else if ((LA(1)=='r') && (LA(2)=='e')) {
				mREADRIGHT(false);
				_ttype = RIGHT;
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
	
	protected final void mUSERDNBINDRULE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = USERDNBINDRULE;
		int _saveIndex;
		
		match("userdn");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mUSERATTRBINDRULE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = USERATTRBINDRULE;
		int _saveIndex;
		
		match("userattr");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mGROUPDNBINDRULE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = GROUPDNBINDRULE;
		int _saveIndex;
		
		match("groupdn");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mIPBINDRULE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = IPBINDRULE;
		int _saveIndex;
		
		match("ip");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mTIMEOFDAYBINDRULE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = TIMEOFDAYBINDRULE;
		int _saveIndex;
		
		match("timeofday");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mAUTHMETHODBINDRULE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = AUTHMETHODBINDRULE;
		int _saveIndex;
		
		match("authmethod");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mDAYOFWEEKBINDRULE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DAYOFWEEKBINDRULE;
		int _saveIndex;
		
		match("dayofweek");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mDNSBINDRULE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DNSBINDRULE;
		int _saveIndex;
		
		match("dns");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mROLEDNBINDRULE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ROLEDNBINDRULE;
		int _saveIndex;
		
		match("roledn");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mPERMTYPE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = PERMTYPE;
		int _saveIndex;
		
		switch ( LA(1)) {
		case 'a':
		{
			match("allow");
			break;
		}
		case 'd':
		{
			match("deny");
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
	
	protected final void mRIGHTALL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RIGHTALL;
		int _saveIndex;
		
		match("all");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mRIGHT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RIGHT;
		int _saveIndex;
		
		switch ( LA(1)) {
		case 'w':
		{
			match("write");
			break;
		}
		case 'a':
		{
			match("add");
			break;
		}
		case 'd':
		{
			match("delete");
			break;
		}
		case 's':
		{
			{
			match("se");
			{
			switch ( LA(1)) {
			case 'a':
			{
				match("arch");
				break;
			}
			case 'l':
			{
				match("lfwrite");
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			}
			break;
		}
		case 'c':
		{
			match("compare");
			break;
		}
		case 'p':
		{
			match("proxy");
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
	
	protected final void mREADRIGHT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = READRIGHT;
		int _saveIndex;
		
		match("read");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mWS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = WS;
		int _saveIndex;
		
		{
		switch ( LA(1)) {
		case ' ':
		{
			match(' ');
			break;
		}
		case '\r':
		{
			match('\r');
			match('\n');
			break;
		}
		case '\n':
		{
			match('\n');
			break;
		}
		case '\t':
		{
			match('\t');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		_ttype = Token.SKIP;
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mBINDRULEKEYWORD(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BINDRULEKEYWORD;
		int _saveIndex;
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mDAYOFWEEKVALUES(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DAYOFWEEKVALUES;
		int _saveIndex;
		
		switch ( LA(1)) {
		case 'm':
		{
			match("mon");
			break;
		}
		case 'w':
		{
			match("wed");
			break;
		}
		case 'f':
		{
			match("fri");
			break;
		}
		default:
			if ((LA(1)=='s') && (LA(2)=='u')) {
				match("sun");
			}
			else if ((LA(1)=='t') && (LA(2)=='u')) {
				match("tue");
			}
			else if ((LA(1)=='t') && (LA(2)=='h')) {
				match("thu");
			}
			else if ((LA(1)=='s') && (LA(2)=='a')) {
				match("sat");
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
	
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 0L, 53201631046533120L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 0L, 12897486142177280L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { -17179869185L, -1L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 0L, 38562183478509568L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 0L, 1266843553628160L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 0L, 195790379155456L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	
	}
