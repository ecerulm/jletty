/*
 * Created on 03-ene-2005
 *
 */
package org.jletty.encoder;

import org.apache.commons.lang.ArrayUtils;

/**
 * @author Ruben
 * 
 */
public class BerEncoderBase {

	private final byte[] toByteArray(int value) {
		if (value == 0) {
			return new byte[] { 0 };
		}
		int binary_value = value;

		if (value < 0) {
			binary_value = (value * -1) - 1;
		}

		byte[] array = new byte[4];
		int num_octets = 0;

		for (int iInd = 0; iInd < array.length; iInd++) {
			byte b = (byte) ((binary_value >> (iInd * 8)) & 0xFF);
			if (value < 0 && (b != 0)) {
				b = (byte) (b ^ 0xFF);
			}
			array[iInd] = b;
		}
		ArrayUtils.reverse(array);

		byte lead = 0;
		for (int iInd = 0; iInd < array.length; iInd++) {
			final byte b = array[iInd];
			if (b != 0) {
				lead = b;
				num_octets = 4 - iInd;
				break;
			}
		}
		int extra = 0;
		if ((value > 0) && (lead & 0x80) > 0) {
			lead = 0;
			extra = 1;
		} else if ((value < 0) && ((lead & 0x80) == 0)) {
			lead = (byte) 0xFF;
			extra = 1;
		}
		byte[] tmp = new byte[num_octets + extra];
		System.arraycopy(array, array.length - num_octets, tmp, extra,
				num_octets);
		if (extra > 0) {
			tmp[0] = lead;
		}

		return tmp;
	}

	public final byte[] berInteger(int m_value) {
		return berInteger((byte) 0x02, m_value);
	}

	public final byte[] berInteger(byte tag, int m_value) {
		byte[] content_octets = toByteArray(m_value);
		byte[] length_octets = berLength(content_octets.length);
		byte[] toReturn = new byte[1 + content_octets.length
				+ length_octets.length];
		toReturn[0] = tag;
		System.arraycopy(length_octets, 0, toReturn, 1, length_octets.length);
		System.arraycopy(content_octets, 0, toReturn, length_octets.length + 1,
				content_octets.length);

		return toReturn;
	}

	/**
	 * Length octets. There are two forms: short (for lengths between 0 and
	 * 127), and long definite (for lengths between 0 and 21008-1). <lu>
	 * <li>Short form. One octet. Bit 8 has value "0" and bits 7-1 give the
	 * length.
	 * <li>Long form. Two to 127 octets. Bit 8 of first octet has value "1" and
	 * bits 7-1 give the number of additional length octets. Second and
	 * following octets give the length, base 256, most significant digit first.
	 * <lu>
	 * 
	 * @param i
	 * @return
	 */
	public final byte[] berLength(int contents_length) {
		if (contents_length <= 127) {
			// short form
			return new byte[] { (byte) contents_length };
		} else { // long form
			// count the number of bytes needed to encode length
			int num_length_octets = 0;
			int num = contents_length;
			do {
				num_length_octets++;
				num = (num >> 8);
			} while (num > 0);
			byte[] buffer = new byte[num_length_octets + 1];
			buffer[0] = (byte) (0x80 | num_length_octets); // first byte
			// encodes
			// the number of
			// additional length
			// octets
			num = contents_length;
			for (int i = buffer.length - 1; i > 0; i--) {
				buffer[i] = (byte) (num & 0xFF);
				num = (num >> 8);
			}
			return buffer;
		}
	}

}
