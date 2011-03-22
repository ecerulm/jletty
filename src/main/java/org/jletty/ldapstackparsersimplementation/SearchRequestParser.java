//The Search Request is defined as follows:
//
//    SearchRequest ::= [APPLICATION 3] SEQUENCE {
//            baseObject      LDAPDN,
//            scope           ENUMERATED {
//                    baseObject              (0),
//                    singleLevel             (1),
//                    wholeSubtree            (2) },
//            derefAliases    ENUMERATED {
//                    neverDerefAliases       (0),
//                    derefInSearching        (1),
//                    derefFindingBaseObj     (2),
//                    derefAlways             (3) },
//            sizeLimit       INTEGER (0 .. maxInt),
//            timeLimit       INTEGER (0 .. maxInt),
//            typesOnly       BOOLEAN,
//            filter          Filter,
//            attributes      AttributeDescriptionList }
//
//    Filter ::= CHOICE {
//            and             [0] SET OF Filter,
//            or              [1] SET OF Filter,
//            not             [2] Filter,
//            equalityMatch   [3] AttributeValueAssertion,
//            substrings      [4] SubstringFilter,
//            greaterOrEqual  [5] AttributeValueAssertion,
//            lessOrEqual     [6] AttributeValueAssertion,
//            present         [7] AttributeDescription,
//            approxMatch     [8] AttributeValueAssertion,
//            extensibleMatch [9] MatchingRuleAssertion }
//
//    SubstringFilter ::= SEQUENCE {
//            type            AttributeDescription,
//            -- at least one must be present
//            substrings      SEQUENCE OF CHOICE {
//                    initial [0] LDAPString,
//                    any     [1] LDAPString,
//                    final   [2] LDAPString } }
//
//    MatchingRuleAssertion ::= SEQUENCE {
//            matchingRule    [1] MatchingRuleId OPTIONAL,
//            type            [2] AttributeDescription OPTIONAL,
//            matchValue      [3] AssertionValue,
//            dnAttributes    [4] BOOLEAN DEFAULT FALSE }

package org.jletty.ldapstackparsersimplementation;

import java.nio.ByteBuffer;
import java.util.List;

import org.apache.commons.lang.enums.ValuedEnum;
import org.jletty.ldapstackldapops.AttributeDescriptionListListener;
import org.jletty.ldapstackldapops.BERIntegerListener;
import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.DerefAliases;
import org.jletty.ldapstackldapops.DerefAliasesListener;
import org.jletty.ldapstackldapops.Filter;
import org.jletty.ldapstackldapops.FilterListener;
import org.jletty.ldapstackldapops.LDAPBooleanListener;
import org.jletty.ldapstackldapops.LDAPStringListener;
import org.jletty.ldapstackldapops.RequestListener;
import org.jletty.ldapstackldapops.Scope;
import org.jletty.ldapstackldapops.ScopeListener;
import org.jletty.ldapstackldapops.SearchRequest;

/**
 * @author $Author: ecerulm $
 */
public final class SearchRequestParser extends TLVParser {

	private static final class StateEnum extends ValuedEnum {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public final static int R_BASE_VALUE = 100;

		public final static int R_SCOPE_VALUE = 200;

		public final static int R_DREFALIASES_VALUE = 300;

		public final static int R_SIZELIMIT_VALUE = 400;

		public final static int R_TIMELIMIT_VALUE = 500;

		public final static int R_TYPESONLY_VALUE = 600;

		public final static int R_FILTER_VALUE = 700;

		public final static int R_ATTRS_VALUE = 800;

		public final static int FINAL_VALUE = 800;

		public final static StateEnum R_BASE = new StateEnum("Reading base",
				R_BASE_VALUE);

		public final static StateEnum R_SCOPE = new StateEnum("Reading scope",
				R_SCOPE_VALUE);

		public final static StateEnum R_DREFALIASES = new StateEnum(
				"Reading deref aliases", R_DREFALIASES_VALUE);

		public final static StateEnum R_SIZELIMIT = new StateEnum(
				"Reading sizelimit", R_SIZELIMIT_VALUE);

		public final static StateEnum R_TIMELIMIT = new StateEnum(
				"Reading timelimit", R_TIMELIMIT_VALUE);

		public final static StateEnum R_TYPESONLY = new StateEnum(
				"Reading typesonly", R_TYPESONLY_VALUE);

		public final static StateEnum R_FILTER = new StateEnum(
				"Reading filter", R_FILTER_VALUE);

		public final static StateEnum R_ATTRS = new StateEnum("Reading attrs",
				R_ATTRS_VALUE);

		public final static StateEnum FINAL = new StateEnum("final",
				FINAL_VALUE);

		protected StateEnum(String name, int value) {
			super(name, value);
		}

	}

	private StateEnum stateEnum = StateEnum.R_BASE;

	public static final byte TAG = BerTags.APPLICATION_3;

	private LDAPStringParser baseObjectParser = new LDAPStringParser(
			new LDAPStringListener() {
				public void data(String value) {
					SearchRequestParser.this.baseObject = value;
				}
			});

	private ScopeParser scopeParser = new ScopeParser(new ScopeListener() {
		public void data(Scope value) {
			SearchRequestParser.this.scope = value;
		}
	});

	private DerefAliasesParser deRefAliasesParser = new DerefAliasesParser(
			new DerefAliasesListener() {
				public void data(DerefAliases value) {
					SearchRequestParser.this.deRefAliases = value;
				}
			});

	private BERIntegerParser sizeLimitParser = new BERIntegerParser(
			new BERIntegerListener() {
				public void data(int value) {
					SearchRequestParser.this.sizeLimit = value;
				}
			});

	private BERIntegerParser timeLimitParser = new BERIntegerParser(
			new BERIntegerListener() {
				public void data(int value) {
					SearchRequestParser.this.timeLimit = value;
				}
			});

	private LDAPBooleanParser typesOnlyParser = new LDAPBooleanParser(
			new LDAPBooleanListener() {
				public void data(boolean value) {
					SearchRequestParser.this.typesOnly = value;
				}
			});

	private FilterParser filterParser = new FilterParser(new FilterListener() {
		public void data(Filter f) {
			SearchRequestParser.this.filter = f;
		}
	});

	private AttributeDescriptionListParser attributesParser = new AttributeDescriptionListParser(
			new AttributeDescriptionListListener() {
				public void data(List value) {
					SearchRequestParser.this.attributes = value;
				}
			});

	private Parser currentParser = this.baseObjectParser;

	// /**
	// * <code>state</code> represent the internal state of the parser. To know
	// * which state are valid refer
	// *
	// */
	// private State readingBaseState = new ParserStateBase(
	// "Reading baseObject (LDAPDN:string)") {
	// private LDAPStringListener ldapStringListener = new LDAPStringListener()
	// {
	// public void data(String value) {
	// baseObject = value;
	// }
	// };
	//
	// private LDAPStringParser baseObjectParser = new LDAPStringParser(
	// new LDAPStringListener() {
	// public void data(String value) {
	// baseObject = value;
	// }
	// });
	//
	// public boolean parse(ByteBuffer buffer) {
	// boolean completed = baseObjectParser.parse(buffer);
	// if (!completed)
	// return false;
	// state = readingScopeState;
	// return true;
	// }
	//
	// public void resetInternal() {
	// baseObjectParser.reset();
	// }
	// };

	// private State readingScopeState = new ParserStateBase(
	// "Reading scope (enumerated)") {
	//
	// private ScopeListener scopeListener = new ScopeListener() {
	// public void data(Scope value) {
	// scope = value;
	// }
	// };
	//
	// private ScopeParser scopeParser = new ScopeParser(scopeListener);
	//
	// public boolean parse(ByteBuffer buffer) {
	// boolean completed = scopeParser.parse(buffer);
	// if (!completed)
	// return false;
	// state = readingDeRefAliasesState;
	// return true;
	// }
	//
	// public void resetInternal() {
	// scopeParser.reset();
	// }
	// };

	// private State readingDeRefAliasesState = new ParserStateBase(
	// "Reading deRefAliases (enumerated)") {
	//
	// private DerefAliasesListener deRefAliasesListener = new
	// DerefAliasesListener() {
	// public void data(DerefAliases value) {
	// deRefAliases = value;
	// }
	// };
	//
	// private DerefAliasesParser deRefAliasesParser = new DerefAliasesParser(
	// deRefAliasesListener);
	//
	// public boolean parse(ByteBuffer buffer) {
	// boolean completed = deRefAliasesParser.parse(buffer);
	// if (!completed)
	// return false;
	// state = readingSizeLimitState;
	// return true;
	// }
	//
	// public void resetInternal() {
	// deRefAliasesParser.reset();
	// }
	// };

	// private State readingSizeLimitState = new ParserStateBase(
	// "Reading sizelimit (integer)") {
	//
	// private BERIntegerListener sizeLimitListener = new BERIntegerListener() {
	// public void data(int value) {
	// sizeLimit = value;
	// }
	// };
	//
	// private BERIntegerParser sizeLimitParser = new BERIntegerParser(
	// sizeLimitListener);
	//
	// public void resetInternal() {
	// sizeLimitParser.reset();
	// }
	//
	// public boolean parse(ByteBuffer buffer) {
	// boolean completed = sizeLimitParser.parse(buffer);
	// if (!completed)
	// return false;
	// state = readingTimeLimitState;
	// return true;
	// }
	//
	// };

	// private State readingTimeLimitState = new ParserStateBase(
	// "Reading sizelimit (integer)") {
	//
	// private BERIntegerListener timeLimitListener = new BERIntegerListener() {
	// public void data(int value) {
	// timeLimit = value;
	// }
	// };
	//
	// private BERIntegerParser timeLimitParser = new BERIntegerParser(
	// timeLimitListener);
	//
	// public void resetInternal() {
	// timeLimitParser.reset();
	// }
	//
	// public boolean parse(ByteBuffer buffer) {
	// boolean completed = timeLimitParser.parse(buffer);
	// if (!completed)
	// return false;
	// state = readingTypesOnlyState;
	// return true;
	// }
	// };

	// private State readingTypesOnlyState = new ParserStateBase(
	// "Reading typesOnly (boolean)") {
	// private LDAPBooleanListener typesOnlyListener = new LDAPBooleanListener()
	// {
	// public void data(boolean value) {
	// typesOnly = value;
	// }
	// };
	//
	// private LDAPBooleanParser typesOnlyParser = new LDAPBooleanParser(
	// typesOnlyListener);
	//
	// public void resetInternal() {
	// typesOnlyParser.reset();
	// }
	//
	// public boolean parse(ByteBuffer buffer) {
	// boolean completed = typesOnlyParser.parse(buffer);
	// if (!completed)
	// return false;
	// state = readingFilterState;
	// return true;
	// }
	// };

	// private State readingFilterState = new ParserStateBase(
	// "Reading filter (Filter)") {
	//
	// private FilterListener filterListener = new FilterListener() {
	// public void data(Filter f) {
	// filter = f;
	// }
	// };
	//
	// private FilterParser filterParser = new FilterParser(filterListener);
	//
	// public void resetInternal() {
	// filterParser.reset();
	//
	// }
	//
	// public boolean parse(ByteBuffer buffer) {
	// boolean completed = filterParser.parse(buffer);
	// if (!completed)
	// return false;
	// state = readingAttributesState;
	// return true;
	// }
	// };

	// private State readingAttributesState = new ParserStateBase(
	// "Reading attributes (AttributeDescriptionList)") {
	// private AttributeDescriptionListListener attributesListener = new
	// AttributeDescriptionListListener() {
	// public void data(List value) {
	// attributes = value;
	// }
	// };
	//
	// private AttributeDescriptionListParser attributesParser = new
	// AttributeDescriptionListParser(
	// attributesListener);
	//
	// public void resetInternal() {
	// attributesParser.reset();
	//
	// }
	//
	// public boolean parse(ByteBuffer buffer) {
	// boolean completed = attributesParser.parse(buffer);
	// if (!completed)
	// return false;
	// state = finalState;
	// return true;
	// }
	// };

	// private State finalState = new FinalParserState();

	// private State state = readingBaseState;

	/**
	 * <code>listener</code> is the receiver of the parsed
	 * {@link org.jletty.ldapstackldapops.AddRequest}s
	 * 
	 * @see org.jletty.ldapstackldapops.AddRequest
	 */
	private RequestListener listener;

	private String baseObject;

	private Scope scope;

	private DerefAliases deRefAliases;

	private int sizeLimit;

	private int timeLimit;

	private boolean typesOnly;

	private Filter filter;

	private List attributes;

	public SearchRequestParser(RequestListener l) {
		this.listener = l;
	}

	protected boolean parseContents(ByteBuffer buffer) {

		// while (!state.equals(finalState)) {
		// boolean completed = state.parse(buffer);
		// if (!completed)
		// return false;
		// }

		while (!this.stateEnum.equals(StateEnum.FINAL)) {
			boolean completed = this.currentParser.parse(buffer);
			if (!completed) {
				return false;
			}
			switchToNextState();
		}
		return true;
	}

	private void switchToNextState() {
		switch (this.stateEnum.getValue()) {
		case StateEnum.R_BASE_VALUE:
			this.stateEnum = StateEnum.R_SCOPE;
			break;
		case StateEnum.R_SCOPE_VALUE:
			this.stateEnum = StateEnum.R_DREFALIASES;
			break;
		case StateEnum.R_DREFALIASES_VALUE:
			this.stateEnum = StateEnum.R_SIZELIMIT;
			break;
		case StateEnum.R_SIZELIMIT_VALUE:
			this.stateEnum = StateEnum.R_TIMELIMIT;
			break;
		case StateEnum.R_TIMELIMIT_VALUE:
			this.stateEnum = StateEnum.R_TYPESONLY;
			break;
		case StateEnum.R_TYPESONLY_VALUE:
			this.stateEnum = StateEnum.R_FILTER;
			break;
		case StateEnum.R_FILTER_VALUE:
			this.stateEnum = StateEnum.R_ATTRS;
			break;
		case StateEnum.R_ATTRS_VALUE:
			this.stateEnum = StateEnum.FINAL;
			break;
		default:
			throw new ParserException("Unknown state " + this.stateEnum);
		}
		this.currentParser = getParserForState();
	}

	private Parser getParserForState() {
		Parser toReturn = null;
		switch (this.stateEnum.getValue()) {
		case StateEnum.R_BASE_VALUE:
			toReturn = this.baseObjectParser;
			break;
		case StateEnum.R_SCOPE_VALUE:
			toReturn = this.scopeParser;
			break;
		case StateEnum.R_DREFALIASES_VALUE:
			toReturn = this.deRefAliasesParser;
			break;
		case StateEnum.R_SIZELIMIT_VALUE:
			toReturn = this.sizeLimitParser;
			break;
		case StateEnum.R_TIMELIMIT_VALUE:
			toReturn = this.timeLimitParser;
			break;
		case StateEnum.R_TYPESONLY_VALUE:
			toReturn = this.typesOnlyParser;
			break;
		case StateEnum.R_FILTER_VALUE:
			toReturn = this.filterParser;
			break;
		case StateEnum.R_ATTRS_VALUE:
			toReturn = this.attributesParser;
			break;
		default:
			throw new ParserException("Unknown state " + this.stateEnum);
		}
		return toReturn;
	}

	/**
	 * @see org.jletty.ldapstackparsers.implementation.TLVParser#notifyListeners()
	 */
	protected void notifyListeners() {
		this.listener.data(new SearchRequest(this.baseObject, this.scope,
				this.deRefAliases, this.sizeLimit, this.timeLimit,
				this.typesOnly, this.filter, this.attributes));
	}

	/**
	 * @see org.jletty.ldapstackparsers.implementation.TLVParser#getExpectedTag()
	 */
	protected final boolean checkExpectedTag(byte tag) {
		// [APPLICATION 3] SearchRequest
		return (SearchRequestParser.TAG == tag);
	}

	protected final void resetInternal() {
		// reset variables
		this.baseObject = "";
		this.scope = Scope.BASE_OBJECT;
		this.deRefAliases = DerefAliases.NEVER_DEREF_ALIASES;
		this.sizeLimit = 1;
		this.timeLimit = 10;
		this.typesOnly = false;
		this.attributes = null;

		// reset parsers and state
		if (this.currentParser != null) {
			this.currentParser.reset();
		}
		this.stateEnum = StateEnum.R_BASE;
		this.currentParser = getParserForState();
	}
}