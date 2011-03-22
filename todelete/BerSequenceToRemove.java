/*
 * Created on 12-oct-2004
 *
 */
package jletty.encoder;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jletty.ldapstack.BerTags;

import com.rubenlaguna.utils.NotImplementedException;


/**
 * @author Ruben
 *  
 */
public class BerSequenceToRemove extends BerEncoderBase implements BerEncodeable {
	
	public static final BerSequenceToRemove EMPTY = new BerSequenceToRemove(BerTags.SEQUENCEOF);

	private byte tag;

	private List elements = new ArrayList();

	public BerSequenceToRemove(byte tag) {
		this.tag = tag;
	}
	
	public BerSequenceToRemove() {
		this.tag = BerTags.SEQUENCEOF;
	}

	/**
	 * @param i
	 * @return
	 */
	public BerSequenceToRemove appendEnum(int i) {
		byte[] buffer = new byte[] { 0x0a, 0x01, (byte) i };
		elements.add(buffer);
		return this;
	}

	/**
	 * @param string
	 * @return
	 */
	public BerSequenceToRemove append(String string) {
		byte[] bytes;
		try {
			bytes = string.getBytes("UTF-8");
			byte[] lengthOctets;
			if (bytes.length <= 127) {
				lengthOctets = new byte[] { (byte) bytes.length };
			} else {
				//long form
				throw new NotImplementedException();
			}
			ByteBuffer buffer = ByteBuffer.allocate(1+lengthOctets.length+bytes.length);
			
			
			buffer.put((byte) 0x04);
			buffer.put(lengthOctets);
			buffer.put(bytes);
			elements.add(buffer.array());
			
			
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		return this;
	}

	public void append(BerEncodeable seq) {
		elements.add(seq.getBytes());
	}

	/**
	 * @return
	 */
	public byte[] getBytes() {
		int length = 0;
		for (Iterator iter = elements.iterator(); iter.hasNext();) {
			byte[] element = (byte[]) iter.next();
			length += element.length;			
		}
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		//byte[] buffer  = new byte[length+2];
		//buffer[0] = 0x30;
		buffer.clear();
		buffer.put(tag);
		buffer.put(berLength(length));
		for(Iterator iter = elements.iterator();iter.hasNext();) {
			byte[] element = (byte[]) iter.next();
			buffer.put(element);	
		}
		buffer.flip();
		byte[] toReturn = new byte[buffer.remaining()];
		buffer.get(toReturn);
		return toReturn;		
	}
	
	public void append(byte[] value) {
		elements.add(value);
	}
}
