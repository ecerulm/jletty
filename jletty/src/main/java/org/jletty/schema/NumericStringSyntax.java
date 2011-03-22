package org.jletty.schema;

import java.nio.charset.CharacterCodingException;

import org.jletty.ldapstackldapops.LdapInvalidAttributeSyntaxException;
import org.jletty.util.StringUtil;

public class NumericStringSyntax implements Syntax {

	public AttributeValue get(byte[] octetstring)
			throws LdapInvalidAttributeSyntaxException {
		try {
			String theString = StringUtil.byteArrayToString("UTF-8",
					octetstring);
			if (!theString.matches("\\d*")) {
				throw new LdapInvalidAttributeSyntaxException(
						"NumericString should contain digits only ("
								+ theString + ")");
			}
			return new NumericString(theString);
		} catch (CharacterCodingException e) {
			throw new LdapInvalidAttributeSyntaxException(e);
		}
	}

	public String getName() {
		return "1.3.6.1.4.1.1466.115.121.1.36";
	}

}
