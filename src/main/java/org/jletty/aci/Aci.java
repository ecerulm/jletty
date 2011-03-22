/* Don delete it's not generated */
package org.jletty.aci;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.apache.log4j.Logger;
import org.jletty.dn.DistinguishedName;
import org.jletty.dn.DnMatcher;

public class Aci {
	private static final Logger log = Logger.getLogger(Aci.class);

	private List<Target> targets = new ArrayList<Target>();

	private TargetVersion version;

	private String name;

	private Permission[] permissions;

	public Target[] getTargets() {
		return this.targets.toArray(new Target[this.targets.size()]);
	}

	public TargetVersion getVersion() {
		return this.version;
	}

	public String getName() {
		return this.name;
	}

	public Permission[] getPermissions() {
		return this.permissions;
	}

	public void addTarget(Target t) {
		this.targets.add(t);
	}

	public void setVersion(TargetVersion version) {
		this.version = version;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPermissions(Permission[] permissions) {
		this.permissions = permissions.clone();
	}

	public AciEvaluationResultType evaluate(final PermissionRight right,
			final AciContext context) {
		// lets see if the target match 
		final DistinguishedName entrydn = context.getEntry();
		for (Iterator it = targets.iterator(); it.hasNext();) {
			Target target = (Target) it.next();
			if (TargetType.TARGET.equals(target.getType())) {
				Object targetObject = target.getTargetAsObject();
				if (targetObject instanceof DistinguishedName) {
					DistinguishedName targetDN = (DistinguishedName)targetObject;
					if (DnMatcher.match(targetDN, entrydn)) {
						Permission permission = permissions[0];
						PermissionType type = permission.getType();
						PermissionRight[] rights = permission.getRights();

						for (int i = 0; i < rights.length; i++) {
							PermissionRight permRight = rights[i];
							if (permRight.equals(right)) {
								if (PermissionType.ALLOW.equals(type)) {
									return AciEvaluationResultType.ALLOW;
								} else if (PermissionType.DENY.equals(type)) {
									return AciEvaluationResultType.DENY;
								}
							}
						}
					}
				}				
			}
		}

		return AciEvaluationResultType.UNSPECIFIED;
	}
	
	

}
