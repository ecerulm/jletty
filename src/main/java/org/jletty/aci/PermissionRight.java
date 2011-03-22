/* Don delete it's not generated */
package org.jletty.aci;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

public class PermissionRight extends ValuedEnum {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1723530501097517267L;

	public static final int READ_VALUE = 100;

	public static final int WRITE_VALUE = 200;

	public static final int ADD_VALUE = 300;

	public static final int DELETE_VALUE = 400;

	public static final int SEARCH_VALUE = 500;

	public static final int COMPARE_VALUE = 600;

	public static final int SELFWRITE_VALUE = 700;

	public static final int PROXY_VALUE = 800;

	public static final int ALL_VALUE = 900;

	public static final PermissionRight READ = new PermissionRight("read",
			READ_VALUE);

	public static final PermissionRight WRITE = new PermissionRight("write",
			WRITE_VALUE);

	public static final PermissionRight ADD = new PermissionRight("add",
			ADD_VALUE);

	public static final PermissionRight DELETE = new PermissionRight("delete",
			DELETE_VALUE);

	public static final PermissionRight SEARCH = new PermissionRight("search",
			SEARCH_VALUE);

	public static final PermissionRight COMPARE = new PermissionRight(
			"compare", COMPARE_VALUE);

	public static final PermissionRight SELFWRITE = new PermissionRight(
			"selfwrite", SELFWRITE_VALUE);

	public static final PermissionRight PROXY = new PermissionRight("proxy",
			PROXY_VALUE);

	public static final PermissionRight ALL = new PermissionRight("all",
			ALL_VALUE);

	private PermissionRight(String name, int value) {
		super(name, value);
	}

	public static PermissionRight getEnum(String target) {
		return (PermissionRight) getEnum(PermissionRight.class, target);
	}

	public static PermissionRight getEnum(int target) {
		return (PermissionRight) getEnum(PermissionRight.class, target);
	}

	public static Map getEnumMap() {
		return getEnumMap(PermissionRight.class);
	}

	public static List getEnumList() {
		return getEnumList(PermissionRight.class);
	}

	public static Iterator iterator() {
		return iterator(PermissionRight.class);
	}

}
