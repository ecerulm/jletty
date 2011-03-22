package org.jletty.schema;

import org.jletty.schema.TelephoneNumber;

import junitx.extensions.EqualsHashCodeTestCase;

public class TelephoneNumberTest extends EqualsHashCodeTestCase {

	public TelephoneNumberTest(String name) {
		super(name);
	}

	protected Object createInstance() throws Exception {
		return new TelephoneNumber("+41 1 268 1540".getBytes("UTF-8"));
	}

	protected Object createNotEqualInstance() throws Exception {
		return new TelephoneNumber("+41 1 268 1541".getBytes("UTF-8"));
	}

}
