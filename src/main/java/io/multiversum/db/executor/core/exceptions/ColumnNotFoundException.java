package io.multiversum.db.executor.core.exceptions;

public class ColumnNotFoundException extends Exception {

	private static final long serialVersionUID = 324317345203444222L;

	public ColumnNotFoundException(String columns) {
		super("Column: " + columns + " was not found");
	}

}
