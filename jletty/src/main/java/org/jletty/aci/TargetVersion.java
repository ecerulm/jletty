/* Don delete it's not generated */
package org.jletty.aci;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

public class TargetVersion extends ValuedEnum {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7554842996100612998L;

	public static final int VERSION3_0_VALUE = 100;

	public static final TargetVersion VERSION3_0 = new TargetVersion("3.0",
			VERSION3_0_VALUE);

	private TargetVersion(String name, int value) {
		super(name, value);
	}

	public static TargetVersion getEnum(String targetVersion) {
		return (TargetVersion) getEnum(TargetVersion.class, targetVersion);
	}

	public static TargetVersion getEnum(int targetVersion) {
		return (TargetVersion) getEnum(TargetVersion.class, targetVersion);
	}

	public static Map getEnumMap() {
		return getEnumMap(TargetVersion.class);
	}

	public static List getEnumList() {
		return getEnumList(TargetVersion.class);
	}

	public static Iterator iterator() {
		return iterator(TargetVersion.class);
	}

}
