/*
 * Created on 17-abr-2004
 *
 */
package org.jletty.encoder;

import org.jletty.ldapstackldapops.BerTags;

/**
 * @author rlm
 * 
 */
public class BERInteger extends BerEncoderBase {
	private int value;

	public BERInteger(int val) {
		this.value = val;
	}

	public byte[] getBytes() {
		return berInteger(BerTags.INTEGER, this.value);
	}
}