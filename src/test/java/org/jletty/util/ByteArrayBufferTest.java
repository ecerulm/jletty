/*
 * Created on 12-abr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.jletty.util;

import org.jletty.util.ByteArrayBuffer;
import org.jletty.util.HexUtils;

import junit.framework.TestCase;
import junitx.framework.ArrayAssert;

/**
 * @author rlm
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ByteArrayBufferTest extends TestCase {
	/**
	 * Constructor for ByteArrayBufferTest.
	 * 
	 * @param arg0
	 */
	public ByteArrayBufferTest(String arg0) {
		super(arg0);
	}

	/*
	 * Test for ByteArrayBuffer append(int)
	 */
	public void testAppendint() {
		byte[] actual = new ByteArrayBuffer().append(0x30).toBytes();
		byte[] expected = HexUtils.fromHexString("30");
		// System.out.println("Expected:\n" + HexUtils.toHexString(expected));
		// System.out.println("Actual:\n" + HexUtils.toHexString(actual));
		ArrayAssert.assertEquals(expected, actual);
	}

	/*
	 * Test for ByteArrayBuffer append(byte[])
	 */
	public void testAppendbyteArray() {
		byte[] actual = new ByteArrayBuffer().append(
				new byte[] { 0x30, 0x31, 0x02 }).toBytes();
		byte[] expected = HexUtils.fromHexString("303102");
		// System.out.println("Expected:\n" + HexUtils.toHexString(expected));
		// System.out.println("Actual:\n" + HexUtils.toHexString(actual));
		ArrayAssert.assertEquals(expected, actual);
	}

	public void testLength() {
		ByteArrayBuffer buffer = new ByteArrayBuffer();
		assertEquals(0, buffer.length());
		buffer.append(0x80);
		assertEquals(1, buffer.length());
		buffer.append(HexUtils.fromHexString("303102"));
		// System.out.println(
		// "testLength: " + HexUtils.toHexString(buffer.toBytes()));
		assertEquals(4, buffer.length());
	}
}