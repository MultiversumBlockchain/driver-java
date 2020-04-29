package io.multiversum.db.executor.core.commands;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.insert.Insert;

import io.multiversum.db.executor.core.CommandQueueExecutor;
import io.multiversum.db.executor.core.commands.util.DatabaseUtility;
import io.multiversum.db.executor.core.commands.util.InsertValuesVisitor;
import io.multiversum.util.Pair;

public class InsertCommand extends BaseSqlCommand {

	private Insert statement;
	
	public InsertCommand(SqlCommand parent, Insert statement) {
		super(parent);
		
		this.statement = statement;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public CommandResult<Boolean> run(CommandQueueExecutor executor) throws Exception {
		// Get table
		BigInteger tableIndex = DatabaseUtility.tableIndex(executor, statement.getTable().getName());
		
		// Insert data
		List<List<String>> data = null;
		
		if (statement.isUseValues()) {
			InsertValuesVisitor visitor = new InsertValuesVisitor();
			statement.getItemsList().accept(visitor);
			
			data = visitor.getValues();
		} else {
			data = ((Pair<List<String>, List<List<String>>>)CommandStack.popResult()).getSecond();
		}
		
		if (data.size() > 1) {
			throw new UnsupportedOperationException("insert with mulitple rows is not supported");
		} else if (data.size() == 0) {
			throw new UnsupportedOperationException("no values specified");
		}
		
		List<String> columnNames = new ArrayList<String>();
		for (Column column : statement.getColumns()) {
			columnNames.add(column.getColumnName());
		}
		
		List<Pair<String, BigInteger>> columns = DatabaseUtility.columnIndexes(executor, columnNames, tableIndex);
		for (int i = 0; i < data.get(0).size(); i++) {
			String value = data.get(0).get(i);
			int columnIndex = i % columns.size();
			
			BigInteger index = columns.get(columnIndex).getSecond();
			
			StringBuilder valueBuilder = new StringBuilder(value);
			String newValue = valueBuilder.insert(0, index.byteValue()).toString();
			
			data.get(0).set(i, newValue);
		}
		
		executor.getContract().insert(tableIndex, data.get(0)).send();
		
		return this.<Boolean>result().setResult(true);
	}
}
