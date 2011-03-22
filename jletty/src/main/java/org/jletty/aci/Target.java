package org.jletty.aci;

import javax.naming.InvalidNameException;
import javax.naming.Name;

import org.jletty.dn.Rfc2253NameParserImpl;

public class Target {

	private TargetType type;

	private String target;

	private TargetEqualsType equals;

	private Object targetObject;

	public TargetEqualsType getEquals() {
		return this.equals;
	}

	public void setEquals(TargetEqualsType equals) {
		this.equals = equals;
	}

	public TargetType getType() {
		return this.type;
	}

	public void setType(TargetType tt) {
		this.type = tt;
	}

	public String getTarget() {
		return this.target;
	}
	
	public Object getTargetAsObject() {
		return this.targetObject;
	}

	public void setTarget(final String t) {
		try {
			if (this.type.equals(TargetType.TARGET)) {
				if (!t.startsWith("ldap:///")) {
					throw new IllegalArgumentException("invalid target \"" + t
							+ "\" it must begin with ldap://");
				}
				String t2 = t.substring(8);
				Name name = new Rfc2253NameParserImpl().parse(t2);
				this.targetObject = name;
			}
			this.target = t;
		} catch (InvalidNameException e) {
			throw new IllegalArgumentException("invalid name: ("+t+")",e);
		}		
	}

	public String toString() {
		return type.toString()+equals.toString()+target;
	}
	

}
