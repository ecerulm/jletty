package org.jletty.schema;

import org.jletty.util.NotImplementedException;
import org.jletty.util.StringUtil;

public class DistinguishedName implements AttributeValue {

	private String dn;

	public DistinguishedName(String value) {
		this.dn = value;
	}

	public MatchResult eqMatch(MatchRule matchRule, Matchable object) {
		// TODO implement eqMatch for DistinguishedNameMatch matching rule

		// unsupported matching rule
		return MatchResult.UNDEF;
	}

	public MatchResult ordMatch(MatchRule matchRule, Matchable object,
			MatchResultClosure closure) {
		throw new NotImplementedException();
	}

	public MatchResult subMatch(MatchRule matchRule, String filterValue) {
		throw new NotImplementedException();
	}

	public MatchResult approxMatch(MatchRule eqMatchRule, Matchable object) {
		throw new NotImplementedException();
	}

	public byte[] getContents() {
		return StringUtil.stringToByteArray("UTF-8", this.dn);
	}

}
