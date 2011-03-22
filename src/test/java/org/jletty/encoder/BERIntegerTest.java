package org.jletty.encoder;

import org.jletty.encoder.BERInteger;
import org.jletty.util.HexUtils;

import junit.framework.TestCase;
import junitx.framework.ArrayAssert;

public class BERIntegerTest extends TestCase {

public void testGetBytes() {
		ArrayAssert.assertEquals(new BERInteger(1).getBytes(), HexUtils.fromHexString("020101"));
		ArrayAssert.assertEquals(new BERInteger(2).getBytes(), HexUtils.fromHexString("020102"));
		ArrayAssert.assertEquals(new BERInteger(256).getBytes(), HexUtils.fromHexString("02020100"));
		ArrayAssert.assertEquals(new BERInteger(0).getBytes(), HexUtils.fromHexString("02 01 00"));
		ArrayAssert.assertEquals(new BERInteger(127).getBytes(), HexUtils.fromHexString("02 01 7F"));
		ArrayAssert.assertEquals(new BERInteger(128).getBytes(), HexUtils.fromHexString("02 02 00 80"));
		ArrayAssert.assertEquals(new BERInteger(8388608).getBytes(), HexUtils.fromHexString("02 04 00 80 00 00"));
		ArrayAssert.assertEquals(new BERInteger(-8388608).getBytes(), HexUtils.fromHexString("02 03 80 00 00"));
		ArrayAssert.assertEquals(new BERInteger(256).getBytes(), HexUtils.fromHexString("02 02 01 00"));
		ArrayAssert.assertEquals(new BERInteger(-128).getBytes(), HexUtils.fromHexString("02 01 80"));
		ArrayAssert.assertEquals(new BERInteger(-129).getBytes(), HexUtils.fromHexString("02 02 FF 7F"));
	}}
