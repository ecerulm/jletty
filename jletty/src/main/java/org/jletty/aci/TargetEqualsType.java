/* Don delete it's not generated */
package org.jletty.aci;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

public class TargetEqualsType extends ValuedEnum {

	public static final int EQUALS_VALUE = 100;

	public static final int NEQUALS_VALUE = 200;

	public static final TargetEqualsType EQUALS = new TargetEqualsType("=",
			EQUALS_VALUE);

	public static final TargetEqualsType NEQUALS = new TargetEqualsType("!=",
			NEQUALS_VALUE);

	private TargetEqualsType(String name, int value) {
		super(name, value);
	}

	public static TargetEqualsType getEnum(String target) {
		return (TargetEqualsType) getEnum(TargetEqualsType.class, target);
	}

	public static TargetEqualsType getEnum(int target) {
		return (TargetEqualsType) getEnum(TargetEqualsType.class, target);
	}

	public static Map getEnumMap() {
		return getEnumMap(TargetEqualsType.class);
	}

	public static List getEnumList() {
		return getEnumList(TargetEqualsType.class);
	}

	public static Iterator iterator() {
		return iterator(TargetEqualsType.class);
	}

}
