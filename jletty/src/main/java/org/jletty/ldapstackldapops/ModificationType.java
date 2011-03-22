/*
 * Created on 23-sep-2004
 *
 */
package org.jletty.ldapstackldapops;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public final class ModificationType extends Enum {
	private static final long serialVersionUID = 1L;

	public static final ModificationType REPLACE = new ModificationType(
			"REPLACE");

	public static final ModificationType ADD = new ModificationType("ADD");

	public static final ModificationType DELETE = new ModificationType("DELETE");

	private ModificationType(String color) {
		super(color);
	}

	public static ModificationType getEnum(String color) {
		return (ModificationType) getEnum(ModificationType.class, color);
	}

	public static Map getEnumMap() {
		return getEnumMap(ModificationType.class);
	}

	public static List getEnumList() {
		return getEnumList(ModificationType.class);
	}

	public static Iterator iterator() {
		return iterator(ModificationType.class);
	}

}