package io.multiversum.db.executor.core.commands;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import io.multiversum.db.executor.core.CommandQueueExecutor;
import io.multiversum.db.executor.core.commands.results.CommandResult;
import io.multiversum.db.executor.core.commands.results.ResultRow;
import io.multiversum.db.executor.core.commands.util.ColumnResult;
import io.multiversum.db.executor.core.commands.util.DatabaseUtility;
import io.multiversum.db.executor.core.commands.util.ExpressionResolver;
import io.multiversum.db.executor.core.commands.util.FromItemResolver;
import io.multiversum.db.executor.core.commands.util.SelectItemResolver;
import io.multiversum.util.Pair;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;

public class SelectCommand extends BaseSqlCommand {

	private final long MAX_LIMIT_PER_PAGE = 50;

	private Select select;

	public SelectCommand(SqlCommand parent, Select select) {
		super(parent);

		this.select = select;
	}

	@Override
	public CommandResult run(CommandQueueExecutor executor) throws Exception {
		CommandResult result;
		PlainSelect plainSelect = (PlainSelect) select.getSelectBody();

		if (plainSelect.getFromItem() == null) {
			result = expressionOnlySelect(plainSelect);
		} else {
			result = remoteSelect(executor, plainSelect);
		}

		CommandStack.pushResult(result);

		return result;
	}

	private CommandResult expressionOnlySelect(PlainSelect plainSelect) {
		CommandResult result = new CommandResult();

		SelectItemResolver resolver = new SelectItemResolver();
		for (SelectItem item : plainSelect.getSelectItems()) {
			item.accept(resolver);
		}

		HashMap<String, ColumnResult> expressionResult = resolver.getExpressionResult();

		List<ResultRow> values = new ArrayList<ResultRow>();
		List<String> resultValues = new ArrayList<String>();
		for (ColumnResult currentValue : expressionResult.values()) {
			resultValues.add(currentValue.getValue());
		}
		
		values.add(new ResultRow(null, resultValues));
		
		result.setResult(new ArrayList<String>(expressionResult.keySet()), values);

		return result;
	}

	@SuppressWarnings("unchecked")
	private CommandResult remoteSelect(CommandQueueExecutor executor, PlainSelect plainSelect) throws Exception {
		CommandResult result = null;

		// Resolve the from statement
		FromItemResolver fromResolver = new FromItemResolver();
		plainSelect.getFromItem().accept(fromResolver);

		// Fetch the table index by it's name
		String tableName = fromResolver.getTable().getName();
		BigInteger tableIndex = DatabaseUtility.tableIndex(executor, tableName);

		// Fetch the table size
		long rowsCount = executor.getContract().rowsCount(tableIndex).send().longValue();

		long limit = rowsCount;
		long offset = 0;

		// Check if the select statement explicit a limit condition
		if (plainSelect.getLimit() != null) {
			ExpressionResolver limitExpressionResolver = new ExpressionResolver();
			plainSelect.getLimit().getRowCount().accept(limitExpressionResolver);

			limit = Long.parseLong(limitExpressionResolver.getResult().getValue());
		}

		// Check if the select statement explicit an offset condition
		if (plainSelect.getOffset() != null) {
			offset = plainSelect.getOffset().getOffset();
		}

		// Resolve select values and/or columns
		SelectItemResolver resolver = new SelectItemResolver();
		for (SelectItem item : plainSelect.getSelectItems()) {
			item.accept(resolver);
		}

		// Execute select
		result = selectWithFixedLimit(executor, tableIndex, offset, limit, rowsCount);

		if (plainSelect.getWhere() != null) {
			// Resolve where
			ExpressionResolver whereResolver = new ExpressionResolver(true);
			plainSelect.getWhere().accept(whereResolver);
		}
		
		// Set the select aliases/names to the select result
		boolean allColumns = false;
		if (resolver.isAllColumns()) {
			List<String> aliases = new ArrayList<String>();
			List<byte[]> columns = executor.getContract().desc(tableIndex).send();
			for (int i = 0; i < columns.size(); i += 2) {
				String name = new String(columns.get(i + 1)).trim();
				
				aliases.add(name);
			}
			
			allColumns = true;
			
			result.setColumnNames(aliases);
		} else {
			result.setColumnNames(new ArrayList<String>(resolver.getExpressionResult().keySet()));
		}
		
		Collection<ColumnResult> selectedValues = resolver.getExpressionResult().values();
		List<String> columnNames = new ArrayList<String>();
		
		for (ColumnResult res : selectedValues) {
			if (res.isColumn()) {
				columnNames.add(res.getColumnName());
			}
		}

		if (!allColumns) {
			List<ResultRow> newValues = new ArrayList<ResultRow>();
			List<Pair<String, BigInteger>> columnIndexes = DatabaseUtility.columnIndexes(executor, columnNames, tableIndex);
			
			for (ResultRow row : result.getRows()) {
				List<String> newRow = new ArrayList<String>();
				
				for (ColumnResult res : selectedValues) {
					if (res.isColumn()) {
						for (String col : row.getColumns()) {
							BigInteger index = new BigInteger(col.substring(0, 1));
							
							boolean found = false;
							for (Pair<String, BigInteger> colIndex : columnIndexes) {
								if (colIndex.getSecond().equals(index)) {
									found = true;
									break;
								}
							}
							
							if (found) {
								newRow.add(col.substring(1));
								
								break;
							}
							
						}
					} else {
						newRow.add(res.getValue());
					}
				}
				
				if (newRow.size() > 0) {
					newValues.add(new ResultRow(row.getIndex(), newRow));
				}
			}
			
			result.setRows(newValues);
		} else {
			List<ResultRow> newValues = new ArrayList<ResultRow>();
			List<Pair<String, BigInteger>> columnIndexes = DatabaseUtility.columnIndexes(executor, tableIndex);
			
			
			for (ResultRow row : result.getRows()) {
				List<String> newRow = new ArrayList<String>();
				
				for (Pair<String, BigInteger> column : columnIndexes) {
					boolean found = false;
					for (String col : row.getColumns()) {
						BigInteger index = new BigInteger(col.substring(0, 1));
						
						if (index.equals(column.getSecond())) {
							found = true;
							newRow.add(col.substring(1));
							
							break;
						}
					}
					
					if (!found) {
						newRow.add(null);
					}
				}
				
				newValues.add(new ResultRow(row.getIndex(), newRow));
			}
			
			result.setRows(newValues);
		}

		return result;
	}

	private CommandResult selectWithFixedLimit(CommandQueueExecutor executor, BigInteger tableIndex, long offset, long limit, long size) throws Exception {
		CommandResult result = new CommandResult();

		// Return an empty list if the offset is bigger than the data set
		if (offset >= size) {
			result.setRows(new ArrayList<ResultRow>());

			return result;
		}

		// Recalculate limit based on offset
		limit = Math.min(size - offset, size);

		long iterations = (long) Math.ceil(limit / (double) MAX_LIMIT_PER_PAGE);

		List<ResultRow> rows = new ArrayList<ResultRow>();

		for (long i = 0; i < iterations; i++) {
			long currentLimit = Math.min(MAX_LIMIT_PER_PAGE, size - (i * MAX_LIMIT_PER_PAGE));
			long currentOffset = offset + (i * MAX_LIMIT_PER_PAGE);

			BigInteger bigLimit = new BigInteger("" + currentLimit);
			BigInteger bigOffset = new BigInteger("" + currentOffset);

			List<List<String>> rawRows = executor.getContract().selectAll(tableIndex, bigOffset,
					bigLimit);
			
			List<ResultRow> currentRows = new ArrayList<ResultRow>();
			for (int j = 0; j < rawRows.size(); j++) {
				List<String> columns = rawRows.get(j);
				String index = "" + (currentOffset + j + 1);
				
				currentRows.add(new ResultRow(index, columns));
			}

			rows.addAll(currentRows);
		}

		result.setRows(rows);

		return result;
	}
}
