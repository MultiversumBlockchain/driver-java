package io.multiversum.db.executor.core.commands.results;

import java.util.List;

import io.multiversum.util.Pair;

public class CommandResult {

	public enum Status { Ok, Error }
	
	protected Status status = Status.Ok;
	protected String errorMessage = null;
	
	protected List<String> columnNames = null;
	protected List<ResultRow> rows = null;
	
	public CommandResult setResult(List<String> columnNames, List<ResultRow> rows) {
		this.columnNames = columnNames;
		this.rows = rows;
		
		return this;
	}
	
	public CommandResult setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
		
		return this;
	}
	
	public CommandResult setRows(List<ResultRow> rows) {
		this.rows = rows;
		
		return this;
	}
	
	public List<String> getColumnNames() {
		return columnNames;
	}
	
	public List<ResultRow> getRows() {
		return rows;
	}
	
	public CommandResult setStatus(Status status) {
		this.status = status;
		
		return this;
	}
	
	public CommandResult setStatus(Status status, String errorMessage) {
		this.status = status;
		this.errorMessage = errorMessage;
		
		return this;
	}
	
	public boolean isOk() {
		return status == Status.Ok;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public Pair<List<String>, List<ResultRow>> getPlainResult() {
		if (columnNames == null || rows == null) {
			return new Pair<List<String>, List<ResultRow>>();
		}
		
		return new Pair<List<String>, List<ResultRow>>(columnNames, rows);
	}
	
	public String toString() {
		return String.format(
			"CommandResult [\n\tstatus: %s\n\tcolumns: %s\n\trows: %s\n]",
			status,
			columnNames,
			rows
		);
	}
	
}
