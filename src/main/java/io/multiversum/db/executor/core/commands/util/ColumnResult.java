package io.multiversum.db.executor.core.commands.util;

public class ColumnResult {
	private String value;
	private String columnName;
	private boolean isColumn;
	
	ColumnResult(String value, String columnName, boolean isColumn) {
		this.value = value;
		this.columnName = columnName;
		this.isColumn = isColumn;
	}
	
	public boolean isColumn() {
		return isColumn;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getColumnName() {
		return columnName;
	}
}
