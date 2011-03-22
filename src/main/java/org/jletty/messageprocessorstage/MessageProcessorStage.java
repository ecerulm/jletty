package org.jletty.messageprocessorstage;

import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.log4j.Logger;
import org.jletty.dn.DistinguishedName;
import org.jletty.dn.DnAtom;
import org.jletty.dn.DnNameComponent;
import org.jletty.dn.Rfc2253NameParserImpl;
import org.jletty.framework.BaseStage;
import org.jletty.framework.MaintenanceEvent;
import org.jletty.framework.Stage;
import org.jletty.jndiprovider.LdapAttribute;
import org.jletty.jndiprovider.LdapAttributes;
import org.jletty.jndiprovider.LdapEntryImpl;
import org.jletty.ldapstackldapops.AbandonRequest;
import org.jletty.ldapstackldapops.AddRequest;
import org.jletty.ldapstackldapops.AddResponse;
import org.jletty.ldapstackldapops.AttributeTypeAndValues;
import org.jletty.ldapstackldapops.AttributeTypeAndValuesList;
import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.BindRequest;
import org.jletty.ldapstackldapops.BindResponse;
import org.jletty.ldapstackldapops.CompareRequest;
import org.jletty.ldapstackldapops.CompareResponse;
import org.jletty.ldapstackldapops.DelResponse;
import org.jletty.ldapstackldapops.DeleteRequest;
import org.jletty.ldapstackldapops.Filter;
import org.jletty.ldapstackldapops.LDAPMessage;
import org.jletty.ldapstackldapops.LDAPOperation;
import org.jletty.ldapstackldapops.LDAPRequest;
import org.jletty.ldapstackldapops.LDAPResponse;
import org.jletty.ldapstackldapops.LDAPResultCode;
import org.jletty.ldapstackldapops.LdapConstraintViolationException;
import org.jletty.ldapstackldapops.LdapEntry;
import org.jletty.ldapstackldapops.LdapEntryAlreadyExistsException;
import org.jletty.ldapstackldapops.LdapException;
import org.jletty.ldapstackldapops.LdapInvalidAttributeSyntaxException;
import org.jletty.ldapstackldapops.LdapInvalidDnSyntaxException;
import org.jletty.ldapstackldapops.LdapNoSuchObjectException;
import org.jletty.ldapstackldapops.LdapNotAllowedOnNonLeafException;
import org.jletty.ldapstackldapops.LdapNotAllowedOnRdnException;
import org.jletty.ldapstackldapops.LdapObjectClassViolationException;
import org.jletty.ldapstackldapops.Modification;
import org.jletty.ldapstackldapops.ModificationType;
import org.jletty.ldapstackldapops.ModifyRequest;
import org.jletty.ldapstackldapops.ModifyResponse;
import org.jletty.ldapstackldapops.Scope;
import org.jletty.ldapstackldapops.SearchRequest;
import org.jletty.ldapstackldapops.SearchResultDone;
import org.jletty.ldapstackldapops.SearchResultEntry;
import org.jletty.schema.AttributeType;
import org.jletty.schema.MatchResult;
import org.jletty.schema.MatchRule;
import org.jletty.schema.Matchable;
import org.jletty.schema.ObjectClass;
import org.jletty.schema.OctetString;
import org.jletty.schema.OctetStringable;
import org.jletty.schema.Schema;
import org.jletty.schema.SchemaUtils;
import org.jletty.schema.Syntax;
import org.jletty.schema.Syntaxes;
import org.jletty.util.StringUtil;

/*
 * Created on 12-oct-2004
 *
 */

/**
 * @author Ruben
 * 
 */
public class MessageProcessorStage extends BaseStage implements Stage,
		MessageProcessor {
	// TODO remove dependencies with javax.naming.directory

	// private DirContext rootCtx;
	private Map entries = new HashMap();

	public MessageProcessorStage() {

	}

	public MessageProcessorStage(Map backend) {
		entries = backend;
	}

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(MessageProcessorStage.class);

	private static final Logger opLogger = Logger.getLogger("ldapoperations");

	private boolean schemaCheckingEnabled;

	private NameParser nameParser = new Rfc2253NameParserImpl();

	private void process(ResponseHandler handler, AddRequest ar)
			throws NamingException {
		opLogger.debug("Received AddRequest " + ar);

		String entryDn = ar.getEntryDn();
		AttributeTypeAndValuesList attrList = ar.getAttrList();

		// check that no operational attributes are in the list
		// TODO move this check to checkAddPreconditions
		checkForOperationalAttributes(attrList);

		// Canonicalize the dn
		DistinguishedName name = (DistinguishedName) nameParser.parse(entryDn);

		// Check that the entry isn't already in the database
		if (entries.containsKey(name)) {
			// the entry is already in the database
			LdapEntryAlreadyExistsException toThrow = new LdapEntryAlreadyExistsException();
			// TODO remove dead code
			// toThrow.setResolvedName(name);
			throw toThrow;
		}
		// check that the parent exists
		Name parentDn = name.getPrefix(name.size() - 1);
		if ((parentDn.size() > 0) && (!entries.containsKey(parentDn))) {
			LdapNoSuchObjectException ldapNoSuchObjectException = new LdapNoSuchObjectException();
			// FIXME find the resolvedname iterate over name prefixes to see if
			// there is one that is entries.
			Name resolvedName = getMatchedDn(parentDn);
			ldapNoSuchObjectException.setResolvedName(resolvedName);
			throw ldapNoSuchObjectException;
		}

		// create an ldapentry
		LdapAttributes attributes = SchemaUtils
				.convertAttrTypeAndValsToAttributes(attrList);
		LdapEntryImpl entry = new LdapEntryImpl(name, attributes);

		// checkAddpreconditions
		checkSchemaAddPreconditions(entry);

		// add the ldapentry to entries Map
		entries.put(name, entry);

		handler.sendResponse(new AddResponse(LDAPResultCode.SUCCESS));
		opLogger.info("Entry added " + name);
		opLogger.debug("AddResponse sent");

	}

	private Name getMatchedDn(Name parentDn) {
		int i = parentDn.size() - 1;
		Name resolvedName = null;
		if (i > 0) {
			for (; i > 0; i--) {
				Name tmp = parentDn.getPrefix(i);
				if (entries.containsKey(tmp)) {
					resolvedName = tmp;
					break;
				}
			}
		}
		return resolvedName;
	}

	private void process(ResponseHandler handler, BindRequest br)
			throws NamingException {
		BindResponse response = new BindResponse(
				LDAPResultCode.INVALID_CREDENTIALS);
		try {
			opLogger.debug("Received BindRequest " + br);

			final String inputDn = br.getLdapDN();
			String inputPw = StringUtil.byteArrayToString("UTF-8", br
					.getPasswd());
			if ("cn=Directory Manager".equals(inputDn)) {
				if ("secret".equals(inputPw)) {
					response = new BindResponse(LDAPResultCode.SUCCESS);
				} else {
					response = new BindResponse(
							LDAPResultCode.INVALID_CREDENTIALS);
				}
			} else {
				// TODO retrieve the entry
				// get the userPassword attribute
				// compare using PasswordUtils
				Name inputDnAsName = nameParser.parse(inputDn);
				LdapEntry entry = (LdapEntry) entries.get(inputDnAsName);
				if (entry == null) {
					throw new LdapNoSuchObjectException();
				}

				LdapAttribute userPassword = entry.getAttributes().get(
						"userPassword");
				Collection upValues = userPassword.getAll();
				for (Iterator iter = upValues.iterator(); iter.hasNext();) {
					OctetString pwOctetString = (OctetString) iter.next();
					String pwString = StringUtil.byteArrayToString("UTF-8",
							pwOctetString.getContents());
					if (PasswordUtil.verifyPassword(pwString, inputPw)) {
						response = new BindResponse(LDAPResultCode.SUCCESS);
						break;
					}
				}
			}

		} catch (LdapNoSuchObjectException e) {
			response = new BindResponse(LDAPResultCode.INVALID_CREDENTIALS);
		} catch (CharacterCodingException e) {
			response = new BindResponse(LDAPResultCode.INVALID_CREDENTIALS);
		}
		handler.sendResponse(response);

		opLogger.debug("BindResponse sent!");

	}

	private void process(ResponseHandler handler, SearchRequest sr)
			throws NamingException {
		opLogger.debug("Received SearchRequest " + sr);
		// TODO add a test to honor timeLimit
		// TODO add a test to honor sizeLimit
		// TODO add a test to honor typesonly
		// TODO add a test to honor attrslist
		long startTime = System.currentTimeMillis();
		long timeLimit = sr.getTimeLimit() * 1000;
		if (timeLimit == 0) {
			timeLimit = 100 * 1000; // TODO default timeLimit
		}
		int sizeLimit = sr.getSizeLimit();
		if (sizeLimit == 0) {
			sizeLimit = 5000; // TODO default sizeLimit
		}
		boolean typesOnly = sr.isTypesOnly();
		DistinguishedName baseDn = (DistinguishedName) nameParser.parse(sr
				.getBaseDn());
		Collection requestedAttrs = sr.getAttributes();
		final Filter filter = sr.getFilter();

		// TODO: Optimize this
		Collection matchingEntries = new ArrayList();
		Iterator iterator = entries.values().iterator();
		while (iterator.hasNext() && (matchingEntries.size() < sizeLimit)) {
			LdapEntry entry = (LdapEntry) iterator.next();
			DistinguishedName entryDn = entry.getName();
			switch (sr.getScope().getValue()) {
			case Scope.BASE_OBJECT_VALUE:
				if (!entryDn.equals(baseDn)) {
					continue; // next entry
				}
				break;
			case Scope.ONE_LEVEL_VALUE:
				if (!entryDn.startsWith(baseDn)
						|| ((entryDn.size() - baseDn.size()) != 1)) {
					continue; // next entry
				}
				break;
			case Scope.WHOLE_SUBTREE_VALUE:
				if (!entryDn.startsWith(baseDn)) {
					continue; // next entry
				}
				break;
			}
			if (MatchResult.TRUE.equals(filter.match(entry))) {
				matchingEntries.add(entry);
			}
		}
		// TODO get rid of matchingEntries call handler.sendResponse inside the
		// above loop
		Iterator matchesIterator = matchingEntries.iterator();
		while (matchesIterator.hasNext()) {
			LdapEntry entry = (LdapEntry) matchesIterator.next();

			AttributeTypeAndValuesList attrlist = getEntryContents(entry,
					typesOnly, requestedAttrs);
			String entryName = entry.getName().toString();
			final SearchResultEntry searchResultEntry = new SearchResultEntry(
					entryName, attrlist);
			opLogger.debug("Sending entry " + entryName + " with contents "
					+ searchResultEntry);
			handler.sendResponse(searchResultEntry);
		}
		opLogger.debug("End processing search request " + sr);
		// TODO Move this check inside the main loop to send time_limit_exceed
		// as quick as possible
		if ((timeLimit != 0)
				&& (System.currentTimeMillis() - startTime) > timeLimit) {
			handler.sendResponse(new SearchResultDone(
					LDAPResultCode.TIME_LIMIT_EXCEEDED));
		} else {
			handler.sendResponse(new SearchResultDone(LDAPResultCode.SUCCESS));
		}

	}

	private void process(ResponseHandler handler, CompareRequest cr)
			throws NamingException {
		opLogger.debug("Received CompareRequest " + cr);

		DistinguishedName baseDn = (DistinguishedName) nameParser.parse(cr
				.getBaseDn());
		final String attrName = cr.getAttrName();
		final Schema schema = Schema.getInstance();
		final AttributeType attributeType = schema.getAttributeType(attrName);
		if (null == attributeType) {
			handler.sendResponse(new CompareResponse(
					LDAPResultCode.COMPARE_FALSE));
			return;
		}
		String eqMatchRuleName = attributeType.getEqMatchRule();
		MatchRule matchRule = MatchRule.getEnum(eqMatchRuleName);
		String syntaxName = attributeType.getSyntax();
		Syntax syntax = Syntaxes.getSyntax(syntaxName);

		LdapEntry entry = (LdapEntry) entries.get(baseDn);
		LdapAttributes attributes = entry.getAttributes();
		LdapAttribute attribute = attributes.get(attrName);
		try {
			Iterator allValues = attribute.getAll().iterator();
			while (allValues.hasNext()) {
				Object value = allValues.next();
				Matchable matchable = (Matchable) value;
				MatchResult b = matchable.eqMatch(matchRule, syntax.get(cr
						.getAttrValue()));
				if (MatchResult.TRUE.equals(b)) {
					handler.sendResponse(new CompareResponse(
							LDAPResultCode.COMPARE_TRUE));
					return;
				}
			}
		} catch (LdapInvalidAttributeSyntaxException e) {
			// TODO is worth the trouble to rework the exception
			// should we remove the whole catch block -- probably yes
			LdapInvalidAttributeSyntaxException rethrowExp = new LdapInvalidAttributeSyntaxException(
					"invalid attribute syntax for attribute '" + attrName + "'",
					e.getCause());
			rethrowExp.setValueNumber(e.getValueNumber());
			throw rethrowExp;
		}
		handler.sendResponse(new CompareResponse(LDAPResultCode.COMPARE_FALSE));

	}

	private void process(ResponseHandler handler, DeleteRequest deleteReq)
			throws NamingException {
		final DistinguishedName dnToDelete = (DistinguishedName) nameParser
				.parse(deleteReq.getDn());
		Iterator iterator = entries.keySet().iterator();
		while (iterator.hasNext()) {
			DistinguishedName dn = (DistinguishedName) iterator.next();
			if (!dn.equals(dnToDelete) && dn.startsWith(dnToDelete)) {
				throw new LdapNotAllowedOnNonLeafException();
			}
		}
		// TODO add a test for non existent entry
		if (!entries.containsKey(dnToDelete)) {
			LdapNoSuchObjectException ldapNoSuchObjectException = new LdapNoSuchObjectException();
			Name matchedDn = getMatchedDn(dnToDelete);
			ldapNoSuchObjectException.setResolvedName(matchedDn);
			throw ldapNoSuchObjectException;
		}
		entries.remove(dnToDelete);
		handler.sendResponse(new DelResponse(LDAPResultCode.SUCCESS));
	}

	private void process(ResponseHandler handler, ModifyRequest modifyReq)
			throws LdapException {

		// TODO add a test for non existent entry

		// get entry to modify
		// DirContext entryToModify = (DirContext) rootCtx.lookup(modifyReq
		// .getDn());

		DistinguishedName dn;
		try {
			dn = (DistinguishedName) nameParser.parse(modifyReq.getDn());
		} catch (InvalidNameException e1) {
			throw new LdapInvalidDnSyntaxException();
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
		LdapEntry entryToModify = (LdapEntry) entries.get(dn);

		Modification[] modificationList = modifyReq.getModificationList();
		ModificationItem[] mods = new ModificationItem[modificationList.length];
		for (int i = 0; i < modificationList.length; i++) {
			Modification mod = modificationList[i];
			final String attrName = mod.getAttrName();
			ModificationType type = mod.getType();

			// get the attribute values in the modification as Matchables
			Matchable[] modValuesAsMatchables = getModValuesAsMatchables(mod);

			// TODO replace with a switch case statement
			if (ModificationType.ADD.equals(type)) {
				mods[i] = modifyAdd(attrName, modValuesAsMatchables);
			} else if (ModificationType.DELETE.equals(type)) {
				final Attribute attrToDelete = new BasicAttribute(attrName);
				for (int j = 0; j < modValuesAsMatchables.length; j++) {
					attrToDelete.add(modValuesAsMatchables[j]);
				}
				mods[i] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE,
						attrToDelete);
			} else if (ModificationType.REPLACE.equals(type)) {
				/*
				 * replace: replace all existing values of the given attribute
				 * with the new values listed, creating the attribute if it did
				 * not already exist. A replace with no value will delete the
				 * entire attribute if it exists, and is ignored if the
				 * attribute does not exist.
				 */
				final Attribute tmpAttribute = new BasicAttribute(attrName);
				for (int j = 0; j < modValuesAsMatchables.length; j++) {
					tmpAttribute.add(modValuesAsMatchables[j]);
				}
				mods[i] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
						tmpAttribute);
			}
		}

		try {
			checkSchemaContraints(entryToModify, mods);
		} catch (LdapInvalidDnSyntaxException e) {
			throw new LdapNotAllowedOnRdnException(e.getMessage()
					+ ": this will lead to rdn modification");
		}
		entryToModify.modify(mods);
		handler.sendResponse(new ModifyResponse(LDAPResultCode.SUCCESS));

	}

	private void checkSchemaContraints(LdapEntry entry, ModificationItem[] mods)
			throws LdapException {
		LdapEntry ldapEntry = entry.copy();
		ldapEntry.modify(mods);
		if (!schemaCheckingEnabled) {
			return;
		}
		checkRequiredAttrs(ldapEntry);
		checkNotAllowedAttributes(ldapEntry);
		checkSingleValueAttributes(ldapEntry);

		checkRdnAttributes(ldapEntry);
	}

	private Matchable[] getModValuesAsMatchables(Modification mod)
			throws LdapInvalidAttributeSyntaxException {
		ModificationType type = mod.getType();
		final String attrName = mod.getAttrName();
		byte[][] tmpValues = mod.getValues();
		AttributeType attributeType = Schema.getInstance().getAttributeType(
				attrName);
		Syntax syntax = Syntaxes.getSyntax(attributeType.getSyntax());

		Matchable[] valuesMod = new Matchable[tmpValues.length];
		for (int j = 0; j < tmpValues.length; j++) {
			try {
				valuesMod[j] = syntax.get(tmpValues[j]);
			} catch (LdapInvalidAttributeSyntaxException e) {
				String modtype = getModTypeString(type);
				throw new LdapInvalidAttributeSyntaxException("modify/"
						+ modtype + ": " + attrName + ": value #" + j
						+ " invalid per syntax");
			}
		}
		return valuesMod;
	}

	private void checkSchemaAddPreconditions(LdapEntry e)
			throws NamingException {
		if (!schemaCheckingEnabled)
			return;

		LdapEntry ldapEntry = e;

		checkRequiredAttrs(ldapEntry);

		checkNotAllowedAttributes(ldapEntry);

		checkRdnAttributes(ldapEntry);

	}

	private void checkRdnAttributes(LdapEntry ldapEntry) throws LdapException {
		// TODO move this method to LdapEntryImpl
		DistinguishedName name = ldapEntry.getName();
		LdapAttributes attrs = ldapEntry.getAttributes();
		DnNameComponent rdn = name.getLastDnNameComponent();
		List atoms = rdn.getAtoms();
		for (Iterator iter = atoms.iterator(); iter.hasNext();) {
			DnAtom atom = (DnAtom) iter.next();
			checkIsContainedInDnAttrs(atom, attrs);
		}
	}

	/**
	 * Check if there is an operational attribute in the list. The current
	 * operational attributes that are not allowed are
	 * <code>creatorsName, createTimeStamp, modifierName, modifyTimeStamp</code>
	 * and <code>subschemaEntry</code>
	 * 
	 * @param attrList
	 * @throws NamingException
	 */
	private void checkForOperationalAttributes(
			AttributeTypeAndValuesList attrList) throws NamingException {
		String[] operationalAttributes = new String[] { "creatorsName",
				"createTimeStamp", "modifiersName", "modifyTimeStamp",
				"subschemaSubentry" };
		for (int i = 0; i < operationalAttributes.length; i++) {
			final String operationalAttrName = operationalAttributes[i];
			if (attrList.get(operationalAttrName) != null) {
				opLogger
						.info(operationalAttrName
								+ " attribute specified but no user modification allowed on this attribute");
				String attrName = attrList.get(operationalAttrName)
						.getAttributeDescription();
				throw new LdapConstraintViolationException(attrName
						+ ": no user modification allowed");
			}
		}
	}

	/**
	 * Check that <code>attrs</code> contains all required attributes of
	 * <code>ob2</code> .
	 * 
	 * @param attrs
	 *            the attributes to check
	 * @param requiredAttributes
	 *            the list of attributes that must be present in attrs
	 * @param objectclassesNames
	 *            the objectclassnames. this in only used to report the
	 *            objectclass name in the event of an error
	 * @throws LdapObjectClassViolationException
	 */
	private void checkRequiredAttrs(LdapEntry entry) throws LdapException {
		// TODO move this method to ldapentry
		LdapAttributes entryAttrs = entry.getAttributes();
		List requiredAttrs = getRequiredAttrs(entry);

		for (Iterator iter = requiredAttrs.iterator(); iter.hasNext();) {
			final String attrName = (String) iter.next();
			if (entryAttrs.get(attrName) == null) {
				LdapAttribute ocAttr = entryAttrs.get("objectclass");
				Collection ocValues = ocAttr.getAll();
				throw new LdapObjectClassViolationException("object class '"
						+ ocValues + "' requires attribute '" + attrName + "'");
			}
		}

	}

	private List getRequiredAttrs(LdapEntry entry) throws LdapException {
		// TODO should be this moved to ldap entry - probably yes
		LdapAttributes entryAttrs = entry.getAttributes();
		LdapAttribute objectclassAttr = entryAttrs.get("objectclass"); // objectclass
		if (objectclassAttr == null) {
			throw new LdapConstraintViolationException(
					"entry has no objectClass attribute");
		}

		List requiredAttrs = new ArrayList();

		Collection allObjectclasses = objectclassAttr.getAll();
		for (Iterator iter = allObjectclasses.iterator(); iter.hasNext();) {
			final String objectclassName = iter.next().toString();
			// objeclassvalue is of type Oid
			ObjectClass ob2 = Schema.getInstance().getObjectClass(
					objectclassName);
			requiredAttrs.addAll(Arrays.asList(ob2.getMustAttrs()));
		}
		return requiredAttrs;
	}

	private List getMayAttrs(LdapEntry entry) throws LdapException {
		// TODO should be this moved to ldap entry - probably yes
		LdapAttributes entryAttrs = entry.getAttributes();
		if (entryAttrs.get("objectclass") == null) {
			throw new LdapConstraintViolationException(
					"entry has no objectClass attribute");
		}
		LdapAttribute objectclassAttr = entryAttrs.get("objectclass"); // objectclass

		List mayAttrs = new ArrayList();

		Collection allObjectclasses = objectclassAttr.getAll();
		for (Iterator iter = allObjectclasses.iterator(); iter.hasNext();) {
			final String objectclassName = iter.next().toString();
			// objeclassvalue is of type Oid
			ObjectClass ob2 = Schema.getInstance().getObjectClass(
					objectclassName);
			mayAttrs.addAll(Arrays.asList(ob2.getMayAttrs()));
		}
		return mayAttrs;
	}

	private void checkNotAllowedAttributes(LdapEntry entry)
			throws LdapException {
		List requiredAttrs = getRequiredAttrs(entry);
		List mayAttrs = getMayAttrs(entry);

		Collection allowedAttrs = CollectionUtils
				.union(requiredAttrs, mayAttrs);
		allowedAttrs.add("objectClass");
		// TODO make this a final class member to avoid creating a new
		// Transformer every time
		final Transformer toLowercaseTransformer = new Transformer() {
			public Object transform(Object input) {
				return ((String) input).toLowerCase();
			}
		};
		CollectionUtils.transform(allowedAttrs, toLowercaseTransformer);
		LdapAttributes attrs = entry.getAttributes();
		Collection presentAttrs = attrs.getIDs();
		CollectionUtils.transform(presentAttrs, toLowercaseTransformer);

		if (!CollectionUtils.isSubCollection(presentAttrs, allowedAttrs)) {
			StringBuffer buffer = new StringBuffer();

			Collection notAllowedAttrs = CollectionUtils.subtract(presentAttrs,
					allowedAttrs);
			for (Iterator iter = notAllowedAttrs.iterator(); iter.hasNext();) {
				final Object tmp = iter.next();
				String attrName = (String) tmp;
				buffer.append("'" + attrName + "' ");
			}

			throw new LdapObjectClassViolationException("attribute " + buffer
					+ "not allowed by objectclasses '"
					+ attrs.get("objectclass").getAll() + "'");
		}
	}

	private void checkSingleValueAttributes(LdapEntry entry)
			throws LdapException {
		Schema schema = Schema.getInstance();
		LdapAttributes attributes = entry.getAttributes();
		Collection attributesList = attributes.asCollection();
		for (Iterator iter = attributesList.iterator(); iter.hasNext();) {
			LdapAttribute attr = (LdapAttribute) iter.next();
			String attrName = attr.getID();
			// check if the attribute allows multiple values
			final AttributeType attributeType = schema
					.getAttributeType(attrName);
			if (attributeType.isSingleValue()) {
				if (attr.size() > 1) {
					throw new LdapConstraintViolationException("attribute '"
							+ attrName + "' cannot have multiple values");
				}
			}

		}

	}

	private void checkIsContainedInDnAttrs(DnAtom atom, LdapAttributes attrs)
			throws LdapException {
		final String attrName = atom.getAttributeType();
		final Object attrValue = atom.getAttributeValue();
		final LdapAttribute attrToCompare = attrs.get(attrName);
		if ((null == attrToCompare) || (!attrToCompare.contains(attrValue))) {
			final LdapInvalidDnSyntaxException exp = new LdapInvalidDnSyntaxException(
					"DN should contain attribute '" + attrName + "'");
			exp.setResolvedName(new DistinguishedName());
			throw exp;
		}
	}

	private Attributes convertOidToNames(Attributes attrs) {
		final Schema schema = Schema.getInstance();

		List attrList = new ArrayList(attrs.size());
		CollectionUtils.addAll(attrList, attrs.getAll());
		Transformer theTransformer = new Transformer() {
			public Object transform(Object input) {
				Attribute attr = (Attribute) input;
				Attribute toReturn = attr;
				AttributeType attributeType = schema.getAttributeType(attr
						.getID());
				if (attributeType != null) {
					try {
						toReturn = new BasicAttribute(attributeType.getName());
						NamingEnumeration all = attr.getAll();
						while (all.hasMoreElements()) {
							toReturn.add(all.nextElement());
						}
					} catch (NamingException e) {
						toReturn = attr;
					}
				}
				return toReturn;
			}
		};
		CollectionUtils.transform(attrList, theTransformer);

		final Attributes toReturn = new BasicAttributes(true);
		CollectionUtils.forAllDo(attrList, new Closure() {
			public void execute(Object input) {
				toReturn.put((Attribute) input);
			}
		});

		return toReturn;

	}

	private String getModTypeString(ModificationType type) {
		String modtype;
		// TODO replace with switch/case statement
		if (ModificationType.ADD.equals(type)) {
			modtype = "add";
		} else if (ModificationType.REPLACE.equals(type)) {
			modtype = "replace";
		} else if (ModificationType.DELETE.equals(type)) {
			modtype = "delete";
		} else {
			throw new RuntimeException("Unknown modification type: " + type);
		}
		return modtype;
	}

	private ModificationItem modifyAdd(String attrName, Matchable[] matchables) {

		final BasicAttribute tmpAttribute = new BasicAttribute(attrName);
		for (int j = 0; j < matchables.length; j++) {
			tmpAttribute.add(matchables[j]);
		}
		return new ModificationItem(DirContext.ADD_ATTRIBUTE, tmpAttribute);
	}

	private void process(ResponseHandler handler, AbandonRequest modifyReq) {
		// TODO add abandon logic
	}

	/**
	 * @param scope
	 * @param cons
	 */
	private SearchControls createSearchControls(final SearchRequest sr) {
		SearchControls toReturn = new SearchControls();
		// set scope
		switch (sr.getScope().getValue()) {
		case Scope.BASE_OBJECT_VALUE:
			toReturn.setSearchScope(SearchControls.OBJECT_SCOPE);
			break;
		case Scope.ONE_LEVEL_VALUE:
			toReturn.setSearchScope(SearchControls.ONELEVEL_SCOPE);
			break;
		case Scope.WHOLE_SUBTREE_VALUE:
			toReturn.setSearchScope(SearchControls.SUBTREE_SCOPE);
			break;

		default:
			throw new RuntimeException("Unknown scope type " + sr.getScope());
		}
		// set size limit
		int sizeLimit = sr.getSizeLimit();
		if (sizeLimit == 0)
			sizeLimit = 5000; // hard limit of 5000 entries to return
		toReturn.setCountLimit(sizeLimit);
		int timeLimit = sr.getTimeLimit();
		if (timeLimit == 0)
			timeLimit = 60;
		toReturn.setTimeLimit(timeLimit);
		return toReturn;
	}

	/**
	 * @param entry
	 * @param typesOnly
	 * @return
	 * @throws NamingException
	 */
	private AttributeTypeAndValuesList getEntryContents(LdapEntry entry,
			boolean typesOnly, Collection requestedAttrs)
			throws NamingException {
		Iterator attrsIterator = entry.getAttributes().asCollection()
				.iterator();
		AttributeTypeAndValuesList attrlist = new AttributeTypeAndValuesList();
		while (attrsIterator.hasNext()) {
			LdapAttribute attr = (LdapAttribute) attrsIterator.next();
			final boolean attrShouldBeIncluded = requestedAttrs.isEmpty()
					|| requestedAttrs.contains("*")
					|| requestedAttrs.contains(attr.getID());
			if (attrShouldBeIncluded) {
				byte[][] tmp = getValuesAsByteArrayArray(typesOnly, attr);

				AttributeTypeAndValues attrTypeAndValues = new AttributeTypeAndValues(
						attr.getID(), tmp);
				attrlist.add(attrTypeAndValues);
			}
		}
		return attrlist;
	}

	private byte[][] getValuesAsByteArrayArray(boolean typesOnly,
			LdapAttribute attr) throws NamingException {
		byte[][] tmp = null;

		if (typesOnly == false) {
			Iterator valuesIterator = attr.getAll().iterator();
			List theValues = new ArrayList();
			while (valuesIterator.hasNext()) {
				Object value = valuesIterator.next();
				if (value instanceof OctetStringable) {
					OctetStringable value2 = (OctetStringable) value;
					theValues.add(value2.getContents());
				} else {
					throw new RuntimeException("value " + value
							+ " should implement OctetStringable but was "
							+ value.getClass());
				}
			}
			tmp = (byte[][]) theValues.toArray(new byte[theValues.size()][]);
		}
		return tmp;
	}

	public void enqueue(Object obj) {
		logger.debug("Entering enqueue");
		EventForMessageProcessor ev = (EventForMessageProcessor) obj;
		ev.visit(this);
		logger.debug("Exiting enqueue");
	}

	public void process(LdapMessageEvent event) {

		LDAPMessage mes = event.getMessage();
		ResponseHandler handler = event.getResponseHandler();
		LDAPOperation operation = mes.getLdapOperation();

		if (!(operation instanceof LDAPRequest)) {
			throw new RuntimeException("Expecting an LdapRequest but was "
					+ operation.getClass());
		}
		LDAPRequest requestOp = (LDAPRequest) operation;

		try {
			try {
				dispatchToSpecificProcessMethod(handler, requestOp);

			} catch (NameAlreadyBoundException e) {
				throw new LdapEntryAlreadyExistsException("");
			} catch (NameNotFoundException e) {
				final LdapNoSuchObjectException noSuchObjectException = new LdapNoSuchObjectException();
				noSuchObjectException.setResolvedName(e.getResolvedName());
				throw noSuchObjectException;
			}

		} catch (LdapException e) {
			dispatchErrorResponse(handler, requestOp, e);
		} catch (NamingException e) {
			// TODO send error code
			opLogger.error("Could not complete addrequest", e);
			throw new RuntimeException(e);
		}

	}

	private void dispatchErrorResponse(ResponseHandler handler,
			LDAPRequest requestOp, LdapException e) {
		final Name resolvedName = e.getResolvedName();
		final String resolvedNameString = (resolvedName != null) ? resolvedName
				.toString() : "";

		LDAPResponse toResponse = null;
		switch (requestOp.getTag()) {
		// dispatch to appropiate method
		case BerTags.APPLICATION_0: // bind request
			toResponse = new BindResponse(e.getResultCode(),
					resolvedNameString, e.getMessage());
			break;
		case BerTags.APPLICATION_8: // add request
			toResponse = new AddResponse(e.getResultCode(), resolvedNameString,
					e.getMessage());
			break;
		case BerTags.APPLICATION_3: // search request
			toResponse = new SearchResultDone(e.getResultCode(),
					resolvedNameString, e.getMessage());
			break;
		case BerTags.APPLICATION_10: // delete request
			toResponse = new DelResponse(e.getResultCode(), resolvedNameString,
					e.getMessage());
			break;
		case BerTags.APPLICATION_6: // delete request
			toResponse = new ModifyResponse(e.getResultCode(),
					resolvedNameString, e.getMessage());
			break;
		case BerTags.APPLICATION_14: // delete request
			toResponse = new CompareResponse(e.getResultCode(),
					resolvedNameString, e.getMessage());
			break;
		case BerTags.APPLICATION_16: // abandon request
			// no response even in error case
			break;
		default:
			final RuntimeException runtimeException = new RuntimeException(
					"Unknown operation " + requestOp + "in message ");
			logger.error("unknown operation", runtimeException);
			throw runtimeException;

		}
		if (null != toResponse) {
			handler.sendResponse(toResponse);
		}
	}

	private void dispatchToSpecificProcessMethod(ResponseHandler handler,
			LDAPRequest requestOp) throws NamingException {
		switch (requestOp.getTag()) {
		// dispatch to appropiate method
		case BerTags.APPLICATION_0: // bind request
			process(handler, (BindRequest) requestOp);
			break;
		case BerTags.APPLICATION_8: // add request
			process(handler, (AddRequest) requestOp);
			break;
		case BerTags.APPLICATION_3: // search request
			process(handler, (SearchRequest) requestOp);
			break;
		case BerTags.APPLICATION_10: // delete request
			process(handler, (DeleteRequest) requestOp);
			break;
		case BerTags.APPLICATION_6: // Modify request
			process(handler, (ModifyRequest) requestOp);
			break;
		case BerTags.APPLICATION_14: // Compare request
			process(handler, (CompareRequest) requestOp);
			break;
		case BerTags.APPLICATION_16: // abandon request
			process(handler, (AbandonRequest) requestOp);
			break;
		default:
			final RuntimeException runtimeException = new RuntimeException(
					"Unknown operation " + requestOp + " with tag "
							+ requestOp.getTag() + " in message ");
			logger.error("unknown operation", runtimeException);
			throw runtimeException;

		}
	}

	public void process(MaintenanceEvent event) {
		logger.debug("Maintenance process");
	}

	public void enableSchemaChecking() {
		schemaCheckingEnabled = true;
	}

	public void setNameParser(NameParser parser) {
		this.nameParser = parser;

	}

}
