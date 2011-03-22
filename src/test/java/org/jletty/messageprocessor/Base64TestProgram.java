package org.jletty.messageprocessor;

import org.jletty.util.HexUtils;

public class Base64TestProgram {

	public Base64TestProgram() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// byte[] md5hashbytes = new
		// byte[]{(byte)0xec,(byte)0x85,(byte)0x07,(byte)0x0a,(byte)0xa7,(byte)0x0e5,(byte)0x98,(byte)0xed,(byte)0xa7,(byte)0x2c,(byte)0xbe,(byte)0x82,(byte)0xd99,0xfa,0xbc};
		byte[] original = HexUtils
				.fromHexString("e4c0603a123ee10a19cad3782fe9c13e04e89c21");

		String encoded = org.jletty.messageprocessorstage.Base64
				.encodeBytes(original);

		System.out.println("original:" + HexUtils.toHexString(original));
		System.out.println("Base64  :" + encoded);

	}

}
