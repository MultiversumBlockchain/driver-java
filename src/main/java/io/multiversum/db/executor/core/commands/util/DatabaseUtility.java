package io.multiversum.db.executor.core.commands.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import io.multiversum.db.executor.core.CommandQueueExecutor;
import io.multiversum.db.executor.core.contracts.ColumnType;
import io.multiversum.db.executor.core.exceptions.TableNotFoundException;
import io.multiversum.util.Pair;
import net.sf.jsqlparser.schema.Column;

public class DatabaseUtility {

	@SuppressWarnings("unchecked")
	public static BigInteger tableIndex(CommandQueueExecutor executor, String tableName) throws Exception {
		List<byte[]> tables = executor.getContract().showTables().send();
		
		if (tables.isEmpty()) {
			throw new TableNotFoundException(tableName);
		}
		
		for (int i = 0; i < tables.size(); i += 2) {
			BigInteger bn = new BigInteger(tables.get(i));
			
			String index = "" + bn.longValue();
			String name = new String(tables.get(i + 1)).trim();
			
			if (name.equals(tableName)) {
				return new BigInteger(index);
			}
		}
		
		throw new TableNotFoundException(tableName);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Pair<String, BigInteger>> columnIndexes(CommandQueueExecutor executor, BigInteger tableIndex) throws Exception {
		List<Pair<String, BigInteger>> result = new ArrayList<Pair<String,BigInteger>>();
		List<byte[]> columns = executor.getContract().desc(tableIndex).send();
		for (int i = 0; i < columns.size(); i += 2) {
			String name = new String(columns.get(i + 1)).trim();
			
			result.add(new Pair<String, BigInteger>(name, new BigInteger("" + (i / 2))));
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static List<ColumnInfo> columns(CommandQueueExecutor executor, BigInteger tableIndex) throws Exception {
		List<ColumnInfo> result = new ArrayList<ColumnInfo>();
		List<byte[]> columns = executor.getContract().desc(tableIndex).send();
		for (int i = 0; i < columns.size(); i += 2) {
			BigInteger typeBN = new BigInteger(columns.get(i));
			String name = new String(columns.get(i + 1)).trim();
			
			BigInteger index = new BigInteger("" + (i / 2));
			ColumnType type = DatabaseUtility.typeIndexToType(typeBN);
			
			result.add(new ColumnInfo(name, index, type));
		}
		
		return result;
	}
	
	public static List<Pair<String, BigInteger>> columnIndexes(CommandQueueExecutor executor, List<String> columns, BigInteger tableIndex) throws Exception {
		List<Pair<String, BigInteger>> result = new ArrayList<Pair<String,BigInteger>>();
		List<Pair<String, BigInteger>> desc = columnIndexes(executor, tableIndex);
		
		for (Pair<String, BigInteger> col : desc) {
			for (String name : columns) {
				if (col.getFirst().equals(name)) {
					result.add(col);
				}
			}
		}
		
		return result;
	}
	
	public static List<ColumnInfo> columns(CommandQueueExecutor executor, List<?> columns, BigInteger tableIndex) throws Exception {
		List<ColumnInfo> result = new ArrayList<ColumnInfo>();
		List<ColumnInfo> desc = columns(executor, tableIndex);
		
		for (ColumnInfo col : desc) {
			for (Object requested : columns) {
				String name = null;
				if (requested instanceof Column) {
					name = ((Column) requested).getName(false);
				} else {
					name = (String) requested;
				}
				
				if (col.getName().equals(name)) {
					result.add(col);
				}
			}
		}
		
		return result;
	}
	
	private static ColumnType typeIndexToType(BigInteger index) {
		switch (index.intValue()) {
		case 0:
		case 1:
			return ColumnType.Int;
		case 2:
			return ColumnType.String;
		case 3:
			return ColumnType.Double;
		case 4:
			return ColumnType.Date;
		case 5:
			return ColumnType.Datetime;
		case 6:
			return ColumnType.Boolean;
		}
		
		return ColumnType.String;
	}
}
