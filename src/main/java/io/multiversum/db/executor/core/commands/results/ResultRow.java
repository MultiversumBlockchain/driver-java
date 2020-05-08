package io.multiversum.db.executor.core.commands.results;

import java.util.List;

public class ResultRow {

	protected String index;
	protected List<String> columns;
	
	public ResultRow(String index) {
		this.index = index;
	}
	
	public ResultRow(String index, List<String> columns) {
		this.index = index;
		this.columns = columns;
	}
	
	public String getIndex() {
		return index;
	}
	
	public List<String> getColumns() {
		return columns;
	}
	
	public String toString() {
		return String.format("Row (%s, %s)", index, columns);
	}
	
}
