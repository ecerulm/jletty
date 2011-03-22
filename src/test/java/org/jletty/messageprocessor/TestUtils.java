package org.jletty.messageprocessor;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import org.jletty.dn.DistinguishedName;
import org.jletty.dn.Rfc2253NameParserImpl;
import org.jletty.jndiprovider.LdapAttribute;
import org.jletty.jndiprovider.LdapAttributes;
import org.jletty.jndiprovider.LdapEntryImpl;
import org.jletty.ldapstackldapops.LdapEntry;
import org.jletty.ldapstackldapops.LdapInvalidAttributeSyntaxException;
import org.jletty.ldapstackldapops.LdapUndefinedAttributeType;
import org.jletty.schema.AttributeType;
import org.jletty.schema.AttributeValue;
import org.jletty.schema.Schema;
import org.jletty.schema.Syntax;
import org.jletty.schema.Syntaxes;

public class TestUtils {
	private TestUtils() {
		// make it non instantiable
	}

	public static LdapAttributes createAttrsForO_org(String orgName)
			throws Exception {
		Attributes attrs = new BasicAttributes();
		attrs.put("o", orgName);
		attrs.put("objectClass", "organization");
		attrs.put("userPassword", "{crypt}X5/DBrWPOQQaI");
		attrs.put("searchGuide", "2.5.6.4#(o$EQ)");
		attrs.put("seeAlso", "CN=Beverly Pyke, O=ISODE Consortium, C=GB");
		attrs.put("businessCategory",
				"description of the business performed by the organization");
		attrs.put("x121Address", "031344159782738");
		// An X.121 address contains between 1 and 15 decimal digits, such as
		// 031344159782738.
		attrs.put("registeredAddress", "1234 Main St.$Anytown, CA 12345$USA");
		attrs.put("destinationIndicator", "Stow, Ohio, USA");
		// The country and city associated with the entry needed to provide
		// Public Telegram Service. Generally used in conjunction with
		// registeredAddress.
		attrs.put("preferredDeliveryMethod", "telephone");
		attrs.put("telexNumber", "817379, ch, ehhg");
		attrs.put("teletexTerminalIdentifier", "415-555-2233");
		attrs.put("telephoneNumber", "+41 1 268 1540");
		// attrs.put("internationaliSDNNumber","+SO 812467"); //Sun Directory
		// Server 5.2 uses IA5String for internationaliSDNNumber
		attrs.put("internationaliSDNNumber", "812467");
		attrs.put("facsimileTelephoneNumber", "+41 1 268 1540");
		attrs.put("street", "Limmatquai 138");
		attrs.put("postOfficeBox", "P.O. Box 1234");
		attrs.put("postalCode", "44224");
		attrs.put("postalAddress", "1234 Ridgeway Drive$Santa Clara, CA$99555");
		attrs.put("physicalDeliveryOfficeName", "Santa Clara");
		attrs.put("st", "California");
		attrs.put("l", "Santa Clara");
		attrs.put("description", "the organization description");

		return convertAttrsValuesToOctetStringable(attrs);
	}

	public static LdapAttributes createAttrsForDc_Com() {
		try {
			Attributes attrs = new BasicAttributes();
			attrs.put("objectClass", "dcObject");
			attrs.put("dc", "com");
			return convertAttrsValuesToOctetStringable(attrs);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static LdapAttributes createAttrsForCN_person(String cnValue) {
		try {
			Attributes attrs;
			attrs = new BasicAttributes();
			Attribute cn = new BasicAttribute("cn");
			cn.add(cnValue);
			cn.add("another_" + cnValue);
			attrs.put(cn);
			attrs.put("objectclass", "person");
			Attribute sn = new BasicAttribute("sn");
			sn.add("personsn1");
			sn.add("another_personsn1");
			attrs.put(sn);
			attrs.put("userPassword", "{crypt}X5/DBrWPOQQaI");
			attrs.put("telephoneNumber", "+41 1 268 1540");
			attrs.put("seeAlso", "CN=Beverly Pyke, O=ISODE Consortium, C=GB");
			attrs.put("description", "the person1 description");

			return convertAttrsValuesToOctetStringable(attrs);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static LdapAttributes createAttrsForC_US()
			throws LdapUndefinedAttributeType, NamingException,
			UnsupportedEncodingException {
		Attributes attrs;
		attrs = new BasicAttributes();
		attrs.put(new BasicAttribute("c", "US"));
		attrs.put(new BasicAttribute("objectClass", "country"));
		attrs.put(new BasicAttribute("searchGuide", "2.5.6.2#(c$EQ)"));
		attrs.put(new BasicAttribute("description",
				"This is a country description"));

		return convertAttrsValuesToOctetStringable(attrs);
	}

	private static LdapAttributes convertAttrsValuesToOctetStringable(
			Attributes inputAttrs) throws LdapUndefinedAttributeType,
			NamingException, UnsupportedEncodingException {
		LdapAttributes attrs = new LdapAttributes();
		// Iterator it = attrList.iterator();
		NamingEnumeration all = inputAttrs.getAll();
		while (all.hasMoreElements()) {
			Attribute attrTaV = (Attribute) all.nextElement();
			final String attrDesc = attrTaV.getID();
			LdapAttribute attr = new LdapAttribute(attrDesc);
			final AttributeType attributeType = Schema.getInstance()
					.getAttributeType(attrDesc);
			if (attributeType == null) {
				throw new LdapUndefinedAttributeType(attrDesc);
			}
			Syntax syntax = Syntaxes.getSyntax(attributeType.getSyntax());
			List tmp = new ArrayList();
			NamingEnumeration all2 = attrTaV.getAll();
			while (all2.hasMoreElements()) {
				String element = (String) all2.nextElement();
				tmp.add(element.getBytes("UTF-8"));
			}
			byte[][] values = (byte[][]) tmp.toArray(new byte[tmp.size()][]);

			if (values.length < 1) {
				throw new LdapInvalidAttributeSyntaxException("attribute "
						+ attrDesc + " cannot be empty");
			}

			for (int i = 0; i < values.length; i++) {
				try {
					byte[] value = values[i];
					AttributeValue matchable = syntax.get(value);
					attr.add(matchable);
				} catch (LdapInvalidAttributeSyntaxException e) {
					final LdapInvalidAttributeSyntaxException toThrow = new LdapInvalidAttributeSyntaxException(
							"value #" + i + " invalid per syntax");
					toThrow.initCause(e);
					throw toThrow;
				}
			}
			attrs.put(attr);
		}
		return attrs;
	}

	public static LdapEntry createLdapEntryForCN_person(String cnValue,
			String rdn) {
		try {
			DistinguishedName name = (DistinguishedName) new Rfc2253NameParserImpl()
					.parse("cn=" + cnValue + "," + rdn);
			LdapEntry toReturn = new LdapEntryImpl(name,
					createAttrsForCN_person(cnValue));
			return toReturn;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Generatos an Attributes object contining cn, objectclass,sn,
	 * userPassword, telephoneNumber, seeAlso, description,
	 * preferredDeliveryMethod,title,internationaliSDNNumber. The following
	 * attributes are not included YET x121Address, registeredAddress,
	 * destinationaIndicator , telexNumber, teletexTerminalIdentifier,
	 * telephoneNumber, facsimileTelephoneNumber, street, postOfficeBox,
	 * postalCode, postalAddress, physicalDeliveryOfficeName, ou, st, l
	 * 
	 * @param cnValue
	 * @return the attributes
	 * @throws Exception
	 */
	public static LdapAttributes createAttrsForOrgPerson(String cnValue,
			String kindOfPassword) throws Exception {
		Attributes attrs;
		attrs = new BasicAttributes();
		attrs.put("cn", cnValue);
		attrs.put("objectclass", "organizationalPerson");
		attrs.put("sn", "personsn1");
		if ("CRYPT".equals(kindOfPassword)) {
			attrs.put("userPassword", "{CRYPT}sagw.50z.ZJqA"); // userPassword:
			// cryptpassword
		} else if ("MD5".equals(kindOfPassword)) {
			attrs.put("userPassword", "{MD5}7IUHCqcOWY7acsvoLZn6vA=="); // userPassword:
			// cryptpassword
		} else if ("SHA1".equals(kindOfPassword)) {
			attrs.put("userPassword", "{SSHA}5MBgOhI+4QoZytN4L+nBPgTonCE="); // userPassword:
			// cryptpassword
		} else {
			throw new RuntimeException();
		}
		attrs.put("telephoneNumber", "+41 1 268 1540");
		attrs.put("seeAlso", "CN=Beverly Pyke, O=ISODE Consortium, C=GB");
		attrs.put("description", "the person1 description");

		// organizationalPerson attributes
		attrs.put("title", "a title");
		// attrs.put("x121Address", "");
		// attrs.put("registeredAddress", "");
		// attrs.put("destinationIndicator", "");
		attrs.put("preferredDeliveryMethod", "telephone");
		attrs.put("telexNumber", "1212312$ES$3322333");
		// attrs.put("teletexTerminalIdentifier", "");
		// attrs.put("telephoneNumber", "");
		attrs.put("internationaliSDNNumber", "999999999");
		// attrs.put("facsimileTelephoneNumber", "");
		// attrs.put("street", "");
		// attrs.put("postOfficeBox", "");
		// attrs.put("postalCode", "");
		// attrs.put("postalAddress", "");
		// attrs.put("physicalDeliveryOfficeName", "");
		// attrs.put("ou", "");
		// attrs.put("st", "");
		// attrs.put("l", "");

		return convertAttrsValuesToOctetStringable(attrs);
	}

	public static Attributes convertToNamingAttributes(LdapAttributes ldapAttrs) {
		BasicAttributes toReturn = new BasicAttributes(true);
		Collection collection = ldapAttrs.asCollection();
		for (Iterator iter = collection.iterator(); iter.hasNext();) {
			LdapAttribute ldapAttr = (LdapAttribute) iter.next();
			toReturn.put(ldapAttr.asNamingAttribute());
		}
		return toReturn;
	}
}