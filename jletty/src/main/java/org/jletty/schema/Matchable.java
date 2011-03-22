/*
 * Created on 13-feb-2005
 *
 */
package org.jletty.schema;

/**
 * @author Ruben
 * 
 */
public interface Matchable {
	/**
	 * Evaluates if this object match the given object with the equality match
	 * rule provided. In summary, is evaluated to either "TRUE", "FALSE" or
	 * "Undefined". It evaluates to Undefined if it is not able to determine
	 * whether the value matches this object. If an attribute description is not
	 * recognized or the assertion value cannot be parsed,, then the filter is
	 * Undefined.
	 * 
	 * @param matchRule
	 * @param object
	 * @return a {@link MatchResult MatchResult}
	 * 
	 */
	public MatchResult eqMatch(MatchRule matchRule, Matchable object);

	/**
	 * Evaluates if this object match the given object with the ordering match
	 * rule provided. In summary, is evaluated to either "TRUE", "FALSE" or
	 * "Undefined". It evaluates to Undefined if it is not able to determine
	 * whether the value matches this object. If an attribute description is not
	 * recognized or the assertion value cannot be parsed, then the filter is
	 * Undefined.
	 * 
	 * @param matchRule
	 *            sets the ordering matchrule to use in the comparison
	 * @param object
	 * @return a {@link MatchResult MatchResult}
	 * 
	 */
	public MatchResult ordMatch(MatchRule matchRule, Matchable object,
			MatchResultClosure closure);

	public MatchResult subMatch(MatchRule matchRule, String filterValue);

	/**
	 * @param eqMatchRule
	 * @param tmp
	 * @return
	 */
	public MatchResult approxMatch(MatchRule eqMatchRule, Matchable object);

}
