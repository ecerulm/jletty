/* Don delete it's not generated */
package org.jletty.aci;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

public class BindRuleEqualsType extends ValuedEnum {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int EQUALS_VALUE = 100;

	public static final int NEQUALS_VALUE = 200;

	public static final BindRuleEqualsType EQUALS = new BindRuleEqualsType("=",
			EQUALS_VALUE);

	public static final BindRuleEqualsType NEQUALS = new BindRuleEqualsType(
			"!=", NEQUALS_VALUE);

	private BindRuleEqualsType(String name, int value) {
		super(name, value);
	}

	public static BindRuleEqualsType getEnum(String target) {
		return (BindRuleEqualsType) getEnum(BindRuleEqualsType.class, target);
	}

	public static BindRuleEqualsType getEnum(int target) {
		return (BindRuleEqualsType) getEnum(BindRuleEqualsType.class, target);
	}

	public static Map getEnumMap() {
		return getEnumMap(BindRuleEqualsType.class);
	}

	public static List getEnumList() {
		return getEnumList(BindRuleEqualsType.class);
	}

	public static Iterator iterator() {
		return iterator(BindRuleEqualsType.class);
	}

}
