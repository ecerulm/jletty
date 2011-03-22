package org.jletty.schema;

import org.jletty.util.EqualsHashCodeTestCase;

public class TelephoneNumberTest extends EqualsHashCodeTestCase {


	protected Object createInstance() throws Exception {
		return new TelephoneNumber("+41 1 268 1540".getBytes("UTF-8"));
	}

	protected Object createNotEqualInstance() throws Exception {
		return new TelephoneNumber("+41 1 268 1541".getBytes("UTF-8"));
	}

}
