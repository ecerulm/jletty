/*
 * Created on 03-ene-2005
 *
 */
package org.jletty.encoder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jletty.util.StringUtil;

/**
 * @author Ruben
 * 
 */
public class BerSequence extends BerEncoderBase implements BerEncodeable {
	List contents = new ArrayList();

	private int length;

	private byte tag;

	public BerSequence() {
		this.tag = 0x30;
	}

	public BerSequence(final byte tag) {
		this.tag = tag;
	}

	public BerSequence append(int v) {
		append(berInteger(v));
		return this;
	}

	public BerSequence append(BerEncodeable v) {
		append(v.getBytes());
		return this;
	}

	public BerSequence append(byte[] buffer) {
		this.contents.add(buffer);
		this.length = this.length + buffer.length;
		return this;
	}

	public byte[] getBytes() {
		final byte[] len_octets = berLength(this.length);
		int buflen = 1 + len_octets.length + this.length; // one byte for the tag
		// plus length bytes
		// plus contents bytes
		byte[] tmp = new byte[buflen];
		int offset = 0;
		tmp[offset++] = this.tag;
		System.arraycopy(len_octets, 0, tmp, offset, len_octets.length);
		offset = offset + len_octets.length;
		for (Iterator iter = this.contents.iterator(); iter.hasNext();) {
			byte[] buffer = (byte[]) iter.next();
			System.arraycopy(buffer, 0, tmp, offset, buffer.length);
			offset = offset + buffer.length;
		}
		return tmp;
	}

	/**
	 * @param matchedDN
	 * @return
	 */
	public BerSequence append(String matchedDN) {
		byte[] bytes = new BerSequence((byte) 0x04).append(
				StringUtil.toUTF8(matchedDN)).getBytes();
		append(bytes);
		return this;

	}
}
