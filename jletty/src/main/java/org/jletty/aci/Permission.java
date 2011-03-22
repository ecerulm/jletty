/* Don delete it's not generated */
package org.jletty.aci;

import java.util.HashSet;
import java.util.Set;

public class Permission {

	private PermissionType type;

	private Set<PermissionRight> rights = new HashSet<PermissionRight>();

	private BindRule bindRule;

	public PermissionType getType() {
		return this.type;
	}

	public PermissionRight[] getRights() {
		return this.rights.toArray(new PermissionRight[this.rights
				.size()]);
	}

	public BindRule getBindRule() {
		return this.bindRule;
	}

	public void addRight(PermissionRight right) {
		this.rights.add(right);
	}

	public void setType(PermissionType type) {
		this.type = type;
	}

	public void setBindRule(BindRule bindRule) {
		this.bindRule = bindRule;
	}

}
