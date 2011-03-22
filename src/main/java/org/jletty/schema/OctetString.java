package org.jletty.schema;

import org.apache.commons.lang.ArrayUtils;
import org.jletty.util.NotImplementedException;

public class OctetString implements AttributeValue {

	private byte[] octetstring;

	public OctetString(byte[] octetstring) {
		this.octetstring = ArrayUtils.clone(octetstring);
	}

	public MatchResult eqMatch(MatchRule matchRule, Matchable object) {
		// TODO find suitable matching rule for this syntax
		// unsupported matching rule for this syntax
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
		return this.octetstring.clone();
	}

}
