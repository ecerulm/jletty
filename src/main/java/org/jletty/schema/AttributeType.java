/*
 * Created on 07-ene-2005
 *
 */
package org.jletty.schema;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Ruben
 * 
 */
public class AttributeType {

	private String numericoid;

	private String[] names;

	private String desc;

	private String eqMatchRule;

	private String syntax;

	private boolean isSingleValue = false;

	private String subMatchRule;

	private String ordMatchRule;

	private boolean isCollective;

	private boolean nonUserModificable;

	private AttributeUsage usage;

	private boolean obsolete;

	private String superAttrName;

	private int length = -1;

	public String toString() {
		return new ToStringBuilder(this).append("numericoid", this.numericoid)
				.append("names", this.names).append("desc", this.desc).append(
						"eqMatchRule", this.eqMatchRule).append("syntax", this.syntax)
				.append("isSingleValue", this.isSingleValue).append("subMatchRule",
						this.subMatchRule).append("ordMatchRule", this.ordMatchRule)
				.append("isCollective", this.isCollective).append(
						"nonUserModificable", this.nonUserModificable).append(
						"usage", this.usage).append("obsolete", this.obsolete).append(
						"superAttrName", this.superAttrName)
				.append("length", this.length).toString();
	}

	// AttributeTypeDescription = "(" whsp
	// numericoid whsp ; AttributeType identifier
	// [ "NAME" qdescrs ] ; name used in AttributeType
	// [ "DESC" qdstring ] ; description
	// [ "OBSOLETE" whsp ]
	// [ "SUP" woid ] ; derived from this other
	// ; AttributeType
	// [ "EQUALITY" woid ; Matching Rule name
	// [ "ORDERING" woid ; Matching Rule name
	// [ "SUBSTR" woid ] ; Matching Rule name
	// [ "SYNTAX" whsp noidlen whsp ] ; see section 4.3
	// [ "SINGLE-VALUE" whsp ] ; default multi-valued
	// [ "COLLECTIVE" whsp ] ; default not collective
	// [ "NO-USER-MODIFICATION" whsp ]; default user modifiable
	// [ "USAGE" whsp AttributeUsage ]; default userApplications
	// whsp ")"
	//
	// AttributeUsage =
	// "userApplications" /
	// "directoryOperation" /
	// "distributedOperation" / ; DSA-shared
	// "dSAOperation" ; DSA-specific, value depends on server

	/**
	 * <p>
	 * Class for AttributeTypeDescription.
	 * </p>
	 * 
	 * <p>
	 * AttributeTypeDescription is defined in RFC2252: Lightweight Directory
	 * Access Protocol (v3): Attribute Syntax Definitions
	 * </p>
	 * 
	 * <pre>
	 *    
	 *     
	 *      
	 *       
	 *        
	 *         
	 *          
	 *           	AttributeTypeDescription = &quot;(&quot; whsp
	 *             numericoid whsp ; AttributeType identifier
	 *             [ &quot;NAME&quot; qdescrs ] ; name used in AttributeType
	 *             [ &quot;DESC&quot; qdstring ] ; description
	 *             [ &quot;OBSOLETE&quot; whsp ]
	 *             [ &quot;SUP&quot; woid ] ; derived from this other ; AttributeType
	 *             [ &quot;EQUALITY&quot; woid ; Matching Rule name
	 *             [ &quot;ORDERING&quot; woid ; Matching Rule name
	 *             [ &quot;SUBSTR&quot; woid ] ; Matching Rule name
	 *             [ &quot;SYNTAX&quot; whsp noidlen whsp ] ; see section 4.3
	 *             [ &quot;SINGLE-VALUE&quot; whsp ] ; default multi-valued
	 *             [ &quot;COLLECTIVE&quot; whsp ] ; default not collective
	 *             [ &quot;NO-USER-MODIFICATION&quot; whsp ]; default user modifiable
	 *             [ &quot;USAGE&quot; whsp AttributeUsage ]; default userApplications
	 *             whsp &quot;)&quot;
	 *          
	 *            AttributeUsage =
	 *            &quot;userApplications&quot; /
	 *            &quot;directoryOperation&quot; /
	 *            &quot;distributedOperation&quot; / ; DSA-shared
	 *            &quot;dSAOperation&quot; ; DSA-specific, value depends on server
	 *           
	 *          
	 *         
	 *        
	 *       
	 *      
	 *     
	 * </pre>
	 * 
	 * @param oid
	 *            the numericoid whsp ; AttributeType identifier
	 * @param names
	 * @param desc
	 * @param obsolete
	 * @param sup
	 * @param eqMatchRule
	 * @param ordMatchRule
	 * @param subMatchRule
	 * @param syntax
	 * @param len
	 * @param isSingleValue
	 * @param isCollective
	 * @param nonUserModificable
	 * @param usage
	 */
	public AttributeType(String oid, String[] names, String desc,
			boolean obsolete, String sup, String eqMatchRule,
			String ordMatchRule, String subMatchRule, String syntax, int len,
			boolean isSingleValue, boolean isCollective,
			boolean nonUserModificable, AttributeUsage usage) {
		this.numericoid = oid;
		this.names = names.clone();
		this.desc = desc;
		this.obsolete = obsolete;
		this.superAttrName = sup;
		this.eqMatchRule = eqMatchRule;
		this.ordMatchRule = ordMatchRule;
		this.subMatchRule = subMatchRule;
		this.syntax = syntax;
		this.isSingleValue = isSingleValue;
		this.isCollective = isCollective;
		this.nonUserModificable = nonUserModificable;
		this.usage = usage;
		this.length = len;
	}

	public String getNumericoid() {
		return this.numericoid;
	}

	public String getName() {
		return getNames()[0];
	}

	public String[] getNames() {
		return this.names.clone();
	}

	public List getNamesAsList() {
		return Arrays.asList(getNames());
	}

	public String getDesc() {
		return this.desc;
	}

	public String getEqMatchRule() {
		return this.eqMatchRule;
	}

	public boolean isSingleValue() {
		return this.isSingleValue;
	}

	public String getSubMatchRule() {
		return this.subMatchRule;
	}

	public String getSyntax() {
		return this.syntax;
	}

	public void setSyntax(String syntax) {
		this.syntax = syntax;
	}

	public boolean isCollective() {
		return this.isCollective;
	}

	public boolean isNonUserModificable() {
		return this.nonUserModificable;
	}

	public AttributeUsage getUsage() {
		return this.usage;
	}

	public boolean isObsolete() {
		return this.obsolete;
	}

	public String getSuperAttr() {
		return this.superAttrName;
	}

	public String getOrdMatchRule() {
		return this.ordMatchRule;
	}

	/**
	 * @param text
	 * @param names2
	 * @param text2
	 * @param b
	 * @return
	 */
	public AttributeType derive(String newNumericoid, String[] newNames,
			String newDesc, boolean newObsolete) {
		return new AttributeType(newNumericoid, newNames, newDesc, newObsolete,
				this.names[0], this.eqMatchRule, this.ordMatchRule, this.subMatchRule, this.syntax,
				this.length, this.isSingleValue, this.isCollective, this.nonUserModificable, this.usage);
	}

	/**
	 * @param eqMatchRule2
	 * @return
	 */
	public AttributeType overrideEqMatchRule(String newEqMatchRule) {
		return new AttributeType(this.numericoid, this.names, this.desc, this.obsolete,
				this.superAttrName, newEqMatchRule, this.ordMatchRule, this.subMatchRule,
				this.syntax, this.length, this.isSingleValue, this.isCollective,
				this.nonUserModificable, this.usage);
	}

	/**
	 * @param ordMatchRule2
	 * @return
	 */
	public AttributeType overrideOrdMatchRule(String newOrdMatchRule) {
		return new AttributeType(this.numericoid, this.names, this.desc, this.obsolete,
				this.superAttrName, this.eqMatchRule, newOrdMatchRule, this.subMatchRule,
				this.syntax, this.length, this.isSingleValue, this.isCollective,
				this.nonUserModificable, this.usage);
	}

	/**
	 * @param subMatchRule2
	 * @return
	 */
	public AttributeType overrideSubMatchRule(String newSubMatchRule) {
		return new AttributeType(this.numericoid, this.names, this.desc, this.obsolete,
				this.superAttrName, this.eqMatchRule, this.ordMatchRule, newSubMatchRule,
				this.syntax, this.length, this.isSingleValue, this.isCollective,
				this.nonUserModificable, this.usage);
	}

	/**
	 * @param syntaxS
	 * @return
	 */
	public AttributeType overrideSyntax(String newSyntax) {
		return new AttributeType(this.numericoid, this.names, this.desc, this.obsolete,
				this.superAttrName, this.eqMatchRule, this.ordMatchRule, this.subMatchRule,
				newSyntax, this.length, this.isSingleValue, this.isCollective,
				this.nonUserModificable, this.usage);
	}

	/**
	 * @param isSingleValue2
	 * @return
	 */
	public AttributeType overrideSingleValue(boolean newIsSingleValue) {
		return new AttributeType(this.numericoid, this.names, this.desc, this.obsolete,
				this.superAttrName, this.eqMatchRule, this.ordMatchRule, this.subMatchRule, this.syntax,
				this.length, newIsSingleValue, this.isCollective, this.nonUserModificable,
				this.usage);
	}

	/**
	 * @param superAttr2
	 */
	public void inherit(AttributeType superAttr2) {
		if ("".equals(this.eqMatchRule)) {
			this.eqMatchRule = superAttr2.eqMatchRule;
		}
		if ("".equals(this.subMatchRule)) {
			this.subMatchRule = superAttr2.subMatchRule;
		}
		if ("".equals(this.ordMatchRule)) {
			this.ordMatchRule = superAttr2.ordMatchRule;
		}
		if ("".equals(this.syntax)) {
			this.syntax = superAttr2.syntax;
		}
		if (this.length == -1) {
			this.length = superAttr2.length;
		}

	}

	/**
	 * @return
	 */
	public int getLength() {
		return this.length;
	}
}
