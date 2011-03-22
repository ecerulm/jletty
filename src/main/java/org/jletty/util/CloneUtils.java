package org.jletty.util;

import org.apache.commons.lang.ArrayUtils;

public final class CloneUtils {
	private CloneUtils() {
	}

	public static byte[][] deepCloneByteArray(byte[][] theValues) {
		if (theValues == null) {
			return null;
		}
		byte[][] toReturn = new byte[theValues.length][];
		for (int i = 0; i < toReturn.length; i++) {
			toReturn[i] = ArrayUtils.clone(theValues[i]);
		}
		return toReturn;
	}

}
