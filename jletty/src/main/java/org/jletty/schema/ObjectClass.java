/*
 * Created on 24-ene-2005
 *
 */
package org.jletty.schema;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

/**
 * @author Ruben
 * 
 */
public class ObjectClass {

	private String numoid;

	private String[] names;

	private String[] superclasses;

	private boolean isObsolete;

	private ObjectClassType type;

	private String[] mustAttrs;

	private String desc;

	private String[] mayAttrs;

	/**
	 * @param numoid
	 * @param names
	 * @param desc
	 * @param superclass
	 * @param isObsolete
	 * @param structural
	 * @param mustOids
	 */
	public ObjectClass(String numoid, String[] names, String desc,
			String[] superclasses, boolean isObsolete, ObjectClassType type,
			String[] mustOids, String[] mayOids) {
		if (null == numoid) {
			throw new IllegalArgumentException("numoid cannot be null");
		}
		this.numoid = numoid;
		if (null == names) {
			throw new IllegalArgumentException("names cannot be null");
		}
		this.names = names.clone();
		this.desc = desc;
		if (null == superclasses) {
			this.superclasses = new String[0];
		} else {
			this.superclasses = superclasses.clone();
		}
		this.isObsolete = isObsolete;
		if (null == type) {
			throw new IllegalArgumentException("type cannot be null");
		}
		this.type = type;
		if (null == mustOids) {
			this.mustAttrs = new String[0];
		} else {
			this.mustAttrs = mustOids.clone();
		}
		if (null == mayOids) {
			this.mayAttrs = new String[0];
		} else {
			this.mayAttrs = mayOids.clone();
		}

	}

	/**
	 * @return
	 */
	public String getNumericoid() {
		return this.numoid;
	}

	/**
	 * @return
	 */
	public String[] getNames() {

		return this.names.clone();
	}

	/**
	 * @return
	 */
	public String getDesc() {

		return this.desc;
	}

	/**
	 * @return
	 */
	public boolean isObsolete() {

		return this.isObsolete;
	}

	/**
	 * @return
	 */
	public String[] getSuperClasses() {
		return this.superclasses.clone();
	}

	/**
	 * @return
	 */
	public ObjectClassType getType() {

		return this.type;
	}

	/**
	 * @return
	 */
	public String[] getMustAttrs() {
		return this.mustAttrs.clone();
	}

	/**
	 * @return
	 */
	public String[] getMayAttrs() {
		return this.mayAttrs.clone();
	}

	public boolean isAllowed(String attrName) {
		if (ArrayUtils.contains(getMustAttrs(), attrName)) {
			return true;
		}
		if (ArrayUtils.contains(getMayAttrs(), attrName)) {
			return true;
		}
		return false;
	}

	public String toString() {
		return "[ObjectClass:" + this.names[0] + "]";
	}

	public void inherit(ObjectClass superClass) {
		// TODO check if there is something more to inherit
		{
			List myMustAttrs = Arrays.asList(this.mustAttrs);
			List superMustAttrs = Arrays.asList(superClass.mustAttrs);
			HashSet tmpSet = new HashSet();
			tmpSet.addAll(myMustAttrs);
			tmpSet.addAll(superMustAttrs);
			this.mustAttrs = (String[]) tmpSet.toArray(new String[tmpSet.size()]);
		}

		{
			List myMayAttrs = Arrays.asList(this.mayAttrs);
			List superMayAttrs = Arrays.asList(superClass.mayAttrs);
			HashSet tmpSet = new HashSet();
			tmpSet.addAll(myMayAttrs);
			tmpSet.addAll(superMayAttrs);
			this.mayAttrs = (String[]) tmpSet.toArray(new String[tmpSet.size()]);
		}

	}

}
