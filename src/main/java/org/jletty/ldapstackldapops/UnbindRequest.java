/*
 * $Header: /usr/local/cvsroot/jletty-new/src/org/jletty/ldapstackldapops/UnbindRequest.java,v 1.1 2006/02/12 19:22:12 ecerulm Exp $
 * Created on 08-may-2004
 *
 */
package org.jletty.ldapstackldapops;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jletty.util.Style;

/**
 * @author $Author: ecerulm $
 */
public final class UnbindRequest implements LDAPRequest {
	public static final UnbindRequest UNBIND_REQUEST = new UnbindRequest();

	private UnbindRequest() {
	}

	public byte[] getBytes() {
		// FIXME Not implemented
		// not needed in the server but we must include for completeness
		throw new NotImplementedException(UnbindRequest.class);
	}

	public String toString() {
		return new ToStringBuilder(this, Style.getDefaultStyle()).toString();
	}

	public byte getTag() {
		return BerTags.APPLICATION_2;
	}

}