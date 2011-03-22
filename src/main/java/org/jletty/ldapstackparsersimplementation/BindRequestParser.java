/*
 * $Id: BindRequestParser.java,v 1.5 2006/02/27 18:24:46 ecerulm Exp $
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

import org.jletty.ldapstackldapops.AuthenticationChoiceListener;
import org.jletty.ldapstackldapops.BERIntegerListener;
import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.BindRequest;
import org.jletty.ldapstackldapops.LDAPStringListener;
import org.jletty.ldapstackldapops.RequestListener;

/**
 * A concrete TLVParser for parsing <code>BindRequest</code>s.
 * BindRequestParser provides a way to parse a stream of bytes to an actual
 * BindRequest object.
 * 
 * @author $Author: ecerulm $
 */
public final class BindRequestParser extends TLVParser {

	private byte[] _passwd;

	private int _version;

	private String _ldapdn;

	private RequestListener _listener;

	private BERIntegerListener versionListener = new BERIntegerListener() {
		public void data(int value) {
			BindRequestParser.this._version = value;
		}
	};

	private Parser versionParser = new BERIntegerParser(this.versionListener);

	private Parser ldapdnParser = new LDAPStringParser(
			new LDAPStringListener() {
				public void data(String value) {
					BindRequestParser.this._ldapdn = value;
				}
			});

	private Parser acParser = new AuthenticationChoiceParser(
			new AuthenticationChoiceListener() {

				public void data(byte[] value) {

					BindRequestParser.this._passwd = value;

				}
			});

	private Parser parser = this.versionParser;

	/**
	 * Class constructor.
	 * 
	 * @param listener
	 *            the listener that will receive the parsed BindRequest
	 */
	public BindRequestParser(RequestListener listener) {
		this._listener = listener;
	}

	protected boolean parseContents(ByteBuffer buffer) {
		while (true) {
			boolean completed = this.parser.parse(buffer);
			if (!completed) {
				return false;
			}

			if (this.parser.equals(this.versionParser)) {
				this.parser = this.ldapdnParser;
			} else if (this.parser.equals(this.ldapdnParser)) {
				this.parser = this.acParser;
			} else {
				return true; // acParser and completed
			}

		}

	}

	/**
	 * @see org.jletty.ldapstackparsers.implementation.TLVParser#notifyListeners()
	 */
	protected void notifyListeners() {
		try {
			BindRequest result = new BindRequest(this._version, this._ldapdn, this._passwd);
			this._listener.data(result);

		} catch (IllegalArgumentException e) {
			// invalid possible invalid version
			throw new ParserException(
					"Not possible to create a Bindrequest with parameters("
							+ this._version + "," + this._ldapdn + "," + this._passwd + ")");
		}

	}

	protected boolean checkExpectedTag(byte tag) {
		// [APPLICATION 0] BindRequest
		return BerTags.APPLICATION_0 == tag;
	}

	protected void resetInternal() {
		// reset variables
		this._passwd = null;
		this._version = 0;
		this._ldapdn = null;

		// reset parsers
		this.parser.reset();
		this.parser = this.versionParser;
	}
}