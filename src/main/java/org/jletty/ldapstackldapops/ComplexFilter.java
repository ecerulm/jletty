/*
 * Created on 04-ene-2005
 *
 */
package org.jletty.ldapstackldapops;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Ruben
 * 
 */
public abstract class ComplexFilter {

	// protected Filter[] operands;

	protected List setOfFilters;

	/**
	 * @param setOfFilters
	 */
	public ComplexFilter(Filter[] setOfFilters) {
		this.setOfFilters = Arrays.asList(setOfFilters.clone());
	}

	public Collection getChildren() {
		return Collections.unmodifiableCollection(this.setOfFilters);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		sb.append(getOperatorString());
		for (Iterator iter = this.setOfFilters.iterator(); iter.hasNext();) {
			Filter element = (Filter) iter.next();
			sb.append(element.toString());
		}
		sb.append(")");
		return sb.toString();
	}

	protected abstract String getOperatorString();
}
