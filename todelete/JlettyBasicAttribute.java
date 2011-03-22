package org.jletty.jndiprovider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.apache.commons.lang.builder.ToStringBuilder;

public class JlettyBasicAttribute implements Attribute {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private boolean isOrdered;

	private List values = new ArrayList();

	public JlettyBasicAttribute(String id, boolean ordered) {
		this.id = id;
		this.isOrdered = ordered;
	}

	public JlettyBasicAttribute(String id, Object value, boolean ordered) {
		this.id = id;
		this.isOrdered = ordered;
		values.add(value);
	}

	public JlettyBasicAttribute(String id, Object value) {
		this.id = id;
		this.isOrdered = false;
		values.add(value);
	}

	public JlettyBasicAttribute(String id) {
		this.id = id;
		this.isOrdered = false;
	}

	public int size() {
		return values.size();
	}

	public void clear() {
		values.clear();
	}

	public boolean isOrdered() {
		return isOrdered;
	}

	public Object clone() {
		JlettyBasicAttribute toReturn;
		try {
			toReturn = (JlettyBasicAttribute) super.clone();
		} catch (CloneNotSupportedException e) {
			toReturn = new JlettyBasicAttribute(id, isOrdered);
		}

		toReturn.values = new ArrayList(values);
		return toReturn;
	}

	public Object get() throws NamingException {
		if (values.size() > 0) {
			return values.get(0);
		} else {
			throw new NoSuchElementException();
		}
	}

	public Object get(int ix) throws NamingException {
		return values.get(ix);
	}

	public Object remove(int ix) {
		return values.remove(ix);
	}

	public void add(int ix, Object attrVal) {
		if (!isOrdered && contains(attrVal)) {
			throw new IllegalStateException();
		}
		values.add(ix, attrVal);
	}

	public boolean add(Object attrVal) {
		if (!isOrdered && values.contains(attrVal)) {
			return false;
		}
		return values.add(attrVal);
	}

	public boolean contains(final Object attrVal) {
		if (attrVal instanceof String) {
			return CollectionUtils.exists(values, new Predicate() {
				public boolean evaluate(Object object) {
					return attrVal.equals(object.toString());
				}
			});
		}
		if (attrVal.getClass().isArray()) {
			return CollectionUtils.exists(values, new Predicate() {
				public boolean evaluate(Object object) {
					if (!object.getClass().isArray()) {
						return false;
					}
					Object[] toCmp = (Object[]) object;
					return Arrays.equals(toCmp, (Object[]) attrVal);
				}
			});
		} else {
			return values.contains(attrVal);
		}
	}

	public boolean remove(Object attrval) {
		boolean isArray = attrval.getClass().isArray();
		Iterator iterator = values.iterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();
			if (isArray) {
				if (object.getClass().isArray()) {
					if (Arrays.equals((Object[]) object, (Object[]) attrval)) {
						iterator.remove();
						return true;
					}
				}
			} else {
				if (object.equals(attrval)) {
					iterator.remove();
					return true;
				}
			}
		}
		return false;
	}

	public String getID() {
		return id;
	}

	public NamingEnumeration getAll() throws NamingException {
		NamingEnumeration toReturn = new NamingEnumeration() {

			// private List backingList = new ArrayList(values);
			private Enumeration enumeration = new IteratorEnumeration(values
					.iterator());

			public void close() throws NamingException {
			}

			public boolean hasMore() throws NamingException {
				return enumeration.hasMoreElements();
			}

			public Object next() throws NamingException {
				return enumeration.nextElement();
			}

			public boolean hasMoreElements() {
				return enumeration.hasMoreElements();
			}

			public Object nextElement() {
				return enumeration.nextElement();
			}
		};
		return toReturn;
	}

	public DirContext getAttributeDefinition() throws NamingException {
		throw new OperationNotSupportedException();
	}

	public DirContext getAttributeSyntaxDefinition() throws NamingException {
		throw new OperationNotSupportedException();
	}

	public Object set(int ix, Object attrVal) {
		if (!isOrdered && contains(attrVal)) {
			throw new IllegalStateException();
		}
		return values.set(ix, attrVal);
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", id).append("values",
				values).toString();
	}

}
