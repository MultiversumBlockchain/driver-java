package io.multiversum.db.executor.core.commands;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import io.multiversum.db.executor.core.CommandQueueExecutor;
import io.multiversum.db.executor.core.commands.results.SelectResult;
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
	public SelectResult run(CommandQueueExecutor executor) throws Exception {
		SelectResult result;
		PlainSelect plainSelect = (PlainSelect) select.getSelectBody();

		if (plainSelect.getFromItem() == null) {
			result = expressionOnlySelect(plainSelect);
		} else {
			result = remoteSelect(executor, plainSelect);
		}

		CommandStack.pushResult(result.getResult());

		return result;
	}

	private SelectResult expressionOnlySelect(PlainSelect plainSelect) {
		SelectResult result = new SelectResult();

		SelectItemResolver resolver = new SelectItemResolver();
		for (SelectItem item : plainSelect.getSelectItems()) {
			item.accept(resolver);
		}

		HashMap<String, ColumnResult> expressionResult = resolver.getExpressionResult();

		ArrayList<List<String>> values = new ArrayList<List<String>>();
		List<String> resultValues = new ArrayList<String>();
		for (ColumnResult currentValue : expressionResult.values()) {
			resultValues.add(currentValue.getValue());
		}
		
		values.add(resultValues);

		result.setAliases(new ArrayList<String>(expressionResult.keySet()));
		result.setValues(values);

		return result;
	}

	@SuppressWarnings("unchecked")
	private SelectResult remoteSelect(CommandQueueExecutor executor, PlainSelect plainSelect) throws Exception {
		SelectResult result = null;

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
			
			result.setAliases(aliases);
		} else {
			result.setAliases(new ArrayList<String>(resolver.getExpressionResult().keySet()));
		}
		
		Collection<ColumnResult> selectedValues = resolver.getExpressionResult().values();
		List<String> columnNames = new ArrayList<String>();
		
		for (ColumnResult res : selectedValues) {
			if (res.isColumn()) {
				columnNames.add(res.getColumnName());
			}
		}

		if (!allColumns) {
			List<List<String>> newValues = new ArrayList<List<String>>();
			List<Pair<String, BigInteger>> columnIndexes = DatabaseUtility.columnIndexes(executor, columnNames, tableIndex);
			
			for (List<String> row : result.getValues()) {
				List<String> newRow = new ArrayList<String>();
				
				for (ColumnResult res : selectedValues) {
					if (res.isColumn()) {
						for (String col : row) {
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
					newValues.add(newRow);
				}
			}
			
			result.setValues(newValues);
		} else {
			List<List<String>> newValues = new ArrayList<List<String>>();
			for (List<String> row : result.getValues()) {
				List<String> newRow = new ArrayList<String>();
				for (String col : row) {
					newRow.add(col.substring(1));
				}
				
				newValues.add(newRow);
			}
			
			result.setValues(newValues);
		}

		return result;
	}

	private SelectResult selectWithFixedLimit(CommandQueueExecutor executor, BigInteger tableIndex, long offset, long limit, long size) throws Exception {
		SelectResult result = new SelectResult();

		// Return an empty list if the offset is bigger than the data set
		if (offset >= size) {
			result.setValues(new ArrayList<List<String>>());

			return result;
		}

		// Recalculate limit based on offset
		limit = Math.min(size - offset, size);

		long iterations = (long) Math.ceil(limit / (double) MAX_LIMIT_PER_PAGE);

		List<List<String>> rows = new ArrayList<List<String>>();

		for (long i = 0; i < iterations; i++) {
			long currentLimit = Math.min(MAX_LIMIT_PER_PAGE, size - (i * MAX_LIMIT_PER_PAGE));
			long currentOffset = offset + (i * MAX_LIMIT_PER_PAGE);

			BigInteger bigLimit = new BigInteger("" + currentLimit);
			BigInteger bigOffset = new BigInteger("" + currentOffset);

			List<List<String>> currentRows = executor.getContract().selectAll(tableIndex, bigOffset,
					bigLimit);

			rows.addAll(currentRows);
		}

		result.setValues(rows);

		return result;
	}
}
