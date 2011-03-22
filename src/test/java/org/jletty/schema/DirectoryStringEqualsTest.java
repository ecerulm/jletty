package org.jletty.schema;

import org.jletty.util.EqualsHashCodeTestCase;


public class DirectoryStringEqualsTest extends EqualsHashCodeTestCase {


	protected Object createInstance() throws Exception {
		return new DirectoryString("description");
	}

	protected Object createNotEqualInstance() throws Exception {
		return new DirectoryString("another description");
	}
	

}
