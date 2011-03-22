package org.jletty.jndiprovider;

import java.util.Collection;
import java.util.Iterator;

import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.jletty.dn.DistinguishedName;
import org.jletty.ldapstackldapops.LdapAttrOrValuesExistsException;
import org.jletty.ldapstackldapops.LdapEntry;
import org.jletty.ldapstackldapops.LdapException;
import org.jletty.ldapstackldapops.LdapInappropiateMatchingException;
import org.jletty.ldapstackldapops.LdapNoSuchAttributeException;
import org.jletty.schema.AttributeValue;
import org.jletty.schema.MatchResult;
import org.jletty.schema.MatchRule;
import org.jletty.schema.Matchable;
import org.jletty.schema.Schema;

//TODO remove links to javax.naming.directory. like Attribute and Modification
public class LdapEntryImpl implements LdapEntry {

	private DistinguishedName name;

	private LdapAttributes attributes;

	public LdapEntryImpl(DistinguishedName name, LdapAttributes attributes) {
		this.name = name;
		this.attributes = attributes;
	}

	public LdapAttributes getAttributes() {
		return this.attributes;
	}

	public DistinguishedName getName() {
		return this.name;
	}

	public void modify(ModificationItem[] mods) throws LdapException {
		// TODO why we recieve ModificationItem, we should receive
		// another type of object to decouple this impl from
		// DirContext api
		for (int i = 0; i < mods.length; i++) {
			ModificationItem mod = mods[i];
			final LdapAttribute inputAttr = new LdapAttribute(mod
					.getAttribute());
			switch (mod.getModificationOp()) {
			case DirContext.ADD_ATTRIBUTE:
				addAttribute(inputAttr);
				break;
			case DirContext.REMOVE_ATTRIBUTE:
				deleteAttribute(inputAttr);
				break;

			case DirContext.REPLACE_ATTRIBUTE:
				replaceAttribute(inputAttr);
				break;
			default:
				throw new RuntimeException("Unknown modification type "
						+ mod.getModificationOp());
			}
		}

	}

	private void addAttribute(LdapAttribute inputAttr) throws LdapException {
		// TODO we should not use Attribute it's better to use another type that
		// always returns matchables

		final String attrName = inputAttr.getID();
		LdapAttribute currentAttr = this.attributes.get(attrName);
		if (null == currentAttr) {
			// create one and add it to attributes
			currentAttr = new LdapAttribute(attrName);
			this.attributes.put(currentAttr);
		}
		final Schema schema = Schema.getInstance();
		final String eqMatchRuleName = schema.getAttributeType(attrName)
				.getEqMatchRule();
		if ("".equals(eqMatchRuleName) || null == eqMatchRuleName) {
			throw new LdapInappropiateMatchingException("modify/add" + ": "
					+ attrName + ": no equality matching rule");
		}
		final MatchRule eqMatchRule = MatchRule.getEnum(eqMatchRuleName);

		Collection currentAttrValues = currentAttr.getAll();

		// add modification values to the attribute
		Collection inputAttrVals = inputAttr.getAll();
		int i = 0;
		for (Iterator iter = inputAttrVals.iterator(); iter.hasNext();) {
			final AttributeValue inputVal = (AttributeValue) iter.next();
			boolean alreadyExists = CollectionUtils.exists(currentAttrValues,
					new Predicate() {
						public boolean evaluate(Object object) {
							if (object instanceof Matchable) {
								Matchable matchableObject = (Matchable) object;

								return MatchResult.TRUE.equals(inputVal
										.eqMatch(eqMatchRule, matchableObject));
							} else {
								// if it not a matchable, then it's a error all
								throw new RuntimeException(
										"The attribute value " + object
												+ " of attribute " + attrName
												+ " is not a Matchable");
							}

						}

					});
			if (alreadyExists) {
				throw new LdapAttrOrValuesExistsException("modify/add: "
						+ attrName + ": value #" + i + " already exists");
			}
			currentAttr.add(inputVal);
			i++;
		}

	}

	private void deleteAttribute(LdapAttribute inputAttr) throws LdapException {
		final String attrName = inputAttr.getID();
		final Schema schema = Schema.getInstance();
		final String eqMatchRuleName = schema.getAttributeType(attrName)
				.getEqMatchRule();
		if ("".equals(eqMatchRuleName) || null == eqMatchRuleName) {
			throw new LdapInappropiateMatchingException("modify/delete" + ": "
					+ attrName + ": no equality matching rule");
		}
		LdapAttribute currentAttr = this.attributes.get(attrName);
		if (null == currentAttr) {
			// we cannot delete an attribute that
			// is not even present on the entry
			throw new LdapNoSuchAttributeException("modify/delete: " + attrName
					+ ": no such attribute");
		}
		Collection inputAttrVals = inputAttr.getAll();

		// check that the attribute value exists
		Collection currentAttrValues = currentAttr.getAll();
		// iterate over the values to delete
		for (Iterator iter = inputAttrVals.iterator(); iter.hasNext();) {
			final AttributeValue currentAttrVal = (AttributeValue) iter.next();
			boolean alreadyExists = CollectionUtils.exists(currentAttrValues,
					new Predicate() {
						public boolean evaluate(Object object) {
							return MatchResult.TRUE.equals(currentAttrVal
									.eqMatch(
											MatchRule.getEnum(eqMatchRuleName),
											(Matchable) object));
						}
					});
			if (!alreadyExists) {
				// we cannot delete an attribute value
				// that is not contained in the entry
				throw new LdapNoSuchAttributeException("modify/delete: "
						+ attrName + ": no such value");
			}

		}

		// delete modification values from the attribute
		if (inputAttrVals.size() == 0) {
			this.attributes.remove(attrName);
		} else {
			for (Iterator iter = inputAttrVals.iterator(); iter.hasNext();) {
				AttributeValue val = (AttributeValue) iter.next();
				currentAttr.remove(val);
				if (currentAttr.size() == 0) {
					this.attributes.remove(attrName);
				}
			}
		}

	}

	private void replaceAttribute(LdapAttribute inputAttr) throws LdapException {
		if (inputAttr.size() == 0) {
			// replace with empty attr mean delete whole attr
			this.attributes.remove(inputAttr.getID());
		} else {
			this.attributes.put(inputAttr);
		}
	}

	public LdapEntry copy() {
		return new LdapEntryImpl((DistinguishedName) this.name.clone(), this.attributes
				.copy());
	}

}
