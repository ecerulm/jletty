package org.jletty.schema;

import org.jletty.util.NotImplementedException;
import org.jletty.util.StringUtil;

public class FacsimileTelephoneNumber implements AttributeValue {

	private String facsimileTelephoneNumber;

	public FacsimileTelephoneNumber(String value) {
		this.facsimileTelephoneNumber = value;
	}

	public MatchResult eqMatch(MatchRule matchRule, Matchable object) {
		throw new NotImplementedException();
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
		return StringUtil.stringToByteArray("UTF-8",
				this.facsimileTelephoneNumber);
	}

}
