package io.multiversum.db.executor.core.commands;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.multiversum.db.executor.core.CommandQueueExecutor;
import io.multiversum.db.executor.core.commands.results.CommandResult;
import io.multiversum.db.executor.core.commands.util.ColumnInfo;
import io.multiversum.db.executor.core.commands.util.DatabaseUtility;
import io.multiversum.db.executor.core.commands.util.ExpressionResolver;
import io.multiversum.db.executor.core.commands.util.RowIndexResolver;
import io.multiversum.db.executor.core.contracts.ColumnType;
import io.multiversum.db.executor.core.exceptions.InvalidDateValueException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.update.Update;

public class UpdateCommand extends BaseSqlCommand {

	private Update statement;
	
	public UpdateCommand(SqlCommand parent, Update statement) {
		super(parent);
		
		this.statement = statement;
	}

	@Override
	public CommandResult run(CommandQueueExecutor executor) throws Exception {
		RowIndexResolver indexResolver = new RowIndexResolver();
		
		statement.getWhere().accept(indexResolver);
		
		if (indexResolver.getIndex() == null) {
			throw new UnsupportedOperationException("You must specify a row index");
		}
		
		BigInteger rowIndex = indexResolver.getIndex();
		BigInteger tableIndex = DatabaseUtility.tableIndex(executor, statement.getTable().getName());
		
		List<Column> columns = statement.getColumns();
		List<ColumnInfo> infos = DatabaseUtility.columns(executor, columns, tableIndex);
		
		List<BigInteger> columnIndexes = columnIndexes(infos);
		
		List<String> values = new ArrayList<String>();
		for (Expression expression : statement.getExpressions()) {
			ExpressionResolver resolver = new ExpressionResolver();
			
			expression.accept(resolver);
			
			values.add(resolver.getResult().getValue());
		}
		
		for (int i = 0; i < values.size(); i++) {
			String value = values.get(i);
			ColumnType type = infos.get(i).getType();
			BigInteger index = infos.get(i).getIndex();
			
			String newValue = mapType(type, value);
			
			StringBuilder valueBuilder = new StringBuilder(value);
			newValue = valueBuilder.insert(0, index.byteValue()).toString();
			
			values.set(i, newValue);
		}
		
		executor.getContract().updateDirect(tableIndex, rowIndex, columnIndexes, values).send();
		
		return result();
	}
	
	private List<BigInteger> columnIndexes(List<ColumnInfo> columns) {
		List<BigInteger> result = new ArrayList<BigInteger>();
		
		for (ColumnInfo info : columns) {
			result.add(info.getIndex());
		}
		
		return result;
	}
	
	private String mapType(ColumnType type, String value) throws InvalidDateValueException {
		switch (type) {
		case Date:
			return valueToDate(value);
		case Datetime:
			return valueToDateTime(value);
		default:
			return value;
		}
	}
	
	private String valueToDate(String value) throws InvalidDateValueException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			Date date = format.parse(value);
			return "" + date.getTime();
		} catch (ParseException e) {
			throw new InvalidDateValueException(value, "yyyy-MM-dd");
		}
	}
	
	private String valueToDateTime(String value) throws InvalidDateValueException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			Date date = format.parse(value);
			return "" + date.getTime();
		} catch (ParseException e) {
			throw new InvalidDateValueException(value, "yyyy-MM-dd HH:mm:ss");
		}
	}
	
}
