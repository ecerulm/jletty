package org.jletty.encoder;

import org.jletty.util.HexUtils;

import org.junit.Test;
import static org.junit.Assert.*;


public class BerOctetStringTest  {

	@Test
        public void testGetBytes() {
		assertArrayEquals(HexUtils
				.fromHexString("0414636e3d4469726563746f7279204d616e61676572"),
				new BerOctetString("cn=Directory Manager").getBytes());
		assertArrayEquals(
				HexUtils.fromHexString("0408636e3d527562656e"),
				new BerOctetString("cn=Ruben").getBytes());
		assertArrayEquals(HexUtils.fromHexString("0400"),
				new BerOctetString("").getBytes());
		assertArrayEquals(HexUtils
				.fromHexString("0414636e3d4469726563746f7279204d616e61676572"),
				new BerOctetString("cn=Directory Manager".getBytes()).getBytes());
		assertArrayEquals(
				HexUtils.fromHexString("0408636e3d527562656e"),
				new BerOctetString("cn=Ruben".getBytes()).getBytes());
		assertArrayEquals(HexUtils.fromHexString("0400"),
				new BerOctetString("".getBytes()).getBytes());
	}
	
	@Test
        public void testGetBytesBigString() {
		String theString = "";
		String theHexString = "04820477";
		for (int i = 0; i< 127;i++) {
			theString = theString + "Directory";
			theHexString = theHexString+"4469726563746f7279";
		}
		
		
		assertArrayEquals(HexUtils
				.fromHexString(theHexString),
				new BerOctetString(theString).getBytes());
		
	}
}
