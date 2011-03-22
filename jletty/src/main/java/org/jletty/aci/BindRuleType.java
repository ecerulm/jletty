/* Don delete it's not generated */
package org.jletty.aci;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

public class BindRuleType extends ValuedEnum {

	/**
	 * 
	 */
	private static final long serialVersionUID = -109253438258504881L;

	public static final int USERDN_VALUE = 100;

	public static final int GROUPDN_VALUE = 200;

	public static final int ROLEDN_VALUE = 300;

	public static final int USERATTR_VALUE = 400;

	public static final int IP_VALUE = 500;

	public static final int DNS_VALUE = 600;

	public static final int DAYOFWEEK_VALUE = 700;

	public static final int TIMEOFDAY_VALUE = 800;

	public static final int AUTHMETHOD_VALUE = 900;

	
	public static final BindRuleType USERDN = new BindRuleType("userdn",
			USERDN_VALUE);

	public static final BindRuleType GROUPDN = new BindRuleType("groupdn",
			GROUPDN_VALUE);

	public static final BindRuleType ROLEDN = new BindRuleType("roledn",
			ROLEDN_VALUE);

	public static final BindRuleType USERATTR = new BindRuleType("userattr",
			USERATTR_VALUE);

	public static final BindRuleType IP = new BindRuleType("ip", IP_VALUE);

	public static final BindRuleType DNS = new BindRuleType("dns", DNS_VALUE);

	public static final BindRuleType DAYOFWEEK = new BindRuleType("dayofweek", DAYOFWEEK_VALUE);

	public static final BindRuleType TIMEOFDAY = new BindRuleType("timeofday", TIMEOFDAY_VALUE);

	public static final BindRuleType AUTHMETHOD = new BindRuleType("authmethod", AUTHMETHOD_VALUE);
	
	private BindRuleType(String name, int value) {
		super(name, value);
	}

	public static BindRuleType getEnum(String target) {
		return (BindRuleType) getEnum(BindRuleType.class, target);
	}

	public static BindRuleType getEnum(int target) {
		return (BindRuleType) getEnum(BindRuleType.class, target);
	}

	public static Map getEnumMap() {
		return getEnumMap(BindRuleType.class);
	}

	public static List getEnumList() {
		return getEnumList(BindRuleType.class);
	}

	public static Iterator iterator() {
		return iterator(BindRuleType.class);
	}

}
