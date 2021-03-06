/*
 * Created on 31-dic-2004
 *
 */
package org.jletty.util;


import junit.framework.TestCase;
import static org.junit.Assert.*;

/**
 * @author Ruben
 * 
 */
public class IntToArrayUtilTest extends TestCase {

	/*
	 * Class under test for byte[] toByteArray(int)
	 */
	public void testToByteArrayint1() {
		// Integer BER encoding
		// value
		// 0 02 01 00
		assertArrayEquals(HexUtils.fromHexString("00"), IntToArrayUtil
				.toByteArray(0));

	}

	public void testToByteArrayint2() {
		// 127 02 01 7F

                assertArrayEquals(HexUtils.fromHexString("7f"), IntToArrayUtil
				.toByteArray(127));
	}

	public void testToByteArrayint3() {
		// 128 02 02 00 80
		assertArrayEquals(HexUtils.fromHexString("0080"), IntToArrayUtil
				.toByteArray(128));

	}

	public void testToByteArrayint4() {
		// 256 02 02 01 00
		final byte[] expected = HexUtils.fromHexString("0100");
		final byte[] actual = IntToArrayUtil.toByteArray(256);
		assertArrayEquals(expected, actual);

	}

	public void testToByteArrayint5() {
		// -128 02 01 80
		assertArrayEquals(HexUtils.fromHexString("80"), IntToArrayUtil
				.toByteArray(-128));

	}

	public void testToByteArrayint6() {
		// -129 02 02 FF 7F
		assertArrayEquals(HexUtils.fromHexString("ff7f"), IntToArrayUtil
				.toByteArray(-129));
	}

	public void testToByteArrayint7() {
		// 10000 02 02 27 10
		assertArrayEquals(HexUtils.fromHexString("2710"), IntToArrayUtil
				.toByteArray(10000));
	}

}
