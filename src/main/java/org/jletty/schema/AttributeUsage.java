/*
 * Created on 21-ene-2005
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
public final class AttributeUsage extends Enum {
	// AttributeUsage =
	// "userApplications" /
	// "directoryOperation" /
	// "distributedOperation" / ; DSA-shared
	// "dSAOperation" ; DSA-specific, value depends on server

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	public static final AttributeUsage USER_APPLICATIONS = new AttributeUsage(
			"userApplications");

	public static final AttributeUsage DIRECTORY_OPERATION = new AttributeUsage(
			"directoryOperation");

	public static final AttributeUsage DISTRIBUTED_OPERATION = new AttributeUsage(
			"distributedOperation");

	public static final AttributeUsage DSA_OPERATION = new AttributeUsage(
			"dSAOperation");

	private AttributeUsage(String usage) {
		super(usage);
	}

	public static AttributeUsage getEnum(String usage) {
		return (AttributeUsage) getEnum(AttributeUsage.class, usage);
	}

	public static Map getEnumMap() {
		return getEnumMap(AttributeUsage.class);
	}

	public static List getEnumList() {
		return getEnumList(AttributeUsage.class);
	}

	public static Iterator iterator() {
		return iterator(AttributeUsage.class);
	}

}
