/*
 * Created on 08-dic-2004
 *
 */
package org.jletty.ldapstackldapops;

import java.util.Iterator;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jletty.encoder.BerOctetString;
import org.jletty.encoder.BerSequence;

/**
 * @author Ruben
 */
// 4.5.2. Search Result
//
// The results of the search attempted by the server upon receipt of a
// Search Request are returned in Search Responses, which are LDAP
// messages containing either SearchResultEntry, SearchResultReference,
// ExtendedResponse or SearchResultDone data types.
//
// SearchResultEntry ::= [APPLICATION 4] SEQUENCE {
// objectName LDAPDN,
// attributes PartialAttributeList }
//
// PartialAttributeList ::= SEQUENCE OF SEQUENCE {
// type AttributeDescription,
// vals SET OF AttributeValue }
// -- implementors should note that the PartialAttributeList may
// -- have zero elements (if none of the attributes of that entry
// -- were requested, or could be returned), and that the vals set
// -- may also have zero elements (if types only was requested, or
// -- all values were excluded from the result.)
//
// SearchResultReference ::= [APPLICATION 19] SEQUENCE OF LDAPURL
// -- at least one LDAPURL element must be present
//
// SearchResultDone ::= [APPLICATION 5] LDAPResult
//
// Upon receipt of a Search Request, a server will perform the necessary
// search of the DIT.
//
// If the LDAP session is operating over a connection-oriented transport
// such as TCP, the server will return to the client a sequence of
// responses in separate LDAP messages. There may be zero or more
// responses containing SearchResultEntry, one for each entry found
// during the search. There may also be zero or more responses
// containing SearchResultReference, one for each area not explored by
// this server during the search. The SearchResultEntry and
// SearchResultReference PDUs may come in any order. Following all the
// SearchResultReference responses and all SearchResultEntry responses
// to be returned by the server, the server will return a response
// containing the SearchResultDone, which contains an indication of
// success, or detailing any errors that have occurred.
//
// Each entry returned in a SearchResultEntry will contain all
// attributes, complete with associated values if necessary, as
// specified in the attributes field of the Search Request. Return of
// attributes is subject to access control and other administrative
// policy. Some attributes may be returned in binary format (indicated
// by the AttributeDescription in the response having the binary option
// present).
//
// Some attributes may be constructed by the server and appear in a
// SearchResultEntry attribute list, although they are not stored
// attributes of an entry. Clients MUST NOT assume that all attributes
// can be modified, even if permitted by access control.
//
// LDAPMessage responses of the ExtendedResponse form are reserved for
// returning information associated with a control requested by the
// client. These may be defined in future versions of this document.
public class SearchResultEntry implements LDAPResponse {
	private AttributeTypeAndValuesList attrTypeAndValues;

	private String objectName;

	public SearchResultEntry(String dn,
			AttributeTypeAndValuesList partialattributelist) {
		this.objectName = dn;
		this.attrTypeAndValues = partialattributelist;
	}

	public String getObjectName() {
		return this.objectName;
	}

	public byte[] getBytes() {
		BerSequence seq = new BerSequence(BerTags.APPLICATION_4)
				.append(this.objectName);

		BerSequence pal = new BerSequence(BerTags.SEQUENCEOF);
		Iterator i = this.attrTypeAndValues.iterator();
		while (i.hasNext()) {
			AttributeTypeAndValues atav = (AttributeTypeAndValues) i.next();
			BerSequence tmp = new BerSequence();
			tmp.append(atav.getAttributeDescription());
			BerSequence values = new BerSequence(BerTags.SETOF);
			// AttributeValues attributeValues = atav.getValues();
			byte[][] attrValues = atav.getValues();
			for (int j = 0; j < attrValues.length; j++) {
				byte[] aValue = attrValues[j];
				BerOctetString octetstring = new BerOctetString(aValue);
				values.append(octetstring);
			}
			tmp.append(values);
			pal.append(tmp);
		}

		seq.append(pal);
		return seq.getBytes();
	}

	/**
	 * @return Returns the attrTypeAndValues.
	 */
	public AttributeTypeAndValuesList getAttrTypeAndValues() {
		return this.attrTypeAndValues;
	}

	public String toString() {

		// TODO replace with ToStringBuilder when the easymock - tostringbuilder
		// interaction is resolved
		// return new
		// ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE).append("objectName",
		// objectName)
		// .append("attrTypeAndValues", attrTypeAndValues).toString();
		return "SearchResultEntry: name=\"" + this.objectName + "\"\nattrs=\""
				+ this.attrTypeAndValues + "\"";
	}

	public boolean equals(final Object other) {
		if (!(other instanceof SearchResultEntry)) {
			return false;
		}
		SearchResultEntry castOther = (SearchResultEntry) other;
		return new EqualsBuilder().append(this.attrTypeAndValues,
				castOther.attrTypeAndValues).append(this.objectName,
				castOther.objectName).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.attrTypeAndValues).append(
				this.objectName).toHashCode();
	}

}
