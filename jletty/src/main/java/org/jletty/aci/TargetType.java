/* Don delete it's not generated */
package org.jletty.aci;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

public class TargetType extends ValuedEnum {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7554842996100612998L;

	public static final int TARGET_VALUE = 100;

	public static final int TARGETATTR_VALUE = 200;

	public static final int TARGETFILTER_VALUE = 300;

	public static final int TARGATATTRFILTERS_VALUE = 400;

	public static final TargetType TARGET = new TargetType("target",
			TARGET_VALUE);

	public static final TargetType TARGETATTR = new TargetType("targetattr",
			TARGETATTR_VALUE);

	public static final TargetType TARGETFILTER = new TargetType(
			"targetfilter", TARGETFILTER_VALUE);

	public static final TargetType TARGATATTRFILTERS = new TargetType(
			"targattrfilters", TARGATATTRFILTERS_VALUE);

	private TargetType(String name, int value) {
		super(name, value);
	}

	public static TargetType getEnum(String target) {
		return (TargetType) getEnum(TargetType.class, target);
	}

	public static TargetType getEnum(int target) {
		return (TargetType) getEnum(TargetType.class, target);
	}

	public static Map getEnumMap() {
		return getEnumMap(TargetType.class);
	}

	public static List getEnumList() {
		return getEnumList(TargetType.class);
	}

	public static Iterator iterator() {
		return iterator(TargetType.class);
	}

}
