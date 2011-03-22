package org.jletty.ldapstackldapops;

public interface AuthenticationChoiceListener {
	public void data(byte[] simplePassword);

	// TODO add a method to receive Sasl auth
}
