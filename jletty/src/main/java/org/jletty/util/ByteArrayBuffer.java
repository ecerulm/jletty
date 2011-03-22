/*
 * Created on 12-abr-2004
 * 
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.jletty.util;

/**
 * @author rlm
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ByteArrayBuffer {
	private byte[] buffer = null;

	private int pos = 0;

	public ByteArrayBuffer append(String value) {
		return append(StringUtil.toUTF8(value));
	}

	/**
	 * @param i
	 */
	public ByteArrayBuffer append(int i) {
		return append(new byte[] { (byte) i });
	}

	/**
	 * @param string
	 */
	public ByteArrayBuffer append(byte[] tlv) {
		if (null == this.buffer) {
			this.buffer = new byte[tlv.length];
		}
		if ((this.buffer.length - this.pos) < tlv.length) {
			byte[] tmp = new byte[this.pos + tlv.length];
			System.arraycopy(this.buffer, 0, tmp, 0, this.buffer.length);
			this.buffer = tmp;
		}
		System.arraycopy(tlv, 0, this.buffer, this.pos, tlv.length);
		this.pos += tlv.length;
		return this;
	}

	/**
	 * @return
	 */
	public byte[] toBytes() {
		final int length = this.buffer.length;
		byte[] toReturn = new byte[length];
		System.arraycopy(this.buffer, 0, toReturn, 0, length);
		return toReturn;
	}

	/**
	 * @return
	 */
	public int length() {
		return (this.buffer != null) ? this.buffer.length : 0;
	}
}