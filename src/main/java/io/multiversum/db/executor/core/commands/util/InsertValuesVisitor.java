package io.multiversum.db.executor.core.commands.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NamedExpressionList;
import net.sf.jsqlparser.statement.select.SubSelect;

public class InsertValuesVisitor implements ItemsListVisitor {

	final private List<List<String>> values = new ArrayList<List<String>>();
	
	public List<List<String>> getValues() {
		return values;
	}
	
	@Override
	public void visit(SubSelect subSelect) {
		throw new UnsupportedOperationException("sub select in insert values is not supported yet");
	}

	@Override
	public void visit(ExpressionList expressionList) {
		List<String> result = new ArrayList<String>();
		for (Expression expression : expressionList.getExpressions()) {
			StringBuilder buffer = new StringBuilder();
			ExpressionResolver resolver = new ExpressionResolver();
			
			resolver.setBuffer(buffer);
			expression.accept(resolver);
			
			result.add(resolver.getResult().getValue());
		}
		
		values.add(result);
	}

	@Override
	public void visit(NamedExpressionList namedExpressionList) {
		throw new UnsupportedOperationException("named expression list in insert values is not supported yet");
	}

	@Override
	public void visit(MultiExpressionList multiExprList) {
		for (ExpressionList list : multiExprList.getExprList()) {
			list.accept(this);
		}
	}

}
