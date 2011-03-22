package org.jletty.schema;

import java.nio.charset.CharacterCodingException;

import org.jletty.ldapstackldapops.LdapInvalidAttributeSyntaxException;
import org.jletty.util.StringUtil;

public class TelexNumberSyntax implements Syntax {

	public AttributeValue get(byte[] octetstring)
			throws LdapInvalidAttributeSyntaxException {
		try {
			String theString = StringUtil.byteArrayToString("UTF-8",
					octetstring);
			// TODO add syntax check
			return new TelexNumber(theString);
		} catch (CharacterCodingException e) {
			throw new LdapInvalidAttributeSyntaxException(e);
		}
	}

	public String getName() {
		return "1.3.6.1.4.1.1466.115.121.1.52";
	}

}
