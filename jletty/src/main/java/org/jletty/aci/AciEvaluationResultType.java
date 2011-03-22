package org.jletty.aci;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

public class AciEvaluationResultType extends ValuedEnum {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int ALLOW_VALUE = 100;
	public static final int DENY_VALUE = 200;
	public static final int UNSPECIFIED_VALUE = 300;
	
	public static final AciEvaluationResultType ALLOW = new AciEvaluationResultType("allow",ALLOW_VALUE);
	public static final AciEvaluationResultType DENY = new AciEvaluationResultType("deny",DENY_VALUE);
	public static final AciEvaluationResultType UNSPECIFIED = new AciEvaluationResultType("unspecified",UNSPECIFIED_VALUE);
	
	private AciEvaluationResultType(String name, int value) {
		super( name, value );
	}

	public static AciEvaluationResultType getEnum(String javaVersion) {
		return (AciEvaluationResultType) getEnum(AciEvaluationResultType.class, javaVersion);
	}

	public static AciEvaluationResultType getEnum(int javaVersion) {
		return (AciEvaluationResultType) getEnum(AciEvaluationResultType.class, javaVersion);
	}

	public static Map getEnumMap() {
		return getEnumMap(AciEvaluationResultType.class);
	}

	public static List getEnumList() {
		return getEnumList(AciEvaluationResultType.class);
	}

	public static Iterator iterator() {
		return iterator(AciEvaluationResultType.class);
	}


}
