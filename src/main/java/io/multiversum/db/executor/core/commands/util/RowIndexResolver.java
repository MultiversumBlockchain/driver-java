package io.multiversum.db.executor.core.commands.util;

import java.math.BigInteger;

import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

public class RowIndexResolver extends ExpressionDeParser {

	private BigInteger index = null;
	
	public BigInteger getIndex() {
		return index;
	}
	
	@Override
	public void visit(EqualsTo equalsTo) {
		super.visit(equalsTo);
		
		if (!(equalsTo.getLeftExpression() instanceof Column)) {
			return;
		}
		
		Column column = (Column) equalsTo.getLeftExpression();
		if (!column.getColumnName().equalsIgnoreCase("index")) {
			return;
		}
		
		equalsTo.getRightExpression().accept(this);
	}
	
	
	@Override
	public void visit(LongValue longValue) {
		super.visit(longValue);
		
		String value = "" + longValue.getValue();
		
		index = new BigInteger(value);
	}
	
	@Override
	public void visit(StringValue stringValue) {
		super.visit(stringValue);
		
		String value = stringValue.getValue();
		
		index = new BigInteger(value);
	}
}
