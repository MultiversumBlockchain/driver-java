package io.multiversum.db.executor.core.commands.util;

import java.util.HashMap;

import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;

public class SelectItemResolver implements SelectItemVisitor {

	private final HashMap<String, ColumnResult> expressionResult = new HashMap<String, ColumnResult>();

	private boolean allColumns = false;
	
	public HashMap<String, ColumnResult> getExpressionResult() {
		return expressionResult;
	}
	
	public boolean isAllColumns() {
		return allColumns;
	}
	
	@Override
	public void visit(AllColumns all) {
		allColumns = true;
	}

	@Override
	public void visit(AllTableColumns allTableColumns) {
		// TODO Auto-generated method stub
		System.out.println("all table columns");
	}

	@Override
	public void visit(SelectExpressionItem selectExpressionItem) {
		ExpressionResolver resolver = new ExpressionResolver();
		
		selectExpressionItem.getExpression().accept(resolver);
		
		ColumnResult result = resolver.getResult();
		
		String alias = result.isColumn() ? result.getColumnName() : result.getValue();
		if (selectExpressionItem.getAlias() != null) {
			alias = selectExpressionItem.getAlias().getName();
		}
		
		expressionResult.put(alias, result);
	}
}
