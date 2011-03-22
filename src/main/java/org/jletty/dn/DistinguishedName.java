package org.jletty.dn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.naming.InvalidNameException;
import javax.naming.Name;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

//TODO remove implements Name
public final class DistinguishedName implements Name {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<DnNameComponent> rdnsAsDnNameComponents;

	class ListOfDnNameComponents {

	}

	public DistinguishedName() {
		this.rdnsAsDnNameComponents = new ArrayList<DnNameComponent>();
	}

	public DistinguishedName(String[] strings) throws InvalidNameException {
		this();
		for (int i = strings.length - 1; i >= 0; i--) {
			String string = strings[i];
			add(string);
		}
	}

	public int size() {
		return this.rdnsAsDnNameComponents.size();
	}

	public boolean isEmpty() {
		return this.rdnsAsDnNameComponents.isEmpty();
	}

	public Object remove(int posn) throws InvalidNameException {
		return this.rdnsAsDnNameComponents.remove(posn).toString();
	}

	public int compareTo(Object obj) {
		if (!(obj instanceof DistinguishedName)) {
			throw new ClassCastException();
		}
		DistinguishedName a = this;
		DistinguishedName b = (DistinguishedName) obj;

		for (int i = 0; i < Math.min(a.size(), b.size()); i++) {
			final String aString = a.get(i);
			final String bString = b.get(i);
			int compare = aString.compareTo(bString);
			if (compare != 0) {
				return compare;
			}
		}
		if (a.size() > b.size()) {
			return 1;
		}
		if (a.size() < b.size()) {
			return -1;
		}
		return 0;
	}

	public String get(int posn) {
		return this.rdnsAsDnNameComponents.get(posn)
				.asUnescapedString();
	}

	public Name add(int posn, String componentAsString)
			throws InvalidNameException {
		DnNameComponent component = new Rfc2253NameParserImpl()
				.parseDnNameComponent(componentAsString);
		add(posn, component);
		return this;
	}

	public Name add(String componentAsString) throws InvalidNameException {
		DnNameComponent component = new Rfc2253NameParserImpl()
				.parseDnNameComponent(componentAsString);
		add(component);
		return this;
	}

	public Name addAll(int posn, Name n) throws InvalidNameException {
		if (n instanceof DistinguishedName) {
			// optimization
			DistinguishedName source = (DistinguishedName) n;
			List<DnNameComponent> allDnNameComponentsAsList = new ArrayList<DnNameComponent>(source
					.getAllDnNameComponentsAsList());
			Iterator iterator = allDnNameComponentsAsList.iterator();
			int i = posn;
			while (iterator.hasNext()) {
				add(i++, (DnNameComponent) iterator.next());
			}
			return this;
		}

		Enumeration e = n.getAll();
		int i = posn;
		while (e.hasMoreElements()) {
			String compAsString = (String) e.nextElement();
			add(i++, compAsString);

		}
		return this;
	}

	public Name addAll(Name n) throws InvalidNameException {
		if (n instanceof DistinguishedName) {
			// optimization
			DistinguishedName source = (DistinguishedName) n;
			Iterator iterator = source.getAllDnNameComponentsAsList()
					.iterator();
			while (iterator.hasNext()) {
				add((DnNameComponent) iterator.next());
			}
			return this;
		}
		Enumeration e = n.getAll();
		while (e.hasMoreElements()) {
			String compAsString = (String) e.nextElement();
			add(compAsString);
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	public Enumeration<String> getAll() {
		Collection<String> asStrings = CollectionUtils.collect(this.rdnsAsDnNameComponents,
				new Transformer() {
					public Object transform(Object arg0) {
						return arg0.toString();
					}
				});
		return Collections.enumeration(asStrings);
	}

	public Name getPrefix(int posn) {
		if (posn > size()) {
			throw new ArrayIndexOutOfBoundsException();
		}
		DistinguishedName toReturn = new DistinguishedName();
		Iterator<DnNameComponent> iterator = this.rdnsAsDnNameComponents.subList(0, posn).iterator();
		while (iterator.hasNext()) {
			toReturn.add(iterator.next());
		}
		return toReturn;
	}

	public Name getSuffix(int posn) {
		DistinguishedName toReturn = new DistinguishedName();
		int size = this.rdnsAsDnNameComponents.size();
		if (posn > size || posn < 0) {
			// FIXME illegal argument handling
			throw new RuntimeException();
		}
		Iterator<DnNameComponent> iterator = this.rdnsAsDnNameComponents.subList(posn, size)
				.iterator();
		while (iterator.hasNext()) {
			toReturn.add(iterator.next());
		}
		return toReturn;
	}

	public boolean endsWith(Name n) {
		int posn = size() - n.size();
		if (posn < 0) {
			// if the given name it's bigger than this name it's imposible that
			// this name could end in the given name
			return false;
		}
		final Name suffix = getSuffix(posn);
		return suffix.equals(n);
	}

	public boolean startsWith(Name n) {
		if (n.size() > this.size()) {
			return false;
		}
		final Name prefix = getPrefix(n.size());
		return prefix.equals(n);
	}

	public void add(DnNameComponent component) {
		this.rdnsAsDnNameComponents.add(component);
	}

	public void add(int posn, DnNameComponent component) {
		this.rdnsAsDnNameComponents.add(posn, component);
	}

	public Enumeration<DnNameComponent> getAllDnNameComponents() {
		return Collections.enumeration(this.rdnsAsDnNameComponents);
	}

	public List<DnNameComponent> getAllDnNameComponentsAsList() {
		return Collections.unmodifiableList(this.rdnsAsDnNameComponents);
	}

	public DnNameComponent getDnNameComponent(int i) {
		// no need to clone it because it's inmmutable
		return this.rdnsAsDnNameComponents.get(i);
	}

	/**
	 * Get the rdn component (the leftmost).
	 * 
	 * @return the leftmost DnNameComponent
	 */
	public DnNameComponent getLastDnNameComponent() {
		// no need to clone it because it's inmmutable
		return this.rdnsAsDnNameComponents
				.get(this.rdnsAsDnNameComponents.size() - 1);
	}

	public final Object clone() {
		try {
			DistinguishedName toReturn = (DistinguishedName) super.clone();
			toReturn.rdnsAsDnNameComponents = new LinkedList();
			toReturn.addAll(this);
			return toReturn;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		} catch (InvalidNameException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean equals(final Object other) {
		if (!(other instanceof DistinguishedName)) {
			return false;
		}
		DistinguishedName castOther = (DistinguishedName) other;
		return new EqualsBuilder().append(this.rdnsAsDnNameComponents,
				castOther.rdnsAsDnNameComponents).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.rdnsAsDnNameComponents)
				.toHashCode();
	}

	public String toString() {
		// TODO cache toString representation. Is it worth?
		StringBuffer sb = new StringBuffer();
		List<DnNameComponent> tmp = new LinkedList<DnNameComponent>(this.rdnsAsDnNameComponents);
		Collections.reverse(tmp);
		for (Iterator<DnNameComponent> iter = tmp.iterator(); iter.hasNext();) {
			String element = iter.next().toString();
			sb.append(element);
			if (iter.hasNext()) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

}
