/*
 * Created on 05-feb-2005
 *
 */
package org.jletty.schema;

import java.util.HashMap;

import org.jletty.util.NotImplementedException;

/**
 * @author Ruben
 * 
 */
public class Syntaxes {
	private static Syntaxes theInstance = new Syntaxes();

	private HashMap map;

	// 1.3.6.1.1.1.0.0
	// 1.3.6.1.1.1.0.1
	// 1.3.6.1.4.1.1466.115.121.1.10
	// 1.3.6.1.4.1.1466.115.121.1.12
	// 1.3.6.1.4.1.1466.115.121.1.13
	// 1.3.6.1.4.1.1466.115.121.1.14
	// 1.3.6.1.4.1.1466.115.121.1.15
	// 1.3.6.1.4.1.1466.115.121.1.19
	// 1.3.6.1.4.1.1466.115.121.1.21
	// 1.3.6.1.4.1.1466.115.121.1.22
	// 1.3.6.1.4.1.1466.115.121.1.23
	// 1.3.6.1.4.1.1466.115.121.1.25
	// 1.3.6.1.4.1.1466.115.121.1.26
	// 1.3.6.1.4.1.1466.115.121.1.27
	// 1.3.6.1.4.1.1466.115.121.1.28
	// 1.3.6.1.4.1.1466.115.121.1.34
	// 1.3.6.1.4.1.1466.115.121.1.36
	// 1.3.6.1.4.1.1466.115.121.1.38
	// 1.3.6.1.4.1.1466.115.121.1.39
	// 1.3.6.1.4.1.1466.115.121.1.4
	// 1.3.6.1.4.1.1466.115.121.1.40
	// 1.3.6.1.4.1.1466.115.121.1.41
	// 1.3.6.1.4.1.1466.115.121.1.42
	// 1.3.6.1.4.1.1466.115.121.1.43
	// 1.3.6.1.4.1.1466.115.121.1.44
	// 1.3.6.1.4.1.1466.115.121.1.49
	// 1.3.6.1.4.1.1466.115.121.1.5
	// 1.3.6.1.4.1.1466.115.121.1.50
	// 1.3.6.1.4.1.1466.115.121.1.51
	// 1.3.6.1.4.1.1466.115.121.1.52
	// 1.3.6.1.4.1.1466.115.121.1.53
	// 1.3.6.1.4.1.1466.115.121.1.6
	// 1.3.6.1.4.1.1466.115.121.1.8
	// 1.3.6.1.4.1.1466.115.121.1.9
	// ACI Item N 1.3.6.1.4.1.1466.115.121.1.1
	// Access Point Y 1.3.6.1.4.1.1466.115.121.1.2
	// Attribute Type Description Y 1.3.6.1.4.1.1466.115.121.1.3
	// Audio N 1.3.6.1.4.1.1466.115.121.1.4
	// Binary N 1.3.6.1.4.1.1466.115.121.1.5
	// Bit String Y 1.3.6.1.4.1.1466.115.121.1.6
	// Boolean Y 1.3.6.1.4.1.1466.115.121.1.7
	// Certificate N 1.3.6.1.4.1.1466.115.121.1.8
	// Certificate List N 1.3.6.1.4.1.1466.115.121.1.9
	// Certificate Pair N 1.3.6.1.4.1.1466.115.121.1.10
	// Country String Y 1.3.6.1.4.1.1466.115.121.1.11
	// DN Y 1.3.6.1.4.1.1466.115.121.1.12
	// Data Quality Syntax Y 1.3.6.1.4.1.1466.115.121.1.13
	// Delivery Method Y 1.3.6.1.4.1.1466.115.121.1.14
	// Directory String Y 1.3.6.1.4.1.1466.115.121.1.15
	// DIT Content Rule Description Y 1.3.6.1.4.1.1466.115.121.1.16
	// DIT Structure Rule Description Y 1.3.6.1.4.1.1466.115.121.1.17
	// DL Submit Permission Y 1.3.6.1.4.1.1466.115.121.1.18
	// DSA Quality Syntax Y 1.3.6.1.4.1.1466.115.121.1.19
	// DSE Type Y 1.3.6.1.4.1.1466.115.121.1.20
	// Enhanced Guide Y 1.3.6.1.4.1.1466.115.121.1.21
	// Facsimile Telephone Number Y 1.3.6.1.4.1.1466.115.121.1.22
	// Fax N 1.3.6.1.4.1.1466.115.121.1.23
	// Generalized Time Y 1.3.6.1.4.1.1466.115.121.1.24
	// Guide Y 1.3.6.1.4.1.1466.115.121.1.25
	// IA5 String Y 1.3.6.1.4.1.1466.115.121.1.26
	// INTEGER Y 1.3.6.1.4.1.1466.115.121.1.27
	// JPEG N 1.3.6.1.4.1.1466.115.121.1.28
	// LDAP Syntax Description Y 1.3.6.1.4.1.1466.115.121.1.54
	// LDAP Schema Definition Y 1.3.6.1.4.1.1466.115.121.1.56
	// LDAP Schema Description Y 1.3.6.1.4.1.1466.115.121.1.57
	// Master And Shadow Access Points Y 1.3.6.1.4.1.1466.115.121.1.29
	// Matching Rule Description Y 1.3.6.1.4.1.1466.115.121.1.30
	// Matching Rule Use Description Y 1.3.6.1.4.1.1466.115.121.1.31
	// Mail Preference Y 1.3.6.1.4.1.1466.115.121.1.32
	// MHS OR Address Y 1.3.6.1.4.1.1466.115.121.1.33
	// Modify Rights Y 1.3.6.1.4.1.1466.115.121.1.55
	// Name And Optional UID Y 1.3.6.1.4.1.1466.115.121.1.34
	// Name Form Description Y 1.3.6.1.4.1.1466.115.121.1.35
	// Numeric String Y 1.3.6.1.4.1.1466.115.121.1.36
	// Object Class Description Y 1.3.6.1.4.1.1466.115.121.1.37
	// Octet String Y 1.3.6.1.4.1.1466.115.121.1.40
	// OID Y 1.3.6.1.4.1.1466.115.121.1.38
	// Other Mailbox Y 1.3.6.1.4.1.1466.115.121.1.39
	// Postal Address Y 1.3.6.1.4.1.1466.115.121.1.41
	// Protocol Information Y 1.3.6.1.4.1.1466.115.121.1.42
	//
	// Presentation Address Y 1.3.6.1.4.1.1466.115.121.1.43
	// Printable String Y 1.3.6.1.4.1.1466.115.121.1.44
	// Substring Assertion Y 1.3.6.1.4.1.1466.115.121.1.58
	// Subtree Specification Y 1.3.6.1.4.1.1466.115.121.1.45
	// Supplier Information Y 1.3.6.1.4.1.1466.115.121.1.46
	// Supplier Or Consumer Y 1.3.6.1.4.1.1466.115.121.1.47
	// Supplier And Consumer Y 1.3.6.1.4.1.1466.115.121.1.48
	// Supported Algorithm N 1.3.6.1.4.1.1466.115.121.1.49
	// Telephone Number Y 1.3.6.1.4.1.1466.115.121.1.50
	// Teletex Terminal Identifier Y 1.3.6.1.4.1.1466.115.121.1.51
	// Telex Number Y 1.3.6.1.4.1.1466.115.121.1.52
	// UTC Time Y 1.3.6.1.4.1.1466.115.121.1.53

	private Syntaxes() {
		this.map = new HashMap();

		registerSyntax(new DirectoryStringSyntax());
		registerSyntax(new OidSyntax());
		registerSyntax(new IA5StringSyntax());
		registerSyntax(new TelephoneNumberSyntax());
		registerSyntax(new PrintableStringSyntax());
		registerSyntax(new GuideSyntax());
		registerSyntax(new PostalAddressSyntax());
		registerSyntax(new NumericStringSyntax());
		registerSyntax(new OctetStringSyntax());
		registerSyntax(new DeliveryMethodSyntax());
		registerSyntax(new TelexNumberSyntax());
		registerSyntax(new TeletexTerminalIdentifierSyntax());
		registerSyntax(new FacsimileTelephoneNumberSyntax());
		registerSyntax(new DistinguishedNameSyntax());

	}

	/**
	 * @param theSyntax
	 */
	private void registerSyntax(Syntax theSyntax) {
		this.map.put(theSyntax.getName(), theSyntax);
	}

	public static Syntax getSyntax(String syntaxName) {
		Syntax toReturn = (Syntax) theInstance.map.get(syntaxName);
		if (null != toReturn) {
			return toReturn;
		}

		throw new NotImplementedException("Syntax \"" + syntaxName
				+ "\" not implemented");
	}

}
