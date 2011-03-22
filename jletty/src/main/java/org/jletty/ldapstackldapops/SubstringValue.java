/*
 * Created on 09-dic-2004
 *
 */
package org.jletty.ldapstackldapops;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * @author Ruben
 * 
 */
public class SubstringValue {

	private String initial;

	private List any;

	private String fin;

	/**
	 * @param initial
	 * @param any
	 * @param fin
	 */
	public SubstringValue(String initial, String[] any, String fin) {
		this.initial = StringEscapeUtils.unescapeJava(initial);
		if (any != null) {
			this.any = Arrays.asList(any);
		} else {
			this.any = new ArrayList();
		}
		this.fin = StringEscapeUtils.unescapeJava(fin);

	}

	public SubstringValue(String initial, List any, String fin) {
		this.initial = initial;
		if (any != null) {
			this.any = any;
		} else {
			this.any = new ArrayList();
		}
		this.fin = fin;

	}

	/**
	 * @return
	 */
	public String getInitial() {
		return this.initial;
	}

	/**
	 * @return
	 */
	public List getAny() {
		// return (String[]) any.clone();
		return this.any;
	}

	public String getFinal() {
		return this.fin;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		if (this.initial != null) {
			sb.append(this.initial);
		}
		sb.append("*");

		for (Iterator iter = this.any.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			sb.append(element + "*");
		}

		if (this.fin != null) {
			sb.append(this.fin);
		}
		return sb.toString();
	}
}
