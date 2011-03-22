package org.jletty.schema;

import java.nio.charset.CharacterCodingException;

import org.jletty.ldapstackldapops.LdapInvalidAttributeSyntaxException;
import org.jletty.util.StringUtil;

public class DeliveryMethodSyntax implements Syntax {

	public AttributeValue get(byte[] octetstring)
			throws LdapInvalidAttributeSyntaxException {
		try {
			String theString = StringUtil.byteArrayToString("UTF-8",
					octetstring);
			if (!theString
					.matches("(any|mhs|physical|telex|teletex|g3fax|g4fax|ia5|videotex|telephone)")
					&& !theString
							.matches("(any|mhs|physical|telex|teletex|g3fax|g4fax|ia5|videotex|telephone)((\\s)*\\$(\\s)*(any|mhs|physical|telex|teletex|g3fax|g4fax|ia5|videotex|telephone))+")) {
				throw new LdapInvalidAttributeSyntaxException(
						"Invalid DeliveryMethodSyntax");
			}
			return new DeliveryMethod(theString);
		} catch (CharacterCodingException e) {
			throw new LdapInvalidAttributeSyntaxException(e);
		}
	}

	public String getName() {
		return "1.3.6.1.4.1.1466.115.121.1.14";
	}

}
