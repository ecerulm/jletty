/* Don delete it's not generated */
package org.jletty.aci;

public class BindRule {

	private BindRuleType type;

	private String expression;

	private BindRuleEqualsType equals;

	public BindRuleEqualsType getEquals() {
		return this.equals;
	}

	public void setEquals(BindRuleEqualsType equals) {
		this.equals = equals;
	}

	public BindRuleType getType() {
		return this.type;
	}

	public String getExpression() {
		return this.expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void setType(BindRuleType type) {
		this.type = type;
	}

}
