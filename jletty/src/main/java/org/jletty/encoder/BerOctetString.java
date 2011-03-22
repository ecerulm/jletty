package org.jletty.encoder;

import org.jletty.ldapstackldapops.BerTags;
import org.jletty.util.StringUtil;

public class BerOctetString extends BerEncoderBase implements BerEncodeable {
	private static byte tag = BerTags.OCTETSTRING;

	private byte[] theValue;

	public BerOctetString(byte[] value) {
		this.theValue = value.clone();
	}

	public BerOctetString(String string) {
		this.theValue = StringUtil.toUTF8(string);
	}

	public byte[] getBytes() {

		byte[] lengthBytes = berLength(this.theValue.length);

		byte[] toReturn = new byte[1 + lengthBytes.length + this.theValue.length];

		toReturn[0] = tag;
		System.arraycopy(lengthBytes, 0, toReturn, 1, lengthBytes.length);
		System.arraycopy(this.theValue, 0, toReturn, 1 + lengthBytes.length,
				this.theValue.length);

		return toReturn;
	}

}
