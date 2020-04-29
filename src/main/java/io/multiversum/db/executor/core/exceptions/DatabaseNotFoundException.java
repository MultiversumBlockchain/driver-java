package io.multiversum.db.executor.core.exceptions;

public class DatabaseNotFoundException extends Exception {

	private static final long serialVersionUID = 480505010192758254L;
	
	public DatabaseNotFoundException(String database) {
		super("Database: " + database + " was not found");
	}

}
