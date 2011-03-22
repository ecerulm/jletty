/*
 * Created on 14-oct-2004
 *
 */
package org.jletty.ldapstackldapops;

import org.jletty.ldapstackldapops.BindResponse;
import org.jletty.ldapstackldapops.LDAPResultCode;
import org.jletty.util.HexUtils;

import junit.framework.TestCase;
import junitx.framework.ArrayAssert;


/**
 * @author Ruben
 *
 */
public class BindResponseTest extends TestCase {
	
	public void testBindResponseOk() {
		BindResponse response = new BindResponse(LDAPResultCode.SUCCESS);
		final byte[] expectedBytes = HexUtils.fromHexString("61 07 0a  01 00 04 00 04 00");
		ArrayAssert.assertEquals("Should be exactly equals", expectedBytes,response.getBytes());		
	}

}
