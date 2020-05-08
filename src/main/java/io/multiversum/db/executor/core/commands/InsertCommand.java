package io.multiversum.db.executor.core.commands;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.insert.Insert;

import io.multiversum.db.executor.core.CommandQueueExecutor;
import io.multiversum.db.executor.core.commands.results.CommandResult;
import io.multiversum.db.executor.core.commands.results.ResultRow;
import io.multiversum.db.executor.core.commands.util.DatabaseUtility;
import io.multiversum.db.executor.core.commands.util.InsertValuesVisitor;
import io.multiversum.db.executor.core.contracts.Database.RowCreatedEventResponse;
import io.multiversum.util.Pair;

public class InsertCommand extends BaseSqlCommand {

	private Insert statement;
	
	public InsertCommand(SqlCommand parent, Insert statement) {
		super(parent);
		
		this.statement = statement;
	}
	
	@Override
	public CommandResult run(CommandQueueExecutor executor) throws Exception {
		// Get table
		BigInteger tableIndex = DatabaseUtility.tableIndex(executor, statement.getTable().getName());
		
		// Insert data
		List<List<String>> data = null;
		
		if (statement.isUseValues()) {
			InsertValuesVisitor visitor = new InsertValuesVisitor();
			statement.getItemsList().accept(visitor);
			
			data = visitor.getValues();
		} else {
			List<ResultRow> rows = CommandStack.popResult().getRows();
			
			data = new ArrayList<List<String>>();
			for (ResultRow row : rows) {
				data.add(row.getColumns());
			}
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
		
		TransactionReceipt receipt = executor.getContract().insert(tableIndex, data.get(0)).send();
		List<RowCreatedEventResponse> events = executor.getContract().getRowCreatedEvents(receipt);
		RowCreatedEventResponse response = events.get(0);
		
		List<String> resultColumns = new ArrayList<String>();
		List<ResultRow> resultRows = new ArrayList<ResultRow>();
		
		resultColumns.add("index");
		resultRows.add(new ResultRow(response.index.toString(10)));
		
		return result().setResult(resultColumns, resultRows);
	}
}
