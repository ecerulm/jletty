/*
 * Created on 14-oct-2004
 *
 */
package org.jletty.ldapstackldapops;

import org.jletty.util.HexUtils;

import junit.framework.TestCase;
import static org.junit.Assert.*;

/**
 * @author Ruben
 *  
 */
public class LDAPMessageTest extends TestCase {
	public void testLDAPMessageBindRequest() {
		LDAPMessage message = new LDAPMessage(1,new BindResponse(LDAPResultCode.SUCCESS));
		final byte[] expectedBytes = HexUtils.fromHexString("30 0c 02 01 01 61 07 0a  01 00 04 00 04 00");
		assertArrayEquals(expectedBytes,message.getBytes());
		
	}
}
