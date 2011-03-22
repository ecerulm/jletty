/*
 * $Id: Style.java,v 1.2 2006/02/27 18:23:01 ecerulm Exp $ Created on May 23, 2004
 */
package org.jletty.util;

import org.apache.commons.lang.builder.StandardToStringStyle;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author $Author: ecerulm $
 * 
 */
public class Style {
	/*
	 * private static StandardToStringStyle defaultStyle = new
	 * StandardToStringStyle(); public static final ToStringStyle DEFAULT_STYLE =
	 * defaultStyle; static { defaultStyle.setUseShortClassName(true); }
	 */
	private static Style theInstance = null;

	public final StandardToStringStyle defaultStyle;

	private Style() {
		super();
		this.defaultStyle = new StandardToStringStyle();
		this.defaultStyle.setUseShortClassName(true);
	}

	private static Style getInstance() {
		if (theInstance == null) {
			theInstance = new Style();
		}
		return theInstance;
	}

	public static ToStringStyle getDefaultStyle() {
		return getInstance().defaultStyle;
	}

}