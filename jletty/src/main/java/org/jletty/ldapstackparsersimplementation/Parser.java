/*
 * Created on 22-feb-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;

/**
 * @author rlm
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface Parser {
	/**
	 * @param berInteger1
	 */
	public boolean parse(byte[] in);

	public boolean parse(ByteBuffer in);

	/**
	 * TODO JAVADOC
	 * 
	 * @param tlvLength
	 */
	public void implicit(int tlvLength);

	/**
	 * (add JAVADOC)
	 * 
	 * 
	 */
	public void reset();

}