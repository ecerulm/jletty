/* Don delete it's not generated */
package org.jletty.aci;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

public class PermissionType extends ValuedEnum {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6979424935952140578L;

	public static final int ALLOW_VALUE = 100;

	public static final int DENY_VALUE = 200;

	public static final PermissionType ALLOW = new PermissionType("allow",
			ALLOW_VALUE);

	public static final PermissionType DENY = new PermissionType("deny",
			DENY_VALUE);

	private PermissionType(String name, int value) {
		super(name, value);
	}

	public static PermissionType getEnum(String target) {
		return (PermissionType) getEnum(PermissionType.class, target);
	}

	public static PermissionType getEnum(int target) {
		return (PermissionType) getEnum(PermissionType.class, target);
	}

	public static Map getEnumMap() {
		return getEnumMap(PermissionType.class);
	}

	public static List getEnumList() {
		return getEnumList(PermissionType.class);
	}

	public static Iterator iterator() {
		return iterator(PermissionType.class);
	}

}
