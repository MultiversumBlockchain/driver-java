package io.multiversum.db.executor.core.commands.util;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.ParenthesisFromItem;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.TableFunction;
import net.sf.jsqlparser.statement.select.ValuesList;

public class FromItemResolver implements FromItemVisitor {

	private Table table;
	
	public Table getTable() {
		return table;
	}
	
	@Override
	public void visit(Table table) {
		this.table = table;
	}

	@Override
	public void visit(SubSelect subSelect) {
		throw new UnsupportedOperationException("sub selects are not supported in from statements");
	}

	@Override
	public void visit(SubJoin subjoin) {
		throw new UnsupportedOperationException("sub joins are not supported in from statements");
	}

	@Override
	public void visit(LateralSubSelect lateralSubSelect) {
		throw new UnsupportedOperationException("lateral sub selects are not supported in from statements");
	}

	@Override
	public void visit(ValuesList valuesList) {
		throw new UnsupportedOperationException("value lists are not supported in from statements");
	}

	@Override
	public void visit(TableFunction tableFunction) {
		throw new UnsupportedOperationException("functions are not supported in from statements");
	}

	@Override
	public void visit(ParenthesisFromItem aThis) {
		throw new UnsupportedOperationException("parenthesis are not supported in from statements");
	}

}
