package org.jletty.dn;

import java.util.Iterator;
import java.util.List;

public class DnMatcher {
	private DnMatcher() {
		//To avoid instatiation
	}
	public static boolean match(DistinguishedName arg1, DistinguishedName arg2) {
		if (arg1.equals(arg2)) {
			return true;
		}
		//		List<DnNameComponent> arg1dncomps = arg1.getAllDnNameComponentsAsList();
//		for (Iterator<DnNameComponent> it = arg1dncomps.iterator(); it.hasNext();) {
//			DnNameComponent comp = it.next();
//			
//			
//			
//		}
		final DnNameComponent lastDnNameComponent = arg1.getLastDnNameComponent();
		DnAtom atom = lastDnNameComponent.getAtoms().get(0);
		String value = atom.getAttributeValue();
		if ( "*".equals( value)) {
			return true;
		}
		
		return false; 
	}
}
