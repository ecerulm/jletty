/*
 * Created on 24-ene-2005
 *
 */
package org.jletty.schema;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

/**
 * @author Ruben
 * 
 */
public class ObjectClassType extends Enum {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3256719589433227064L;

	public static final ObjectClassType STRUCTURAL = new ObjectClassType(
			"STRUCTURAL");

	public static final ObjectClassType AUXILIARY = new ObjectClassType(
			"AUXILIARY");

	public static final ObjectClassType ABSTRACT = new ObjectClassType(
			"ABSTRACT");

	private ObjectClassType(String type) {
		super(type);
	}

	public static AttributeUsage getEnum(String type) {
		return (AttributeUsage) getEnum(ObjectClassType.class, type);
	}

	public static Map getEnumMap() {
		return getEnumMap(ObjectClassType.class);
	}

	public static List getEnumList() {
		return getEnumList(ObjectClassType.class);
	}

	public static Iterator iterator() {
		return iterator(ObjectClassType.class);
	}

}
