package io.multiversum.db.executor.core.commands.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import io.multiversum.db.executor.core.CommandQueueExecutor;
import io.multiversum.db.executor.core.exceptions.TableNotFoundException;
import io.multiversum.util.Pair;

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
}
