/*
 * Created on 25-sep-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.AbandonRequest;
import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.RequestListener;

public class AbandonRequestParser extends BERIntegerParser implements Parser {

	private RequestListener listener;

	public AbandonRequestParser(RequestListener listener) {
		super();
		this.listener = listener;
	}

	protected boolean checkExpectedTag(byte tag) {

		return (BerTags.APPLICATION_16 == tag);
	}

	protected void notifyListeners() {
		this.listener.data(new AbandonRequest(this.m_value));
	}
}