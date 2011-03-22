/*
 * Created on 31-dic-2004
 *
 */
package org.jletty.util;

import org.apache.commons.lang.ArrayUtils;

/**
 * @author Ruben
 * 
 */
public class IntToArrayUtil {

	public static byte[] toByteArray(int value) {
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

}
