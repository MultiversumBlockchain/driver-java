package io.multiversum.db.executor.core.commands.util;

import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.statement.values.ValuesStatement;

public class SelectResolver implements SelectVisitor {

	@Override
	public void visit(PlainSelect plainSelect) {
		for (SelectItem item : plainSelect.getSelectItems()) {
			item.accept(new SelectItemResolver());
		}
	}

	@Override
	public void visit(SetOperationList setOpList) {
		throw new UnsupportedOperationException("set operation list is not supported");
	}

	@Override
	public void visit(WithItem withItem) {
		throw new UnsupportedOperationException("with item is not supported");
	}

	@Override
	public void visit(ValuesStatement aThis) {
		throw new UnsupportedOperationException("values statement is not supported");
	}

}
