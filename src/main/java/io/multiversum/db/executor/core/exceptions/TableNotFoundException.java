package io.multiversum.db.executor.core.exceptions;

public class TableNotFoundException extends Exception {

	private static final long serialVersionUID = -7865424613857359293L;

	public TableNotFoundException(String table) {
		super("Table: " + table + " was not found");
	}

}
