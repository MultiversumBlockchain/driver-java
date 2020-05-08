package io.multiversum.db.executor.core.commands;

import java.util.ArrayList;
import java.util.List;

import io.multiversum.db.executor.core.CommandQueueExecutor;
import io.multiversum.db.executor.core.commands.results.CommandResult;
import io.multiversum.db.executor.core.commands.util.DatabaseUtility;
import io.multiversum.db.executor.core.contracts.ColumnType;
import io.multiversum.db.executor.core.exceptions.TableNotFoundException;
import io.multiversum.db.executor.core.exceptions.UnsupportedDataTypeException;
import io.multiversum.db.executor.core.util.StringUtils;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;

public class CreateTableCommand extends BaseSqlCommand {

	protected String table;
	protected boolean ifNotExists;
	protected List<ColumnDefinition> columnDefinitions;
	
	public CreateTableCommand(SqlCommand parent, String table, boolean ifNotExists, List<ColumnDefinition> columnDefinitions) {
		super(parent);
		
		this.table = table;
		this.ifNotExists = ifNotExists;
		this.columnDefinitions = columnDefinitions;
	}
	
	public String getTable() {
		return table;
	}
	
	public List<ColumnDefinition> getColumnDefinitions() {
		return columnDefinitions;
	}
	
	@Override
	public CommandResult run(CommandQueueExecutor executor) throws Exception {
		if (ifNotExists) {
			if (doesTableExists(executor)) {
				return result();
			}
		}
		
		List<byte[]> columns = columnsToBytesArray();
		byte[] tableName = StringUtils.toBytes(this.table);
		
		executor.getContract().createTable(tableName, columns).send();
		
		return result();
	}
	
	private boolean doesTableExists(CommandQueueExecutor executor) throws Exception {
		try {
			DatabaseUtility.tableIndex(executor, table);
		} catch (TableNotFoundException e) {
			return false;
		}
		
		return true;
	}
	
	private List<byte[]> columnsToBytesArray() throws UnsupportedDataTypeException {
		List<byte[]> result = new ArrayList<byte[]>();
		for (ColumnDefinition def : columnDefinitions) {
			byte[] type = columnTypeToBytes(def.getColDataType().getDataType());
			byte[] name = StringUtils.toBytes(def.getColumnName());

			result.add(type);
			result.add(name);
		}
		
		return result;
	}
	
	private byte[] columnTypeToBytes(String type) throws UnsupportedDataTypeException {
		type = type.toLowerCase();
		switch (type) {
		case "int":
		case "tinyint":
		case "smallint":
		case "mediumint":
		case "bigint":
		case "timestamp":
			return ColumnType.Int.toBytes();
		case "decimal":
		case "numeric":
		case "float":
		case "double":
			return ColumnType.Double.toBytes();
		case "date":
			return ColumnType.Date.toBytes();
		case "datetime":
			return ColumnType.Datetime.toBytes();
		case "boolean":
		case "bool":
			return ColumnType.Boolean.toBytes();
		case "char":
		case "varchar":
		case "blob":
		case "binary":
		case "varbinary":
		case "text":
		case "longtext":
		case "enum":
		case "mediumtext":
		case "tinytext":
			return ColumnType.String.toBytes();
		default:
			throw new UnsupportedDataTypeException(type);
		}
	}

}
