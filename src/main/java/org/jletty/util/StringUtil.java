package org.jletty.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtil {

	private static final Pattern ldapEscapePattern = Pattern
			.compile("\\\\[A-Za-z0-9][A-Za-z0-9]");

	private StringUtil() {
	} // prevents instantiation

	public static byte[] toUTF8(String theString) {
		if (theString == null) {
			return new byte[] {};
		}
		try {
			return theString.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e); // imposible i think
		}
	}

	public static String unescapeLDAPDN(String theString) {

		String toReturn = escape(theString);

		return toReturn;
	}

	private static String escape(String theString) {
		Matcher m = ldapEscapePattern.matcher(theString);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String group = m.group();
			group = group.substring(1);
			// String a = new Character((char)
			// HexUtils.fromHexString(group)[0]).toString();
			String asChar = new String(HexUtils.fromHexString(group));
			if (asChar.equals("\\")) {
				asChar = "\\\\";
			}
			if (asChar.equals("$")) {
				asChar = "\\$";
			}
			String a = asChar;
			// one of the characters ",", "+", """, "\", "<", ">" or ";"
			if (a.equals(",") || a.equals("+") || a.equals("\"")
					|| a.equals("\\\\") || a.equals("<") || a.equals(">")
					|| a.equals(";")) {
				a = "\\\\" + a;
			}
			m.appendReplacement(sb, a);
		}
		m.appendTail(sb);
		String toReturn = sb.toString();
		return toReturn;
	}

	public static String byteArrayToString(String charsetName,
			byte[] octetstring) throws CharacterCodingException {
		Charset charset = Charset.forName(charsetName);
		CharsetDecoder decoder = charset.newDecoder();

		// Convert ISO-LATIN-1 bytes in a ByteBuffer to a character
		// ByteBuffer and then to a string.
		// The new ByteBuffer is ready to be read.
		CharBuffer cbuf = decoder.decode(ByteBuffer.wrap(octetstring));
		String s = cbuf.toString();
		return s;
	}

	public static byte[] stringToByteArray(String charset, String valueToEncode) {
		try {
			return valueToEncode.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}
