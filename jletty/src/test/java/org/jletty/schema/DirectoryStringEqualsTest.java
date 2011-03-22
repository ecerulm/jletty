package org.jletty.schema;

import org.jletty.schema.DirectoryString;

import junitx.extensions.EqualsHashCodeTestCase;

public class DirectoryStringEqualsTest extends EqualsHashCodeTestCase {

	public DirectoryStringEqualsTest(String name) {
		super(name);
	}

	protected Object createInstance() throws Exception {
		return new DirectoryString("description");
	}

	protected Object createNotEqualInstance() throws Exception {
		return new DirectoryString("another description");
	}
	

}
