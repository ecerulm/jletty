package org.jletty.encoder;

import org.jletty.encoder.BerOctetString;
import org.jletty.util.HexUtils;

import junit.framework.TestCase;
import junitx.framework.ArrayAssert;


public class BerOctetStringTest extends TestCase {

	public void testGetBytes() {
		ArrayAssert.assertEquals(HexUtils
				.fromHexString("0414636e3d4469726563746f7279204d616e61676572"),
				new BerOctetString("cn=Directory Manager").getBytes());
		ArrayAssert.assertEquals(
				HexUtils.fromHexString("0408636e3d527562656e"),
				new BerOctetString("cn=Ruben").getBytes());
		ArrayAssert.assertEquals(HexUtils.fromHexString("0400"),
				new BerOctetString("").getBytes());
		ArrayAssert.assertEquals(HexUtils
				.fromHexString("0414636e3d4469726563746f7279204d616e61676572"),
				new BerOctetString("cn=Directory Manager".getBytes()).getBytes());
		ArrayAssert.assertEquals(
				HexUtils.fromHexString("0408636e3d527562656e"),
				new BerOctetString("cn=Ruben".getBytes()).getBytes());
		ArrayAssert.assertEquals(HexUtils.fromHexString("0400"),
				new BerOctetString("".getBytes()).getBytes());
	}
	
	public void testGetBytesBigString() {
		String theString = "";
		String theHexString = "04820477";
		for (int i = 0; i< 127;i++) {
			theString = theString + "Directory";
			theHexString = theHexString+"4469726563746f7279";
		}
		
		
		ArrayAssert.assertEquals(HexUtils
				.fromHexString(theHexString),
				new BerOctetString(theString).getBytes());
		
	}
}
