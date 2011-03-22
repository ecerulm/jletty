/*
 * Created on 11-abr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.jletty.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author rlm
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class HexUtils {

	// table to convert a nibble to a hex char.
	static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f' };

	// Fast convert a byte array to a hex string
	// with possible leading zero.

	public static String toHexString(byte[] b) {
		if (b == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			byte bb = b[i];
			String oneByteString = toHexString(bb);
			sb.append(oneByteString);
			sb.append(" ");
			if (((i + 1) % 20) == 0) {
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	public static String toHexString(byte bb) {
		StringBuffer oneByteBuffer = new StringBuffer();
		// look up high nibble char
		oneByteBuffer.append(hexChar[(bb & 0xf0) >>> 4]);
		// look up low nibble char
		oneByteBuffer.append(hexChar[bb & 0x0f]);
		String oneByteString = oneByteBuffer.toString();
		return oneByteString;
	}

	public static byte[] fromHexInputStream(InputStream is) {

		try {
			ArrayList list = new ArrayList();
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String str;
			while ((str = in.readLine()) != null) {
				byte[] tmp = fromHexString(str);
				list.add(tmp);
			}
			in.close();
			int length = 0;
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				byte[] element = (byte[]) iter.next();
				length += element.length;
			}
			java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(length);
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				byte[] element = (byte[]) iter.next();
				buffer.put(element);
			}
			return buffer.array();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public static byte[] fromHexString(String inputStr) {
		String s = removeWhitespace(inputStr).toString();
		int stringLength = s.length();
		if ((stringLength & 0x1) != 0) {
			throw new IllegalArgumentException(
					"fromHexString requires an even number of hex characters");
		}
		byte[] b = new byte[stringLength / 2];

		for (int i = 0, j = 0; i < stringLength; i += 2, j++) {
			int high = charToNibble(s.charAt(i));
			int low = charToNibble(s.charAt(i + 1));
			b[j] = (byte) ((high << 4) | low);
		}
		return b;
	}

	// Returns a version of the input where all whitespace
	// characters are eliminated. Line terminators are treated
	// like whitespace.
	public static CharSequence removeWhitespace(CharSequence inputStr) {
		String patternStr = "[\\s_]+";
		String replaceStr = "";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(inputStr);
		String tmp = matcher.replaceAll(replaceStr);

		return tmp;
	}

	/**
	 * convert a single char to corresponding nibble.
	 * 
	 * @param c
	 *            char to convert. must be 0-9 a-f A-F, no spaces, plus or minus
	 *            signs.
	 * 
	 * @return corresponding integer
	 */
	private static int charToNibble(char c) {
		if ('0' <= c && c <= '9') {
			return c - '0';
		} else if ('a' <= c && c <= 'f') {
			return c - 'a' + 0xa;
		} else if ('A' <= c && c <= 'F') {
			return c - 'A' + 0xa;
		} else {
			throw new IllegalArgumentException("Invalid hex character: " + c);
		}
	}
}