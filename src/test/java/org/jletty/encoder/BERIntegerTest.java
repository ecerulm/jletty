package org.jletty.encoder;

import org.jletty.util.HexUtils;
import org.junit.Test;
import static org.junit.Assert.*;


public class BERIntegerTest  {

@Test
public void testGetBytes() {
		assertArrayEquals(new BERInteger(1).getBytes(), HexUtils.fromHexString("020101"));
                assertArrayEquals(new BERInteger(2).getBytes(), HexUtils.fromHexString("020102"));
                assertArrayEquals(new BERInteger(256).getBytes(), HexUtils.fromHexString("02020100"));
		assertArrayEquals(new BERInteger(0).getBytes(), HexUtils.fromHexString("02 01 00"));
		assertArrayEquals(new BERInteger(127).getBytes(), HexUtils.fromHexString("02 01 7F"));
		assertArrayEquals(new BERInteger(128).getBytes(), HexUtils.fromHexString("02 02 00 80"));
		assertArrayEquals(new BERInteger(8388608).getBytes(), HexUtils.fromHexString("02 04 00 80 00 00"));
		assertArrayEquals(new BERInteger(-8388608).getBytes(), HexUtils.fromHexString("02 03 80 00 00"));
		assertArrayEquals(new BERInteger(256).getBytes(), HexUtils.fromHexString("02 02 01 00"));
		assertArrayEquals(new BERInteger(-128).getBytes(), HexUtils.fromHexString("02 01 80"));
		assertArrayEquals(new BERInteger(-129).getBytes(), HexUtils.fromHexString("02 02 FF 7F"));
	}}
