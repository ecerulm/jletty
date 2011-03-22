package org.jletty.dn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class DnNameComponent {

	/**
	 * List of {@link DnAtom}s
	 */
	private List<DnAtom> atoms = new ArrayList<DnAtom>();

	public void add(DnAtom attrTaV) {
		if (attrTaV == null) {
			throw new IllegalArgumentException("attrTaV cannot be null");
		}
		this.atoms.add(attrTaV);
	}

	public List<DnAtom> getAtoms() {
		return new ArrayList<DnAtom>(this.atoms);
	}

	/**
	 * String representation of this object. Note that this string
	 * representation takes care of escaping special characters as specified by
	 * RFC 1779 A String Representation of Distinguished Names. To obtain
	 * unescaped string representation use
	 * {@link DnNameComponent#asUnescapedString}
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		// TODO add a cache of the string representation
		StringBuffer sb = new StringBuffer();

		Iterator iterator = this.atoms.iterator();
		while (iterator.hasNext()) {
			sb.append(iterator.next().toString());
			if (iterator.hasNext()) {
				sb.append("+");
			}
		}
		return sb.toString();
	}

	public String asUnescapedString() {
		// TODO add a cache of the string representation
		StringBuffer sb = new StringBuffer();

		Iterator iterator = this.atoms.iterator();
		while (iterator.hasNext()) {
			sb.append(((DnAtom) iterator.next()).asUnescapedString());
			if (iterator.hasNext()) {
				sb.append("+");
			}
		}
		return sb.toString();

	}

	public boolean equals(final Object other) {
		if (!(other instanceof DnNameComponent)) {
			return false;
		}
		DnNameComponent castOther = (DnNameComponent) other;
		return new EqualsBuilder().append(this.atoms, castOther.atoms).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.atoms).toHashCode();
	}

}
