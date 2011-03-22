/*
 * Created on 16-feb-2005
 *
 */
package org.jletty.schema;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

/**
 * @author Ruben
 * 
 */
public final class MatchRule extends Enum {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3688786985724490549L;

	public static final MatchRule CASE_IGNORE_MATCH = new MatchRule(
			"caseIgnoreMatch", "2.5.13.2", "1.3.6.1.4.1.1466.115.121.1.15"); // syntax

	// DirectoryString

	public static final MatchRule CASE_IGNORE_IA5_MATCH = new MatchRule(
			"caseIgnoreIA5Match", "1.3.6.1.4.1.1466.109.114.2",
			"1.3.6.1.4.1.1466.115.121.1.26"); // syntax ia5String syntax

	public static final MatchRule CASE_EXACT_MATCH = new MatchRule(
			"caseExactMatch", "2.5.13.5", "1.3.6.1.4.1.1466.115.121.1.15");// syntax

	// DirectoryString

	public static final MatchRule TELEPHONE_NUMBER_MATCH = new MatchRule(
			"telephoneNumberMatch", "2.5.13.21",
			"1.3.6.1.4.1.1466.115.121.1.50"); // syntax telephoneNumber

	public static final MatchRule CASE_IGNORE_ORDERING_MATCH = new MatchRule(
			"caseIgnoreOrderingMatch", "2.5.13.3",
			"1.3.6.1.4.1.1466.115.121.1.15"); // syntax DirectoryString

	public static final MatchRule CASE_IGNORE_SUBSTRING_MATCH = new MatchRule(
			"caseIgnoreSubstringsMatch", "2.5.13.4",
			"1.3.6.1.4.1.1466.115.121.1.58"); // syntax substring assertion

	public static final MatchRule NUMERIC_STRING_MATCH = new MatchRule(
			"numericStringMatch", "2.5.13.8", "1.3.6.1.4.1.1466.115.121.1.36"); // syntax

	// numeric
	// string

	private static Map oidMap;

	private String oid;

	private String syntaxOid;

	private MatchRule(String matchRuleName, String oid, String syntaxOid) {
		super(matchRuleName);
		this.oid = oid;
		this.syntaxOid = syntaxOid;
	}

	public static MatchRule getEnum(String matchRuleName) {
		MatchRule toReturn = (MatchRule) getEnum(MatchRule.class, matchRuleName);
		if (null == toReturn) {
			// try finding it by oid
			toReturn = getEnumByOid(matchRuleName);
		}
		// if (null == toReturn) {
		// throw new MatchingRuleNotFoundException("Matching rule with name "
		// + matchRuleName + " not found");
		// }
		return toReturn;
	}

	private static MatchRule getEnumByOid(String oid) {
		if (oidMap == null) {
			oidMap = new HashMap();
			List enumList = getEnumList();
			for (Iterator iter = enumList.iterator(); iter.hasNext();) {
				MatchRule element = (MatchRule) iter.next();
				oidMap.put(element.oid, element);
			}
		}

		return (MatchRule) oidMap.get(oid);
	}

	public static Map getEnumMap() {
		return getEnumMap(MatchRule.class);
	}

	public static List getEnumList() {
		return getEnumList(MatchRule.class);
	}

	public static Iterator iterator() {
		return iterator(MatchRule.class);
	}

	public String getSyntaxOid() {
		return this.syntaxOid;
	}

}
