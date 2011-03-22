package org.jletty.ldapstackldapops;

public class LdapUndefinedAttributeType extends LdapException {

	private static final long serialVersionUID = 1L;

	private String attrTypeName;

	public LdapUndefinedAttributeType(String attrTypeName) {
		super(attrTypeName + ": attribute type undefined",
				LDAPResultCode.UNDEF_ATTR_TYPE);
		this.attrTypeName = attrTypeName;
	}

	public String getAttrTypeName() {
		return this.attrTypeName;
	}

}
