/*
 * Created on 20-sep-2004
 *
 */
package org.jletty.ldapstackldapops;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jletty.util.Style;

/**
 * @author Ruben
 * 
 */
public final class SearchRequest implements LDAPRequest {

	private String baseObject;

	private DerefAliases deRefAliases;

	private int sizeLimit;

	private int timeLimit;

	private Collection attributes;

	private Scope scope;

	private boolean typesOnly;

	private Filter filter;

	public SearchRequest(String baseObject, Scope scope,
			DerefAliases deRefAliases, int sizelimit, int timelimit,
			boolean typesOnly, Filter filter, Collection attributes) {
		this.baseObject = baseObject;
		this.scope = scope;
		this.deRefAliases = deRefAliases;
		this.sizeLimit = sizelimit;
		this.timeLimit = timelimit;
		this.typesOnly = typesOnly;
		this.filter = filter;
		if (attributes != null) {
			this.attributes = attributes;
		} else {
			this.attributes = Collections.EMPTY_SET;
		}

	}

	public String getBaseDn() {
		return this.baseObject;
	}

	public int getSizeLimit() {
		return this.sizeLimit;
	}

	public int getTimeLimit() {
		return this.timeLimit;
	}

	public byte[] getBytes() {
		throw new RuntimeException("Method getBytes not implemented yet");
	}

	public String toString() {
		return new ToStringBuilder(this, Style.getDefaultStyle()).append(
				"baseObject", this.baseObject).append("deRefAliases", this.deRefAliases)
				.append("sizeLimit", this.sizeLimit).append("timeLimit", this.timeLimit)
				.append("attributes", this.attributes).append("scope", this.scope)
				.append("typesOnly", this.typesOnly).append("filter", this.filter)
				.toString();
	}

	public boolean equals(final Object other) {
		if (!(other instanceof SearchRequest)) {
			return false;
		}
		SearchRequest castOther = (SearchRequest) other;
		final EqualsBuilder equalsBuilder = new EqualsBuilder().append(
				this.baseObject, castOther.baseObject).append(this.deRefAliases,
				castOther.deRefAliases).append(this.sizeLimit, castOther.sizeLimit)
				.append(this.timeLimit, castOther.timeLimit).append(
						new HashSet(this.attributes),
						new HashSet(castOther.attributes)).append(this.scope,
						castOther.scope).append(this.typesOnly, castOther.typesOnly)
				.append(this.filter, castOther.filter);
		return equalsBuilder.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.baseObject).append(this.deRefAliases)
				.append(this.sizeLimit).append(this.timeLimit).append(this.attributes).append(
						this.scope).append(this.typesOnly).append(this.filter).toHashCode();
	}

	public Filter getFilter() {
		return this.filter;
	}

	/**
	 * @return Returns the scope.
	 */
	public Scope getScope() {
		return this.scope;
	}

	public boolean isTypesOnly() {
		return this.typesOnly;
	}

	public Collection getAttributes() {
		return this.attributes;
	}

	public byte getTag() {
		return BerTags.APPLICATION_3;
	}
}