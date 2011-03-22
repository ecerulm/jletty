/*
 * Created on 08-dic-2004
 *
 */
package org.jletty.jndiprovider;

import java.util.Hashtable;
import java.util.Vector;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;
import org.jletty.dn.DistinguishedName;
import org.jletty.filterparser.JlettyFilterParser;
import org.jletty.ldapstackldapops.Filter;
import org.jletty.ldapstackldapops.LdapEntry;
import org.jletty.schema.MatchResult;

/**
 * @author Ruben
 */
public class JlettyHierCtx extends HierDirCtx {

	static final Logger logger = Logger.getLogger(JlettyHierCtx.class);

	/**
	 * @param inEnv
	 */
	public JlettyHierCtx(Hashtable inEnv) {
		super(inEnv);
	}

	/**
	 * @param parent
	 * @param name
	 * @param inEnv
	 * @param bindings
	 * @param myAttrs
	 * @param bindingAttrs
	 */
	public JlettyHierCtx(HierCtx parent, String name, Hashtable inEnv,
			Hashtable bindings, Attributes myAttrs, Hashtable bindingAttrs) {
		super(parent, name, inEnv, bindings, myAttrs, bindingAttrs);
	}

	public NamingEnumeration search(Name name, String f, SearchControls cons)
			throws NamingException {
		long startTime = System.currentTimeMillis();
		if (cons == null) {
			logger
					.debug("No search controls specified using SUBTREE_SCOPE as default");
			cons = new SearchControls();
			cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
			cons.setCountLimit(5000); // hard limit of 5000
			cons.setTimeLimit(60); // hard limit of 60
		}
		// Vector for storing answers
		Vector answer = new Vector();
		Binding item;

		Filter filter = JlettyFilterParser.parse(f);

		// Get context
		Context target = (Context) lookup(name);

		try {
			// List context
			NamingEnumeration objs = target.listBindings("");
			int entriesToReturn = 0;
			long sizeLimit = cons.getCountLimit();
			long timeLimit = cons.getTimeLimit() * 1000;
			if (SearchControls.OBJECT_SCOPE == cons.getSearchScope()
					|| SearchControls.SUBTREE_SCOPE == cons.getSearchScope()) {
				// Check if this object matches
				if (target instanceof DirContext) {
					DirContext currentCtx = (DirContext) target;
					LdapEntry e = createLdapEntry(currentCtx);
					LdapAttributes attrs = e.getAttributes();
					if (MatchResult.TRUE.equals(filter.match(e))) {
						final SearchResult searchResult = new SearchResult("",
								null, currentCtx.getAttributes(""));
						answer.addElement(searchResult);
						entriesToReturn++;
					}
				}
			}

			// ONE_LEVEL or SUBTREE
			// For each item on list, examine its attributes
			if (SearchControls.OBJECT_SCOPE != cons.getSearchScope()) {
				while (objs.hasMore()
						&& (entriesToReturn < sizeLimit)
						&& ((System.currentTimeMillis() - startTime) < timeLimit)) {
					item = (Binding) objs.next();
					final String theName = item.getName();
					if (SearchControls.ONELEVEL_SCOPE == cons.getSearchScope()) { // ONE_LEVEL
						LdapEntry e = null;
						if (item.getObject() instanceof DirContext) {
							e = createLdapEntry((DirContext) item.getObject());
						} else {
							e = createLdapEntry((DirContext) lookup(theName));
						}
						if (MatchResult.TRUE.equals(filter.match(e))) {
							final SearchResult searchResult = new SearchResult(
									theName, null, e
											.getAttributesAsNamingAttributes());
							answer.addElement(searchResult);
							entriesToReturn++;
						}
					} else { // SUBTREE
						if (cons.getSearchScope() == SearchControls.SUBTREE_SCOPE) {
							NamingEnumeration theAnswer = ((DirContext) target)
									.search(theName, f, cons);
							while (theAnswer.hasMore()
									&& (entriesToReturn < sizeLimit)) {
								final SearchResult next = (SearchResult) theAnswer
										.next();
								String theName2 = next.getName();
								if ("".equals(theName2)) {
									theName2 = theName;
								} else {
									if (!"".equals(theName)) {
										theName2 = theName2 + "," + theName;
									}
								}
								next.setName(theName2);
								answer.addElement(next);
								entriesToReturn++;
							}
						}

					}
				}
			}
		} catch (NamingException e) {
			logger.debug("NamingException", e);
		} finally {

			target.close();
		}
		logger
				.debug("Search finished returning " + answer.size()
						+ " elements");
		return new WrapEnum(answer.elements());

	}

	private LdapEntry createLdapEntry(DirContext ctx) {
		try {
			DistinguishedName name = (DistinguishedName) ctx.getNameParser("")
					.parse(getNameInNamespace());
			LdapEntry toReturn = new LdapEntryImpl(name, new LdapAttributes(ctx
					.getAttributes("")));
			return toReturn;
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	protected Context createCtx(HierCtx parent, String name, Hashtable inEnv) {
		return new JlettyHierCtx(parent, name, inEnv, new Hashtable(11),
				new BasicAttributes(), new Hashtable(11));
	}

	protected Context cloneCtx() {
		return new JlettyHierCtx(parent, myAtomicName, myEnv, bindings,
				myAttrs, bindingAttrs);
	}

	public void setNameParser(NameParser parser) {
		myParser = parser;
	}
}
