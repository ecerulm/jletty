package org.jletty.schema;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class MatchResult extends Enum {

	private static final long serialVersionUID = -5901339549476143773L;

	public static final MatchResult TRUE = new MatchResult("True");

	public static final MatchResult FALSE = new MatchResult("False");

	public static final MatchResult UNDEF = new MatchResult("Undefined");

	private MatchResult(String name) {
		super(name);
	}

	public static MatchResult getEnum(String name) {
		return (MatchResult) getEnum(MatchResult.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(MatchResult.class);
	}

	public static List getEnumList() {
		return getEnumList(MatchResult.class);
	}

	public static Iterator iterator() {
		return iterator(MatchResult.class);
	}

}
