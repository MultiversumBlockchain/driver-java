package io.multiversum.db.executor.core.exceptions;

public class UnsupportedDataTypeException extends Exception {

	private static final long serialVersionUID = -9112132224838777590L;
	
	public UnsupportedDataTypeException(String type) {
		super("Unsupported type: " + type);
	}

}
