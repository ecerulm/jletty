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
public class BindResponseTest extends TestCase {
	
	public void testBindResponseOk() {
		BindResponse response = new BindResponse(LDAPResultCode.SUCCESS);
		final byte[] expectedBytes = HexUtils.fromHexString("61 07 0a  01 00 04 00 04 00");
		assertArrayEquals("Should be exactly equals", expectedBytes,response.getBytes());		
	}

}
